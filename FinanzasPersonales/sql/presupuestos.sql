-- Entrega 3 - Tabla presupuestos
-- ANTES DE EJECUTAR en phpMyAdmin (base finanzas_personales, pestaña SQL):
--   1) SHOW TABLES LIKE 'presupuestos';            -> debe dar 0 filas
--   2) SHOW CREATE TABLE usuarios;                 -> comprobar el tipo de id_usuario
--      SHOW CREATE TABLE categorias;               -> comprobar el tipo de id_categoria
--      (si son INT UNSIGNED, cambiar los tipos de abajo para que coincidan)
--   3) SELECT id_usuario FROM usuarios;  SELECT id_categoria, nombre, tipo_permitido FROM categorias;

CREATE TABLE IF NOT EXISTS presupuestos (
    id_presupuesto    INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario        INT NOT NULL,
    id_categoria      INT NOT NULL,
    anio              SMALLINT NOT NULL,
    mes               TINYINT NOT NULL,
    monto_planificado DECIMAL(12,2) NOT NULL,
    observaciones     VARCHAR(255) NULL,
    CONSTRAINT uq_presupuesto UNIQUE (id_usuario, id_categoria, anio, mes),
    CONSTRAINT ck_presupuesto_mes CHECK (mes BETWEEN 1 AND 12),
    CONSTRAINT ck_presupuesto_monto CHECK (monto_planificado > 0),
    CONSTRAINT fk_presupuesto_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario),
    CONSTRAINT fk_presupuesto_categoria FOREIGN KEY (id_categoria) REFERENCES categorias (id_categoria)
);

-- Datos de ejemplo (marzo 2026). AJUSTAR los IDs a los reales (categorías de tipo GASTO o AMBOS).
-- INSERT IGNORE evita duplicados gracias al UNIQUE.
INSERT IGNORE INTO presupuestos (id_usuario, id_categoria, anio, mes, monto_planificado, observaciones) VALUES
(1, 1, 2026, 3, 60000.00, 'Límite ficticio categoría 1'),
(1, 2, 2026, 3, 40000.00, 'Límite ficticio categoría 2'),
(1, 3, 2026, 3, 25000.00, 'Límite ficticio categoría 3'),
(1, 4, 2026, 3, 15000.00, 'Límite ficticio categoría 4');

-- Verificación
SELECT * FROM presupuestos WHERE anio = 2026 AND mes = 3;
