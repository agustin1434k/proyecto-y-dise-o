# Semana 4 — Autenticación y sesiones · Guía de instalación, pruebas y presentación

> **Estado:** el código está escrito pero **no fue ejecutado**: no había PHP disponible donde se armó.
> Solo se verificó que llaves, paréntesis y bloques `if/foreach` estén balanceados.
> Todas las pruebas de la sección 3 las tenés que hacer vos en XAMPP.
> Esta versión se construyó sobre la arquitectura modular de la Semana 3 (`gestion_clientes_v2`).
> Si tu proyecto real tiene diferencias (columnas de pedidos, reportes propios), mantené tus archivos y
> copiá solo lo nuevo (ver sección 2).

## 1. Puesta en marcha
1. **Copia de seguridad** de tu carpeta actual y exportación de la base desde phpMyAdmin (Exportar → Rápido → SQL).
2. Copiá esta carpeta a `C:\xampp\htdocs\` (el nombre puede ser cualquiera; las redirecciones se calculan solas).
3. Iniciá **Apache** y **MySQL** en XAMPP.
4. En phpMyAdmin, base `gestion_clientes` → pestaña **SQL** → pegá el contenido de `sql/usuarios_estructura.sql` → Continuar.
   (Si ya tenías tabla `usuarios`, no ejecutes esto y pasame su estructura.)
5. Abrí `http://localhost/<tu_carpeta>/crear_usuario.php` **una sola vez**. Crea el usuario `admin`
   (contraseña de laboratorio dentro del archivo, variable `$passwordPlano`: conviene cambiarla antes de ejecutarlo).
6. Al terminar, `crear_usuario.php` **intenta eliminarse solo**. Verificá en la carpeta que ya no esté; si sigue, borralo vos.
7. Abrí `http://localhost/<tu_carpeta>/login.php` e ingresá.

## 2. Qué cambió respecto a la Semana 3
| Archivo | Cambio |
|---|---|
| `includes/auth.php` (**nuevo**) | Inicia la sesión con cookie segura y define `requiereLogin()`, `estaLogueado()` y `urlBase()`. |
| `login.php`, `autenticar.php`, `logout.php`, `panel.php` (**nuevos**) | Flujo de acceso. `panel.php` es el antiguo `index.php`. |
| `index.php` | Ahora solo redirige: al panel si hay sesión, al login si no. |
| `clientes/*`, `pedidos/*`, `reportes/index.php` | Todas llaman a `requiereLogin()` al principio, **antes** de conectarse a la base. Incluye los que solo procesan (`guardar`, `actualizar`, `eliminar`). |
| `includes/funciones.php` | La sesión ahora la inicia `auth.php` (un solo lugar). |
| `includes/menu.php` + `css/estilos.css` | Sin sesión muestra solo **Ingresar**; con sesión muestra los módulos, el nombre del usuario y **Cerrar sesión**. Se usa también en `login.php`. |
| `crear_usuario.php` (**temporal**) | Solo funciona desde localhost y se auto-elimina al terminar (si no puede, lo borrás vos). |
| `sql/usuarios_estructura.sql` | Tabla `usuarios` con `IF NOT EXISTS`. |

Diferencias deliberadas con la guía del profesor:
- `logout.php` se ejecuta por **POST con token CSRF** (la guía lo recomienda para producción); el menú trae un botón en vez de un enlace.
- La URL de redirección al login **no está fija** a `/gestion_clientes/`: `urlBase()` la calcula.
  Si por una configuración especial (host virtual, enlace simbólico) las redirecciones fueran a una ruta equivocada, avisame.
- `login.php` usa `header.php`, `menu.php`, `footer.php` y la hoja de estilos compartida. El panel tiene además su propio botón **Cerrar sesión**.

## 3. Pruebas que tenés que hacer vos
- [ ] Apache y MySQL en verde.
- [ ] La tabla `usuarios` existe en phpMyAdmin (base `gestion_clientes`) con las 5 columnas pedidas.
- [ ] Existe el usuario `admin` (pestaña *Examinar*) y `crear_usuario.php` ya no está en la carpeta.
- [ ] Sin sesión, el menú de `login.php` muestra solo **Ingresar**; con sesión, los módulos y **Cerrar sesión**.
- [ ] `login.php` se ve bien (con estilos).
- [ ] Credenciales correctas → entra al panel y muestra "Bienvenido, Administrador".
- [ ] Contraseña incorrecta → "Usuario o contraseña incorrectos." (mismo mensaje con usuario inexistente).
- [ ] Sin iniciar sesión, abrir `.../clientes/listar.php`, `.../clientes/nuevo.php`, `.../pedidos/listar.php`,
      `.../reportes/index.php` y `.../panel.php` → todas redirigen al login.
- [ ] Sin sesión, abrir `.../clientes/guardar.php` y `.../clientes/eliminar.php` directo → redirigen al login.
- [ ] Con sesión: búsqueda, filtros, paginación, alta, edición, eliminación, pedidos y reportes siguen funcionando.
- [ ] **Cerrar sesión** (botón del menú o del panel) → vuelve al login con "Cerraste sesión correctamente".
- [ ] Después de cerrar sesión, usar el botón "Atrás" del navegador y recargar una página privada → pide login.
- [ ] Abrir `.../logout.php` escribiendo la URL con sesión iniciada → **no** cierra la sesión (solo funciona por POST).
- [ ] Desactivar la cuenta en phpMyAdmin (`UPDATE usuarios SET activo = 0 WHERE usuario = 'admin';`) → el login la rechaza
      con el mismo mensaje genérico. Reactivala con `activo = 1`.
- [ ] En phpMyAdmin, tabla `usuarios`: la columna `password` muestra un hash que empieza con `$2y$`, no la contraseña.
- [ ] Con sesión iniciada, abrir `login.php` → redirige al panel.
- [ ] No aparece "headers already sent" ni rutas rotas desde las subcarpetas.
Si algo falla, pasame el mensaje exacto y el archivo.

## 4. Ejercicios conceptuales (respuestas cortas, para explicar)
**Autenticación vs. autorización.** Autenticar es comprobar *quién sos* (usuario y contraseña). Autorizar es decidir
*qué podés hacer* una vez identificado (roles y permisos). Esta semana solo hay autenticación.

**Ej. 1 – Tabla `usuarios`.** `id`: identificador único autoincremental. `usuario`: nombre de acceso; `UNIQUE` impide dos
usuarios con el mismo nombre. `password`: guarda el hash (255 caracteres para que entren los hashes de PHP). `nombre`:
nombre para mostrar. `activo`: permite deshabilitar una cuenta sin borrarla.

**Ej. 2 – Hash.** `password_hash()` convierte la contraseña en un hash con sal que no se puede revertir; en la base queda
algo como `$2y$10$...`. Se comprueba mirando la tabla en phpMyAdmin. `password_verify()` compara lo que se escribe con el hash.

**Ej. 3 – Mensaje genérico.** Si dijéramos "el usuario no existe" o "la contraseña es incorrecta" por separado, un atacante
podría descubrir qué usuarios existen. Por eso el mensaje es siempre el mismo.

**Ej. 4 – Datos de `$_SESSION`.** `usuario_id` identifica al usuario (es lo que se consulta para saber si hay sesión),
`usuario` es su nombre de acceso y `nombre` sirve para el saludo. No se guarda la contraseña.

**Ej. 8 – `exit` después de `header()`.** `header("Location: ...")` solo pide al navegador que redirija; el script
sigue ejecutándose. Sin `exit`, el código privado de abajo se ejecutaría igual. Todas las redirecciones del proyecto terminan en `exit`.

**Ej. 9 – `session_regenerate_id(true)`.** Cambia el ID de sesión al autenticar. Evita la *fijación de sesión*: que alguien
conozca de antemano el ID con el que luego el usuario inicia sesión. El `true` borra la sesión vieja.

**Ej. 10 – Acceso directo.** Escribir una URL privada sin sesión redirige al login porque cada página privada
llama a `requiereLogin()`. Documentalo con una captura de la URL escrita y del login resultante.

## 5. Cómo funciona el flujo (para presentar)
1. `login.php` muestra el formulario (POST) → 2. `autenticar.php` busca el usuario con consulta preparada, comprueba que
esté activo y verifica el hash con `password_verify()` → 3. si todo es correcto, regenera el ID de sesión y guarda
`usuario_id`, `usuario` y `nombre` → 4. `panel.php` y cada módulo llaman a `requiereLogin()` al principio →
5. `logout.php` vacía `$_SESSION`, borra la cookie y destruye la sesión.
Regla clave: **ninguna página privada confía en que el usuario llegó desde el menú**; cada petición verifica la sesión en el servidor.

Medidas de seguridad incluidas: consultas preparadas, hash de contraseñas, mensaje genérico, cuenta activa,
`session_regenerate_id(true)`, cookie `HttpOnly` + `SameSite=Lax` (+ `Secure` si hay HTTPS), `use_strict_mode`,
logout por POST con CSRF, escape de salida con `e()`, errores técnicos solo en `error_log()`.

Limitaciones (para ser honestos con el profesor / próximas ampliaciones): no hay límite de intentos de login,
el login no tiene token CSRF, un usuario desactivado conserva su sesión actual hasta que la cierre, no hay roles,
y en producción hace falta HTTPS.

## 6. Capturas sugeridas
1. Tabla `usuarios` en phpMyAdmin mostrando el hash. 2. Pantalla de login. 3. Error de credenciales incorrectas.
4. Panel con el saludo. 5. Intento de abrir `clientes/listar.php` sin sesión y el login resultante.
6. Módulos funcionando con sesión (clientes, pedidos, reportes). 7. Cierre de sesión y mensaje.
8. Prueba con cuenta inactiva. 9. Código de `includes/auth.php`. 10. Código de `autenticar.php` (consulta preparada, `password_verify`, `session_regenerate_id`).
11. Cabecera de `clientes/listar.php` con `requiereLogin()`.

## 7. Explicación oral (modelo, para decir con tus palabras)
"Mi sistema ahora tiene acceso controlado. La **autenticación** es comprobar quién es el usuario; la **autorización**
es decidir qué puede hacer. En esta etapa implementé la autenticación.

Creé la tabla `usuarios`. La contraseña no se guarda como texto: uso `password_hash()`, que genera un hash con sal y no
se puede revertir. Al ingresar, `autenticar.php` busca el usuario con una consulta preparada, comprueba que esté activo
y usa `password_verify()` para comparar lo escrito contra el hash. Si algo falla, muestro siempre el mismo mensaje
para no revelar si existe el usuario.

Si las credenciales son correctas llamo a `session_regenerate_id(true)`, que cambia el identificador de sesión y evita
la fijación de sesión, y guardo en `$_SESSION` el `usuario_id`, el `usuario` y el `nombre`. La sesión vive en el servidor;
el navegador solo guarda una cookie con el ID, configurada como HttpOnly y SameSite Lax (y Secure si hay HTTPS).
`session_start()` tiene que ejecutarse antes de enviar cualquier salida.

Para proteger las páginas hice `includes/auth.php` con la función `requiereLogin()`: si no existe `$_SESSION["usuario_id"]`,
redirige al login con `header()` y corta con `exit`, porque `header()` solo pide la redirección y sin `exit` el código
privado seguiría ejecutándose. Esa función se llama al principio de cada página privada, incluso las que solo procesan
datos como guardar, actualizar y eliminar, porque nadie tiene por qué llegar desde el menú: puede escribir la URL.

Para cerrar sesión, `logout.php` vacía `$_SESSION`, borra la cookie y ejecuta `session_destroy()`. Lo hice por POST con
token CSRF para que otra página no pueda cerrar la sesión de un usuario sin que lo sepa."

Preguntas posibles: *¿por qué un hash y no cifrar la contraseña?* (el hash no se revierte; el cifrado sí tiene clave y se puede descifrar).
*¿qué pasa si alguien copia la URL privada?* (sin sesión lo manda al login). *¿qué falta?* (roles, límite de intentos,
CSRF en el login, HTTPS en producción).
