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
<main class="contenedor">
    <?php mostrarFlash(); ?>

    <section class="tarjeta">
        <div class="barra-superior">
            <h2>Buscar clientes</h2>
            <a class="btn btn-verde btn-chico" href="nuevo.php">+ Nuevo cliente</a>
        </div>

        <form class="filtros" method="GET" action="listar.php">
            <div class="campo">
                <label for="buscar">Buscar cliente (nombre o email)</label>
                <input type="text" id="buscar" name="buscar" value="<?= e($buscar) ?>" placeholder="Ej.: Ana">
            </div>
            <div class="campo">
                <label for="ciudad">Ciudad</label>
                <select id="ciudad" name="ciudad">
                    <option value="">Todas</option>
                    <?php foreach ($ciudades as $item): ?>
                        <option value="<?= e($item["ciudad"]) ?>" <?= $ciudad === $item["ciudad"] ? "selected" : "" ?>>
                            <?= e($item["ciudad"]) ?>
                        </option>
                    <?php endforeach; ?>
                </select>
            </div>
            <div class="campo">
                <label for="orden">Orden por nombre</label>
                <select id="orden" name="orden">
                    <option value="asc" <?= $orden === "ASC" ? "selected" : "" ?>>A → Z</option>
                    <option value="desc" <?= $orden === "DESC" ? "selected" : "" ?>>Z → A</option>
                </select>
            </div>
            <div class="campo">
                <label for="por_pagina">Por página</label>
                <select id="por_pagina" name="por_pagina">
                    <?php foreach ([5, 10, 20] as $opcion): ?>
                        <option value="<?= (int) $opcion ?>" <?= $porPagina === $opcion ? "selected" : "" ?>><?= (int) $opcion ?></option>
                    <?php endforeach; ?>
                </select>
            </div>
            <div class="acciones-form">
                <button type="submit" class="btn btn-primario">BUSCAR</button>
                <a class="btn btn-secundario" href="listar.php">LIMPIAR</a>
            </div>
        </form>
    </section>

    <section class="tarjeta">
        <?php if ($errorBD): ?>
            <div class="alerta alerta-error">No se pudo consultar la base de datos. Verificá que MySQL esté iniciado en XAMPP.</div>
        <?php else: ?>
            <div class="resumen">
                <span>Registros encontrados: <strong><?= (int) $totalRegistros ?></strong></span>
                <?php if ($totalRegistros > 0): ?>
                    <span>Mostrando <strong><?= (int) $mostrandoDesde ?></strong> a <strong><?= (int) $mostrandoHasta ?></strong>
                        de <strong><?= (int) $totalRegistros ?></strong> registros</span>
                <?php endif; ?>
            </div>

            <?php if (empty($clientes)): ?>
                <div class="alerta alerta-aviso">
                    <strong>No se encontraron clientes.</strong>
                    <?php if ($hayFiltros): ?>
                        Probá con otro texto o ciudad, o <a href="listar.php">limpiá los filtros</a>.
                    <?php else: ?>
                        Todavía no hay clientes cargados. <a href="nuevo.php">Cargá el primero</a>.
                    <?php endif; ?>
                </div>
            <?php else: ?>
                <div class="tabla-scroll">
                    <table>
                        <thead><tr><th>ID</th><th>Nombre</th><th>Email</th><th>Ciudad</th><th>Acciones</th></tr></thead>
                        <tbody>
                        <?php foreach ($clientes as $cliente): ?>
                            <tr>
                                <td><?= (int) $cliente["id"] ?></td>
                                <td><?= e($cliente["nombre"]) ?></td>
                                <td><?= e($cliente["email"]) ?></td>
                                <td><?= e($cliente["ciudad"]) ?></td>
                                <td class="acciones">
                                    <a class="btn btn-secundario btn-chico" href="ver.php?id=<?= (int) $cliente["id"] ?>">Ver</a>
                                    <a class="btn btn-primario btn-chico" href="editar.php?id=<?= (int) $cliente["id"] ?>">Editar</a>
                                    <form class="form-inline" method="POST" action="eliminar.php"
                                          data-confirmar="¿Seguro que querés eliminar este cliente? Esta acción no se puede deshacer.">
                                        <?= campoCsrf() ?>
                                        <input type="hidden" name="id" value="<?= (int) $cliente["id"] ?>">
                                        <button type="submit" class="btn btn-peligro btn-chico">Eliminar</button>
                                    </form>
                                </td>
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
