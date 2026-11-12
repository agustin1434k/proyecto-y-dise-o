<?php
declare(strict_types=1);

/**
 * config/conexion.php
 * ÚNICO lugar donde se crea la conexión PDO. Todos los módulos la reutilizan
 * mediante require_once, que garantiza que el archivo se ejecute una sola vez.
 * Deja disponible la variable $pdo.
 */

function conectarBD(): PDO
{
    $host    = "localhost";
    $base    = "gestion_clientes";
    $usuario = "root";
    $clave   = "";

    try {
        return new PDO(
            "mysql:host={$host};dbname={$base};charset=utf8mb4",
            $usuario,
            $clave,
            [
                PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES   => false,
            ]
        );
    } catch (PDOException $e) {
        // El detalle técnico va al registro de errores, no a la pantalla.
        error_log("[gestion_clientes] Error de conexión: " . $e->getMessage());
        http_response_code(500);
        exit("No se pudo conectar con la base de datos. "
           . "Verificá que MySQL esté iniciado en el panel de XAMPP.");
    }
}

$pdo = conectarBD();
