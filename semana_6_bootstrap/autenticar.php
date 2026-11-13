<?php
declare(strict_types=1);

require_once __DIR__ . "/includes/auth.php";
require_once __DIR__ . "/config/conexion.php";
require_once __DIR__ . "/includes/funciones.php";
require_once __DIR__ . "/includes/intentos.php";

// Hash ficticio (de un valor descartable) para gastar el mismo tiempo cuando el usuario no existe
const HASH_FICTICIO = '$2b$10$BCoJR5FEx/gqdoQJUY9OT.bTV77hmJssmUJUqCLSvC3VLbuyZmPma';

// Solo se acepta el envío del formulario (POST)
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    header("Location: login.php");
    exit;
}

// Token CSRF del formulario de login
if (!csrfValido()) {
    header("Location: login.php?error=3");
    exit;
}

$usuario  = is_string($_POST["usuario"] ?? null) ? trim($_POST["usuario"]) : "";
$password = is_string($_POST["password"] ?? null) ? $_POST["password"] : "";

if ($usuario === "" || $password === "" || mb_strlen($usuario) > 50 || strlen($password) > 1000
    || !textoSeguro($usuario)) {
    header("Location: login.php?error=1");
    exit;
}

$ip = ipCliente();

// Demasiados intentos fallidos recientes con este usuario desde esta IP
if (loginBloqueado($pdo, $usuario, $ip)) {
    header("Location: login.php?error=4");
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

// Siempre se ejecuta password_verify(), exista o no el usuario, para que el tiempo de respuesta
// no revele si el nombre de usuario existe.
$hashAlmacenado = $fila ? (string) $fila["password"] : HASH_FICTICIO;
$passwordOk     = password_verify($password, $hashAlmacenado);
$cuentaActiva   = $fila && (bool) $fila["activo"];

// Mensaje genérico: no se revela si falló el usuario, la contraseña o si la cuenta está inactiva
if (!$fila || !$passwordOk || !$cuentaActiva) {
    registrarIntentoFallido($pdo, $usuario, $ip);
    header("Location: login.php?error=1");
    exit;
}

// Si el algoritmo o el costo del hash quedó desactualizado, se vuelve a generar
if (password_needs_rehash($hashAlmacenado, PASSWORD_DEFAULT)) {
    try {
        $upd = $pdo->prepare("UPDATE usuarios SET password = :password WHERE id = :id");
        $upd->bindValue(":password", password_hash($password, PASSWORD_DEFAULT));
        $upd->bindValue(":id", (int) $fila["id"], PDO::PARAM_INT);
        $upd->execute();
    } catch (PDOException $e) {
        error_log("[gestion_clientes] autenticar.php (rehash): " . $e->getMessage());
    }
}

limpiarIntentos($pdo, $usuario, $ip);

// Credenciales correctas: recién ahora se crea la identidad en la sesión
session_regenerate_id(true);   // nuevo ID de sesión (evita la fijación de sesión)
unset($_SESSION["csrf"]);      // también se renueva el token CSRF
$_SESSION["usuario_id"]       = (int) $fila["id"];
$_SESSION["usuario"]          = $fila["usuario"];
$_SESSION["nombre"]           = $fila["nombre"];
$_SESSION["ultima_actividad"] = time();

header("Location: index.php");
exit;
