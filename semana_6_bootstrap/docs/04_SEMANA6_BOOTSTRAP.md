# Semana 6 — Bootstrap e interfaz profesional · Documentación técnica

> **Estado:** el código está escrito pero **no fue ejecutado** (no había PHP, Apache, MariaDB ni navegador disponibles
> donde se preparó). Solo se revisó con comandos que no queden llaves sin cerrar, clases viejas, JavaScript en línea ni
> controles de seguridad perdidos. **No hay capturas** porque nada se renderizó: las tenés que sacar vos (sección 7).
> Todas las pruebas figuran **PENDIENTE** hasta que las ejecutes.

## 1. Diagnóstico de partida (Semana 5)
- Interfaz con CSS propio (`css/estilos.css`) y plantillas `header.php`, `menu.php`, `footer.php` ya modulares → se **modificaron** esos archivos, no se duplicaron.
- `panel.php` era el panel e `index.php` solo redirigía → ahora el panel es `index.php` (como pide la consigna) y `panel.php` quedó como redirección para no romper nada.
- Seguridad de la Semana 5 (CSRF, `requiereLogin()`, `e()`, consultas preparadas, CSP, límite de intentos) → se conserva; el único ajuste fue la CSP (ver sección 6).
- Supuestos que **no pude comprobar** en tu base: tabla `pedidos(id, cliente_id, fecha, total)` y `clientes(id, nombre, email, ciudad)`.
  Si tus columnas se llaman distinto, el dashboard y los pedidos mostrarán el aviso "Sistema con inconvenientes": pasame la estructura y lo adapto.

## 2. Cómo se integró Bootstrap
`includes/header.php` carga, desde el CDN jsDelivr y con versiones fijas:
- Bootstrap CSS **5.3.3**, Bootstrap Icons **1.11.3** (CSS + fuente), y `bootstrap.bundle.min.js` 5.3.3 (incluye Popper; lo usan el menú desplegable, las alertas y el modal).
- `<meta name="viewport" content="width=device-width, initial-scale=1">` y `<html lang="es">`, UTF-8, título dinámico con `e($tituloPagina)`.
- Después de Bootstrap se carga `css/estilos.css` (complementa, no redefine clases de Bootstrap: solo color de la barra, icono de tarjetas, tablas y detalles) y `js/app.js`.
- **No se usan atributos `integrity`**: no tenía acceso a internet para calcular los hashes y un hash incorrecto bloquea la carga. Es un endurecimiento opcional:
  los hashes oficiales de Bootstrap 5.3.3 están en https://getbootstrap.com/ (sección *Download → CDN*); si los agregás, copiá el `integrity` exacto del enlace oficial y sumá `crossorigin="anonymous"`.
- **Se necesita internet** para ver los estilos (CDN). Sin conexión la página se vería sin diseño.

## 3. Cómo funciona el dashboard (`index.php`)
Es la página de inicio, protegida con `requiereLogin()`. Los indicadores salen de MariaDB, nada está escrito a mano:

| Indicador | Consulta |
|---|---|
| Total de clientes | `SELECT COUNT(*) FROM clientes` |
| Total de pedidos | `SELECT COUNT(*) FROM pedidos` |
| Total de ventas | `SELECT COALESCE(SUM(total), 0) FROM pedidos` (COALESCE evita NULL sin pedidos) |
| Últimos 5 pedidos | `SELECT p.id, p.fecha, c.nombre, p.total FROM pedidos p INNER JOIN clientes c ON c.id = p.cliente_id ORDER BY p.fecha DESC, p.id DESC LIMIT 5` |

- Las consultas de clientes y de pedidos están en bloques `try/catch` separados: si falla una, el resto sigue y el estado pasa a **"Sistema con inconvenientes"** (en verde "Sistema operativo" si todo anduvo). El detalle técnico va a `logs/errores.log`, no a la pantalla.
- Importes con `dinero()` → formato argentino (`$ 1.234,56`). Todo texto de la base sale por `e()`.
- Si no hay pedidos se muestra "Todavía no hay pedidos registrados".

## 4. Grid System, Cards, Tables, Forms, Alerts y Modal
**Grid.** Bootstrap divide el ancho en 12 columnas y es *mobile first*: sin prefijo, la clase vale para todo tamaño; con `md`/`xl` se aplica desde esa pantalla.
Las tarjetas usan `row g-3` y `col-12 col-md-6 col-xl-4`: **1 por fila en celular, 2 en tablet y 3 en escritorio** (la tercera ocupa toda la fila en tablet para no quedar huérfana). `g-3` es el espacio entre columnas; los contenedores son `container-fluid`.
**Cards.** Bloques con borde suave y sombra (`card border-0 shadow-sm rounded-4`) para indicadores, filtros y tablas. **Tables.** `table table-striped table-hover` dentro de `table-responsive`: en pantallas chicas la tabla se desplaza horizontalmente en vez de romper el diseño.
**Forms.** `form-label` (asociada con `for`/`id`), `form-control`, `form-select`, `form-text` para ayudas e `input-group` para el signo `$`. `is-invalid` + `invalid-feedback` solo **muestran** los errores que detectó PHP; la validación real sigue en el servidor (`validarCliente()`).
**Alerts.** `alertaHtml()` genera alertas Bootstrap (éxito = verde, error = rojo, aviso = amarillo, info = celeste) con icono y botón de cierre. `mostrarFlash()` las muestra **una sola vez** y las borra de la sesión; el texto siempre se escapa con `e()`.
**Modal.** El botón **Eliminar** de cada fila abre el modal "Confirmar eliminación" (`data-bs-toggle="modal"`); `js/app.js` copia el `id` y el nombre del cliente al modal (el nombre con `textContent`, nunca como HTML). Al confirmar se envía un formulario **POST** a `eliminar.php`.
**Iconos.** `<i class="bi bi-…" aria-hidden="true"></i>` junto a un texto visible, así ningún botón queda sin nombre accesible.
**Navbar.** `navbar-expand-lg`: en escritorio es horizontal; debajo de 992 px se contrae en el botón "hamburguesa" (collapse de Bootstrap). Muestra el usuario (`bi-person-circle`) y el botón **Cerrar sesión** (formulario POST con token CSRF).

## 5. Qué archivos cambiaron
**Reescritos (vista):** `includes/header.php`, `includes/menu.php`, `includes/footer.php`, `css/estilos.css`, `js/app.js`, `index.php`, `login.php`, `includes/form_cliente.php`,
y la parte HTML de `clientes/listar.php`, `ver.php`, `nuevo.php`, `editar.php`, `pedidos/listar.php`, `pedidos/nuevo.php`, `reportes/index.php` (la lógica PHP de arriba de cada archivo no se tocó).
**Modificados:** `includes/funciones.php` (`mostrarFlash()` ahora usa alertas Bootstrap; nuevos `alertaHtml()` y `tarjetaIndicador()`; paginación Bootstrap), `config/seguridad.php` (CSP), `autenticar.php` y `logout.php` (redirigen a `index.php`), `panel.php` (redirección).
**Sin cambios:** `clientes/guardar.php`, `actualizar.php`, `eliminar.php`, `includes/auth.php`, `includes/intentos.php`, `config/conexion.php`.

## 6. Seguridad: qué se conservó y el único ajuste
Conservado (verificado por revisión de código, **sin ejecutar**): consultas preparadas y listas blancas (sin concatenar datos del usuario), `password_hash/verify/needs_rehash`, límite de intentos de login,
`e()` en toda salida, token CSRF (`random_bytes(32)` + `hash_equals`) en guardar, actualizar, eliminar, pedidos, logout y login, validación PHP de formularios, `requiereLogin()` en todas las páginas privadas
(también en las que solo procesan), sesión con vencimiento y comprobación de cuenta activa, errores solo al log, `.htaccess`.
**El modal no es un control de seguridad:** `eliminar.php` sigue exigiendo POST, token CSRF, ID entero positivo, sesión activa y consulta preparada, aunque alguien salte el modal.
**Ajuste de CSP:** para que cargue Bootstrap, la política ahora permite `https://cdn.jsdelivr.net` solo en `style-src`, `script-src` y `font-src`; el resto sigue en `'self'` (sin scripts ni estilos en línea, sin `unsafe-inline`).
Contrapartida honesta: se confía en el CDN. Para eliminar esa dependencia, descargá Bootstrap a `css/` y `js/` y quitá jsDelivr de la CSP (o usá SRI).
Si algo de Bootstrap no funciona (el menú no se despliega, el modal no abre), abrí F12 → Consola: un mensaje "Refused to load… Content Security Policy" indica qué bloqueó la CSP.

## 7. Pruebas (todas PENDIENTE: ejecutalas vos)
| ID | Prueba | Resultado esperado | Estado |
|---|---|---|---|
| S6-01 | Iniciar sesión y abrir `index.php` | Dashboard con título "Panel de control", estado del sistema y 3 tarjetas | PENDIENTE |
| S6-02 | F12 → Red: recargar | `bootstrap.min.css`, `bootstrap-icons.min.css` (y su fuente `.woff2`) y `bootstrap.bundle.min.js` en 200; F12 → Consola sin errores CSP | PENDIENTE |
| S6-03 | Mirar iconos (menú, tarjetas, botones) | Se ven los iconos, no cuadraditos | PENDIENTE |
| S6-04 | Comparar tarjetas con phpMyAdmin: `SELECT COUNT(*) FROM clientes;`, `SELECT COUNT(*) FROM pedidos;`, `SELECT COALESCE(SUM(total),0) FROM pedidos;` | Los números coinciden | PENDIENTE |
| S6-05 | Tabla "Últimos pedidos" | 5 filas, del más reciente al más antiguo, con n.º, fecha, cliente e importe | PENDIENTE |
| S6-06 | Importes | Formato `$ 1.234,56` | PENDIENTE |
| S6-07 | Navegación | Inicio, Clientes, Pedidos, Reportes y "Cerrar sesión" funcionan; el enlace activo se resalta | PENDIENTE |
| S6-08 | DevTools (Ctrl+Shift+M) con 375 px, 768 px y 1366 px | Móvil: tarjetas apiladas, menú "hamburguesa" que despliega, tablas con scroll horizontal. Tablet: 2 tarjetas por fila. Escritorio: 3 en una fila | PENDIENTE |
| S6-09 | Alta y edición de un cliente | Guardan y vuelven al listado | PENDIENTE |
| S6-10 | Alertas | Verde al guardar/actualizar/eliminar; roja/amarilla en errores; desaparecen al recargar | PENDIENTE |
| S6-11 | Modal | Eliminar → ventana "Confirmar eliminación" con el nombre; Cancelar no borra; Confirmar borra | PENDIENTE |
| S6-12 | F12: cambiar el valor del campo oculto `csrf` del modal y confirmar; abrir `clientes/eliminar.php` directo | Se rechaza y no borra; la URL directa no hace nada | PENDIENTE |
| S6-13 | Nombre vacío, email `abc` en el formulario | Errores del servidor mostrados en rojo bajo el campo | PENDIENTE |
| S6-14 | Sin sesión abrir `index.php`, `clientes/listar.php`, `pedidos/listar.php`, `reportes/index.php`, `panel.php` | Redirige al login | PENDIENTE |
| S6-15 | Regresión: búsqueda, filtro de ciudad, paginación, pedidos, reportes, límite de intentos de login, cierre de sesión | Todo funciona como en la Semana 5 | PENDIENTE |
| S6-16 | (Opcional) Dashboard con tabla `pedidos` vacía | Ventas `$ 0,00` y mensaje "Todavía no hay pedidos" | PENDIENTE |

## 8. Capturas para entregar (las sacás vos)
1. Dashboard en escritorio (≈1366 px). 2. Dashboard en móvil (375 px) con las tarjetas apiladas. 3. Menú móvil desplegado. 4. Dashboard en tablet (768 px).
5. Listado de clientes con filtros y paginación. 6. Modal de confirmación abierto. 7. Alerta de éxito tras guardar. 8. Formulario con errores de validación.
9. Reportes. 10. F12 → Red mostrando Bootstrap cargado y F12 → cabeceras con la CSP.
Tip: en Chrome/Edge, F12 → botón de dispositivos (Ctrl+Shift+M) → elegir tamaño → menú ⋮ → "Capturar captura de pantalla".

## 9. Explicación oral (modelo)
"Pasé la aplicación a Bootstrap 5.3 cargado desde un CDN, junto con Bootstrap Icons. El header carga Bootstrap, los iconos y mi CSS propio, que solo complementa. El dashboard es `index.php` y sus números salen de la base: `COUNT(*)` para clientes y pedidos, y `SUM()` con `COALESCE` para las ventas, para que no dé NULL si no hay pedidos. Los últimos cinco pedidos salen de un `INNER JOIN` entre pedidos y clientes con `ORDER BY` y `LIMIT 5`.
El diseño usa el Grid: una tarjeta por fila en celular, dos en tablet y tres en escritorio, con `col-12 col-md-6 col-xl-4`. Las tablas van dentro de `table-responsive` y el menú se contrae en pantallas chicas.
La seguridad de la semana anterior se mantuvo: toda salida pasa por `e()`, los formularios llevan token CSRF, las páginas privadas llaman a `requiereLogin()` y las consultas son preparadas. El modal de eliminación es solo una confirmación visual: el servidor vuelve a validar todo en `eliminar.php`. Lo único que ajusté fue la política de seguridad de contenido para permitir el CDN de Bootstrap."
Preguntas posibles: *¿qué significa mobile first?* (se diseña para pantallas chicas y se agregan reglas con `md`/`xl` para las grandes) · *¿por qué `table-responsive`?* · *¿por qué el modal no alcanza como seguridad?* · *¿qué riesgo tiene usar un CDN?* (depender de un tercero; se mitiga con versión fija, SRI o archivos locales).
