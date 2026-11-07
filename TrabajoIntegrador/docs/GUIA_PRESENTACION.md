# Trabajo Integrador – Guía de presentación

## 1. Arquitectura (explicación sencilla)
| Capa | Paquete | Qué hace |
|---|---|---|
| Vista | `vista` (`FinanzasFrame`, `OpcionCombo`) | Ventanas y componentes Swing. No tiene SQL. |
| Servicio | `servicio` (`FinanzasServicio`) | Reglas y cálculos: tasa de ahorro, porcentajes (BigDecimal). |
| DAO | `dao` (`MovimientoDAO`, `PresupuestoDAO`, `CatalogoDAO`) | Todas las consultas SQL con PreparedStatement. |
| Modelo | `modelo` | Objetos de datos: Movimiento, ResumenMensual, etc. |
| Conexión | `conexion` | `ConexionBD` abre la conexión JDBC. |
| BD | MySQL/MariaDB | Almacena usuarios, cuentas, categorías, movimientos, presupuestos. |

Flujo: el usuario hace clic → la Vista lee y valida el formulario → llama al DAO → el DAO ejecuta el SQL → la Vista actualiza tabla y resumen.

## 2. Guion de demostración (≈ 8 minutos)
1. Abrir la app (Main). Mostrar que los combos muestran nombres (cuentas y categorías).
2. **Guardar** un ingreso y un gasto. Mostrar la fila nueva en la tabla y en phpMyAdmin.
3. Seleccionar una fila → se carga en el formulario → **Modificar** el importe → comprobar en phpMyAdmin.
4. **Eliminar** un movimiento de prueba (aparece la confirmación).
5. Probar un error: importe 0 o texto en la fecha → mensaje claro (no se cierra la app).
6. Filtros: por tipo, por fechas, por descripción y los tres combinados.
7. Pestaña **Resumen mensual**: ingresos, gastos, resultado y tasa de ahorro; mostrar el mes sin movimientos.
8. Pestaña **Presupuestos**: guardar un límite, ver gasto real, disponible y estado.
9. Conclusión financiera (usar los números reales que muestre la app).

## 3. Lista de pruebas (tildar y sacar captura)
- [ ] La aplicación abre y conecta
- [ ] Combos con cuentas y categorías
- [ ] 3 ingresos registrados
- [ ] 10 gastos en al menos 4 categorías
- [ ] Modificación persistente (verificada en phpMyAdmin)
- [ ] Eliminación con confirmación
- [ ] Filtros individuales y combinados
- [ ] Resumen mensual correcto (comparar con consulta SQL de `sql/consultas_entrega3.sql`)
- [ ] Tasa de ahorro correcta
- [ ] Presupuestos en 3 categorías
- [ ] Comparación presupuesto vs gasto real
- [ ] Errores de entrada mostrados con JOptionPane

Capturas sugeridas: cada pestaña, guardar/modificar/eliminar, resumen, presupuestos, y phpMyAdmin.

## 4. Validaciones implementadas
Fecha (formato y obligatoria), importe (número, > 0, máx. 2 decimales), cuenta y categoría seleccionadas, tipo INGRESO/GASTO, compatibilidad categoría–tipo (`tipo_permitido`), descripción y medio de pago no vacíos, ID existente al modificar/eliminar, mes 1–12, monto de presupuesto > 0, categoría de presupuesto no exclusiva de ingresos.

## 5. Posibles preguntas del profesor
- **¿Por qué DAO?** Separa el acceso a datos de la interfaz; la vista no sabe SQL.
- **¿Por qué PreparedStatement?** Evita inyección SQL y maneja bien tipos y fechas.
- **¿Por qué BigDecimal?** double comete errores de redondeo con dinero.
- **¿Por qué LEFT JOIN en presupuestos?** Para mostrar también presupuestos sin gastos; el gasto se suma antes en una subconsulta para no duplicarlo.
- **¿Qué hace COALESCE?** Convierte NULL en 0.
- **¿Qué pasa si el mes no tiene movimientos?** Todo en 0 y la tasa de ahorro 0,00 % (se controla la división por cero).
- **¿Cómo se calcula la tasa de ahorro?** (ingresos − gastos) / ingresos × 100.
- **¿Qué es try-with-resources?** Cierra automáticamente conexión, statement y resultado.
- **¿Por qué no hay SQL en FinanzasFrame?** Para mantener responsabilidades separadas.
- **¿Por qué SwingUtilities.invokeLater?** Swing debe iniciarse en el hilo de eventos (EDT).
- **¿Qué mejorarías?** Consultas en segundo plano con SwingWorker, usuarios con login, gráficos.

## 6. Informe corto (completar con datos reales)
Ver `docs/INFORME_PLANTILLA.md` de la Entrega 3 (en la raíz de docs).
