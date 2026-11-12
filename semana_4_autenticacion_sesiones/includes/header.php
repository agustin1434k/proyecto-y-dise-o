<?php
/**
 * includes/header.php
 * Variables que puede definir la página ANTES de incluirlo:
 *   $tituloPagina  título de la pestaña
 *   $raiz          ruta hacia la carpeta principal ("" en la raíz, "../" en subcarpetas)
 */
$raiz         = $raiz ?? "";
$tituloPagina = $tituloPagina ?? "Gestión de Clientes";
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><?= e($tituloPagina) ?> | Gestión de Clientes</title>
    <link rel="stylesheet" href="<?= e($raiz) ?>css/estilos.css">
</head>
<body>
<header class="encabezado">
    <div class="encabezado-interno">
        <h1>Gestión de Clientes</h1>
        <p>Clientes · Pedidos · Reportes</p>
    </div>
</header>
