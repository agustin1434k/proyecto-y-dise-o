<?php
declare(strict_types=1);

require_once __DIR__ . "/../includes/auth.php";
requiereLogin();
require_once __DIR__ . "/../config/conexion.php";
require_once __DIR__ . "/../includes/funciones.php";

/* ---- 1. Parámetros (GET) ---- */
$buscar = mb_substr(textoGet("buscar"), 0, 100); // largo máximo de la búsqueda
$ciudad = mb_substr(textoGet("ciudad"), 0, 80);
$orden  = strtolower(textoGet("orden")) === "desc" ? "DESC" : "ASC";

$porPagina = filter_input(INPUT_GET, "por_pagina", FILTER_VALIDATE_INT);
if (!in_array($porPagina, [5, 10, 20], true)) {
    $porPagina = 10;
}
$pagina = filter_input(INPUT_GET, "pagina", FILTER_VALIDATE_INT);
$pagina = ($pagina && $pagina > 0) ? $pagina : 1;

/* ---- 2. Condiciones dinámicas ---- */
$condiciones = [];
$parametros  = [];

if ($buscar !== "") {
    $patron = "%" . addcslashes($buscar, "\\%_") . "%";
    $condiciones[] = "(nombre LIKE :buscar_nombre OR email LIKE :buscar_email)";
    $parametros[":buscar_nombre"] = $patron;
    $parametros[":buscar_email"]  = $patron;
}
if ($ciudad !== "") {
    $condiciones[] = "ciudad = :ciudad";
    $parametros[":ciudad"] = $ciudad;
}
$where = empty($condiciones) ? "" : " WHERE " . implode(" AND ", $condiciones);

/* ---- 3. Consultas ---- */
$clientes = [];
$ciudades = [];
$totalRegistros = 0;
$totalPaginas = 1;
$inicio = 0;
$errorBD = false;

try {
    $stmtCount = $pdo->prepare("SELECT COUNT(*) FROM clientes" . $where);
    foreach ($parametros as $nombre => $valor) {
        $stmtCount->bindValue($nombre, $valor);
    }
    $stmtCount->execute();
    $totalRegistros = (int) $stmtCount->fetchColumn();

    $totalPaginas = max(1, (int) ceil($totalRegistros / $porPagina));
    if ($pagina > $totalPaginas) {
        $pagina = $totalPaginas;
    }
    $inicio = ($pagina - 1) * $porPagina;

    // $orden solo puede ser "ASC" o "DESC" (validado arriba)
    $stmt = $pdo->prepare(
        "SELECT id, nombre, email, ciudad FROM clientes" . $where .
        " ORDER BY nombre " . $orden . ", id ASC LIMIT :inicio, :cantidad"
    );
    foreach ($parametros as $nombre => $valor) {
        $stmt->bindValue($nombre, $valor);
    }
    $stmt->bindValue(":inicio", $inicio, PDO::PARAM_INT);
    $stmt->bindValue(":cantidad", $porPagina, PDO::PARAM_INT);
    $stmt->execute();
    $clientes = $stmt->fetchAll();

    $ciudades = $pdo->query(
        "SELECT DISTINCT ciudad FROM clientes WHERE ciudad IS NOT NULL AND ciudad <> '' ORDER BY ciudad"
    )->fetchAll();
} catch (PDOException $e) {
    error_log("[gestion_clientes] clientes/listar.php: " . $e->getMessage());
    $errorBD = true;
}

$filtros = ["buscar" => $buscar, "ciudad" => $ciudad, "por_pagina" => $porPagina, "orden" => strtolower($orden)];
$mostrandoDesde = $totalRegistros === 0 ? 0 : $inicio + 1;
$mostrandoHasta = $inicio + count($clientes);
$hayFiltros = ($buscar !== "" || $ciudad !== "");

$tituloPagina = "Clientes";
$raiz         = "../";
$paginaActiva = "clientes";

require __DIR__ . "/../includes/header.php";
require __DIR__ . "/../includes/menu.php";
?>
<main class="container-fluid px-3 px-lg-4 py-4">

    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
        <div>
            <h1 class="h3 mb-1 titulo-seccion"><i class="bi bi-people-fill me-2" aria-hidden="true"></i>Clientes</h1>
            <p class="text-body-secondary mb-0">Buscá, filtrá y administrá tus clientes.</p>
        </div>
        <a class="btn btn-success" href="nuevo.php"><i class="bi bi-person-plus-fill me-1" aria-hidden="true"></i>Nuevo cliente</a>
    </div>

    <?php mostrarFlash(); ?>

    <!-- Filtros -->
    <div class="card border-0 shadow-sm rounded-4 mb-3">
        <div class="card-body">
            <form class="row g-3" method="GET" action="listar.php">
                <div class="col-12 col-lg-4">
                    <label for="buscar" class="form-label">Buscar cliente</label>
                    <input type="text" class="form-control" id="buscar" name="buscar" maxlength="100"
                           value="<?= e($buscar) ?>" placeholder="Nombre o email" aria-describedby="ayudaBuscar">
                    <div id="ayudaBuscar" class="form-text">Coincidencias parciales en nombre o email.</div>
                </div>
                <div class="col-12 col-sm-6 col-lg-3">
                    <label for="ciudad" class="form-label">Ciudad</label>
                    <select class="form-select" id="ciudad" name="ciudad">
                        <option value="">Todas</option>
                        <?php foreach ($ciudades as $item): ?>
                            <option value="<?= e($item["ciudad"]) ?>" <?= $ciudad === $item["ciudad"] ? "selected" : "" ?>>
                                <?= e($item["ciudad"]) ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </div>
                <div class="col-6 col-sm-3 col-lg-2">
                    <label for="orden" class="form-label">Orden</label>
                    <select class="form-select" id="orden" name="orden">
                        <option value="asc" <?= $orden === "ASC" ? "selected" : "" ?>>A → Z</option>
                        <option value="desc" <?= $orden === "DESC" ? "selected" : "" ?>>Z → A</option>
                    </select>
                </div>
                <div class="col-6 col-sm-3 col-lg-1">
                    <label for="por_pagina" class="form-label">Por pág.</label>
                    <select class="form-select" id="por_pagina" name="por_pagina">
                        <?php foreach ([5, 10, 20] as $opcion): ?>
                            <option value="<?= (int) $opcion ?>" <?= $porPagina === $opcion ? "selected" : "" ?>><?= (int) $opcion ?></option>
                        <?php endforeach; ?>
                    </select>
                </div>
                <div class="col-12 col-lg-2 d-flex gap-2 align-items-end">
                    <button type="submit" class="btn btn-primary flex-fill"><i class="bi bi-search me-1" aria-hidden="true"></i>BUSCAR</button>
                    <a class="btn btn-outline-secondary flex-fill" href="listar.php"><i class="bi bi-x-circle me-1" aria-hidden="true"></i>LIMPIAR</a>
                </div>
            </form>
        </div>
    </div>

    <!-- Resultados -->
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-body">
            <?php if ($errorBD): ?>
                <?= alertaHtml("error", "No se pudo consultar la base de datos. Verificá que MySQL esté iniciado en XAMPP.", false) ?>
            <?php else: ?>
                <div class="d-flex flex-wrap justify-content-between gap-2 mb-3 text-body-secondary">
                    <span>Registros encontrados: <span class="badge text-bg-primary"><?= (int) $totalRegistros ?></span></span>
                    <?php if ($totalRegistros > 0): ?>
                        <span>Mostrando <strong><?= (int) $mostrandoDesde ?></strong> a <strong><?= (int) $mostrandoHasta ?></strong>
                            de <strong><?= (int) $totalRegistros ?></strong> registros</span>
                    <?php endif; ?>
                </div>

                <?php if (empty($clientes)): ?>
                    <?= alertaHtml("aviso", $hayFiltros
                        ? "No se encontraron clientes con esos filtros. Probá con otro texto o ciudad, o limpiá los filtros."
                        : "Todavía no hay clientes cargados.", false) ?>
                <?php else: ?>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover align-middle mb-0 tabla-gc">
                            <thead class="table-light">
                                <tr><th scope="col">ID</th><th scope="col">Nombre</th><th scope="col">Email</th><th scope="col">Ciudad</th><th scope="col">Acciones</th></tr>
                            </thead>
                            <tbody>
                            <?php foreach ($clientes as $cliente): ?>
                                <tr>
                                    <td><?= (int) $cliente["id"] ?></td>
                                    <td><?= e($cliente["nombre"]) ?></td>
                                    <td><?= e($cliente["email"]) ?></td>
                                    <td><?= e($cliente["ciudad"]) ?></td>
                                    <td>
                                        <div class="d-flex flex-wrap gap-1">
                                            <a class="btn btn-outline-secondary btn-sm" href="ver.php?id=<?= (int) $cliente["id"] ?>">
                                                <i class="bi bi-eye me-1" aria-hidden="true"></i>Ver
                                            </a>
                                            <a class="btn btn-outline-primary btn-sm" href="editar.php?id=<?= (int) $cliente["id"] ?>">
                                                <i class="bi bi-pencil me-1" aria-hidden="true"></i>Editar
                                            </a>
                                            <button type="button" class="btn btn-outline-danger btn-sm"
                                                    data-bs-toggle="modal" data-bs-target="#modalEliminar"
                                                    data-id="<?= (int) $cliente["id"] ?>"
                                                    data-nombre="<?= e($cliente["nombre"]) ?>">
                                                <i class="bi bi-trash me-1" aria-hidden="true"></i>Eliminar
                                            </button>
                                        </div>
                                    </td>
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

<!-- Modal de confirmación de eliminación.
     La eliminación real la valida eliminar.php (POST + token CSRF + ID + sesión). El modal NO es el control de seguridad. -->
<div class="modal fade" id="modalEliminar" tabindex="-1" aria-labelledby="modalEliminarTitulo" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <form class="modal-content" method="POST" action="eliminar.php">
            <?= campoCsrf() ?>
            <input type="hidden" name="id" id="modalEliminarId" value="">
            <div class="modal-header">
                <h2 class="modal-title fs-5" id="modalEliminarTitulo">
                    <i class="bi bi-trash text-danger me-2" aria-hidden="true"></i>Confirmar eliminación
                </h2>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <p class="mb-1">¿Está seguro de que desea eliminar este cliente?</p>
                <p class="fw-semibold mb-2" id="modalEliminarNombre"></p>
                <p class="text-body-secondary small mb-0">Esta acción no se puede deshacer.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                    <i class="bi bi-x-lg me-1" aria-hidden="true"></i>Cancelar
                </button>
                <button type="submit" class="btn btn-danger">
                    <i class="bi bi-trash me-1" aria-hidden="true"></i>Confirmar eliminación
                </button>
            </div>
        </form>
    </div>
</div>
<?php require __DIR__ . "/../includes/footer.php"; ?>
