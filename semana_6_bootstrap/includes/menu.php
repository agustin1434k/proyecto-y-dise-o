<?php
/**
 * includes/menu.php - Barra de navegación Bootstrap (se usa con o sin sesión).
 * Usa $raiz para que los enlaces funcionen desde cualquier carpeta.
 * $paginaActiva (opcional): login | inicio | clientes | pedidos | reportes
 */
$raiz         = $raiz ?? "";
$paginaActiva = $paginaActiva ?? "";
$enlaces = [
    "inicio"   => ["Inicio",   "bi-house",         $raiz . "index.php"],
    "clientes" => ["Clientes", "bi-people-fill",   $raiz . "clientes/listar.php"],
    "pedidos"  => ["Pedidos",  "bi-cart-check",    $raiz . "pedidos/listar.php"],
    "reportes" => ["Reportes", "bi-bar-chart-line", $raiz . "reportes/index.php"],
];
?>
<nav class="navbar navbar-expand-lg navbar-dark navbar-gc sticky-top shadow-sm" aria-label="Navegación principal">
    <div class="container-fluid px-3 px-lg-4">
        <a class="navbar-brand fw-semibold" href="<?= e($raiz) ?>index.php">
            <i class="bi bi-people-fill me-2" aria-hidden="true"></i>Gestión de Clientes
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal"
                aria-controls="menuPrincipal" aria-expanded="false" aria-label="Mostrar u ocultar el menú">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="menuPrincipal">
            <?php if (estaLogueado()): ?>
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <?php foreach ($enlaces as $clave => [$texto, $icono, $url]): ?>
                        <li class="nav-item">
                            <a class="nav-link<?= $paginaActiva === $clave ? " active" : "" ?>"
                               <?= $paginaActiva === $clave ? 'aria-current="page"' : "" ?>
                               href="<?= e($url) ?>">
                                <i class="bi <?= e($icono) ?> me-1" aria-hidden="true"></i><?= e($texto) ?>
                            </a>
                        </li>
                    <?php endforeach; ?>
                </ul>
                <div class="d-flex flex-column flex-lg-row align-items-lg-center gap-2">
                    <span class="navbar-text text-white">
                        <i class="bi bi-person-circle me-1" aria-hidden="true"></i><?= e($_SESSION["nombre"] ?? "") ?>
                    </span>
                    <form method="POST" action="<?= e($raiz) ?>logout.php" class="d-flex">
                        <?= campoCsrf() ?>
                        <button type="submit" class="btn btn-outline-light btn-sm">
                            <i class="bi bi-box-arrow-right me-1" aria-hidden="true"></i>Cerrar sesión
                        </button>
                    </form>
                </div>
            <?php else: ?>
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link<?= $paginaActiva === "login" ? " active" : "" ?>" href="<?= e($raiz) ?>login.php">
                            <i class="bi bi-box-arrow-in-right me-1" aria-hidden="true"></i>Ingresar
                        </a>
                    </li>
                </ul>
            <?php endif; ?>
        </div>
    </div>
</nav>
