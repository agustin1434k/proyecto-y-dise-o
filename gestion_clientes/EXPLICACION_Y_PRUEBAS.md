# Semana 2 — Búsquedas, filtros y paginación (guía de estudio y pruebas)

## Dónde va cada archivo
Copiá a `C:\xampp\htdocs\gestion_clientes\` (o la carpeta de tu proyecto):
- `listar.php` → reemplaza al listar.php anterior (guardá una copia del viejo antes).
- `ver.php` → **solo si no tenés uno**. Si ya existe, dejá el tuyo.
- `sql/clientes_demo_OPCIONAL.sql` → solo para tener 40 clientes de prueba (importar en phpMyAdmin). No hace falta si ya tenés datos.
No toqué `conexion.php`: tiene que definir la variable `$pdo`, como en la guía.

Prueba: `http://localhost/gestion_clientes/listar.php`

## Cómo funciona (para explicarlo)
1. **GET**: el formulario envía `buscar`, `ciudad`, `orden` y `por_pagina` por la URL, así que se puede repetir o compartir una búsqueda. POST se usa para modificar datos.
2. **Validación**: `trim()` limpia los textos; `filter_input(..., FILTER_VALIDATE_INT)` valida la página; `por_pagina` solo acepta 5, 10 o 20 y `orden` solo ASC o DESC.
3. **Filtros dinámicos**: se arma un arreglo de condiciones solo con los filtros que el usuario completó y se unen con `AND`.
4. **LIKE**: `nombre LIKE '%texto%'` busca coincidencias parciales; el `%` reemplaza cero o más caracteres.
5. **COUNT(*)**: cuenta los registros con *exactamente los mismos filtros*. Con eso: `totalPaginas = ceil(total / porPagina)`.
6. **LIMIT y OFFSET**: `OFFSET = (pagina - 1) * porPagina` (pág. 1 → 0, pág. 2 → 10, pág. 3 → 20). LIMIT dice cuántos traer.
7. **Consultas preparadas**: el SQL y los datos viajan separados (`prepare` + `bindValue` + `execute`). LIMIT y OFFSET se vinculan con `PDO::PARAM_INT`.
8. **Seguridad al mostrar**: `htmlspecialchars()` (función `h()`) en todo dato que sale por pantalla.
9. **Conservar filtros**: los enlaces de página se arman con `http_build_query()` incluyendo todos los filtros.
10. Si piden una página que no existe (ej. `?pagina=999`) se corrige a la última; si piden 0 o negativa, a la 1.

Mejoras incluidas: búsqueda por nombre **o email**, orden A→Z / Z→A, 5/10/20 por página, Primera/Última, números de página y "Mostrando X a Y de Z registros".
Detalle para explicar: el texto buscado se escapa con `addcslashes` para que un `%` o `_` escrito por el usuario se busque como texto y no como comodín.

## Pruebas que tenés que hacer vos (marcá cada una)
- [ ] Abrir `listar.php`: se ve la tabla con ID, Nombre, Email, Ciudad y Acciones.
- [ ] Buscar `ana`: aparecen coincidencias parciales.
- [ ] Elegir una ciudad sin texto: solo clientes de esa ciudad.
- [ ] Texto + ciudad juntos: se combinan.
- [ ] LIMPIAR: vuelve a la lista completa y sin filtros.
- [ ] Con los datos demo (40 clientes, 10 por página): hay 4 páginas; "Siguiente" muestra la 2.
- [ ] En la página 2 con filtros aplicados, tocar Siguiente/Anterior no pierde la búsqueda ni la ciudad.
- [ ] "Registros encontrados" coincide con lo que contás en la tabla sumando las páginas.
- [ ] `listar.php?pagina=999` → muestra la última página, sin errores.
- [ ] `listar.php?pagina=0` y `?pagina=-3` → muestra la página 1.
- [ ] Buscar `zzzz`: aparece el cartel "No se encontraron clientes" sin errores.
- [ ] Cambiar por página a 5 y 20 y el orden Z→A.
- [ ] Ver y Editar abren los archivos correctos con el `id` del cliente.
- [ ] Probar con `<b>x</b>` en el buscador: no rompe el diseño (se escapa).
- [ ] Nuevo, Editar y Eliminar siguen funcionando como antes.

## Capturas recomendadas
1. Listado inicial (sin filtros) con el total.
2. Búsqueda por nombre con resultados.
3. Filtro por ciudad.
4. Nombre + ciudad combinados.
5. Página 2 mostrando la URL con los filtros en la barra de direcciones.
6. Mensaje "No se encontraron clientes".
7. Fragmento del código con `COUNT(*)`, `LIMIT :inicio, :cantidad` y `bindValue(..., PDO::PARAM_INT)`.

## Preguntas posibles del profesor
- **¿Por qué GET y no POST?** Porque es una consulta que no modifica datos y permite conservar/compartir la búsqueda en la URL.
- **¿Por qué COUNT con los mismos filtros?** Si no, el total de páginas no coincide con los resultados.
- **¿Cómo se calcula el OFFSET?** `(página - 1) × registros por página`.
- **¿Qué evita las consultas preparadas?** La inyección SQL: el dato nunca se mezcla con la sentencia.
- **¿Para qué sirve htmlspecialchars?** Para que un texto con etiquetas HTML se muestre como texto y no se ejecute.
- **¿Por qué `ORDER BY nombre ASC/DESC` no usa parámetro?** Porque los parámetros solo reemplazan valores, no palabras reservadas; por eso el orden se valida contra una lista de dos opciones.
