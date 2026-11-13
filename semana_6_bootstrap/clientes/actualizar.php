<?php
declare(strict_types=1);

require_once __DIR__ . "/../includes/auth.php";
requiereLogin();
require_once __DIR__ . "/../config/conexion.php";
require_once __DIR__ . "/../includes/funciones.php";

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    redirigir("listar.php");
}

$id = idPositivo($_POST["id"] ?? null);
if ($id === null) {
    flash("error", "El identificador del cliente no es válido.");
    redirigir("listar.php");
}
if (!csrfValido()) {
    flash("error", "El formulario venció o no es válido. Volvé a intentarlo.");
    redirigir("editar.php?id=" . $id);
}

$datos   = leerDatosCliente();
$errores = validarCliente($datos);

if (!empty($errores)) {
    guardarFormulario($errores, $datos);
    redirigir("editar.php?id=" . $id);
}

try {
    $stmt = $pdo->prepare(
        "UPDATE clientes SET nombre = :nombre, email = :email, ciudad = :ciudad WHERE id = :id"
    );
    $stmt->bindValue(":nombre", $datos["nombre"]);
    $stmt->bindValue(":email", $datos["email"] === "" ? null : $datos["email"]);
    $stmt->bindValue(":ciudad", $datos["ciudad"] === "" ? null : $datos["ciudad"]);
    $stmt->bindValue(":id", $id, PDO::PARAM_INT);
    $stmt->execute();
} catch (PDOException $e) {
    error_log("[gestion_clientes] clientes/actualizar.php: " . $e->getMessage());
    flash("error", "No se pudo actualizar el cliente. Intentá nuevamente.");
    redirigir("editar.php?id=" . $id);
}

flash("exito", "Cliente actualizado correctamente.");
redirigir("listar.php");
