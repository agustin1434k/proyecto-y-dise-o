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
<main class="contenedor">
    <?php mostrarFlash(); ?>
    <section class="tarjeta">
        <h2>Editar cliente #<?= (int) $cliente["id"] ?></h2>
        <?php require __DIR__ . "/../includes/form_cliente.php"; ?>
    </section>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
