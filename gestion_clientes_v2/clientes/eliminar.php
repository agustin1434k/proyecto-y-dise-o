<?php
declare(strict_types=1);

require_once __DIR__ . "/../config/conexion.php";
require_once __DIR__ . "/../includes/funciones.php";

// Solo se elimina por POST (nunca por un enlace GET)
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    redirigir("listar.php");
}
if (!csrfValido()) {
    flash("error", "La solicitud no es válida. Volvé a intentarlo.");
    redirigir("listar.php");
}

$id = idPositivo($_POST["id"] ?? null);
if ($id === null) {
    flash("error", "El identificador del cliente no es válido.");
    redirigir("listar.php");
}

try {
    $stmt = $pdo->prepare("DELETE FROM clientes WHERE id = :id");
    $stmt->bindValue(":id", $id, PDO::PARAM_INT);
    $stmt->execute();

    if ($stmt->rowCount() === 0) {
        flash("aviso", "El cliente ya no existe.");
    } else {
        flash("exito", "Cliente eliminado correctamente.");
    }
} catch (PDOException $e) {
    error_log("[gestion_clientes] clientes/eliminar.php: " . $e->getMessage());
    // 23000 = violación de integridad (el cliente tiene pedidos asociados)
    if ($e->getCode() === "23000") {
        flash("error", "No se puede eliminar el cliente porque tiene pedidos asociados.");
    } else {
        flash("error", "No se pudo eliminar el cliente. Intentá nuevamente.");
    }
}

redirigir("listar.php");
