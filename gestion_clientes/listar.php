<?php
/**
 * listar.php - Módulo de búsqueda, filtros y paginación de clientes.
 * Requiere conexion.php, que debe definir la variable $pdo (conexión PDO).
 * Columnas esperadas en la tabla clientes: id, nombre, email, ciudad.
 */
require_once "conexion.php";

/** Escapa un valor para mostrarlo de forma segura en HTML. */
function h($valor): string
{
    return htmlspecialchars((string) $valor, ENT_QUOTES, "UTF-8");
}

/** Lee un parámetro GET como texto limpio (ignora arrays y valores raros). */
function parametro(string $nombre): string
{
    $valor = $_GET[$nombre] ?? "";
    return is_string($valor) ? trim($valor) : "";
}

/** Arma la URL de una página conservando todos los filtros. */
function urlPagina(int $pagina, array $filtros): string
{
    return "listar.php?" . http_build_query($filtros + ["pagina" => $pagina]);
}

/* ---------------------------------------------------------------
   1. Recibir y validar parámetros (GET)
   --------------------------------------------------------------- */
$buscar = parametro("buscar");
$ciudad = parametro("ciudad");

// Orden: solo se aceptan dos valores; cualquier otro se reemplaza por ASC
$orden = (strtolower(parametro("orden")) === "desc") ? "DESC" : "ASC";

// Cantidad por página: solo 5, 10 o 20 (por defecto 10)
$porPagina = filter_input(INPUT_GET, "por_pagina", FILTER_VALIDATE_INT);
if (!in_array($porPagina, [5, 10, 20], true)) {
    $porPagina = 10;
}

// Número de página: entero positivo (por defecto 1)
$pagina = filter_input(INPUT_GET, "pagina", FILTER_VALIDATE_INT);
$pagina = ($pagina && $pagina > 0) ? $pagina : 1;

/* ---------------------------------------------------------------
   2. Construir condiciones dinámicas (solo las filtros usados)
   --------------------------------------------------------------- */
$condiciones = [];
$parametros  = [];

if ($buscar !== "") {
    // Se buscan coincidencias parciales en nombre o email.
    // addcslashes evita que un % o _ escrito por el usuario actúe como comodín.
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

/* ---------------------------------------------------------------
   3. Consultas a la base de datos
   --------------------------------------------------------------- */
$clientes       = [];
$ciudades       = [];
$totalRegistros = 0;
$totalPaginas   = 1;
$inicio         = 0;
$error          = "";

try {
    // 3.1 Total de registros (mismos filtros que la consulta principal)
    $stmtCount = $pdo->prepare("SELECT COUNT(*) FROM clientes" . $where);
    foreach ($parametros as $nombre => $valor) {
        $stmtCount->bindValue($nombre, $valor);
    }
    $stmtCount->execute();
    $totalRegistros = (int) $stmtCount->fetchColumn();

    // 3.2 Cantidad de páginas y corrección de la página solicitada
    $totalPaginas = max(1, (int) ceil($totalRegistros / $porPagina));
    if ($pagina > $totalPaginas) {
        $pagina = $totalPaginas;
    }
    $inicio = ($pagina - 1) * $porPagina;

    // 3.3 Registros de la página actual.
    // $orden solo puede valer "ASC" o "DESC" (validado arriba), por eso se puede
    // incluir en el SQL; los datos del usuario siempre van como parámetros.
    $sql = "SELECT id, nombre, email, ciudad
            FROM clientes" . $where . "
            ORDER BY nombre " . $orden . ", id ASC
            LIMIT :inicio, :cantidad";
    $stmt = $pdo->prepare($sql);
    foreach ($parametros as $nombre => $valor) {
        $stmt->bindValue($nombre, $valor);
    }
    $stmt->bindValue(":inicio", $inicio, PDO::PARAM_INT);
    $stmt->bindValue(":cantidad", $porPagina, PDO::PARAM_INT);
    $stmt->execute();
    $clientes = $stmt->fetchAll();

    // 3.4 Ciudades disponibles para el desplegable
    $ciudades = $pdo->query(
        "SELECT DISTINCT ciudad
         FROM clientes
         WHERE ciudad IS NOT NULL AND ciudad <> ''
         ORDER BY ciudad"
    )->fetchAll();
} catch (PDOException $e) {
    $error = $e->getMessage();
}

/* ---------------------------------------------------------------
   4. Datos para la presentación
   --------------------------------------------------------------- */
$filtros = [
    "buscar"     => $buscar,
    "ciudad"     => $ciudad,
    "por_pagina" => $porPagina,
    "orden"      => strtolower($orden),
];

$mostrandoDesde = $totalRegistros === 0 ? 0 : $inicio + 1;
$mostrandoHasta = $inicio + count($clientes);

$ventana    = 2; // páginas visibles a cada lado de la actual
$numDesde   = max(1, $pagina - $ventana);
$numHasta   = min($totalPaginas, $pagina + $ventana);
$hayFiltros = ($buscar !== "" || $ciudad !== "");
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Gestión de Clientes</title>
<style>
    :root {
        --azul: #1f3a5f; --azul-claro: #2f6fb0; --verde: #2e8b57;
        --gris: #5f6b7a; --borde: #d9dee7; --fondo: #f1f4f9;
    }
    * { box-sizing: border-box; }
    body { margin: 0; font-family: "Segoe UI", Arial, sans-serif; background: var(--fondo); color: #1c2733; }
    header.encabezado { background: var(--azul); color: #fff; padding: 22px 30px; }
    header.encabezado h1 { margin: 0; font-size: 1.7rem; }
    header.encabezado p { margin: 4px 0 0; color: #bfd2e6; }
    .contenedor { max-width: 1100px; margin: 24px auto; padding: 0 16px; }
    .tarjeta { background: #fff; border: 1px solid var(--borde); border-radius: 10px; padding: 20px; margin-bottom: 20px; }
    .barra-superior { display: flex; justify-content: space-between; align-items: center; gap: 10px; flex-wrap: wrap; margin-bottom: 14px; }
    .barra-superior h2 { margin: 0; font-size: 1.2rem; color: var(--azul); }

    form.filtros { display: grid; grid-template-columns: 2fr 1.3fr 1fr 1fr auto; gap: 14px; align-items: end; }
    .campo { display: flex; flex-direction: column; gap: 5px; }
    .campo label { font-size: .85rem; font-weight: 600; color: var(--gris); }
    input[type=text], select { padding: 10px; border: 1px solid var(--borde); border-radius: 6px; font-size: .95rem; width: 100%; background: #fff; }
    input[type=text]:focus, select:focus { outline: 2px solid var(--azul-claro); border-color: var(--azul-claro); }
    .acciones-form { display: flex; gap: 8px; align-items: center; }

    .btn { display: inline-block; padding: 10px 18px; border-radius: 6px; border: 0; font-size: .9rem; font-weight: 600;
           text-decoration: none; cursor: pointer; text-align: center; }
    .btn-primario { background: var(--azul-claro); color: #fff; }
    .btn-primario:hover { background: var(--azul); }
    .btn-secundario { background: #e7ebf1; color: var(--azul); }
    .btn-secundario:hover { background: #d5dbe5; }
    .btn-verde { background: var(--verde); color: #fff; }
    .btn-chico { padding: 6px 12px; font-size: .82rem; }

    .resumen { display: flex; justify-content: space-between; flex-wrap: wrap; gap: 8px; margin-bottom: 12px; color: var(--gris); }
    .resumen strong { color: var(--azul); }
    .tabla-scroll { overflow-x: auto; }
    table { width: 100%; border-collapse: collapse; }
    th { background: var(--azul); color: #fff; text-align: left; padding: 11px 12px; font-size: .9rem; }
    td { padding: 11px 12px; border-bottom: 1px solid var(--borde); }
    tbody tr:nth-child(even) { background: #f7f9fc; }
    tbody tr:hover { background: #eaf1fa; }
    td.acciones { white-space: nowrap; display: flex; gap: 6px; }

    .aviso { padding: 16px 18px; border-radius: 8px; margin: 10px 0; }
    .aviso-vacio { background: #fff8e5; border: 1px solid #f0d58a; color: #7a5a00; }
    .aviso-error { background: #fdecea; border: 1px solid #f1aeb5; color: #8a1c1c; }

    nav.paginacion { margin-top: 18px; display: flex; flex-wrap: wrap; gap: 6px; align-items: center; justify-content: center; }
    .pag { padding: 7px 12px; border: 1px solid var(--borde); border-radius: 6px; text-decoration: none; color: var(--azul); background: #fff; font-size: .9rem; }
    a.pag:hover { background: #eaf1fa; }
    .pag.activa { background: var(--azul-claro); color: #fff; border-color: var(--azul-claro); font-weight: 700; }
    .pag.desactivada { color: #a9b2bf; background: #f3f5f8; }
    .info-pagina { width: 100%; text-align: center; color: var(--gris); font-size: .85rem; margin-top: 6px; }

    @media (max-width: 860px) {
        form.filtros { grid-template-columns: 1fr 1fr; }
        .acciones-form { grid-column: 1 / -1; }
    }
    @media (max-width: 520px) {
        form.filtros { grid-template-columns: 1fr; }
        header.encabezado { padding: 18px; }
    }
</style>
</head>
<body>

<header class="encabezado">
    <h1>Gestión de Clientes</h1>
    <p>Búsqueda, filtros y paginación</p>
</header>

<main class="contenedor">

    <section class="tarjeta">
        <div class="barra-superior">
            <h2>Buscar clientes</h2>
            <a class="btn btn-verde btn-chico" href="nuevo.php">+ Nuevo cliente</a>
        </div>

        <form class="filtros" method="GET" action="listar.php">
            <div class="campo">
                <label for="buscar">Buscar cliente (nombre o email)</label>
                <input type="text" id="buscar" name="buscar" value="<?= h($buscar) ?>" placeholder="Ej.: Ana">
            </div>

            <div class="campo">
                <label for="ciudad">Ciudad</label>
                <select id="ciudad" name="ciudad">
                    <option value="">Todas</option>
                    <?php foreach ($ciudades as $item): ?>
                        <option value="<?= h($item["ciudad"]) ?>" <?= $ciudad === $item["ciudad"] ? "selected" : "" ?>>
                            <?= h($item["ciudad"]) ?>
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
                        <option value="<?= $opcion ?>" <?= $porPagina === $opcion ? "selected" : "" ?>><?= $opcion ?></option>
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
        <?php if ($error !== ""): ?>
            <div class="aviso aviso-error">
                <strong>No se pudo consultar la base de datos.</strong><br>
                Verificá que MySQL esté iniciado en XAMPP y que la tabla <em>clientes</em> tenga las columnas
                id, nombre, email y ciudad.<br>
                <small>Detalle técnico: <?= h($error) ?></small>
            </div>
        <?php else: ?>

            <div class="resumen">
                <span>Registros encontrados: <strong><?= $totalRegistros ?></strong></span>
                <?php if ($totalRegistros > 0): ?>
                    <span>Mostrando <strong><?= $mostrandoDesde ?></strong> a <strong><?= $mostrandoHasta ?></strong>
                        de <strong><?= $totalRegistros ?></strong> registros</span>
                <?php endif; ?>
            </div>

            <?php if (empty($clientes)): ?>
                <div class="aviso aviso-vacio">
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
                        <thead>
                            <tr><th>ID</th><th>Nombre</th><th>Email</th><th>Ciudad</th><th>Acciones</th></tr>
                        </thead>
                        <tbody>
                        <?php foreach ($clientes as $cliente): ?>
                            <tr>
                                <td><?= (int) $cliente["id"] ?></td>
                                <td><?= h($cliente["nombre"]) ?></td>
                                <td><?= h($cliente["email"]) ?></td>
                                <td><?= h($cliente["ciudad"]) ?></td>
                                <td class="acciones">
                                    <a class="btn btn-secundario btn-chico" href="ver.php?id=<?= (int) $cliente["id"] ?>">Ver</a>
                                    <a class="btn btn-primario btn-chico" href="editar.php?id=<?= (int) $cliente["id"] ?>">Editar</a>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                        </tbody>
                    </table>
                </div>
            <?php endif; ?>

            <?php if ($totalPaginas > 1): ?>
                <nav class="paginacion" aria-label="Paginación">
                    <?php if ($pagina > 1): ?>
                        <a class="pag" href="<?= h(urlPagina(1, $filtros)) ?>">&laquo; Primera</a>
                        <a class="pag" href="<?= h(urlPagina($pagina - 1, $filtros)) ?>">&lt;&lt; Anterior</a>
                    <?php else: ?>
                        <span class="pag desactivada">&laquo; Primera</span>
                        <span class="pag desactivada">&lt;&lt; Anterior</span>
                    <?php endif; ?>

                    <?php for ($n = $numDesde; $n <= $numHasta; $n++): ?>
                        <?php if ($n === $pagina): ?>
                            <span class="pag activa"><?= $n ?></span>
                        <?php else: ?>
                            <a class="pag" href="<?= h(urlPagina($n, $filtros)) ?>"><?= $n ?></a>
                        <?php endif; ?>
                    <?php endfor; ?>

                    <?php if ($pagina < $totalPaginas): ?>
                        <a class="pag" href="<?= h(urlPagina($pagina + 1, $filtros)) ?>">Siguiente &gt;&gt;</a>
                        <a class="pag" href="<?= h(urlPagina($totalPaginas, $filtros)) ?>">Última &raquo;</a>
                    <?php else: ?>
                        <span class="pag desactivada">Siguiente &gt;&gt;</span>
                        <span class="pag desactivada">Última &raquo;</span>
                    <?php endif; ?>

                    <div class="info-pagina">Página <?= $pagina ?> de <?= $totalPaginas ?></div>
                </nav>
            <?php endif; ?>

        <?php endif; ?>
    </section>

</main>
</body>
</html>
