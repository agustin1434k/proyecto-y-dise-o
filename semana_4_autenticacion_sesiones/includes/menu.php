<?php
/**
 * includes/menu.php (se usa en todas las páginas, con o sin sesión)
 * Usa $raiz para que los enlaces funcionen desde cualquier carpeta.
 * $paginaActiva (opcional): login | inicio | clientes | nuevo | pedidos | reportes
 */
$raiz         = $raiz ?? "";
$paginaActiva = $paginaActiva ?? "";
$enlacesPrivados = [
    "inicio"   => ["Inicio",        $raiz . "panel.php"],
    "clientes" => ["Clientes",      $raiz . "clientes/listar.php"],
    "nuevo"    => ["Nuevo cliente", $raiz . "clientes/nuevo.php"],
    "pedidos"  => ["Pedidos",       $raiz . "pedidos/listar.php"],
    "reportes" => ["Reportes",      $raiz . "reportes/index.php"],
];
// Sin sesión: solo se ofrece ingresar. Con sesión: los módulos privados.
$enlaces = estaLogueado()
    ? $enlacesPrivados
    : ["login" => ["Ingresar", $raiz . "login.php"]];
?>
<nav class="menu" aria-label="Menú principal">
    <div class="menu-interno">
        <ul>
            <?php foreach ($enlaces as $clave => [$texto, $url]): ?>
                <li><a href="<?= e($url) ?>" class="<?= $paginaActiva === $clave ? "activo" : "" ?>"><?= e($texto) ?></a></li>
            <?php endforeach; ?>
        </ul>
        <?php if (estaLogueado()): ?>
            <div class="menu-usuario">
                <span>Hola, <strong><?= e($_SESSION["nombre"] ?? "") ?></strong></span>
                <form method="POST" action="<?= e($raiz) ?>logout.php">
                    <?= campoCsrf() ?>
                    <button type="submit" class="btn-salir">Cerrar sesión</button>
                </form>
            </div>
        <?php endif; ?>
    </div>
</nav>
