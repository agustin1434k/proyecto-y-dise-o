<?php
declare(strict_types=1);

/**
 * includes/intentos.php
 * Límite de intentos de login fallidos (protección contra fuerza bruta).
 * Usa la tabla intentos_login (sql/seguridad_intentos_login.sql).
 * Si la tabla no existe, el error se registra en el log y el login sigue funcionando sin límite.
 */

const MAX_INTENTOS_LOGIN    = 5;   // intentos fallidos permitidos...
const VENTANA_MINUTOS_LOGIN = 15;  // ...dentro de este período

function ipCliente(): string
{
    // Solo REMOTE_ADDR: las cabeceras tipo X-Forwarded-For las puede falsificar cualquiera
    return substr((string) ($_SERVER["REMOTE_ADDR"] ?? ""), 0, 45);
}

function normalizarUsuarioIntento(string $usuario): string
{
    return mb_substr(mb_strtolower($usuario), 0, 50);
}

function loginBloqueado(PDO $pdo, string $usuario, string $ip): bool
{
    try {
        $stmt = $pdo->prepare(
            "SELECT COUNT(*) FROM intentos_login
             WHERE usuario = :usuario AND ip = :ip
               AND fecha >= (NOW() - INTERVAL :minutos MINUTE)"
        );
        $stmt->bindValue(":usuario", normalizarUsuarioIntento($usuario));
        $stmt->bindValue(":ip", $ip);
        $stmt->bindValue(":minutos", VENTANA_MINUTOS_LOGIN, PDO::PARAM_INT);
        $stmt->execute();
        return (int) $stmt->fetchColumn() >= MAX_INTENTOS_LOGIN;
    } catch (PDOException $e) {
        error_log("[gestion_clientes] intentos.php (consultar): " . $e->getMessage());
        return false;
    }
}

function registrarIntentoFallido(PDO $pdo, string $usuario, string $ip): void
{
    try {
        $stmt = $pdo->prepare("INSERT INTO intentos_login (usuario, ip) VALUES (:usuario, :ip)");
        $stmt->execute([":usuario" => normalizarUsuarioIntento($usuario), ":ip" => $ip]);

        // De vez en cuando se borran los registros viejos para que la tabla no crezca sin límite
        if (random_int(1, 20) === 1) {
            $pdo->exec("DELETE FROM intentos_login WHERE fecha < (NOW() - INTERVAL 1 DAY)");
        }
    } catch (PDOException $e) {
        error_log("[gestion_clientes] intentos.php (registrar): " . $e->getMessage());
    }
}

function limpiarIntentos(PDO $pdo, string $usuario, string $ip): void
{
    try {
        $stmt = $pdo->prepare("DELETE FROM intentos_login WHERE usuario = :usuario AND ip = :ip");
        $stmt->execute([":usuario" => normalizarUsuarioIntento($usuario), ":ip" => $ip]);
    } catch (PDOException $e) {
        error_log("[gestion_clientes] intentos.php (limpiar): " . $e->getMessage());
    }
}
