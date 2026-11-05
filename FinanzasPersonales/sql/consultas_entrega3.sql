-- Consultas para las capturas en phpMyAdmin (cambiar fechas si hace falta)

-- Resumen mensual
SELECT COALESCE(SUM(CASE WHEN tipo='INGRESO' THEN importe ELSE 0 END),0) AS total_ingresos,
       COALESCE(SUM(CASE WHEN tipo='GASTO'   THEN importe ELSE 0 END),0) AS total_gastos,
       COALESCE(SUM(CASE WHEN tipo='INGRESO' THEN importe ELSE -importe END),0) AS resultado
FROM movimientos
WHERE fecha >= '2026-03-01' AND fecha < '2026-04-01';

-- Gastos por categoría con porcentaje
SELECT c.nombre AS categoria, SUM(m.importe) AS total_gastado, COUNT(*) AS cantidad,
       ROUND(SUM(m.importe)*100/(SELECT SUM(importe) FROM movimientos
             WHERE tipo='GASTO' AND fecha >= '2026-03-01' AND fecha < '2026-04-01'),2) AS porcentaje
FROM movimientos m JOIN categorias c ON c.id_categoria = m.id_categoria
WHERE m.tipo='GASTO' AND m.fecha >= '2026-03-01' AND m.fecha < '2026-04-01'
GROUP BY c.id_categoria, c.nombre ORDER BY total_gastado DESC;

-- Presupuesto vs gasto real (se asume cuentas.id_usuario)
SELECT p.id_usuario, c.nombre AS categoria, p.anio, p.mes, p.monto_planificado,
       COALESCE(g.total_gastado,0) AS gasto_real,
       p.monto_planificado - COALESCE(g.total_gastado,0) AS disponible,
       CASE WHEN COALESCE(g.total_gastado,0) > p.monto_planificado THEN 'EXCEDIDO'
            WHEN COALESCE(g.total_gastado,0) = p.monto_planificado THEN 'AL LIMITE'
            ELSE 'DENTRO DEL PRESUPUESTO' END AS estado
FROM presupuestos p
JOIN categorias c ON c.id_categoria = p.id_categoria
LEFT JOIN (SELECT cu.id_usuario, m.id_categoria, YEAR(m.fecha) AS anio, MONTH(m.fecha) AS mes,
                  SUM(m.importe) AS total_gastado
           FROM movimientos m JOIN cuentas cu ON cu.id_cuenta = m.id_cuenta
           WHERE m.tipo='GASTO'
           GROUP BY cu.id_usuario, m.id_categoria, YEAR(m.fecha), MONTH(m.fecha)) g
  ON g.id_usuario=p.id_usuario AND g.id_categoria=p.id_categoria AND g.anio=p.anio AND g.mes=p.mes
WHERE p.anio=2026 AND p.mes=3;
