# Semana 5 — Seguridad PHP + MariaDB · Informe de auditoría

**Alcance:** proyecto *Gestión de Clientes* (PHP, PDO, MariaDB, XAMPP) en el estado de la Semana 4.
**Método:** revisión estática del código (lectura y búsquedas con comandos sobre los archivos) antes y después de las correcciones.
**Limitación importante:** nada de esto fue **ejecutado** (no había PHP/Apache/MariaDB en el entorno donde se preparó).
Por eso ningún hallazgo figura como *verificado*: el estado máximo es **"corregido en código — verificación pendiente"**.
Cada hallazgo tiene una prueba (P-xx) en `02_PUESTA_EN_MARCHA_Y_PRUEBAS.md`; cuando la ejecutes y salga bien,
pasá el estado a *verificado*.

## 1. Matriz de hallazgos
Estados: **A** = abierto · **C** = corregido en código, verificación pendiente · **V** = verificado (ejecutado).

| N° | Sev. | Archivo | Problema y evidencia | Corrección aplicada | Prueba | Estado |
|---|---|---|---|---|---|---|
| H-01 | Media | `crear_usuario.php` (raíz) | Script web dentro del proyecto con una contraseña por defecto escrita en el código; si se copiaba o quedaba, el usuario `admin` tenía una clave conocida. | Eliminado. Nuevo `herramientas/crear_usuario.php`: solo corre desde consola (se corta si no es CLI), pide la contraseña (mín. 10) y no hay claves en el código. | P-17, P-18 | C |
| H-02 | Media | `sql/`, guías `.md`, `config/`, `includes/` | Sin `.htaccess`: los `.sql` y `.md` se descargaban por URL y mostraban estructura y usuario. | `.htaccess` raíz (sin listado de carpetas; bloquea `.md .sql .log .bak .ini .txt` y archivos ocultos) y `Require all denied` en `config/ includes/ sql/ docs/ logs/ herramientas/`. | P-14 | C |
| H-03 | Baja | `autenticar.php` | No existía `password_needs_rehash()` (lo exige la consigna). | Tras un login correcto se vuelve a generar el hash si el algoritmo/costo quedó viejo. | P-05 | C |
| H-04 | Media | `autenticar.php` | Sin límite de intentos: fuerza bruta posible. | `includes/intentos.php` + tabla `intentos_login`: 5 fallos en 15 min por usuario+IP bloquean el login (mensaje genérico, cuenta también el usuario inexistente). Si la tabla no existe se registra en el log y el login sigue sin límite. | P-09 | C |
| H-05 | Baja | `login.php`, `autenticar.php` | Formulario de login sin token CSRF. | Campo oculto en el formulario y `csrfValido()` en `autenticar.php`. | P-08 | C |
| H-06 | Media | `includes/auth.php` | Solo se miraba `usuario_id` en la sesión: una cuenta desactivada seguía operando hasta cerrar sesión; sin vencimiento por inactividad. | `requiereLogin()` ahora vence la sesión a los 30 min sin actividad y consulta en cada petición que la cuenta siga activa (si falla la consulta, deniega). | P-11, P-12b | C |
| H-07 | Media (por verificar) | todo el proyecto | Nada controlaba `display_errors`; con el `php.ini` de desarrollo de XAMPP un warning puede mostrar rutas/SQL en pantalla. No se pudo comprobar tu `php.ini`. | `config/seguridad.php`: `display_errors=0`, `log_errors=1`, registro en `logs/errores.log` (carpeta protegida). Bandera `MODO_DESARROLLO` para depurar localmente. | P-15 | C |
| H-08 | Baja | todo el proyecto | Sin cabeceras de seguridad (clickjacking, sniffing, CSP). | `X-Frame-Options`, `X-Content-Type-Options`, `Referrer-Policy`, `Content-Security-Policy` y HSTS (solo con HTTPS). Se quitó el JavaScript en línea (`onsubmit`) para cumplir la CSP: ahora `js/app.js` + `data-confirmar`. | P-16 | C |
| H-09 | Baja | `autenticar.php` | Con usuario inexistente no se ejecutaba `password_verify()`: la demora delataba qué usuarios existen. | Se ejecuta siempre `password_verify()` (con un hash ficticio si el usuario no existe). | revisión de código | C |
| H-10 | Baja | `includes/funciones.php` | `e()` sin `ENT_SUBSTITUTE`: un texto con bytes UTF-8 inválidos se mostraba **vacío**. | `ENT_QUOTES \| ENT_SUBSTITUTE`; documentado el uso según contexto. | P-04 | C |
| H-11 | Baja | `clientes/listar.php`, `funciones.php` | Búsqueda/ciudad sin largo máximo; textos con caracteres de control o UTF-8 inválido aceptados al guardar. | `mb_substr` (100 / 80) en los filtros; `textoSeguro()` en `validarCliente()` y en el login. | P-03, P-06 | C |
| H-12 | Baja | `autenticar.php` | El token CSRF no se renovaba al iniciar sesión. | `unset($_SESSION["csrf"])` tras `session_regenerate_id(true)`. | P-08 | C |
| H-13 | Baja | `includes/auth.php` | Cookie `PHPSESSID` con `path=/`: compartida con otras apps en localhost. | Nombre propio `GESTION_SID` y `path` limitado al proyecto. | P-13 | C |
| H-14 | Media | todo el proyecto | Sin `Cache-Control`: tras cerrar sesión el botón "Atrás" podía mostrar páginas privadas guardadas en caché. | `Cache-Control: no-store` y `Pragma: no-cache` en todas las respuestas PHP. | P-12 | C |

**Sin hallazgo (revisado en código, sin ejecutar):**
- *SQL injection:* toda consulta con datos del usuario usa `prepare()` + `bindValue/execute`. Las únicas concatenaciones son `$where` (fragmentos fijos con marcadores), el nombre de tabla en `contarRegistros()` (lista blanca) y `ASC/DESC` (lista blanca), más `por_pagina` (lista blanca 5/10/20). Reportes usan consultas fijas.
- *XSS:* todo dato dinámico sale por `e()`; las salidas sin `e()` son enteros casteados `(int)`.
- *CSRF:* token de `random_bytes(32)` en sesión, comparado con `hash_equals()`, validado en guardar, actualizar, eliminar, pedidos/nuevo, logout y login.
- *Eliminación:* solo POST; no hay enlaces GET a `eliminar.php`.
- *Páginas privadas:* todas llaman a `requiereLogin()` antes de conectar a la base o imprimir.
- *Autorización:* no existen roles; **todo usuario autenticado puede ver y modificar todo**. Es una decisión de alcance (no se inventaron roles); conviene decirlo en la defensa. Los permisos se comprueban en el servidor, no ocultando botones.

## 2. Medidas implementadas (resumen)
Consultas preparadas y listas blancas · `password_hash/verify/needs_rehash` · mensajes genéricos · límite de intentos ·
CSRF en todos los POST · escape de salida con `e()` · validación en servidor (longitud, formato, UTF-8, caracteres de control, IDs enteros positivos) ·
sesión con regeneración de ID, vencimiento por inactividad, cuenta activa en cada petición, cookie HttpOnly/SameSite/Secure-si-HTTPS ·
errores solo al log · cabeceras de seguridad y CSP · `.htaccess` · sin scripts temporales ni credenciales en el código.

## 3. Archivos modificados o creados (respecto de la Semana 4)
**Nuevos:** `config/seguridad.php`, `includes/intentos.php`, `js/app.js`, `.htaccess` (raíz y 6 carpetas), `herramientas/crear_usuario.php`,
`sql/seguridad_intentos_login.sql`, `logs/` (vacía y protegida), `docs/`.
**Modificados:** `config/conexion.php` (`obtenerPDO()`), `includes/auth.php` (reescrito), `includes/funciones.php` (`e()`, `textoSeguro()`, `validarCliente()`),
`includes/header.php` (script externo), `autenticar.php` (reescrito), `login.php` (CSRF y mensajes), `logout.php` (usa `cerrarSesion()`),
`clientes/listar.php` (confirmación sin JS en línea, límites, casts), `panel.php` y `pedidos/listar.php` (casts).
**Eliminado:** `crear_usuario.php` de la raíz.
Sin cambios: el resto de `clientes/`, `pedidos/`, `reportes/`, `css/`.

## 4. Explicación técnica de las correcciones
- **Sesión con control en servidor:** `requiereLogin()` ya no confía solo en que exista `usuario_id`; vence por inactividad y revisa en la base que la cuenta siga activa. Así, desactivar a alguien surte efecto en su próxima petición.
- **Fuerza bruta:** cada fallo se registra (usuario + IP); con 5 fallos en 15 minutos se bloquea. El bloqueo cuenta también nombres que no existen, por eso no sirve para descubrir usuarios.
- **Tiempo constante:** `password_verify()` siempre se ejecuta; si el usuario no existe se usa un hash de un valor descartable.
- **CSP:** al prohibir scripts en línea, un XSS que inyecte `<script>` no se ejecuta; por eso la confirmación de borrado pasó a un archivo JS externo.
- **`e()` y contexto:** `e()` sirve para texto y atributos HTML. Para armar URLs se usa `http_build_query()`; no se imprimen datos del usuario dentro de bloques `<script>`.
- **Credenciales:** la contraseña de usuarios se crea por consola; ya no hay claves en archivos del proyecto (la de la base `root` sin clave es solo para XAMPP local: en un servidor real hay que cambiarla).

## 5. Lista de verificación final
Se completa con las pruebas del documento 02. Hoy: **ninguna ejecutada**.
- [ ] Todas las pruebas P-01 a P-20 ejecutadas y sus capturas guardadas.
- [ ] Estados de la matriz actualizados de C a V.
- [ ] `MODO_DESARROLLO` en `false` (`config/seguridad.php`).
- [ ] No existe `crear_usuario.php` en la raíz.
- [ ] La tabla `intentos_login` está creada.
- [ ] `logs/errores.log` revisado: sin errores inesperados.
