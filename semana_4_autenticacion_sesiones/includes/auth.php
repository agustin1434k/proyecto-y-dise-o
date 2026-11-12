<?php
declare(strict_types=1);

/**
 * includes/auth.php
 * Centraliza el inicio de sesión y la protección de páginas privadas.
 * Debe incluirse ANTES de enviar cualquier salida (HTML, espacios, echo).
 */

// Configuración de la cookie de sesión: se aplica antes de session_start()
if (session_status() !== PHP_SESSION_ACTIVE) {
    ini_set("session.use_strict_mode", "1");
    ini_set("session.use_only_cookies", "1");
    session_set_cookie_params([
        "lifetime" => 0,
        "path"     => "/",
        "secure"   => !empty($_SERVER["HTTPS"]) && $_SERVER["HTTPS"] !== "off",
        "httponly" => true,
        "samesite" => "Lax",
    ]);
    session_start();
}

/**
 * Ruta web de la carpeta del proyecto (por ejemplo "/gestion_clientes").
 * Se calcula sola a partir de la ubicación real de los archivos, así que
 * funciona aunque cambies el nombre de la carpeta.
 */
function urlBase(): string
{
    $docRootCrudo = (string) ($_SERVER["DOCUMENT_ROOT"] ?? "");
    if ($docRootCrudo === "") {
        return "";
    }
    $proyecto = str_replace("\\", "/", (string) realpath(__DIR__ . "/.."));
    $docRoot  = str_replace("\\", "/", (string) realpath($docRootCrudo));

    if ($proyecto !== "" && $docRoot !== "" && stripos($proyecto, $docRoot) === 0) {
        return rtrim(substr($proyecto, strlen($docRoot)), "/");
    }
    return "";
}

function estaLogueado(): bool
{
    return !empty($_SESSION["usuario_id"]);
}

/** Si no hay sesión iniciada, redirige al login y DETIENE la ejecución. */
function requiereLogin(): void
{
    if (!estaLogueado()) {
        header("Location: " . urlBase() . "/login.php?aviso=login");
        exit;
    }
}
