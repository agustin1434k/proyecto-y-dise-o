<?php
declare(strict_types=1);

/**
 * pedidos/listar.php
 * ADAPTAR: asume la tabla pedidos(id, cliente_id, fecha, total).
 * Si tu tabla tiene otras columnas, cambiá las consultas de este archivo.
 */
require_once __DIR__ . "/../includes/auth.php";
requiereLogin();
require_once __DIR__ . "/../config/conexion.php";
require_once __DIR__ . "/../includes/funciones.php";

$clienteId = idPositivo($_GET["cliente_id"] ?? null);
$porPagina = 10;
$pagina = filter_input(INPUT_GET, "pagina", FILTER_VALIDATE_INT);
$pagina = ($pagina && $pagina > 0) ? $pagina : 1;

$where = "";
$parametros = [];
if ($clienteId !== null) {
    $where = " WHERE p.cliente_id = :cliente_id";
    $parametros[":cliente_id"] = $clienteId;
}

$pedidos = [];
$clientes = [];
$totalRegistros = 0;
$totalPaginas = 1;
$errorBD = false;

try {
    $stmtCount = $pdo->prepare("SELECT COUNT(*) FROM pedidos p" . $where);
    foreach ($parametros as $nombre => $valor) {
        $stmtCount->bindValue($nombre, $valor, PDO::PARAM_INT);
    }
    $stmtCount->execute();
    $totalRegistros = (int) $stmtCount->fetchColumn();
    $totalPaginas = max(1, (int) ceil($totalRegistros / $porPagina));
    if ($pagina > $totalPaginas) {
        $pagina = $totalPaginas;
    }
    $inicio = ($pagina - 1) * $porPagina;

    $stmt = $pdo->prepare(
        "SELECT p.id, p.fecha, p.total, c.nombre AS cliente
         FROM pedidos p
         JOIN clientes c ON c.id = p.cliente_id" . $where . "
         ORDER BY p.fecha DESC, p.id DESC
         LIMIT :inicio, :cantidad"
    );
    foreach ($parametros as $nombre => $valor) {
        $stmt->bindValue($nombre, $valor, PDO::PARAM_INT);
    }
    $stmt->bindValue(":inicio", $inicio, PDO::PARAM_INT);
    $stmt->bindValue(":cantidad", $porPagina, PDO::PARAM_INT);
    $stmt->execute();
    $pedidos = $stmt->fetchAll();

    $clientes = $pdo->query("SELECT id, nombre FROM clientes ORDER BY nombre")->fetchAll();
} catch (PDOException $e) {
    error_log("[gestion_clientes] pedidos/listar.php: " . $e->getMessage());
    $errorBD = true;
}

$filtros = $clienteId !== null ? ["cliente_id" => $clienteId] : [];

$tituloPagina = "Pedidos";
$raiz         = "../";
$paginaActiva = "pedidos";

require __DIR__ . "/../includes/header.php";
require __DIR__ . "/../includes/menu.php";
?>
<main class="container-fluid px-3 px-lg-4 py-4">

    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
        <div>
            <h1 class="h3 mb-1 titulo-seccion"><i class="bi bi-cart-check me-2" aria-hidden="true"></i>Pedidos</h1>
            <p class="text-body-secondary mb-0">Pedidos registrados por los clientes.</p>
        </div>
        <a class="btn btn-success" href="nuevo.php"><i class="bi bi-plus-circle me-1" aria-hidden="true"></i>Nuevo pedido</a>
    </div>

    <?php mostrarFlash(); ?>

    <div class="card border-0 shadow-sm rounded-4 mb-3">
        <div class="card-body">
            <form class="row g-3 align-items-end" method="GET" action="listar.php">
                <div class="col-12 col-md-6 col-lg-4">
                    <label for="cliente_id" class="form-label">Filtrar por cliente</label>
                    <select class="form-select" id="cliente_id" name="cliente_id">
                        <option value="">Todos</option>
                        <?php foreach ($clientes as $c): ?>
                            <option value="<?= (int) $c["id"] ?>" <?= $clienteId === (int) $c["id"] ? "selected" : "" ?>>
                                <?= e($c["nombre"]) ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </div>
                <div class="col-12 col-md-6 col-lg-4 d-flex gap-2">
                    <button type="submit" class="btn btn-primary"><i class="bi bi-funnel me-1" aria-hidden="true"></i>FILTRAR</button>
                    <a class="btn btn-outline-secondary" href="listar.php"><i class="bi bi-x-circle me-1" aria-hidden="true"></i>LIMPIAR</a>
                </div>
            </form>
        </div>
    </div>

    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-body">
            <?php if ($errorBD): ?>
                <?= alertaHtml("error", "No se pudieron consultar los pedidos. Verificá que exista la tabla pedidos y que MySQL esté iniciado.", false) ?>
            <?php else: ?>
                <p class="text-body-secondary mb-3">Pedidos encontrados: <span class="badge text-bg-primary"><?= (int) $totalRegistros ?></span></p>

                <?php if (empty($pedidos)): ?>
                    <?= alertaHtml("aviso", "No se encontraron pedidos.", false) ?>
                <?php else: ?>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover align-middle mb-0 tabla-gc">
                            <thead class="table-light">
                                <tr><th scope="col">N.º</th><th scope="col">Fecha</th><th scope="col">Cliente</th><th scope="col" class="num">Total</th></tr>
                            </thead>
                            <tbody>
                            <?php foreach ($pedidos as $p): ?>
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

                <?= htmlPaginacion("listar.php", $filtros, $pagina, $totalPaginas) ?>
            <?php endif; ?>
        </div>
    </div>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
