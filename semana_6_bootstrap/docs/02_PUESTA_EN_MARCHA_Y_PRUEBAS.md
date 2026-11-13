# Puesta en marcha y pruebas — Semana 5

> **Ninguna prueba de este documento fue ejecutada todavía.** Todas figuran como **PENDIENTE**.
> Tenés que correrlas vos en XAMPP y anotar el resultado real. Si una falla, pasame el mensaje exacto y el archivo.

## 1. Puesta en marcha
1. **Copia de seguridad** de tu proyecto actual y exportación de la base (phpMyAdmin → Exportar → Rápido → SQL).
2. Copiá `semana_5_seguridad` a `C:\xampp\htdocs\` (cualquier nombre de carpeta sirve).
3. Iniciá Apache y MySQL.
4. phpMyAdmin → base `gestion_clientes` → pestaña **SQL** → ejecutá `sql/seguridad_intentos_login.sql`
   (solo crea la tabla `intentos_login`; no toca tus datos). Si ya hay tabla `usuarios`, no hace falta tocar nada más.
5. Si necesitás un usuario nuevo: abrí el *Símbolo del sistema* en la carpeta del proyecto y ejecutá
   `C:\xampp\php\php.exe herramientas\crear_usuario.php` (elegís usuario y contraseña de al menos 10 caracteres).
   Si seguís con el usuario de la Semana 4 (`admin`), no hace falta.
6. Abrí `http://localhost/<carpeta>/login.php`.
7. **Si el `.htaccess` no tiene efecto** (P-14 falla), abrí `C:\xampp\apache\conf\httpd.conf`, buscá el bloque `<Directory "C:/xampp/htdocs">`
   y verificá que diga `AllowOverride All`; reiniciá Apache.

## 2. Pruebas (marcá el resultado real)
| ID | Qué probar | Resultado esperado | Estado |
|---|---|---|---|
| P-01 | Login con credenciales correctas | Entra al panel con el saludo | PENDIENTE |
| P-02 | En phpMyAdmin → `usuarios` → columna `password` | Empieza con `$2y$`; no se ve la clave | PENDIENTE |
| P-03 | Buscar `' OR '1'='1` y `%` en el listado de clientes | Sin error; `%` se busca como texto (0 o pocos resultados); no devuelve toda la tabla por el `OR` | PENDIENTE |
| P-04 | Crear un cliente con nombre `<script>alert(1)</script>` | Se guarda y se **muestra como texto** en la lista; no aparece ninguna alerta | PENDIENTE |
| P-05 | `password_needs_rehash`: ejecutar `C:\xampp\php\php.exe -r "echo password_hash('Prueba_Clave_123', PASSWORD_BCRYPT, ['cost'=>4]);"`, copiar el hash, en phpMyAdmin hacer `UPDATE usuarios SET password='<hash>' WHERE usuario='<usuario de prueba>';`, loguearse con esa clave | Entra, y el `password` en la tabla ahora empieza con `$2y$10$` (se renovó) | PENDIENTE |
| P-06 | Alta de cliente con nombre vacío, email `abc`, ciudad de 90 letras, y con un ID `?id=abc` en `editar.php` | Muestra los errores de validación; el ID inválido redirige con mensaje | PENDIENTE |
| P-07 | Abrir `clientes/eliminar.php` directo en el navegador (GET) y probar un enlace a `eliminar.php?id=1` | No borra nada; vuelve al listado | PENDIENTE |
| P-08 | CSRF: en el listado, con las herramientas del navegador (F12 → Elementos) cambiar el valor del campo oculto `csrf` de un formulario de eliminar y enviarlo; repetir con el formulario de login | Se rechaza ("La solicitud no es válida" / "El formulario venció") y no se borra nada | PENDIENTE |
| P-09 | Límite de intentos: 5 contraseñas incorrectas seguidas con el mismo usuario | Al 6.º intento: "Demasiados intentos fallidos…" aunque pongas la clave correcta. Para desbloquear: `DELETE FROM intentos_login;` | PENDIENTE |
| P-10 | Usuario inexistente vs contraseña incorrecta | Mismo mensaje genérico en ambos casos | PENDIENTE |
| P-11 | Con sesión iniciada, en phpMyAdmin `UPDATE usuarios SET activo=0 WHERE usuario='<tu usuario>';` y recargar una página privada | Te saca al login con "Tu cuenta ya no está habilitada". Reactivá con `activo=1` | PENDIENTE |
| P-12 | Cerrar sesión y usar el botón "Atrás" del navegador | No muestra páginas privadas (pide login) | PENDIENTE |
| P-12b | Vencimiento por inactividad: cambiar temporalmente `TIEMPO_INACTIVIDAD` a `60` en `includes/auth.php`, iniciar sesión, esperar 70 s y recargar | Redirige al login con "Tu sesión venció por inactividad". **Volvé a poner 1800.** | PENDIENTE |
| P-13 | F12 → Aplicación/Almacenamiento → Cookies | Cookie `GESTION_SID` con `HttpOnly` y `SameSite=Lax`; path = carpeta del proyecto | PENDIENTE |
| P-14 | Abrir en el navegador `.../sql/usuarios_estructura.sql`, `.../includes/auth.php`, `.../config/conexion.php`, `.../logs/` | Todas dan **403 Prohibido** (o 404) | PENDIENTE |
| P-15 | Detener MySQL en XAMPP y abrir `panel.php` | Mensaje general sin detalle técnico; en `logs/errores.log` aparece la línea del error | PENDIENTE |
| P-16 | F12 → Red → clic en una página PHP → Cabeceras de respuesta | Se ven `X-Frame-Options`, `Content-Security-Policy`, `X-Content-Type-Options`, `Cache-Control: no-store` | PENDIENTE |
| P-17 | Buscar en la carpeta del proyecto archivos `crear_usuario.php` en la raíz y claves escritas | No existen | PENDIENTE |
| P-18 | Abrir `.../herramientas/crear_usuario.php` en el navegador | 403/404 (y aunque pasara, el script se corta si no es consola) | PENDIENTE |
| P-19 | Páginas privadas sin sesión: `panel.php`, `clientes/listar.php`, `clientes/guardar.php`, `pedidos/listar.php`, `reportes/index.php` | Redirigen al login | PENDIENTE |
| P-20 | Funcionalidad previa: clientes (alta, edición, borrado confirmado con el cartel), búsqueda, filtros, paginación, pedidos, reportes | Todo funciona como en la Semana 4 | PENDIENTE |

Prueba de la confirmación de borrado: al tocar **Eliminar** debe aparecer el cartel "¿Seguro que querés eliminar…?" (ahora lo maneja `js/app.js`).
Si el cartel **no** aparece, abrí F12 → Consola: puede que la política CSP o la ruta de `js/app.js` falle; avisame.

## 3. Las 15 pruebas finales de la consigna ↔ pruebas de este documento
1 login → P-01 · 2 hashes → P-02 · 3 consultas preparadas → P-03 (+ revisión de código) · 4 datos inválidos → P-06 ·
5 XSS → P-04 · 6 CSRF → P-08 · 7 eliminar por GET → P-07 · 8 acceso sin sesión → P-19 · 9 permisos (solo existe "autenticado": P-11, P-19) ·
10 logout → P-12 · 11 clientes · 12 pedidos · 13 búsquedas/filtros · 14 paginación · 15 reportes → P-20.

## 4. Capturas sugeridas
Tabla `usuarios` con el hash · login con mensaje genérico · bloqueo por intentos · resultado de P-04 (texto, sin alerta) ·
rechazo CSRF (P-08) · 403 en `/sql/...` · cabeceras de respuesta · cookie `GESTION_SID` · código de `autenticar.php` ·
código de `config/seguridad.php` · matriz de hallazgos actualizada a "verificado".
