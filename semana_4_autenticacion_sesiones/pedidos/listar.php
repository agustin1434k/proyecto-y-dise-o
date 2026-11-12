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
<main class="contenedor">
    <?php mostrarFlash(); ?>

    <section class="tarjeta">
        <div class="barra-superior">
            <h2>Pedidos</h2>
            <a class="btn btn-verde btn-chico" href="nuevo.php">+ Nuevo pedido</a>
        </div>
        <form class="filtros-simple" method="GET" action="listar.php">
            <div class="campo">
                <label for="cliente_id">Filtrar por cliente</label>
                <select id="cliente_id" name="cliente_id">
                    <option value="">Todos</option>
                    <?php foreach ($clientes as $c): ?>
                        <option value="<?= (int) $c["id"] ?>" <?= $clienteId === (int) $c["id"] ? "selected" : "" ?>>
                            <?= e($c["nombre"]) ?>
                        </option>
                    <?php endforeach; ?>
                </select>
            </div>
            <div class="acciones-form">
                <button type="submit" class="btn btn-primario">FILTRAR</button>
                <a class="btn btn-secundario" href="listar.php">LIMPIAR</a>
            </div>
        </form>
    </section>

    <section class="tarjeta">
        <?php if ($errorBD): ?>
            <div class="alerta alerta-error">
                No se pudieron consultar los pedidos. Verificá que exista la tabla <em>pedidos</em>
                y que MySQL esté iniciado.
            </div>
        <?php else: ?>
            <div class="resumen"><span>Pedidos encontrados: <strong><?= $totalRegistros ?></strong></span></div>

            <?php if (empty($pedidos)): ?>
                <div class="alerta alerta-aviso"><strong>No se encontraron pedidos.</strong></div>
            <?php else: ?>
                <div class="tabla-scroll">
                    <table>
                        <thead><tr><th>ID</th><th>Fecha</th><th>Cliente</th><th class="num">Total</th></tr></thead>
                        <tbody>
                        <?php foreach ($pedidos as $p): ?>
                            <tr>
                                <td><?= (int) $p["id"] ?></td>
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
    </section>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
