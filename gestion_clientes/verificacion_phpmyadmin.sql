-- Ejecutar en phpMyAdmin (base gestion_clientes, pestaña SQL) y comparar con reportes.php.
-- Solo lectura. Ajustar nombres de columnas si en tu base son distintos.

SHOW TABLES;
DESCRIBE clientes;
DESCRIBE pedidos;

-- Reporte 3
SELECT COUNT(*) AS cantidad, SUM(importe) AS total, AVG(importe) AS promedio,
       MIN(importe) AS minimo, MAX(importe) AS maximo FROM pedidos;

-- Reporte 1 (clientes sin pedidos deben aparecer con 0)
SELECT c.nombre, COUNT(p.id) AS cantidad_pedidos
FROM clientes c LEFT JOIN pedidos p ON p.cliente_id = c.id
GROUP BY c.id, c.nombre ORDER BY cantidad_pedidos DESC, c.nombre;

-- Reporte 2
SELECT c.nombre, SUM(p.importe) AS total_comprado
FROM clientes c INNER JOIN pedidos p ON p.cliente_id = c.id
GROUP BY c.id, c.nombre ORDER BY total_comprado DESC;

-- Reporte 4
SELECT p.id, c.nombre, p.fecha, p.importe
FROM pedidos p INNER JOIN clientes c ON c.id = p.cliente_id
WHERE p.importe > (SELECT AVG(importe) FROM pedidos) ORDER BY p.importe DESC;

-- Control: ningún pedido debería quedar sin cliente (integridad)
SELECT COUNT(*) AS pedidos_huerfanos FROM pedidos p
LEFT JOIN clientes c ON c.id = p.cliente_id WHERE c.id IS NULL;

-- Control: la suma de los totales por cliente debe igualar el total general
SELECT SUM(total) FROM (SELECT SUM(importe) AS total FROM pedidos GROUP BY cliente_id) t;
