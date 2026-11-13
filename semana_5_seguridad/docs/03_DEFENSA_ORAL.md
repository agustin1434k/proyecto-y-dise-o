# Defensa oral — Semana 5 (modelo para decir con tus palabras)

**Introducción.** "Auditamos la aplicación con una lista de amenazas típicas y corregimos lo que encontramos. Cada punto está
en una matriz con el archivo, el riesgo, la corrección y la prueba. Lo que está probado lo marco como verificado;
lo que no probé todavía, no lo afirmo."

**SQL injection.** "Los datos del usuario nunca se pegan en el SQL. Uso `prepare()` y pasos de parámetros con `bindValue`.
Para lo que no se puede parametrizar, como el orden ascendente/descendente o la cantidad por página, uso listas blancas:
solo acepto valores permitidos."

**Contraseñas.** "Se guardan con `password_hash()` y se comprueban con `password_verify()`. Agregué `password_needs_rehash()`
para renovar el hash si el algoritmo se vuelve viejo. El mensaje de error es siempre el mismo para no revelar qué usuarios existen,
y hay un límite de 5 intentos en 15 minutos contra fuerza bruta."

**XSS.** "Cada dato que sale a pantalla pasa por `e()`, que usa `htmlspecialchars` con `ENT_QUOTES`. Se escapa **al mostrar**, no al guardar,
porque guardar el texto ya escapado lo deformaría y no protege otros contextos. Además la política CSP no permite scripts en línea."

**CSRF.** "Cada formulario que modifica datos lleva un token aleatorio de `random_bytes(32)` guardado en la sesión. El servidor lo compara con
`hash_equals()`. Sin token válido no se hace nada. Y las eliminaciones son solo por POST, nunca por un enlace."

**Validación.** "El HTML solo ayuda al usuario; la validación real se hace en PHP: obligatorios, largos, `FILTER_VALIDATE_EMAIL`,
IDs enteros positivos, y rechazo de caracteres de control o texto mal codificado."

**Sesiones y acceso.** "Cada página privada llama a `requiereLogin()` antes de mostrar nada. Esa función exige sesión, la vence a los
30 minutos sin actividad y consulta en la base que la cuenta siga activa. El ID de sesión se regenera al iniciar sesión, la cookie es HttpOnly y SameSite,
y al cerrar sesión se vacía `$_SESSION`, se borra la cookie y se destruye la sesión. Los permisos se controlan en el servidor, no ocultando botones."

**Errores.** "El usuario ve mensajes generales; el detalle técnico va a un archivo de registro dentro de una carpeta que el navegador no puede abrir."

**Lo que falta (decirlo con honestidad).** "No hay roles: cualquier usuario autenticado puede hacer todo. Para producción haría falta HTTPS,
contraseña propia para la base (hoy es `root` sin clave por ser XAMPP local), recuperación de contraseña y un registro de auditoría."

**Preguntas posibles**
- *¿Por qué no alcanza con `htmlspecialchars` al guardar?* Porque el escape depende del contexto de salida; se hace al imprimir.
- *¿Qué es CSRF?* Que otra página haga que tu navegador, ya logueado, envíe una acción sin que lo sepas; el token lo impide.
- *¿Por qué `session_regenerate_id(true)`?* Evita la fijación de sesión.
- *¿Por qué siempre `password_verify()`, incluso con usuario inexistente?* Para que el tiempo de respuesta no delate qué usuarios existen.
- *¿Qué hace la CSP?* Le indica al navegador qué recursos puede cargar; sin scripts en línea, un XSS pierde casi todo su efecto.
- *¿Qué hallazgo fue el más importante?* Dependiendo de lo que pruebes: la cuenta desactivada que seguía con acceso, o los `.sql` descargables.
