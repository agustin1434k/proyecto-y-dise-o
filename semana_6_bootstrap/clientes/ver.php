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
    error_log("[gestion_clientes] clientes/ver.php: " . $e->getMessage());
    flash("error", "No se pudo consultar el cliente. Intentá nuevamente.");
    redirigir("listar.php");
}

if (!$cliente) {
    flash("error", "El cliente solicitado no existe.");
    redirigir("listar.php");
}

$tituloPagina = "Ver cliente";
$raiz         = "../";
$paginaActiva = "clientes";

require __DIR__ . "/../includes/header.php";
require __DIR__ . "/../includes/menu.php";
?>
<main class="container-fluid px-3 px-lg-4 py-4">
    <div class="row justify-content-center">
        <div class="col-12 col-lg-8 col-xl-6">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white border-0 pt-3">
                    <h1 class="h4 mb-0 titulo-seccion"><i class="bi bi-person-vcard me-2" aria-hidden="true"></i>Datos del cliente</h1>
                </div>
                <div class="card-body">
                    <dl class="row mb-4">
                        <dt class="col-sm-3">ID</dt><dd class="col-sm-9"><?= (int) $cliente["id"] ?></dd>
                        <dt class="col-sm-3">Nombre</dt><dd class="col-sm-9"><?= e($cliente["nombre"]) ?></dd>
                        <dt class="col-sm-3">Email</dt><dd class="col-sm-9"><?= e($cliente["email"]) ?></dd>
                        <dt class="col-sm-3">Ciudad</dt><dd class="col-sm-9"><?= e($cliente["ciudad"]) ?></dd>
                    </dl>
                    <div class="d-flex flex-wrap gap-2">
                        <a class="btn btn-primary" href="editar.php?id=<?= (int) $cliente["id"] ?>"><i class="bi bi-pencil me-1" aria-hidden="true"></i>Editar</a>
                        <a class="btn btn-outline-secondary" href="listar.php"><i class="bi bi-arrow-left me-1" aria-hidden="true"></i>Volver al listado</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
