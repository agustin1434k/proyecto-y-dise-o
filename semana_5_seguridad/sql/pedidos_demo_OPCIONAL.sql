-- OPCIONAL: pedidos ficticios para probar los reportes.
-- Requiere la tabla pedidos y clientes con id del 1 al 8
-- (los clientes demo de la Semana 2 cumplen esto). Ejecutar UNA sola vez.
USE gestion_clientes;

INSERT INTO pedidos (cliente_id, fecha, total) VALUES
 (1, '2026-10-05', 15200.00),
 (1, '2026-10-21', 8300.50),
 (2, '2026-10-07', 22000.00),
 (3, '2026-10-12', 4500.00),
 (3, '2026-11-02', 17850.75),
 (4, '2026-11-03', 9900.00),
 (5, '2026-11-04', 31500.00),
 (6, '2026-11-05', 6200.00),
 (7, '2026-11-05', 12750.00),
 (8, '2026-11-06', 5400.00),
 (2, '2026-11-06', 19800.00),
 (5, '2026-11-07', 7300.25);
