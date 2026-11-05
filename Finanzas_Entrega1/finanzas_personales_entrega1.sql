-- =====================================================================
-- FINANZAS PERSONALES INTELIGENTES - ENTREGA 1
-- Base de datos: finanzas_personales (MySQL / MariaDB, XAMPP, phpMyAdmin)
--
-- SEGURIDAD: este script NO contiene DROP, DELETE ni TRUNCATE.
-- Usa CREATE ... IF NOT EXISTS, por lo que no borra tablas ni datos existentes.
-- Las PARTES 1 y 2 crean la estructura y cargan datos. Ejecutarlas dos veces
-- repetiría los INSERT (los emails de usuarios son UNIQUE y darían error).
-- =====================================================================

-- ===================== PARTE 1: ESTRUCTURA ===========================
CREATE DATABASE IF NOT EXISTS finanzas_personales
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE finanzas_personales;

CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario  INT AUTO_INCREMENT PRIMARY KEY,
  nombre      VARCHAR(80)  NOT NULL,
  email       VARCHAR(120) NOT NULL UNIQUE,
  fecha_alta  DATE         NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cuentas (
  id_cuenta     INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario    INT          NOT NULL,
  nombre        VARCHAR(60)  NOT NULL,
  tipo          ENUM('efectivo','banco','billetera_virtual','tarjeta') NOT NULL,
  saldo_inicial DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  activa        TINYINT(1)   NOT NULL DEFAULT 1,
  CONSTRAINT chk_cuentas_saldo CHECK (saldo_inicial >= 0),
  CONSTRAINT uq_cuenta_usuario UNIQUE (id_usuario, nombre),
  CONSTRAINT fk_cuentas_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categorias (
  id_categoria   INT AUTO_INCREMENT PRIMARY KEY,
  nombre         VARCHAR(60) NOT NULL UNIQUE,
  tipo_permitido ENUM('ingreso','gasto','ambos') NOT NULL,
  descripcion    VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS movimientos (
  id_movimiento  INT AUTO_INCREMENT PRIMARY KEY,
  id_cuenta      INT NOT NULL,
  id_categoria   INT NOT NULL,
  tipo           ENUM('ingreso','gasto') NOT NULL,
  fecha          DATE NOT NULL,
  importe        DECIMAL(12,2) NOT NULL,
  descripcion    VARCHAR(200) NOT NULL,
  medio_pago     ENUM('efectivo','debito','credito','transferencia','billetera_virtual') NOT NULL,
  es_recurrente  TINYINT(1) NOT NULL DEFAULT 0,
  fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_mov_importe CHECK (importe > 0),
  CONSTRAINT fk_mov_cuenta    FOREIGN KEY (id_cuenta)    REFERENCES cuentas(id_cuenta),
  CONSTRAINT fk_mov_categoria FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
) ENGINE=InnoDB;

-- ===================== PARTE 2: DATOS DE PRUEBA ======================
-- (datos ficticios: 2 usuarios, 4 cuentas, 10 categorías, 24 movimientos)

INSERT INTO usuarios (nombre, email, fecha_alta) VALUES
  ('Lucía Fernández', 'lucia.fernandez@mail.com', '2026-01-10'),
  ('Martín Gómez', 'martin.gomez@mail.com', '2026-01-15');

INSERT INTO cuentas (id_usuario, nombre, tipo, saldo_inicial, activa) VALUES
  (1, 'Efectivo', 'efectivo', 100000.00, 1),
  (1, 'Banco Galicia', 'banco', 120000.00, 1),
  (2, 'Efectivo', 'efectivo', 150000.00, 1),
  (2, 'Banco Nación', 'banco', 90000.00, 1);

INSERT INTO categorias (nombre, tipo_permitido, descripcion) VALUES
  ('Sueldo', 'ingreso', 'Remuneración mensual por trabajo en relación de dependencia'),
  ('Freelance', 'ingreso', 'Ingresos por trabajos independientes'),
  ('Alimentos', 'gasto', 'Supermercado, verdulería y carnicería'),
  ('Transporte', 'gasto', 'Transporte público y combustible'),
  ('Alquiler', 'gasto', 'Pago mensual de la vivienda'),
  ('Servicios', 'gasto', 'Luz, gas, agua, internet y celular'),
  ('Salud', 'gasto', 'Consultas médicas y farmacia'),
  ('Ocio', 'gasto', 'Salidas, cine y entretenimiento'),
  ('Educación', 'gasto', 'Cursos y material de estudio'),
  ('Reintegros', 'ambos', 'Reintegros y devoluciones de dinero');

INSERT INTO movimientos (id_cuenta, id_categoria, tipo, fecha, importe, descripcion, medio_pago, es_recurrente) VALUES
  (2, 1, 'ingreso', '2026-03-05', 450000.00, 'Sueldo de marzo', 'transferencia', 1),
  (2, 2, 'ingreso', '2026-03-18', 60000.00, 'Trabajo freelance diseño', 'transferencia', 0),
  (2, 5, 'gasto', '2026-03-06', 180000.00, 'Alquiler de marzo', 'transferencia', 1),
  (2, 6, 'gasto', '2026-03-10', 32000.00, 'Electricidad y gas', 'debito', 1),
  (2, 6, 'gasto', '2026-03-12', 18500.00, 'Internet y celular', 'debito', 1),
  (1, 3, 'gasto', '2026-03-08', 28000.00, 'Supermercado semanal', 'efectivo', 0),
  (1, 3, 'gasto', '2026-03-15', 31500.00, 'Supermercado quincenal', 'efectivo', 0),
  (2, 3, 'gasto', '2026-03-22', 24000.00, 'Verdulería y carnicería', 'debito', 0),
  (1, 4, 'gasto', '2026-03-09', 9500.00, 'Carga SUBE', 'efectivo', 0),
  (2, 7, 'gasto', '2026-03-14', 22000.00, 'Consulta médica', 'debito', 0),
  (2, 9, 'gasto', '2026-03-20', 35000.00, 'Curso de inglés', 'credito', 1),
  (1, 8, 'gasto', '2026-03-25', 12000.00, 'Cine y salida', 'efectivo', 0),
  (2, 1, 'ingreso', '2026-02-05', 450000.00, 'Sueldo de febrero', 'transferencia', 1),
  (2, 3, 'gasto', '2026-02-20', 41000.00, 'Supermercado mensual', 'debito', 0),
  (4, 1, 'ingreso', '2026-03-04', 380000.00, 'Sueldo de marzo', 'transferencia', 1),
  (4, 5, 'gasto', '2026-03-07', 150000.00, 'Alquiler de marzo', 'transferencia', 1),
  (4, 6, 'gasto', '2026-03-11', 27000.00, 'Luz, agua e internet', 'debito', 1),
  (3, 3, 'gasto', '2026-03-13', 45000.00, 'Supermercado', 'efectivo', 0),
  (3, 4, 'gasto', '2026-03-16', 20000.00, 'Nafta', 'efectivo', 0),
  (4, 8, 'gasto', '2026-03-21', 25000.00, 'Cena con amigos', 'credito', 0),
  (4, 10, 'ingreso', '2026-03-24', 8000.00, 'Reintegro de tarjeta', 'transferencia', 0),
  (4, 7, 'gasto', '2026-03-27', 15000.00, 'Farmacia', 'debito', 0),
  (4, 1, 'ingreso', '2026-04-04', 380000.00, 'Sueldo de abril', 'transferencia', 1),
  (3, 3, 'gasto', '2026-04-10', 38000.00, 'Supermercado', 'efectivo', 0);

-- ===================== PARTE 3: CONSULTAS ============================
-- Ejecutá cada consulta por separado (seleccionala y presioná Continuar).

-- 1. Listado de usuarios
-- Muestra todas las columnas de todos los usuarios.
SELECT * FROM usuarios;

-- 2. Listado de movimientos
-- Muestra todos los movimientos ordenados por fecha.
SELECT * FROM movimientos ORDER BY fecha, id_movimiento;

-- 3. Filtrar movimientos por tipo
-- Muestra solo los gastos (cambiá 'gasto' por 'ingreso' para ver los ingresos).
SELECT * FROM movimientos WHERE tipo = 'gasto' ORDER BY fecha;

-- 4. Gastos de 20.000 o más
-- Combina dos condiciones con AND: tipo gasto e importe mayor o igual a 20000.
SELECT * FROM movimientos WHERE tipo = 'gasto' AND importe >= 20000 ORDER BY importe DESC;

-- 5. Búsqueda con LIKE
-- Busca movimientos cuya descripción contenga la palabra 'Supermercado'. El % reemplaza cualquier texto.
SELECT * FROM movimientos WHERE descripcion LIKE '%Supermercado%';

-- 6. Contar movimientos con COUNT(*)
-- Cuenta cuántas filas tiene la tabla movimientos.
SELECT COUNT(*) AS total_movimientos FROM movimientos;

-- 7. Totales por tipo (SUM y GROUP BY)
-- Agrupa los movimientos por tipo y suma sus importes.
SELECT tipo, COUNT(*) AS cantidad, SUM(importe) AS total FROM movimientos GROUP BY tipo;

-- 8. Gastos por categoría (JOIN)
-- Une movimientos con categorias para mostrar el nombre de la categoría y suma los gastos.
SELECT c.nombre AS categoria, SUM(m.importe) AS total_gastado
FROM movimientos m
JOIN categorias c ON c.id_categoria = m.id_categoria
WHERE m.tipo = 'gasto'
GROUP BY c.id_categoria, c.nombre
ORDER BY total_gastado DESC;

-- 9. Totales de marzo
-- Suma ingresos y gastos de marzo de 2026.
SELECT tipo, SUM(importe) AS total_marzo
FROM movimientos
WHERE fecha BETWEEN '2026-03-01' AND '2026-03-31'
GROUP BY tipo;

-- 10. Resultado por usuario
-- Para cada usuario calcula ingresos, gastos y resultado (ingresos menos gastos). Usa tres tablas unidas con JOIN.
SELECT u.nombre AS usuario,
  SUM(CASE WHEN m.tipo = 'ingreso' THEN m.importe ELSE 0 END) AS ingresos,
  SUM(CASE WHEN m.tipo = 'gasto' THEN m.importe ELSE 0 END) AS gastos,
  SUM(CASE WHEN m.tipo = 'ingreso' THEN m.importe ELSE -m.importe END) AS resultado
FROM usuarios u
JOIN cuentas c ON c.id_usuario = u.id_usuario
JOIN movimientos m ON m.id_cuenta = c.id_cuenta
GROUP BY u.id_usuario, u.nombre;

-- 11. Porcentaje de gastos por categoría
-- Calcula qué porcentaje del gasto total representa cada categoría.
SELECT c.nombre AS categoria,
  SUM(m.importe) AS total_gastado,
  ROUND(SUM(m.importe) * 100.0 / (SELECT SUM(importe) FROM movimientos WHERE tipo = 'gasto'), 2) AS porcentaje
FROM movimientos m
JOIN categorias c ON c.id_categoria = m.id_categoria
WHERE m.tipo = 'gasto'
GROUP BY c.id_categoria, c.nombre
ORDER BY total_gastado DESC;

-- 12. Saldo estimado de cada cuenta
-- Saldo estimado = saldo inicial + ingresos - gastos de esa cuenta. LEFT JOIN incluye cuentas sin movimientos.
SELECT c.id_cuenta, u.nombre AS usuario, c.nombre AS cuenta, c.saldo_inicial,
  COALESCE(SUM(CASE WHEN m.tipo = 'ingreso' THEN m.importe ELSE -m.importe END), 0) AS movimiento_neto,
  c.saldo_inicial + COALESCE(SUM(CASE WHEN m.tipo = 'ingreso' THEN m.importe ELSE -m.importe END), 0) AS saldo_estimado
FROM cuentas c
JOIN usuarios u ON u.id_usuario = c.id_usuario
LEFT JOIN movimientos m ON m.id_cuenta = c.id_cuenta
GROUP BY c.id_cuenta, u.nombre, c.nombre, c.saldo_inicial
ORDER BY c.id_cuenta;

-- 13a. Integridad: movimientos sin cuenta
-- Debe devolver 0 filas: todo movimiento debe pertenecer a una cuenta existente.
SELECT m.*
FROM movimientos m
LEFT JOIN cuentas c ON c.id_cuenta = m.id_cuenta
WHERE c.id_cuenta IS NULL;

-- 13b. Integridad: importes no positivos
-- Debe devolver 0 filas.
SELECT * FROM movimientos WHERE importe <= 0;

-- 13c. Integridad: tipo incompatible con la categoría
-- Debe devolver 0 filas: un gasto no debería usar una categoría solo de ingresos (ni al revés).
SELECT m.id_movimiento, m.tipo, c.nombre AS categoria, c.tipo_permitido
FROM movimientos m
JOIN categorias c ON c.id_categoria = m.id_categoria
WHERE c.tipo_permitido <> 'ambos' AND c.tipo_permitido <> m.tipo;

-- 13d. Integridad: posibles movimientos duplicados
-- Debe devolver 0 filas: busca registros con misma cuenta, fecha, importe y descripción.
SELECT id_cuenta, fecha, importe, descripcion, COUNT(*) AS repeticiones
FROM movimientos
GROUP BY id_cuenta, fecha, importe, descripcion
HAVING COUNT(*) > 1;

-- Extra A. Movimientos por usuario
-- Responde qué usuario registra más movimientos.
SELECT u.nombre AS usuario, COUNT(*) AS cantidad_movimientos
FROM usuarios u
JOIN cuentas c ON c.id_usuario = u.id_usuario
JOIN movimientos m ON m.id_cuenta = c.id_cuenta
GROUP BY u.id_usuario, u.nombre;

-- Extra B. Ingresos y gastos de Lucía en marzo
-- Filtra por usuario y por mes.
SELECT m.tipo, SUM(m.importe) AS total
FROM movimientos m
JOIN cuentas c ON c.id_cuenta = m.id_cuenta
JOIN usuarios u ON u.id_usuario = c.id_usuario
WHERE u.nombre LIKE 'Luc%' AND m.fecha BETWEEN '2026-03-01' AND '2026-03-31'
GROUP BY m.tipo;

-- Extra C. Gasto por categoría de Lucía
-- Muestra la categoría con mayor gasto total de Lucía.
SELECT cat.nombre AS categoria, SUM(m.importe) AS total_gastado
FROM movimientos m
JOIN cuentas c ON c.id_cuenta = m.id_cuenta
JOIN usuarios u ON u.id_usuario = c.id_usuario
JOIN categorias cat ON cat.id_categoria = m.id_categoria
WHERE u.nombre LIKE 'Luc%' AND m.tipo = 'gasto'
GROUP BY cat.id_categoria, cat.nombre
ORDER BY total_gastado DESC;

-- Consulta propia: movimientos entre dos fechas
-- Filtra movimientos entre el 10 y el 20 de marzo de 2026 con BETWEEN. Cambiá las fechas para otro período.
SELECT id_movimiento, fecha, tipo, importe, descripcion
FROM movimientos
WHERE fecha BETWEEN '2026-03-10' AND '2026-03-20'
ORDER BY fecha;

-- ===================== PARTE 4: PRUEBAS DE INTEGRIDAD =================
-- Estas sentencias DEBEN FALLAR. Ejecutalas de a una: ninguna modifica datos
-- porque MySQL las rechaza. Sacá una captura del mensaje de error de cada una.

-- Prueba 1 (clave foránea): la cuenta 999 no existe
-- Error esperado: #1452 - Cannot add or update a child row: a foreign key constraint fails
INSERT INTO movimientos (id_cuenta, id_categoria, tipo, fecha, importe, descripcion, medio_pago)
VALUES (999, 3, 'gasto', '2026-03-30', 5000.00, 'Prueba cuenta inexistente', 'efectivo');

-- Prueba 2 (CHECK): importe negativo
-- Error esperado: #4025 / #3819 - CONSTRAINT chk_mov_importe failed (según MariaDB o MySQL)
INSERT INTO movimientos (id_cuenta, id_categoria, tipo, fecha, importe, descripcion, medio_pago)
VALUES (1, 3, 'gasto', '2026-03-30', -100.00, 'Prueba importe negativo', 'efectivo');

-- Prueba 3 (UNIQUE): email repetido
-- Error esperado: #1062 - Duplicate entry 'lucia.fernandez@mail.com' for key 'email'
INSERT INTO usuarios (nombre, email, fecha_alta)
VALUES ('Otra Persona', 'lucia.fernandez@mail.com', '2026-03-30');
