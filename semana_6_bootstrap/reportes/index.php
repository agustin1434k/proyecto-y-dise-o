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
<main class="container-fluid px-3 px-lg-4 py-4">

    <div class="mb-4">
        <h1 class="h3 mb-1 titulo-seccion"><i class="bi bi-bar-chart-line me-2" aria-hidden="true"></i>Reportes</h1>
        <p class="text-body-secondary mb-0">Resúmenes de clientes y pedidos.</p>
    </div>

    <?php mostrarFlash(); ?>

    <!-- Totales -->
    <?php if ($errorPedidos || $totales === null): ?>
        <?= alertaHtml("error", "No se pudieron generar los totales de pedidos. Verificá que exista la tabla pedidos.", false) ?>
    <?php else: ?>
        <div class="row g-3 mb-4">
            <div class="col-12 col-md-6 col-xl-4"><?= tarjetaIndicador("Pedidos registrados", number_format((int) $totales["cantidad"], 0, ",", "."), "bi-cart-check", "success") ?></div>
            <div class="col-12 col-md-6 col-xl-4"><?= tarjetaIndicador("Total facturado", dinero($totales["total"]), "bi-currency-dollar", "warning") ?></div>
            <div class="col-12 col-md-12 col-xl-4"><?= tarjetaIndicador("Promedio por pedido", dinero($totales["promedio"]), "bi-graph-up", "info") ?></div>
        </div>
    <?php endif; ?>

    <div class="row g-3">
        <!-- Clientes por ciudad -->
        <div class="col-12 col-xl-5">
            <div class="card border-0 shadow-sm rounded-4 h-100">
                <div class="card-header bg-white border-0 pt-3">
                    <h2 class="h5 mb-0 titulo-seccion"><i class="bi bi-geo-alt me-2" aria-hidden="true"></i>Clientes por ciudad</h2>
                </div>
                <div class="card-body">
                    <?php if ($errorClientes): ?>
                        <?= alertaHtml("error", "No se pudo generar este reporte.", false) ?>
                    <?php elseif (empty($porCiudad)): ?>
                        <?= alertaHtml("aviso", "No hay datos para mostrar.", false) ?>
                    <?php else: ?>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover align-middle mb-0 tabla-gc">
                                <thead class="table-light"><tr><th scope="col">Ciudad</th><th scope="col" class="num">Clientes</th></tr></thead>
                                <tbody>
                                <?php foreach ($porCiudad as $fila): ?>
                                    <tr><td><?= e($fila["ciudad"]) ?></td><td class="num"><?= (int) $fila["cantidad"] ?></td></tr>
                                <?php endforeach; ?>
                                </tbody>
                            </table>
                        </div>
                    <?php endif; ?>
                </div>
            </div>
        </div>

        <!-- Pedidos por cliente -->
        <div class="col-12 col-xl-7">
            <div class="card border-0 shadow-sm rounded-4 h-100">
                <div class="card-header bg-white border-0 pt-3">
                    <h2 class="h5 mb-0 titulo-seccion"><i class="bi bi-trophy me-2" aria-hidden="true"></i>Pedidos por cliente (top 10 por monto)</h2>
                </div>
                <div class="card-body">
                    <?php if ($errorPedidos): ?>
                        <?= alertaHtml("error", "No se pudo generar este reporte.", false) ?>
                    <?php elseif (empty($porCliente)): ?>
                        <?= alertaHtml("aviso", "No hay datos para mostrar.", false) ?>
                    <?php else: ?>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover align-middle mb-0 tabla-gc">
                                <thead class="table-light"><tr><th scope="col">Cliente</th><th scope="col" class="num">Pedidos</th><th scope="col" class="num">Total</th></tr></thead>
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
                </div>
            </div>
        </div>
    </div>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
