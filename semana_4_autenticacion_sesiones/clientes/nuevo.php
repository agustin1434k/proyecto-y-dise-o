<?php
declare(strict_types=1);

require_once __DIR__ . "/../includes/auth.php";
requiereLogin();
require_once __DIR__ . "/../includes/funciones.php";

['errores' => $errores, 'viejo' => $viejo] = recuperarFormulario();

$cliente = [
    "id"     => null,
    "nombre" => $viejo["nombre"] ?? "",
    "email"  => $viejo["email"] ?? "",
    "ciudad" => $viejo["ciudad"] ?? "",
];
$accionForm = "guardar.php";
$textoBoton = "Guardar cliente";

$tituloPagina = "Nuevo cliente";
$raiz         = "../";
$paginaActiva = "nuevo";

require __DIR__ . "/../includes/header.php";
require __DIR__ . "/../includes/menu.php";
?>
<main class="contenedor">
    <?php mostrarFlash(); ?>
    <section class="tarjeta">
        <h2>Registrar nuevo cliente</h2>
        <?php require __DIR__ . "/../includes/form_cliente.php"; ?>
    </section>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
