<?php
/**
 * reportes.php - Semana 1: SQL avanzado aplicado a PHP
 * Proyecto: Gestión de Clientes (PHP + PDO + MariaDB/MySQL + XAMPP)
 *
 * Ubicación: htdocs/gestion_clientes/reportes.php
 * Requiere:  conexion.php (que define un objeto PDO en la variable $pdo)
 */
declare(strict_types=1);

// -------------------------------------------------------------------
// 1) NOMBRES DE TABLAS Y COLUMNAS
//    Si en tu base se llaman distinto, cambialos SOLO acá.
//    (Son valores fijos escritos por el programador, nunca datos del usuario.)
// -------------------------------------------------------------------
$tablaClientes = 'clientes';
$cId           = 'id';
$cNombre       = 'nombre';

$tablaPedidos  = 'pedidos';
$pId           = 'id';
$pClienteId    = 'cliente_id';
$pFecha        = 'fecha';
$pImporte      = 'importe';
$pDescripcion  = 'descripcion';

// -------------------------------------------------------------------
// 2) CONEXIÓN (se reutiliza conexion.php del proyecto)
// -------------------------------------------------------------------
$errorConexion = false;
try {
    require_once __DIR__ . '/conexion.php';

    // Por si en tu conexion.php el objeto PDO tiene otro nombre de variable.
    if (!isset($pdo) || !($pdo instanceof PDO)) {
        foreach (['conexion', 'conn', 'db', 'dbh', 'cn'] as $nombreVar) {
            if (isset($$nombreVar) && $$nombreVar instanceof PDO) {
                $pdo = $$nombreVar;
                break;
            }
        }
    }
    if (!isset($pdo) || !($pdo instanceof PDO)) {
        $errorConexion = true;
    } else {
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }
} catch (Throwable $e) {
    error_log('reportes.php - conexión: ' . $e->getMessage());
    $errorConexion = true;
}

// -------------------------------------------------------------------
// 3) FUNCIONES AUXILIARES
// -------------------------------------------------------------------

/** Escapa texto para mostrarlo de forma segura en HTML. */
function h($valor): string
{
    return htmlspecialchars((string) $valor, ENT_QUOTES, 'UTF-8');
}

/** Formatea un importe: $ 1.234,50 */
function dinero($valor): string
{
    return '$ ' . number_format((float) $valor, 2, ',', '.');
}

/**
 * Ejecuta una consulta preparada y devuelve las filas.
 * Si falla, registra el detalle técnico en el log de PHP y devuelve null
 * (al usuario solo se le muestra un mensaje comprensible).
 */
function consultar(PDO $pdo, string $sql, array $parametros = []): ?array
{
    try {
        $sentencia = $pdo->prepare($sql);
        $sentencia->execute($parametros);
        return $sentencia->fetchAll(PDO::FETCH_ASSOC);
    } catch (PDOException $e) {
        error_log('reportes.php - SQL: ' . $e->getMessage());
        return null;
    }
}

/**
 * Valida un importe recibido por GET. Acepta 5000, 5000.50 o 5.000,50.
 * Devuelve el importe normalizado (string) o null. Si hay un valor
 * incorrecto, deja el mensaje en $error.
 */
function leerImporte(string $clave, ?string &$error, ?string &$textoOriginal): ?string
{
    $error = null;
    $textoOriginal = '';
    if (!isset($_GET[$clave]) || !is_string($_GET[$clave]) || trim($_GET[$clave]) === '') {
        return null;
    }
    $textoOriginal = trim($_GET[$clave]);
    $valor = str_replace(['$', ' '], '', $textoOriginal);
    if (strpos($valor, ',') !== false) {
        $valor = str_replace('.', '', $valor);
        $valor = str_replace(',', '.', $valor);
    }
    if (!preg_match('/^\d{1,10}(\.\d{1,2})?$/', $valor)) {
        $error = 'Ingresá un importe válido (número positivo, hasta 2 decimales). Ejemplo: 5000 o 5000,50.';
        return null;
    }
    return $valor;
}

/** Da formato a una celda según su tipo. */
function celda($valor, string $tipo): string
{
    if ($tipo === 'dinero') {
        return dinero($valor);
    }
    if ($tipo === 'entero') {
        return number_format((int) $valor, 0, ',', '.');
    }
    if ($tipo === 'cantidad') {
        if ((int) $valor === 0) {
            return '<span class="pill pill-gris">0 (sin pedidos)</span>';
        }
        return number_format((int) $valor, 0, ',', '.');
    }
    if ($tipo === 'fecha') {
        $t = strtotime((string) $valor);
        return $t !== false ? h(date('d/m/Y', $t)) : h($valor);
    }
    return h($valor);
}

/**
 * Dibuja una tabla HTML.
 * $columnas = ['clave_sql' => ['Título', 'tipo'], ...]
 * tipos: texto | entero | cantidad | dinero | fecha
 */
function tabla(?array $filas, array $columnas, string $vacio = 'No hay resultados.'): string
{
    if ($filas === null) {
        return '<p class="aviso error">No se pudo cargar este reporte. '
            . 'Verificá que las tablas y columnas existan (ver nombres al inicio de reportes.php).</p>';
    }
    if (count($filas) === 0) {
        return '<p class="aviso">' . h($vacio) . '</p>';
    }
    $html = '<div class="tabla-wrap"><table><thead><tr>';
    foreach ($columnas as $clave => $def) {
        $numerica = in_array($def[1], ['dinero', 'entero', 'cantidad'], true);
        $html .= '<th' . ($numerica ? ' class="num"' : '') . '>' . h($def[0]) . '</th>';
    }
    $html .= '</tr></thead><tbody>';
    foreach ($filas as $fila) {
        $html .= '<tr>';
        foreach ($columnas as $clave => $def) {
            $numerica = in_array($def[1], ['dinero', 'entero', 'cantidad'], true);
            $html .= '<td' . ($numerica ? ' class="num"' : '') . '>'
                . celda($fila[$clave] ?? '', $def[1]) . '</td>';
        }
        $html .= '</tr>';
    }
    $html .= '</tbody></table></div>';
    $html .= '<p class="pie">' . count($filas) . ' fila(s)</p>';
    return $html;
}

// -------------------------------------------------------------------
// 4) CONSULTAS DE LOS REPORTES
// -------------------------------------------------------------------
$stats = $r1 = $r2 = $r4 = null;
$ej1 = $ej2 = $ej4 = $ej9 = null;
$resMin = $res5 = null;
$errMin = $errTotal = null;
$txtMin = $txtTotal = '';
$minimo = $limite = null;

if (!$errorConexion) {

    // Reporte 3: estadísticas generales (COUNT, SUM, AVG, MIN, MAX + COALESCE)
    $stats = consultar($pdo,
        "SELECT COUNT(*)                          AS cantidad,
                COALESCE(SUM(p.`$pImporte`), 0)   AS total,
                COALESCE(AVG(p.`$pImporte`), 0)   AS promedio,
                COALESCE(MIN(p.`$pImporte`), 0)   AS minimo,
                COALESCE(MAX(p.`$pImporte`), 0)   AS maximo
         FROM `$tablaPedidos` p");

    // Reporte 1: cantidad de pedidos por cliente (LEFT JOIN: incluye clientes sin pedidos)
    $r1 = consultar($pdo,
        "SELECT c.`$cNombre` AS cliente, COUNT(p.`$pId`) AS cantidad_pedidos
         FROM `$tablaClientes` c
         LEFT JOIN `$tablaPedidos` p ON p.`$pClienteId` = c.`$cId`
         GROUP BY c.`$cId`, c.`$cNombre`
         ORDER BY cantidad_pedidos DESC, c.`$cNombre` ASC");

    // Reporte 2: total comprado por cliente (INNER JOIN + GROUP BY + SUM + ORDER BY)
    $r2 = consultar($pdo,
        "SELECT c.`$cNombre` AS cliente, SUM(p.`$pImporte`) AS total_comprado
         FROM `$tablaClientes` c
         INNER JOIN `$tablaPedidos` p ON p.`$pClienteId` = c.`$cId`
         GROUP BY c.`$cId`, c.`$cNombre`
         ORDER BY total_comprado DESC");

    // Reporte 4: pedidos superiores al promedio (INNER JOIN + subconsulta)
    $r4 = consultar($pdo,
        "SELECT p.`$pId` AS pedido, c.`$cNombre` AS cliente, p.`$pFecha` AS fecha, p.`$pImporte` AS importe
         FROM `$tablaPedidos` p
         INNER JOIN `$tablaClientes` c ON c.`$cId` = p.`$pClienteId`
         WHERE p.`$pImporte` > (SELECT AVG(`$pImporte`) FROM `$tablaPedidos`)
         ORDER BY p.`$pImporte` DESC");

    // Ejercicio 1: pedidos superiores a $5.000 (WHERE con parámetro)
    $ej1 = consultar($pdo,
        "SELECT p.`$pId` AS pedido, c.`$cNombre` AS cliente, p.`$pFecha` AS fecha, p.`$pImporte` AS importe
         FROM `$tablaPedidos` p
         INNER JOIN `$tablaClientes` c ON c.`$cId` = p.`$pClienteId`
         WHERE p.`$pImporte` > :limite
         ORDER BY p.`$pFecha` DESC",
        [':limite' => '5000']);

    // Ejercicio 2: todos los pedidos de mayor a menor importe (ORDER BY)
    $ej2 = consultar($pdo,
        "SELECT p.`$pId` AS pedido, c.`$cNombre` AS cliente, p.`$pFecha` AS fecha, p.`$pImporte` AS importe
         FROM `$tablaPedidos` p
         INNER JOIN `$tablaClientes` c ON c.`$cId` = p.`$pClienteId`
         ORDER BY p.`$pImporte` DESC");

    // Ejercicio 4: clientes con al menos dos pedidos (GROUP BY + HAVING)
    $ej4 = consultar($pdo,
        "SELECT c.`$cNombre` AS cliente, COUNT(p.`$pId`) AS cantidad_pedidos
         FROM `$tablaClientes` c
         INNER JOIN `$tablaPedidos` p ON p.`$pClienteId` = c.`$cId`
         GROUP BY c.`$cId`, c.`$cNombre`
         HAVING COUNT(p.`$pId`) >= 2
         ORDER BY cantidad_pedidos DESC, c.`$cNombre` ASC");

    // Ejercicio 9: clientes que no realizaron pedidos (LEFT JOIN + IS NULL)
    $ej9 = consultar($pdo,
        "SELECT c.`$cId` AS id, c.`$cNombre` AS cliente
         FROM `$tablaClientes` c
         LEFT JOIN `$tablaPedidos` p ON p.`$pClienteId` = c.`$cId`
         WHERE p.`$pId` IS NULL
         ORDER BY c.`$cNombre` ASC");

    // Ejercicio 11: formulario - pedidos con importe mínimo (consulta preparada)
    $minimo = leerImporte('importe_min', $errMin, $txtMin);
    if ($minimo !== null) {
        $resMin = consultar($pdo,
            "SELECT p.`$pId` AS pedido, c.`$cNombre` AS cliente, p.`$pFecha` AS fecha,
                    p.`$pImporte` AS importe, p.`$pDescripcion` AS descripcion
             FROM `$tablaPedidos` p
             INNER JOIN `$tablaClientes` c ON c.`$cId` = p.`$pClienteId`
             WHERE p.`$pImporte` >= :minimo
             ORDER BY p.`$pImporte` DESC",
            [':minimo' => $minimo]);
    }

    // Reporte 5 / Ejercicio 12: clientes cuyo total acumulado supera un importe (HAVING con parámetro)
    $limite = leerImporte('total_min', $errTotal, $txtTotal);
    if ($limite !== null) {
        $res5 = consultar($pdo,
            "SELECT c.`$cNombre` AS cliente, COUNT(p.`$pId`) AS cantidad_pedidos,
                    SUM(p.`$pImporte`) AS total_comprado
             FROM `$tablaClientes` c
             INNER JOIN `$tablaPedidos` p ON p.`$pClienteId` = c.`$cId`
             GROUP BY c.`$cId`, c.`$cNombre`
             HAVING SUM(p.`$pImporte`) > :limite
             ORDER BY total_comprado DESC",
            [':limite' => $limite]);
    }
}

$e = ($stats !== null && isset($stats[0])) ? $stats[0] : null;
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reportes | Gestión de Clientes</title>
    <style>
        :root {
            --primario: #1f3a5f;
            --acento: #2a9d8f;
            --fondo: #f4f6f8;
            --borde: #d9dee3;
            --texto: #1d2733;
            --gris: #5f6b7a;
            --rojo: #c62828;
            --verde: #2e7d32;
        }
        * { box-sizing: border-box; }
        body { margin: 0; font-family: "Segoe UI", Roboto, Arial, sans-serif; background: var(--fondo); color: var(--texto); line-height: 1.5; }
        header { background: linear-gradient(135deg, var(--primario), #2b5d8a); color: #fff; padding: 28px 20px 22px; }
        header .contenedor { max-width: 1100px; margin: 0 auto; }
        header h1 { margin: 0 0 4px; font-size: 1.8rem; }
        header p { margin: 0; color: #cfe0f1; }
        nav { background: #fff; border-bottom: 1px solid var(--borde); position: sticky; top: 0; z-index: 5; }
        nav .contenedor { max-width: 1100px; margin: 0 auto; padding: 8px 20px; display: flex; flex-wrap: wrap; gap: 8px; }
        nav a { text-decoration: none; color: var(--primario); font-size: .9rem; padding: 5px 12px; border-radius: 999px; background: #eaf0f6; }
        nav a:hover { background: var(--acento); color: #fff; }
        main { max-width: 1100px; margin: 0 auto; padding: 20px; }
        section { background: #fff; border: 1px solid var(--borde); border-radius: 10px; padding: 18px 20px; margin-bottom: 20px; }
        section h2 { margin: 0 0 4px; color: var(--primario); font-size: 1.25rem; }
        section h3 { margin: 18px 0 4px; color: var(--primario); font-size: 1.05rem; }
        .sql { color: var(--gris); font-size: .85rem; margin: 0 0 12px; }
        .sql code { background: #eef2f6; padding: 1px 6px; border-radius: 4px; }
        .tarjetas { display: grid; grid-template-columns: repeat(auto-fit, minmax(190px, 1fr)); gap: 12px; margin-top: 10px; }
        .tarjeta { border: 1px solid var(--borde); border-left: 5px solid var(--acento); border-radius: 8px; padding: 12px 14px; background: #fbfcfd; }
        .tarjeta .etq { color: var(--gris); font-size: .85rem; }
        .tarjeta .val { color: var(--primario); font-size: 1.5rem; font-weight: 700; }
        .tabla-wrap { overflow-x: auto; max-height: 380px; overflow-y: auto; border: 1px solid var(--borde); border-radius: 8px; }
        table { border-collapse: collapse; width: 100%; font-size: .93rem; }
        th { background: var(--primario); color: #fff; text-align: left; padding: 9px 12px; position: sticky; top: 0; }
        td { padding: 8px 12px; border-top: 1px solid var(--borde); }
        tbody tr:nth-child(even) { background: #f8fafc; }
        tbody tr:hover { background: #e7f3f1; }
        th.num, td.num { text-align: right; white-space: nowrap; }
        .pie { color: var(--gris); font-size: .82rem; margin: 6px 0 0; }
        .aviso { padding: 10px 14px; border-radius: 8px; background: #eef4fb; color: var(--primario); margin: 8px 0; }
        .aviso.error { background: #fdecea; color: var(--rojo); }
        .pill { display: inline-block; padding: 1px 10px; border-radius: 999px; font-size: .82rem; }
        .pill-gris { background: #e6e9ed; color: var(--gris); }
        form { display: flex; flex-wrap: wrap; gap: 10px; align-items: flex-end; margin: 10px 0 12px; }
        label { display: block; font-size: .85rem; color: var(--gris); margin-bottom: 3px; }
        input[type=text] { padding: 9px 12px; border: 1px solid var(--borde); border-radius: 6px; font-size: 1rem; width: 220px; max-width: 100%; }
        button { background: var(--acento); color: #fff; border: 0; padding: 10px 20px; border-radius: 6px; font-size: 1rem; cursor: pointer; font-weight: 600; }
        button:hover { background: #21867a; }
        footer { text-align: center; color: var(--gris); font-size: .82rem; padding: 10px 0 30px; }
        @media (max-width: 600px) { header h1 { font-size: 1.4rem; } input[type=text] { width: 100%; } form > div { width: 100%; } }
    </style>
</head>
<body>

<header>
    <div class="contenedor">
        <h1>Reportes de Gestión de Clientes</h1>
        <p>SQL avanzado aplicado a PHP · PDO · MariaDB/MySQL</p>
    </div>
</header>

<nav>
    <div class="contenedor">
        <a href="#estadisticas">Estadísticas</a>
        <a href="#r1">Pedidos por cliente</a>
        <a href="#r2">Total por cliente</a>
        <a href="#r4">Sobre el promedio</a>
        <a href="#ejercicios">Ejercicios</a>
        <a href="#minimo">Importe mínimo</a>
        <a href="#r5">Reporte 5</a>
    </div>
</nav>

<main>
<?php if ($errorConexion): ?>
    <section>
        <h2>No se pudo conectar con la base de datos</h2>
        <p class="aviso error">Verificá que Apache y MySQL estén iniciados en XAMPP y que
            <code>conexion.php</code> exista y defina el objeto PDO en la variable <code>$pdo</code>.</p>
    </section>
<?php else: ?>

    <!-- REPORTE 3 -->
    <section id="estadisticas">
        <h2>Reporte 3 · Estadísticas generales</h2>
        <p class="sql">Funciones de agregación: <code>COUNT</code>, <code>SUM</code>, <code>AVG</code>, <code>MIN</code>, <code>MAX</code> con <code>COALESCE</code>. Cubre los ejercicios 6 y 7.</p>
        <?php if ($e === null): ?>
            <p class="aviso error">No se pudieron obtener las estadísticas.</p>
        <?php else: ?>
            <div class="tarjetas">
                <div class="tarjeta"><div class="etq">Cantidad total de pedidos</div><div class="val"><?= number_format((int) $e['cantidad'], 0, ',', '.') ?></div></div>
                <div class="tarjeta"><div class="etq">Importe total</div><div class="val"><?= h(dinero($e['total'])) ?></div></div>
                <div class="tarjeta"><div class="etq">Importe promedio</div><div class="val"><?= h(dinero($e['promedio'])) ?></div></div>
                <div class="tarjeta"><div class="etq">Pedido mínimo</div><div class="val"><?= h(dinero($e['minimo'])) ?></div></div>
                <div class="tarjeta"><div class="etq">Pedido máximo</div><div class="val"><?= h(dinero($e['maximo'])) ?></div></div>
            </div>
        <?php endif; ?>
    </section>

    <!-- REPORTE 1 -->
    <section id="r1">
        <h2>Reporte 1 · Cantidad de pedidos por cliente</h2>
        <p class="sql"><code>LEFT JOIN</code> + <code>COUNT(p.id)</code> + <code>GROUP BY</code>. Incluye los clientes sin pedidos (cantidad 0). Cubre los ejercicios 3 y 8.</p>
        <?= tabla($r1, ['cliente' => ['Cliente', 'texto'], 'cantidad_pedidos' => ['Cantidad de pedidos', 'cantidad']], 'No hay clientes cargados.') ?>
    </section>

    <!-- REPORTE 2 -->
    <section id="r2">
        <h2>Reporte 2 · Total comprado por cliente</h2>
        <p class="sql"><code>INNER JOIN</code> + <code>GROUP BY</code> + <code>SUM</code> + <code>ORDER BY</code> descendente. Cubre el ejercicio 5. Solo aparecen clientes con pedidos.</p>
        <?= tabla($r2, ['cliente' => ['Cliente', 'texto'], 'total_comprado' => ['Total acumulado', 'dinero']], 'Todavía no hay pedidos.') ?>
    </section>

    <!-- REPORTE 4 -->
    <section id="r4">
        <h2>Reporte 4 · Pedidos superiores al promedio</h2>
        <p class="sql"><code>INNER JOIN</code> + subconsulta <code>(SELECT AVG(importe) FROM pedidos)</code>. Cubre el ejercicio 10.
            <?php if ($e !== null): ?>Promedio actual: <strong><?= h(dinero($e['promedio'])) ?></strong>.<?php endif; ?></p>
        <?= tabla($r4, ['pedido' => ['ID pedido', 'entero'], 'cliente' => ['Cliente', 'texto'], 'fecha' => ['Fecha', 'fecha'], 'importe' => ['Importe', 'dinero']], 'Ningún pedido supera el promedio.') ?>
    </section>

    <!-- EJERCICIOS DEL PROFESOR -->
    <section id="ejercicios">
        <h2>Ejercicios del profesor</h2>
        <p class="sql">Los ejercicios 3, 5, 6, 7, 8 y 10 ya están resueltos en los reportes anteriores.</p>

        <h3>Ejercicio 1 · Pedidos superiores a $ 5.000</h3>
        <p class="sql"><code>WHERE importe &gt; ?</code> (consulta preparada)</p>
        <?= tabla($ej1, ['pedido' => ['ID pedido', 'entero'], 'cliente' => ['Cliente', 'texto'], 'fecha' => ['Fecha', 'fecha'], 'importe' => ['Importe', 'dinero']], 'No hay pedidos superiores a $ 5.000.') ?>

        <h3>Ejercicio 2 · Pedidos de mayor a menor importe</h3>
        <p class="sql"><code>ORDER BY importe DESC</code></p>
        <?= tabla($ej2, ['pedido' => ['ID pedido', 'entero'], 'cliente' => ['Cliente', 'texto'], 'fecha' => ['Fecha', 'fecha'], 'importe' => ['Importe', 'dinero']], 'Todavía no hay pedidos.') ?>

        <h3>Ejercicio 4 · Clientes con al menos dos pedidos</h3>
        <p class="sql"><code>GROUP BY</code> + <code>HAVING COUNT(p.id) &gt;= 2</code> (HAVING filtra grupos; WHERE filtra filas)</p>
        <?= tabla($ej4, ['cliente' => ['Cliente', 'texto'], 'cantidad_pedidos' => ['Cantidad de pedidos', 'entero']], 'Ningún cliente tiene dos o más pedidos.') ?>

        <h3>Ejercicio 9 · Clientes que no realizaron pedidos</h3>
        <p class="sql"><code>LEFT JOIN</code> + <code>WHERE p.id IS NULL</code></p>
        <?= tabla($ej9, ['id' => ['ID cliente', 'entero'], 'cliente' => ['Cliente', 'texto']], 'Todos los clientes tienen al menos un pedido.') ?>
    </section>

    <!-- EJERCICIO 11 -->
    <section id="minimo">
        <h2>Ejercicio 11 · Consultar pedidos por importe mínimo</h2>
        <p class="sql">Formulario GET validado + consulta preparada con parámetro nombrado <code>:minimo</code>.</p>
        <form method="get" action="reportes.php#minimo">
            <div>
                <label for="importe_min">Importe mínimo ($)</label>
                <input type="text" id="importe_min" name="importe_min" inputmode="decimal" value="<?= h($txtMin) ?>" placeholder="Ej: 5000">
            </div>
            <?php if ($limite !== null): ?><input type="hidden" name="total_min" value="<?= h($txtTotal) ?>"><?php endif; ?>
            <button type="submit">Consultar</button>
        </form>
        <?php if ($errMin !== null): ?>
            <p class="aviso error"><?= h($errMin) ?></p>
        <?php elseif ($minimo === null): ?>
            <p class="aviso">Ingresá un importe para ver los pedidos de ese valor o superiores.</p>
        <?php else: ?>
            <?= tabla($resMin, ['pedido' => ['ID pedido', 'entero'], 'cliente' => ['Cliente', 'texto'], 'fecha' => ['Fecha', 'fecha'], 'importe' => ['Importe', 'dinero'], 'descripcion' => ['Descripción', 'texto']], 'No hay pedidos con importe de ' . dinero($minimo) . ' o superior.') ?>
        <?php endif; ?>
    </section>

    <!-- REPORTE 5 -->
    <section id="r5">
        <h2>Reporte 5 · Clientes con total acumulado superior a un importe</h2>
        <p class="sql"><code>GROUP BY</code> + <code>HAVING SUM(p.importe) &gt; :limite</code> (consulta preparada). Cubre el ejercicio 12.</p>
        <form method="get" action="reportes.php#r5">
            <div>
                <label for="total_min">Total acumulado mayor a ($)</label>
                <input type="text" id="total_min" name="total_min" inputmode="decimal" value="<?= h($txtTotal) ?>" placeholder="Ej: 10000">
            </div>
            <?php if ($minimo !== null): ?><input type="hidden" name="importe_min" value="<?= h($txtMin) ?>"><?php endif; ?>
            <button type="submit">Consultar</button>
        </form>
        <?php if ($errTotal !== null): ?>
            <p class="aviso error"><?= h($errTotal) ?></p>
        <?php elseif ($limite === null): ?>
            <p class="aviso">Ingresá un importe para ver qué clientes lo superaron en el total de sus compras.</p>
        <?php else: ?>
            <?= tabla($res5, ['cliente' => ['Cliente', 'texto'], 'cantidad_pedidos' => ['Cantidad de pedidos', 'entero'], 'total_comprado' => ['Total acumulado', 'dinero']], 'Ningún cliente supera un total de ' . dinero($limite) . '.') ?>
        <?php endif; ?>
    </section>

<?php endif; ?>
</main>

<footer>Gestión de Clientes · Semana 1 · reportes.php</footer>
</body>
</html>
