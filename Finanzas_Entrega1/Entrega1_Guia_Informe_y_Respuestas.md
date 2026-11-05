# Finanzas Personales Inteligentes — Entrega 1

Contenido de este documento: A) cómo ejecutar todo en phpMyAdmin, B) resultados esperados, C) informe financiero, D) respuestas de interpretación y autoevaluación, E) relación y restricción, F) capturas y lista de verificación.

> Los resultados de este documento los calculé con una copia de los mismos datos. En tu phpMyAdmin tienen que dar iguales. Si algún número difiere, copiame lo que te aparece.

---

## A) Cómo ejecutar el trabajo

1. Abrí el **Panel de Control de XAMPP** y presioná **Start** en **Apache** y en **MySQL** (se ponen en verde).
2. En la fila de MySQL presioná **Admin**, o abrí `http://localhost/phpmyadmin`.
3. **Qué hace el script `finanzas_personales_entrega1.sql`:** crea la base `finanzas_personales`, las 4 tablas y carga datos de ejemplo. **No tiene DROP, DELETE ni TRUNCATE**: no borra nada existente. Si ya tenés una base con ese nombre, usa `IF NOT EXISTS` y no la pisa. Lo único a cuidar: ejecutar las PARTES 1 y 2 dos veces daría error por emails repetidos.
4. Pestaña **SQL** (arriba, sin seleccionar ninguna base) → pegá solo la **PARTE 1 y PARTE 2** (hasta antes de "PARTE 3") → **Continuar**.
5. En el panel izquierdo hacé clic en `finanzas_personales` y comprobá que aparecen las 4 tablas.
6. Pestaña **SQL** de esa base → pegá **una consulta por vez** de la PARTE 3 → **Continuar** → capturá el resultado.
7. **PARTE 4 (pruebas de integridad):** ejecutá cada `INSERT` por separado. Tienen que dar **error rojo** (es lo que se quiere demostrar) y no cambian los datos.

Si el CHECK no da error, tu versión de MySQL/MariaDB es muy vieja (CHECK se aplica desde MySQL 8.0.16 y MariaDB 10.2.1). Avisame la versión (se ve en la pantalla principal de phpMyAdmin).

Datos cargados: 2 usuarios, 4 cuentas (2 por usuario), 10 categorías y 24 movimientos, es decir, por encima de los mínimos pedidos.

---

## B) Resultados esperados

| Consulta | Resultado |
|---|---|
| 1 Usuarios | Lucía Fernández y Martín Gómez |
| 2 Movimientos | 24 filas ordenadas por fecha |
| 3 Gastos | 18 filas |
| 4 Gastos ≥ 20.000 | 14 filas; el mayor es Alquiler de Lucía (180.000) |
| 5 LIKE '%Supermercado%' | 5 filas |
| 6 COUNT(*) | 24 |
| 7 Totales por tipo | gasto: 18 movimientos, 753.500 · ingreso: 6 movimientos, 1.728.000 |
| 9 Marzo | gasto 674.500 · ingreso 898.000 |

**Consulta 8 y 11: gastos por categoría**

| Categoría | Total | % del gasto |
|---|---|---|
| Alquiler | 330.000 | 43,80 |
| Alimentos | 207.500 | 27,54 |
| Servicios | 77.500 | 10,29 |
| Salud | 37.000 | 4,91 |
| Ocio | 37.000 | 4,91 |
| Educación | 35.000 | 4,64 |
| Transporte | 29.500 | 3,92 |

**Consulta 10: resultado por usuario**

| Usuario | Ingresos | Gastos | Resultado |
|---|---|---|---|
| Lucía Fernández | 960.000 | 433.500 | 526.500 |
| Martín Gómez | 768.000 | 320.000 | 448.000 |

**Consulta 12: saldo estimado de cada cuenta**

| Cuenta | Saldo inicial | Neto | Saldo estimado |
|---|---|---|---|
| Lucía – Efectivo | 100.000 | −81.000 | 19.000 |
| Lucía – Banco Galicia | 120.000 | 607.500 | 727.500 |
| Martín – Efectivo | 150.000 | −103.000 | 47.000 |
| Martín – Banco Nación | 90.000 | 551.000 | 641.000 |

**Consulta 13 (integridad):** las cuatro (13a a 13d) deben devolver **0 filas**.

**Consulta propia (entre dos fechas, 10 al 20 de marzo):** 9 filas (ids 4, 17, 5, 18, 10, 7, 19, 2 y 11).

**Pruebas de integridad (PARTE 4)**
1. Cuenta 999: error `#1452` (clave foránea).
2. Importe −100: error de CHECK (`#4025` en MariaDB, `#3819` en MySQL).
3. Email repetido: error `#1062` (UNIQUE).

---

## C) Informe financiero (marzo de 2026)

**Alcance.** Análisis de los movimientos de marzo de 2026 de los dos usuarios de la base `finanzas_personales` (datos ficticios).

**Resumen de marzo**

| Concepto | Importe |
|---|---|
| Ingresos | $898.000 |
| Gastos | $674.500 |
| **Resultado del mes** | **$223.500** (24,9 % de los ingresos) |

Por usuario en marzo, Lucía tuvo ingresos de $510.000 y gastos de $392.500 (resultado: $117.500). En todo el período cargado (febrero a abril), Lucía acumula un resultado de $526.500 y Martín de $448.000.

**Observaciones financieras**
1. **Los gastos fijos pesan más de la mitad del gasto.** Alquiler (43,8 %) y servicios (10,3 %) suman 54,1 % de todos los gastos. Son difíciles de reducir, por eso conviene controlar el resto de las categorías. Alimentos es la siguiente (27,5 %).
2. **Las cuentas en efectivo se están agotando.** Solo registran gastos y ningún ingreso: el efectivo de Lucía baja de $100.000 a $19.000 y el de Martín de $150.000 a $47.000. Hay que prever una reposición desde las cuentas bancarias.
3. **Parte del gasto de marzo quedó financiada con crédito.** Hay $60.000 pagados con tarjeta de crédito (curso de inglés y cena). Ese importe se paga después y debe contarse como compromiso futuro, no como dinero libre.

**Conclusión.** En marzo hubo superávit y las cuentas bancarias tienen buen saldo, pero conviene vigilar el efectivo y los compromisos de tarjeta antes de planificar compras grandes.

---

## D) Interpretación financiera y autoevaluación

1. **¿Qué usuario registra más movimientos? ¿Administra peor su dinero?** Lucía registra 14 y Martín 10. Más movimientos no significa peor administración: solo indica que se anota más detalle. Lo que importa es la relación entre ingresos y gastos: Lucía tiene incluso el mayor resultado (526.500).
2. **Ingresos y gastos de Lucía en marzo:** ingresos $510.000 y gastos $392.500 (consulta Extra B).
3. **Categoría de mayor gasto de Lucía:** Alquiler, con $180.000 (consulta Extra C).
4. **Saldo inicial vs resultado del mes:** el saldo inicial es el dinero que tenía la cuenta al empezar a registrarse. El resultado del mes es ingresos menos gastos de ese período. El saldo actual surge de sumar ambos.
5. **Qué falta para saber si alguien puede afrontar una compra futura:** deudas y cuotas pendientes, gastos fijos de los próximos meses, ingresos esperados, el precio de la compra y si conviene dejar un fondo de reserva.
6. **Por qué no alcanza con mirar el saldo:** porque el saldo muestra lo que hay hoy, pero no lo que ya se debe ni los pagos que vencen pronto. Con mucho saldo y muchas deudas próximas, el dinero realmente disponible puede ser poco.
7. **Riesgo de registrar dos veces el mismo gasto:** los gastos y porcentajes quedan inflados, el saldo estimado queda más bajo que el real y se pueden tomar malas decisiones. La consulta 13d sirve para detectar posibles duplicados.

---

## E) Una relación y una restricción (para explicar)

**Relación:** `movimientos.id_cuenta` es clave foránea que apunta a `cuentas.id_cuenta`. Cada movimiento pertenece a una cuenta existente, y cada cuenta puede tener muchos movimientos (relación uno a muchos). Gracias a esto MySQL rechazó el movimiento con cuenta 999.

**Restricción:** `CHECK (importe > 0)` en `movimientos`. Impide guardar importes cero o negativos, ya que el tipo (ingreso o gasto) ya indica el sentido del dinero. Además, `email` es `UNIQUE` para que no haya dos usuarios con el mismo correo.

---

## F) Capturas y lista de verificación

**Capturas a tomar**
1. XAMPP con Apache y MySQL en verde.
2. phpMyAdmin con las 4 tablas de `finanzas_personales`.
3. Estructura de `movimientos` (pestaña Estructura) y relaciones (Diseñador).
4. Resultado de cada consulta (1 a 12) y de las 4 de integridad (0 filas).
5. Consulta propia entre fechas.
6. Los tres errores de la PARTE 4.

**Lista de verificación**
- [ ] Base `finanzas_personales` con las 4 tablas
- [ ] PK, FK, AUTO_INCREMENT, NOT NULL, UNIQUE, CHECK, ENUM y DECIMAL(12,2)
- [ ] 2 usuarios, 4 cuentas, 10 categorías, 24 movimientos
- [ ] Consultas 1 a 13 ejecutadas
- [ ] Consulta propia entre dos fechas
- [ ] Informe financiero (sección C)
- [ ] Dos reglas de integridad demostradas (FK y CHECK; UNIQUE como extra)
- [ ] Respuestas de autoevaluación (sección D)
- [ ] Explicación de una relación y una restricción (sección E)
- [ ] Archivo `.sql` y capturas organizados en una carpeta
