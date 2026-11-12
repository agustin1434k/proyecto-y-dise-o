<?php
declare(strict_types=1);

/**
 * reportes/index.php
 * IMPORTANTE: estos reportes son ejemplos genéricos. Pegá acá las consultas
 * de tus reportes de la Semana 1 (cada una en su propio bloque try/catch).
 * Asume clientes(id, nombre, email, ciudad) y pedidos(id, cliente_id, fecha, total).
 */
require_once __DIR__ . "/../includes/auth.php";
requiereLogin();
require_once __DIR__ . "/../config/conexion.php";
require_once __DIR__ . "/../includes/funciones.php";

$porCiudad = [];
$porCliente = [];
$totales = null;
$errorClientes = false;
$errorPedidos = false;

// Reporte 1: clientes por ciudad
try {
    $porCiudad = $pdo->query(
        "SELECT COALESCE(NULLIF(ciudad, ''), '(sin ciudad)') AS ciudad, COUNT(*) AS cantidad
         FROM clientes
         GROUP BY COALESCE(NULLIF(ciudad, ''), '(sin ciudad)')
         ORDER BY cantidad DESC, ciudad"
    )->fetchAll();
} catch (PDOException $e) {
    error_log("[gestion_clientes] reportes (por ciudad): " . $e->getMessage());
    $errorClientes = true;
}

// Reporte 2 y 3: pedidos por cliente y totales generales
try {
    $porCliente = $pdo->query(
        "SELECT c.nombre, COUNT(p.id) AS cantidad, COALESCE(SUM(p.total), 0) AS total
         FROM clientes c
         LEFT JOIN pedidos p ON p.cliente_id = c.id
         GROUP BY c.id, c.nombre
         ORDER BY total DESC, c.nombre
         LIMIT 10"
    )->fetchAll();

    $totales = $pdo->query(
        "SELECT COUNT(*) AS cantidad, COALESCE(SUM(total), 0) AS total, COALESCE(AVG(total), 0) AS promedio
         FROM pedidos"
    )->fetch();
} catch (PDOException $e) {
    error_log("[gestion_clientes] reportes (pedidos): " . $e->getMessage());
    $errorPedidos = true;
}

$tituloPagina = "Reportes";
$raiz         = "../";
$paginaActiva = "reportes";

require __DIR__ . "/../includes/header.php";
require __DIR__ . "/../includes/menu.php";
?>
<main class="contenedor">
    <?php mostrarFlash(); ?>
    <h2 class="titulo-pagina">Reportes</h2>

    <section class="tarjeta">
        <h2>Clientes por ciudad</h2>
        <?php if ($errorClientes): ?>
            <div class="alerta alerta-error">No se pudo generar este reporte.</div>
        <?php elseif (empty($porCiudad)): ?>
            <div class="alerta alerta-aviso">No hay datos para mostrar.</div>
        <?php else: ?>
            <div class="tabla-scroll">
                <table>
                    <thead><tr><th>Ciudad</th><th class="num">Clientes</th></tr></thead>
                    <tbody>
                    <?php foreach ($porCiudad as $fila): ?>
                        <tr><td><?= e($fila["ciudad"]) ?></td><td class="num"><?= (int) $fila["cantidad"] ?></td></tr>
                    <?php endforeach; ?>
                    </tbody>
                </table>
            </div>
        <?php endif; ?>
    </section>

    <section class="tarjeta">
        <h2>Totales de pedidos</h2>
        <?php if ($errorPedidos || $totales === null): ?>
            <div class="alerta alerta-error">No se pudo generar este reporte. Verificá que exista la tabla pedidos.</div>
        <?php else: ?>
            <div class="panel">
                <div class="acceso"><div class="cifra"><?= (int) $totales["cantidad"] ?></div><p>Pedidos registrados</p></div>
                <div class="acceso verde"><div class="cifra"><?= e(dinero($totales["total"])) ?></div><p>Total facturado</p></div>
                <div class="acceso ambar"><div class="cifra"><?= e(dinero($totales["promedio"])) ?></div><p>Promedio por pedido</p></div>
            </div>
        <?php endif; ?>
    </section>

    <section class="tarjeta">
        <h2>Pedidos por cliente (top 10 por monto)</h2>
        <?php if ($errorPedidos): ?>
            <div class="alerta alerta-error">No se pudo generar este reporte.</div>
        <?php elseif (empty($porCliente)): ?>
            <div class="alerta alerta-aviso">No hay datos para mostrar.</div>
        <?php else: ?>
            <div class="tabla-scroll">
                <table>
                    <thead><tr><th>Cliente</th><th class="num">Pedidos</th><th class="num">Total</th></tr></thead>
                    <tbody>
                    <?php foreach ($porCliente as $fila): ?>
                        <tr>
                            <td><?= e($fila["nombre"]) ?></td>
                            <td class="num"><?= (int) $fila["cantidad"] ?></td>
                            <td class="num"><?= e(dinero($fila["total"])) ?></td>
                        </tr>
                    <?php endforeach; ?>
                    </tbody>
                </table>
            </div>
        <?php endif; ?>
    </section>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
