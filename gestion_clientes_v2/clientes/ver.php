<?php
declare(strict_types=1);

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
<main class="contenedor">
    <section class="tarjeta">
        <h2>Datos del cliente</h2>
        <dl class="datos">
            <dt>ID</dt><dd><?= (int) $cliente["id"] ?></dd>
            <dt>Nombre</dt><dd><?= e($cliente["nombre"]) ?></dd>
            <dt>Email</dt><dd><?= e($cliente["email"]) ?></dd>
            <dt>Ciudad</dt><dd><?= e($cliente["ciudad"]) ?></dd>
        </dl>
        <div class="acciones-form">
            <a class="btn btn-primario" href="editar.php?id=<?= (int) $cliente["id"] ?>">Editar</a>
            <a class="btn btn-secundario" href="listar.php">&larr; Volver al listado</a>
        </div>
    </section>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
