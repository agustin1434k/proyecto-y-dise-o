# Entrega 3 – Autoevaluación

1. **CRUD:** Create (`insertar`), Read (`listarTodos`, `buscarPorId`, `listarPorTipoYFechas`, `buscarPorDescripcion`), Update (`actualizar`), Delete (`eliminar`).
2. **WHERE en UPDATE/DELETE:** sin WHERE la sentencia afecta a todas las filas de la tabla; el WHERE limita el cambio al registro indicado (`id_movimiento = ?`).
3. **Resultado mensual:** total de ingresos − total de gastos del mes.
4. **Presupuesto vs gasto real:** el presupuesto es el monto planificado (límite); el gasto real es lo efectivamente registrado en `movimientos`.
5. **LEFT JOIN:** conserva los presupuestos que no tienen gastos; con INNER JOIN esas filas desaparecerían.
6. **COALESCE:** devuelve el primer valor no nulo; convierte el NULL de "sin gastos" (SUM de nada) en 0.
7. **DAO sin Swing:** el DAO solo accede a datos; mezclar interfaz lo haría imposible de reutilizar y probar. Informa con valores devueltos o excepciones.
8. **Mes sin movimientos:** gracias a COALESCE, ingresos, gastos y resultado dan 0 y la tasa de ahorro 0,00 %.
9. **Tasa de ahorro:** (resultado mensual ÷ ingresos) × 100, redondeada a 2 decimales; 0 si no hubo ingresos.
10. **Entrega 4:** la interfaz gráfica Swing que usará el DAO y el servicio.
