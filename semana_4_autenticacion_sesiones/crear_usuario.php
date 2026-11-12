<?php
declare(strict_types=1);

/**
 * crear_usuario.php - SOLO PARA LABORATORIO LOCAL.
 * Ejecutalo UNA vez desde el navegador. Al terminar intenta eliminarse solo;
 * si no puede, borralo vos.
 * Solo funciona desde la misma computadora (localhost).
 */
header("Content-Type: text/plain; charset=UTF-8");

$ip = $_SERVER["REMOTE_ADDR"] ?? "";
if (!in_array($ip, ["127.0.0.1", "::1"], true)) {
    http_response_code(403);
    exit("Este archivo solo puede ejecutarse desde la misma computadora.");
}

require_once __DIR__ . "/config/conexion.php";

$usuario       = "admin";
$nombre        = "Administrador";
$passwordPlano = "CambiarEstaClave_2026!"; // solo para el laboratorio; cambiala

$hash = password_hash($passwordPlano, PASSWORD_DEFAULT);

$terminado = false; // true cuando el usuario se creó o ya existía

try {
    $stmt = $pdo->prepare(
        "INSERT INTO usuarios (usuario, password, nombre, activo)
         VALUES (:usuario, :password, :nombre, 1)"
    );
    $stmt->execute([
        ":usuario"  => $usuario,
        ":password" => $hash,
        ":nombre"   => $nombre,
    ]);
    echo "Usuario de prueba creado: \"{$usuario}\".\n";
    echo "La contraseña es la que figura en este archivo (variable \$passwordPlano).\n";
    $terminado = true;
} catch (PDOException $e) {
    error_log("[gestion_clientes] crear_usuario.php: " . $e->getMessage());
    if ($e->getCode() === "23000") {
        echo "El usuario \"{$usuario}\" ya existe. No se creó nada.\n";
        $terminado = true;
    } else {
        echo "No se pudo crear el usuario. Verificá que exista la tabla usuarios (sql/usuarios_estructura.sql).\n";
        echo "Cuando lo soluciones, volvé a abrir este archivo.\n";
    }
}

// Una vez usado, el archivo intenta eliminarse solo para no quedar accesible.
if ($terminado) {
    if (@unlink(__FILE__)) {
        echo "Este archivo se eliminó automáticamente.";
    } else {
        echo "No se pudo eliminar solo: BORRÁ crear_usuario.php manualmente de la carpeta del proyecto.";
    }
}
