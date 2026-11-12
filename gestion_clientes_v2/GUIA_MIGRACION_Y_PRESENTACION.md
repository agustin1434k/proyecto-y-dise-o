# Semana 3 — Arquitectura modular · Guía de migración y presentación

> Esta carpeta es una versión **nueva y separada** (`gestion_clientes_v2`). No toca tu proyecto actual.
> **Nada de esto fue ejecutado todavía**: no hay PHP en el entorno donde se armó. Solo se revisó
> que las llaves, paréntesis y bloques `if/foreach` estén balanceados. Las pruebas las hacés vos en XAMPP.

## 1. Antes de empezar: copia de seguridad
1. Cerrá el editor y copiá `C:\xampp\htdocs\gestion_clientes` a otra carpeta, por ejemplo
   `C:\xampp\htdocs\gestion_clientes_BACKUP_semana2`. Verificá que se copiaron todos los archivos.
2. Exportá la base: `http://localhost/phpmyadmin` → base `gestion_clientes` → pestaña **Exportar** →
   método **Rápido**, formato **SQL** → **Exportar**. Guardá el `.sql`.
3. No borres nada del proyecto viejo hasta que la versión nueva funcione.

## 2. Qué asumí y qué tenés que revisar (importante)
| Tema | Lo que asumí | Qué hacer |
|---|---|---|
| Tabla `clientes` | columnas `id, nombre, email, ciudad` (como la guía de la Semana 2) | Abrila en phpMyAdmin > Estructura y confirmá. |
| Tabla `pedidos` | `id, cliente_id, fecha, total` | **No la conozco.** Si la tuya es distinta (ej.: `producto`, `estado`, `monto`), pasame la estructura y adapto `pedidos/*` y `reportes/index.php`. |
| Reportes | 3 reportes genéricos de ejemplo | **No son tus reportes de la Semana 1.** Pegá tus consultas en `reportes/index.php` (o mandámelas). |
| Contraseña MySQL | usuario `root`, sin contraseña | Si tu XAMPP tiene otra, cambiala en `config/conexion.php`. |
Si tu base no tiene la tabla `pedidos`, podés crearla con `sql/pedidos_estructura_OPCIONAL.sql`
(usa `IF NOT EXISTS`, no borra nada). `sql/pedidos_demo_OPCIONAL.sql` carga pedidos ficticios.

## 3. Cómo migrar, paso a paso
1. Copiá la carpeta `gestion_clientes_v2` dentro de `htdocs`. Probala primero así, con su nombre,
   en `http://localhost/gestion_clientes_v2/index.php`.
2. Revisá `config/conexion.php` (usuario/contraseña/nombre de la base).
3. Si todo funciona, y solo entonces, podés renombrar: la carpeta vieja a `gestion_clientes_VIEJO`
   y la nueva a `gestion_clientes`.
4. Si tenías otros archivos propios (por ejemplo funciones extra), movelos a `includes/` o a la carpeta
   del módulo que corresponda y cambiá sus `require_once` para que usen `__DIR__`.

Equivalencias con tu proyecto anterior:
| Antes | Ahora |
|---|---|
| `conexion.php` | `config/conexion.php` |
| `listar.php` | `clientes/listar.php` |
| `nuevo.php` / `guardar.php` | `clientes/nuevo.php` / `clientes/guardar.php` |
| `editar.php` | `clientes/editar.php` + `clientes/actualizar.php` |
| `eliminar.php` | `clientes/eliminar.php` (ahora solo POST) |
| reportes de la Semana 1 | `reportes/index.php` |
Archivos extra que agregué: `includes/form_cliente.php` (formulario compartido, evita duplicar código)
y `clientes/ver.php` (para no perder la acción Ver de la Semana 2).

## 4. Arquitectura
```
gestion_clientes/
├── config/conexion.php       única conexión PDO ($pdo)
├── includes/
│   ├── header.php            <head>, CSS, encabezado visual
│   ├── menu.php              navegación (rutas con $raiz)
│   ├── footer.php            pie con date("Y") y cierre del HTML
│   ├── funciones.php         e(), textoPost(), idPositivo(), flash, CSRF, paginación, validaciones
│   └── form_cliente.php      formulario reutilizado por nuevo y editar
├── clientes/                 listar, ver, nuevo, guardar, editar, actualizar, eliminar
├── pedidos/                  listar, nuevo
├── reportes/index.php
├── css/estilos.css           todos los estilos en un solo lugar
└── index.php                 panel principal
```
**Responsabilidades:** `config` = configuración; `includes` = piezas reutilizables;
carpetas de módulo = pantallas y procesos de cada tema; `css` = presentación.
Las páginas que *muestran* (listar, nuevo, editar) están separadas de las que *procesan* (guardar, actualizar, eliminar).
Después de procesar, se redirige (patrón Post/Redirect/Get) y el mensaje se muestra con `flash()`.

## 5. Conceptos para explicar
- **include / require / require_once**: `include` carga un archivo y si falta solo da una advertencia;
  `require` da error fatal si falta; `require_once` además evita cargarlo dos veces (por eso no hay funciones
  redeclaradas ni conexiones duplicadas). Usé `require_once` para config y funciones, y `require` para las
  plantillas (header/menu/footer).
- **`__DIR__`**: ruta absoluta de la carpeta del archivo actual. `require_once __DIR__ . "/../config/conexion.php"`
  funciona sin importar desde dónde se ejecute o en qué carpeta esté el proyecto.
- **`$raiz`**: cada página define `$raiz = ""` o `"../"` para que el menú y el CSS se encuentren desde cualquier subcarpeta.
  (Es distinto de `__DIR__`: este es para URLs del navegador, aquel para rutas internas de PHP.)
- **Conexión centralizada**: se crea una sola vez en `config/conexion.php`. Si cambia la contraseña, se cambia en un solo lugar.
  Usa `ERRMODE_EXCEPTION`, `FETCH_ASSOC`, `EMULATE_PREPARES=false` y `utf8mb4`. Los errores técnicos van a `error_log()`,
  el usuario ve un mensaje genérico (no se muestran credenciales).
- **Normalizar vs. escapar**: `textoPost()` normaliza (quita espacios) al **recibir**; `e()` escapa HTML al **mostrar**.
  Se guardan datos normalizados y limpios en la base, nunca ya escapados; si se escapara antes de guardar,
  un `&` quedaría guardado como `&amp;` y se vería mal o se escaparía dos veces.
- **Seguridad**: consultas preparadas, validación en servidor, `REQUEST_METHOD`, IDs validados con `idPositivo()`,
  eliminación solo por POST con confirmación (JavaScript) y token CSRF.

## 6. Pruebas que tenés que hacer vos
URL: `http://localhost/gestion_clientes_v2/index.php` (o `gestion_clientes` si ya renombraste).
- [ ] Apache y MySQL en verde en el panel de XAMPP.
- [ ] Abre el panel principal y muestra los contadores.
- [ ] Cada enlace del menú funciona **desde cada carpeta** (Inicio, Clientes, Nuevo cliente, Pedidos, Reportes).
- [ ] El diseño (CSS) se ve igual en todas las páginas, incluidas las de subcarpetas.
- [ ] Clientes: lista, búsqueda, filtro de ciudad, ambos combinados, LIMPIAR, paginación conservando filtros.
- [ ] `listar.php?pagina=999` y `?pagina=0` no dan errores.
- [ ] Nuevo cliente: guardar correcto; probar nombre vacío y email inválido (muestra errores y conserva lo escrito).
- [ ] Editar: carga los datos, guarda cambios; probar un ID inexistente (`editar.php?id=99999`) y `?id=abc`.
- [ ] Eliminar: pide confirmación; borra. Probar con un cliente que tenga pedidos (debe avisar que no se puede).
- [ ] Abrir `clientes/eliminar.php` directo en el navegador: no borra nada, vuelve al listado.
- [ ] Pedidos: lista, filtra por cliente, registra uno nuevo; probar total con letras.
- [ ] Reportes: muestran datos; cargá tus reportes de la Semana 1.
- [ ] No aparecen errores de rutas ni de "Cannot redeclare function".
- [ ] Un nombre como `<b>Prueba</b>` se ve como texto, no como negrita.
Si algo falla, pasame el mensaje exacto (o captura) y la ruta/archivo.

## 7. Capturas sugeridas
1. Estructura de carpetas en el explorador o editor. 2. Panel principal. 3. Listado de clientes con filtros.
4. Página 2 mostrando los filtros en la URL. 5. Formulario con errores de validación. 6. Mensaje de éxito tras guardar.
7. Edición de un cliente. 8. Confirmación de eliminación. 9. Pedidos. 10. Reportes.
11. Código de `config/conexion.php`. 12. Código de `includes/funciones.php` (e, textoPost, idPositivo).
13. Una página con `require_once __DIR__ ...` y el header/menu/footer incluidos.
