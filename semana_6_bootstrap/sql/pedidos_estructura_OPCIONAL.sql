-- OPCIONAL: ejecutar SOLO si tu base NO tiene la tabla pedidos.
-- Si ya tenés una tabla pedidos, no lo ejecutes: avisame su estructura
-- (phpMyAdmin > tabla pedidos > pestaña Estructura) y adapto el código.
-- No borra nada (CREATE TABLE IF NOT EXISTS).
USE gestion_clientes;

CREATE TABLE IF NOT EXISTS pedidos (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    cliente_id INT NOT NULL,
    fecha      DATE NOT NULL,
    total      DECIMAL(10,2) NOT NULL,
    CONSTRAINT chk_total CHECK (total >= 0),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
) ENGINE=InnoDB;
