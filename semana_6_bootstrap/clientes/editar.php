<?php
declare(strict_types=1);

require_once __DIR__ . "/../includes/auth.php";
requiereLogin();
require_once __DIR__ . "/../config/conexion.php";
require_once __DIR__ . "/../includes/funciones.php";

$id = idPositivo($_GET["id"] ?? null);
if ($id === null) {
    flash("error", "El identificador del cliente no es válido.");
    redirigir("listar.php");
}

try {
    $stmt = $pdo->prepare("SELECT id, nombre, email, ciudad FROM clientes WHERE id = :id");
    $stmt->bindValue(":id", $id, PDO::PARAM_INT);
    $stmt->execute();
    $cliente = $stmt->fetch();
} catch (PDOException $e) {
    error_log("[gestion_clientes] clientes/editar.php: " . $e->getMessage());
    flash("error", "No se pudo consultar el cliente. Intentá nuevamente.");
    redirigir("listar.php");
}

if (!$cliente) {
    flash("error", "El cliente solicitado no existe.");
    redirigir("listar.php");
}

// Si venimos de un intento fallido de actualizar, se muestran los datos escritos y los errores
['errores' => $errores, 'viejo' => $viejo] = recuperarFormulario();
$cliente = [
    "id"     => (int) $cliente["id"],
    "nombre" => $viejo["nombre"] ?? (string) $cliente["nombre"],
    "email"  => $viejo["email"] ?? (string) ($cliente["email"] ?? ""),
    "ciudad" => $viejo["ciudad"] ?? (string) ($cliente["ciudad"] ?? ""),
];
$accionForm = "actualizar.php";
$textoBoton = "Guardar cambios";

$tituloPagina = "Editar cliente";
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
                    <h1 class="h4 mb-0 titulo-seccion"><i class="bi bi-pencil-square me-2" aria-hidden="true"></i>Editar cliente #<?= (int) $cliente["id"] ?></h1>
                </div>
                <div class="card-body">
                    <?php require __DIR__ . "/../includes/form_cliente.php"; ?>
                </div>
            </div>
        </div>
    </div>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
