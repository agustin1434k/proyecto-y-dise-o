<?php
declare(strict_types=1);

/**
 * index.php - Panel de control (dashboard).
 * Los indicadores salen de MariaDB con COUNT(*) y SUM(); no hay números escritos a mano.
 * Asume pedidos(id, cliente_id, fecha, total) y clientes(id, nombre, ...).
 */
require_once __DIR__ . "/includes/auth.php";
requiereLogin();
require_once __DIR__ . "/config/conexion.php";
require_once __DIR__ . "/includes/funciones.php";

$totalClientes = null;
$totalPedidos  = null;
$totalVentas   = null;
$ultimos       = [];
$errorClientes = false;
$errorPedidos  = false;

try {
    $totalClientes = (int) $pdo->query("SELECT COUNT(*) FROM clientes")->fetchColumn();
} catch (PDOException $e) {
    error_log("[gestion_clientes] index.php (clientes): " . $e->getMessage());
    $errorClientes = true;
}

try {
    $totalPedidos = (int) $pdo->query("SELECT COUNT(*) FROM pedidos")->fetchColumn();
    // COALESCE evita NULL cuando todavía no hay pedidos
    $totalVentas  = (float) $pdo->query("SELECT COALESCE(SUM(total), 0) FROM pedidos")->fetchColumn();
    $ultimos = $pdo->query(
        "SELECT p.id, p.fecha, c.nombre AS cliente, p.total
         FROM pedidos p
         INNER JOIN clientes c ON c.id = p.cliente_id
         ORDER BY p.fecha DESC, p.id DESC
         LIMIT 5"
    )->fetchAll();
} catch (PDOException $e) {
    error_log("[gestion_clientes] index.php (pedidos): " . $e->getMessage());
    $errorPedidos = true;
}

$sistemaOk = !$errorClientes && !$errorPedidos;

$tituloPagina = "Panel de control";
$raiz         = "";
$paginaActiva = "inicio";

require __DIR__ . "/includes/header.php";
require __DIR__ . "/includes/menu.php";
?>
<main class="container-fluid px-3 px-lg-4 py-4">

    <div class="d-flex flex-wrap justify-content-between align-items-start gap-2 mb-4">
        <div>
            <h1 class="h3 mb-1 titulo-seccion"><i class="bi bi-speedometer2 me-2" aria-hidden="true"></i>Panel de control</h1>
            <p class="text-body-secondary mb-0">Resumen general de actividad</p>
        </div>
        <div>
            <?php if ($sistemaOk): ?>
                <span class="badge text-bg-success fs-6 fw-normal"><i class="bi bi-check-circle-fill me-1" aria-hidden="true"></i>Sistema operativo</span>
            <?php else: ?>
                <span class="badge text-bg-warning fs-6 fw-normal"><i class="bi bi-exclamation-triangle-fill me-1" aria-hidden="true"></i>Sistema con inconvenientes</span>
            <?php endif; ?>
        </div>
    </div>

    <?php mostrarFlash(); ?>
    <?php if (!$sistemaOk): ?>
        <?= alertaHtml("aviso", "Algunos datos no se pudieron cargar. Verificá que MySQL esté iniciado y que existan las tablas clientes y pedidos.", false) ?>
    <?php endif; ?>

    <!-- Indicadores: 1 por fila en celular, 2 en tablet, 3 en escritorio -->
    <div class="row g-3 mb-4">
        <div class="col-12 col-md-6 col-xl-4">
            <?= tarjetaIndicador("Total de clientes", $totalClientes === null ? "—" : number_format($totalClientes, 0, ",", "."), "bi-people-fill", "primary", "Clientes registrados") ?>
        </div>
        <div class="col-12 col-md-6 col-xl-4">
            <?= tarjetaIndicador("Total de pedidos", $totalPedidos === null ? "—" : number_format($totalPedidos, 0, ",", "."), "bi-cart-check", "success", "Pedidos registrados") ?>
        </div>
        <div class="col-12 col-md-12 col-xl-4">
            <?= tarjetaIndicador("Total de ventas", $totalVentas === null ? "—" : dinero($totalVentas), "bi-currency-dollar", "warning", "Importe total vendido") ?>
        </div>
    </div>

    <!-- Últimos pedidos -->
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-header bg-white border-0 pt-3 d-flex flex-wrap justify-content-between align-items-center gap-2">
            <h2 class="h5 mb-0 titulo-seccion"><i class="bi bi-clock-history me-2" aria-hidden="true"></i>Últimos pedidos</h2>
            <a class="btn btn-outline-primary btn-sm" href="pedidos/listar.php"><i class="bi bi-list-ul me-1" aria-hidden="true"></i>Ver todos</a>
        </div>
        <div class="card-body">
            <?php if ($errorPedidos): ?>
                <?= alertaHtml("error", "No se pudieron consultar los pedidos.", false) ?>
            <?php elseif (empty($ultimos)): ?>
                <div class="text-center text-body-secondary py-4">
                    <i class="bi bi-inbox fs-1 d-block mb-2" aria-hidden="true"></i>
                    Todavía no hay pedidos registrados.
                </div>
            <?php else: ?>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0 tabla-gc">
                        <thead class="table-light">
                            <tr><th scope="col">N.º de pedido</th><th scope="col">Fecha</th><th scope="col">Cliente</th><th scope="col" class="num">Importe</th></tr>
                        </thead>
                        <tbody>
                        <?php foreach ($ultimos as $p): ?>
                            <tr>
                                <td>#<?= (int) $p["id"] ?></td>
                                <td><?= e(fechaLegible((string) $p["fecha"])) ?></td>
                                <td><?= e($p["cliente"]) ?></td>
                                <td class="num"><?= e(dinero($p["total"])) ?></td>
                            </tr>
                        <?php endforeach; ?>
                        </tbody>
                    </table>
                </div>
            <?php endif; ?>
        </div>
    </div>

</main>
<?php require __DIR__ . "/includes/footer.php"; ?>
