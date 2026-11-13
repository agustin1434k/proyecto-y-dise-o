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

    <!-- Bootstrap 5.3.3 y Bootstrap Icons 1.11.3 (CDN jsDelivr, versiones fijas) -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- CSS propio: complementa a Bootstrap -->
    <link rel="stylesheet" href="<?= e($raiz) ?>css/estilos.css">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" defer></script>
    <script src="<?= e($raiz) ?>js/app.js" defer></script>
</head>
<body class="bg-body-tertiary d-flex flex-column min-vh-100">
