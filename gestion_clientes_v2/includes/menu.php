<?php
/**
 * includes/menu.php
 * Usa $raiz para que los enlaces funcionen desde cualquier carpeta.
 * $paginaActiva (opcional): inicio | clientes | nuevo | pedidos | reportes
 */
$raiz         = $raiz ?? "";
$paginaActiva = $paginaActiva ?? "";
$enlaces = [
    "inicio"   => ["Inicio",        $raiz . "index.php"],
    "clientes" => ["Clientes",      $raiz . "clientes/listar.php"],
    "nuevo"    => ["Nuevo cliente", $raiz . "clientes/nuevo.php"],
    "pedidos"  => ["Pedidos",       $raiz . "pedidos/listar.php"],
    "reportes" => ["Reportes",      $raiz . "reportes/index.php"],
];
?>
<nav class="menu" aria-label="Menú principal">
    <ul>
        <?php foreach ($enlaces as $clave => [$texto, $url]): ?>
            <li><a href="<?= e($url) ?>" class="<?= $paginaActiva === $clave ? "activo" : "" ?>"><?= e($texto) ?></a></li>
        <?php endforeach; ?>
    </ul>
</nav>
