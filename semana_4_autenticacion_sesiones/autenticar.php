<?php
declare(strict_types=1);

require_once __DIR__ . "/includes/auth.php";
require_once __DIR__ . "/config/conexion.php";

// Solo se acepta el envío del formulario (POST)
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    header("Location: login.php");
    exit;
}

$usuario  = is_string($_POST["usuario"] ?? null) ? trim($_POST["usuario"]) : "";
$password = is_string($_POST["password"] ?? null) ? $_POST["password"] : "";

if ($usuario === "" || $password === "" || mb_strlen($usuario) > 50 || strlen($password) > 1000) {
    header("Location: login.php?error=1");
    exit;
}

try {
    $stmt = $pdo->prepare(
        "SELECT id, usuario, password, nombre, activo
         FROM usuarios
         WHERE usuario = :usuario
         LIMIT 1"
    );
    $stmt->execute([":usuario" => $usuario]);
    $fila = $stmt->fetch();
} catch (PDOException $e) {
    error_log("[gestion_clientes] autenticar.php: " . $e->getMessage());
    header("Location: login.php?error=2");
    exit;
}

// Mensaje genérico: no se revela si falló el usuario, la contraseña o si la cuenta está inactiva
if (!$fila || !(bool) $fila["activo"] || !password_verify($password, $fila["password"])) {
    header("Location: login.php?error=1");
    exit;
}

// Credenciales correctas: recién ahora se crea la identidad en la sesión
session_regenerate_id(true); // nuevo ID de sesión (evita la fijación de sesión)
$_SESSION["usuario_id"] = (int) $fila["id"];
$_SESSION["usuario"]    = $fila["usuario"];
$_SESSION["nombre"]     = $fila["nombre"];

header("Location: panel.php");
exit;
