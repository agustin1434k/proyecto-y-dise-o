# Semana 1 – SQL avanzado aplicado a PHP · Guía de presentación

## Instalación (2 minutos)
1. Copiar `reportes.php` a `C:\xampp\htdocs\gestion_clientes\` (junto a `conexion.php`, `index.php`, etc.). No reemplazar ningún otro archivo.
2. Iniciar Apache y MySQL en XAMPP.
3. Abrir `http://localhost/gestion_clientes/reportes.php`.
4. Si hay error de tablas/columnas: cambiar los nombres en el bloque 1 al inicio de `reportes.php`.

## Dónde está cada ejercicio
| Ejercicio | Dónde |
|---|---|
| 1 Pedidos > $5.000 | Sección "Ejercicios del profesor" |
| 2 Orden de mayor a menor | Sección "Ejercicios del profesor" |
| 3 Pedidos por cliente | Reporte 1 |
| 4 Al menos dos pedidos (HAVING) | Sección "Ejercicios del profesor" |
| 5 Total por cliente | Reporte 2 |
| 6 y 7 Promedio, mínimo y máximo | Reporte 3 (tarjetas) |
| 8 Todos los clientes | Reporte 1 (LEFT JOIN) |
| 9 Clientes sin pedidos | Sección "Ejercicios del profesor" |
| 10 Sobre el promedio | Reporte 4 |
| 11 Formulario importe mínimo | Sección "Importe mínimo" |
| 12 Reporte 5 parametrizado | Reporte 5 |

## Pruebas para hacer (y capturar)
- [ ] La página abre sin errores PHP/SQL
- [ ] Los 4 reportes muestran datos
- [ ] Clientes sin pedidos aparecen con 0 en el Reporte 1
- [ ] Estadísticas = resultado de `verificacion_phpmyadmin.sql`
- [ ] Reporte 4 coincide con la consulta en phpMyAdmin
- [ ] Importe mínimo: probar `5000`, `5.000,50`, vacío y `abc` (debe mostrar aviso)
- [ ] Reporte 5: probar un importe bajo y uno muy alto (sin resultados)
- [ ] Vista en pantalla angosta (achicar la ventana): diseño adaptable

## Explicaciones cortas
- **WHERE** filtra filas antes de agrupar. **GROUP BY** agrupa filas con el mismo valor. **HAVING** filtra los grupos ya calculados (por ejemplo `COUNT(*) >= 2`).
- **INNER JOIN** devuelve solo coincidencias (clientes con pedidos). **LEFT JOIN** devuelve todos los clientes, y si no hay pedido, las columnas del pedido vienen NULL; por eso `COUNT(p.id)` da 0.
- **COUNT, SUM, AVG, MIN, MAX**: cuentan, suman, promedian y buscan menor/mayor. Con tabla vacía SUM/AVG/MIN/MAX dan NULL; **COALESCE(x, 0)** lo convierte en 0.
- **Subconsulta**: una consulta dentro de otra; aquí calcula el promedio general que se usa en el WHERE.
- **PDO + consultas preparadas**: el SQL se envía con marcadores (`:minimo`) y los valores por separado; evita la inyección SQL. Los identificadores (tablas/columnas) son fijos del programador.
- **Seguridad en la salida**: `htmlspecialchars()` evita que un texto con HTML/JS se ejecute en la página (XSS); `number_format()` da formato a los importes.
- **Errores**: el detalle técnico va al log de PHP (`error_log`), al usuario se le muestra un mensaje comprensible.

## Posibles preguntas del profesor
1. *¿Diferencia entre WHERE y HAVING?* WHERE filtra filas, HAVING filtra grupos.
2. *¿Por qué LEFT JOIN en el Reporte 1?* Para que los clientes sin pedidos no desaparezcan.
3. *¿Por qué COUNT(p.id) y no COUNT(*)?* COUNT(*) contaría 1 para un cliente sin pedidos (la fila del LEFT JOIN); COUNT(p.id) ignora los NULL y da 0.
4. *¿Qué es una consulta preparada?* SQL con marcadores y valores enviados aparte; previene inyección SQL.
5. *¿Cómo evitás XSS?* Con `htmlspecialchars()` al mostrar datos.
6. *¿Qué pasa si el usuario escribe "abc"?* La validación lo rechaza y no se ejecuta la consulta.
7. *¿Cómo sabés que los resultados son correctos?* Comparé con consultas ejecutadas en phpMyAdmin.
