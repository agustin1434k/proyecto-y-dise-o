<?php
declare(strict_types=1);

require_once __DIR__ . "/../config/conexion.php";
require_once __DIR__ . "/../includes/funciones.php";

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    redirigir("nuevo.php");
}
if (!csrfValido()) {
    flash("error", "El formulario venció o no es válido. Volvé a intentarlo.");
    redirigir("nuevo.php");
}

$datos   = leerDatosCliente();
$errores = validarCliente($datos);

if (!empty($errores)) {
    guardarFormulario($errores, $datos);
    redirigir("nuevo.php");
}

try {
    $stmt = $pdo->prepare("INSERT INTO clientes (nombre, email, ciudad) VALUES (:nombre, :email, :ciudad)");
    $stmt->execute([
        ":nombre" => $datos["nombre"],
        ":email"  => $datos["email"] === "" ? null : $datos["email"],
        ":ciudad" => $datos["ciudad"] === "" ? null : $datos["ciudad"],
    ]);
} catch (PDOException $e) {
    error_log("[gestion_clientes] clientes/guardar.php: " . $e->getMessage());
    guardarFormulario(["general" => "No se pudo guardar el cliente."], $datos);
    flash("error", "No se pudo guardar el cliente. Intentá nuevamente.");
    redirigir("nuevo.php");
}

flash("exito", "Cliente registrado correctamente.");
redirigir("listar.php");
