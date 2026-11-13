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
$paginaActiva = "clientes";

require __DIR__ . "/../includes/header.php";
require __DIR__ . "/../includes/menu.php";
?>
<main class="container-fluid px-3 px-lg-4 py-4">
    <div class="row justify-content-center">
        <div class="col-12 col-lg-8 col-xl-6">
            <?php mostrarFlash(); ?>
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white border-0 pt-3">
                    <h1 class="h4 mb-0 titulo-seccion"><i class="bi bi-person-plus-fill me-2" aria-hidden="true"></i>Registrar nuevo cliente</h1>
                </div>
                <div class="card-body">
                    <?php require __DIR__ . "/../includes/form_cliente.php"; ?>
                </div>
            </div>
        </div>
    </div>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
