<?php
declare(strict_types=1);

/**
 * config/seguridad.php
 * Se carga desde includes/auth.php, antes de cualquier salida.
 * - Los errores NUNCA se muestran en pantalla: van al registro (logs/errores.log).
 * - Envía cabeceras de seguridad HTTP.
 */

// Poné true SOLO mientras programás en tu computadora para ver los errores en pantalla.
// Para la entrega y para cualquier uso real tiene que estar en false.
const MODO_DESARROLLO = false;

ini_set("display_errors", MODO_DESARROLLO ? "1" : "0");
ini_set("display_startup_errors", MODO_DESARROLLO ? "1" : "0");
ini_set("log_errors", "1");
error_reporting(E_ALL);

$carpetaLogs = __DIR__ . "/../logs";
if (is_dir($carpetaLogs) && is_writable($carpetaLogs)) {
    ini_set("error_log", $carpetaLogs . "/errores.log");
}
unset($carpetaLogs);

if (PHP_SAPI !== "cli" && !headers_sent()) {
    header("X-Frame-Options: DENY");                     // evita que otra web embeba la app (clickjacking)
    header("X-Content-Type-Options: nosniff");           // el navegador no "adivina" tipos de archivo
    header("Referrer-Policy: same-origin");
    header("Content-Security-Policy: default-src 'self'; img-src 'self' data:; style-src 'self'; "
         . "script-src 'self'; form-action 'self'; frame-ancestors 'none'; base-uri 'self'; object-src 'none'");
    // Las páginas privadas no deben quedar guardadas en la caché (botón "Atrás" tras cerrar sesión)
    header("Cache-Control: no-store, no-cache, must-revalidate, max-age=0");
    header("Pragma: no-cache");

    if (!empty($_SERVER["HTTPS"]) && $_SERVER["HTTPS"] !== "off") {
        header("Strict-Transport-Security: max-age=31536000");
    }
}
