<?php
declare(strict_types=1);

/**
 * herramientas/crear_usuario.php
 * Crea un usuario desde la CONSOLA. No funciona desde el navegador (se corta si no es CLI).
 *
 * Uso (desde la carpeta del proyecto, en el símbolo del sistema de Windows):
 *     C:\xampp\php\php.exe herramientas\crear_usuario.php
 *
 * No hay contraseñas escritas en el código: la elegís vos al ejecutarlo.
 * Nota: lo que escribís se ve en pantalla.
 */
if (PHP_SAPI !== "cli") {
    http_response_code(404);
    exit;
}

require_once __DIR__ . "/../config/conexion.php";

function leer(string $pregunta): string
{
    echo $pregunta;
    $linea = fgets(STDIN);
    return $linea === false ? "" : trim($linea);
}

echo "=== Crear usuario ===\n";
$usuario  = leer("Usuario (3 a 50 caracteres: letras, números, punto, guion): ");
$nombre   = leer("Nombre para mostrar: ");
$password = leer("Contraseña (mínimo 10 caracteres): ");
$repetida = leer("Repetí la contraseña: ");

$errores = [];
if (!preg_match('/^[A-Za-z0-9._-]{3,50}$/', $usuario)) {
    $errores[] = "El usuario debe tener entre 3 y 50 caracteres (letras, números, punto, guion o guion bajo).";
}
if (mb_strlen($nombre) < 2 || mb_strlen($nombre) > 100) {
    $errores[] = "El nombre debe tener entre 2 y 100 caracteres.";
}
if (strlen($password) < 10) {
    $errores[] = "La contraseña debe tener al menos 10 caracteres.";
}
if (strcasecmp($password, $usuario) === 0) {
    $errores[] = "La contraseña no puede ser igual al usuario.";
}
if ($password !== $repetida) {
    $errores[] = "Las contraseñas no coinciden.";
}
if (!empty($errores)) {
    echo "\nNo se creó el usuario:\n - " . implode("\n - ", $errores) . "\n";
    exit(1);
}

try {
    $stmt = $pdo->prepare(
        "INSERT INTO usuarios (usuario, password, nombre, activo) VALUES (:usuario, :password, :nombre, 1)"
    );
    $stmt->execute([
        ":usuario"  => $usuario,
        ":password" => password_hash($password, PASSWORD_DEFAULT),
        ":nombre"   => $nombre,
    ]);
    echo "\nUsuario \"{$usuario}\" creado correctamente.\n";
} catch (PDOException $e) {
    error_log("[gestion_clientes] herramientas/crear_usuario.php: " . $e->getMessage());
    if ($e->getCode() === "23000") {
        echo "\nEse usuario ya existe. No se creó nada.\n";
    } else {
        echo "\nNo se pudo crear el usuario. Verificá que exista la tabla usuarios (sql/usuarios_estructura.sql).\n";
    }
    exit(1);
}
