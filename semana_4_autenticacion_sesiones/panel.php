<?php
declare(strict_types=1);

require_once __DIR__ . "/includes/auth.php";
requiereLogin();
require_once __DIR__ . "/config/conexion.php";
require_once __DIR__ . "/includes/funciones.php";

$tituloPagina = "Panel principal";
$raiz         = "";
$paginaActiva = "inicio";

$totalClientes = contarRegistros($pdo, "clientes");
$totalPedidos  = contarRegistros($pdo, "pedidos");

require __DIR__ . "/includes/header.php";
require __DIR__ . "/includes/menu.php";
?>
<main class="contenedor">
    <?php mostrarFlash(); ?>
    <h2 class="titulo-pagina">Panel principal</h2>
    <p class="saludo">Bienvenido, <strong><?= e($_SESSION["nombre"] ?? "") ?></strong>.</p>

    <div class="panel">
        <a class="acceso" href="clientes/listar.php">
            <?php if ($totalClientes !== null): ?><div class="cifra"><?= $totalClientes ?></div><?php endif; ?>
            <h3>Consultar clientes</h3>
            <p>Buscar, filtrar por ciudad y recorrer el listado.</p>
        </a>
        <a class="acceso verde" href="clientes/nuevo.php">
            <h3>Registrar clientes</h3>
            <p>Dar de alta un nuevo cliente.</p>
        </a>
        <a class="acceso ambar" href="pedidos/listar.php">
            <?php if ($totalPedidos !== null): ?><div class="cifra"><?= $totalPedidos ?></div><?php endif; ?>
            <h3>Consultar pedidos</h3>
            <p>Ver y registrar los pedidos de los clientes.</p>
        </a>
        <a class="acceso rojo" href="reportes/index.php">
            <h3>Consultar reportes</h3>
            <p>Resúmenes y totales del sistema.</p>
        </a>
    </div>

    <form method="POST" action="logout.php" class="form-salir">
        <?= campoCsrf() ?>
        <button type="submit" class="btn btn-secundario">Cerrar sesión</button>
    </form>
</main>
<?php require __DIR__ . "/includes/footer.php"; ?>
