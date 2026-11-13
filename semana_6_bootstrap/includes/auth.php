<?php
declare(strict_types=1);

/**
 * includes/auth.php
 * Inicio seguro de sesión y protección de páginas privadas.
 * Debe incluirse ANTES de enviar cualquier salida (HTML, espacios, echo).
 */

require_once __DIR__ . "/../config/seguridad.php";

/** Segundos de inactividad tras los cuales la sesión vence (30 minutos). */
const TIEMPO_INACTIVIDAD = 1800;

/**
 * Ruta web de la carpeta del proyecto (por ejemplo "/gestion_clientes").
 * Se calcula a partir de la ubicación real de los archivos, así que
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

// Configuración de la cookie de sesión: se aplica antes de session_start()
if (session_status() !== PHP_SESSION_ACTIVE) {
    ini_set("session.use_strict_mode", "1");
    ini_set("session.use_only_cookies", "1");
    session_name("GESTION_SID"); // nombre propio: no se mezcla con otras aplicaciones en localhost
    session_set_cookie_params([
        "lifetime" => 0,
        "path"     => urlBase() === "" ? "/" : urlBase(), // la cookie solo viaja a este proyecto
        "secure"   => !empty($_SERVER["HTTPS"]) && $_SERVER["HTTPS"] !== "off",
        "httponly" => true,
        "samesite" => "Lax",
    ]);
    session_start();
}

function estaLogueado(): bool
{
    return !empty($_SESSION["usuario_id"]);
}

/** Vacía la sesión, borra la cookie del navegador y destruye la sesión en el servidor. */
function cerrarSesion(): void
{
    $_SESSION = [];

    if (ini_get("session.use_cookies")) {
        $params = session_get_cookie_params();
        setcookie(session_name(), "", [
            "expires"  => time() - 42000,
            "path"     => $params["path"],
            "domain"   => $params["domain"],
            "secure"   => $params["secure"],
            "httponly" => $params["httponly"],
            "samesite" => "Lax",
        ]);
    }
    session_destroy();
}

function redirigirLogin(string $motivo): void
{
    header("Location: " . urlBase() . "/login.php?aviso=" . rawurlencode($motivo));
    exit;
}

/** Consulta la base: ¿la cuenta de la sesión sigue activa? Si hay un error, se deniega el acceso. */
function cuentaSigueActiva(): bool
{
    global $pdo; // conexion.php deja ahí la conexión si todavía no estaba cargada
    require_once __DIR__ . "/../config/conexion.php";

    try {
        $stmt = obtenerPDO()->prepare("SELECT activo FROM usuarios WHERE id = :id LIMIT 1");
        $stmt->bindValue(":id", (int) $_SESSION["usuario_id"], PDO::PARAM_INT);
        $stmt->execute();
        $fila = $stmt->fetch();
        return $fila !== false && (bool) $fila["activo"];
    } catch (PDOException $e) {
        error_log("[gestion_clientes] auth.php (cuenta activa): " . $e->getMessage());
        return false;
    }
}

/**
 * Debe llamarse al principio de CADA página privada.
 * Exige sesión, controla el vencimiento por inactividad y que la cuenta siga activa.
 * Si algo falla, redirige al login y detiene la ejecución con exit.
 */
function requiereLogin(): void
{
    if (!estaLogueado()) {
        redirigirLogin("login");
    }

    $ahora  = time();
    $ultima = (int) ($_SESSION["ultima_actividad"] ?? 0);
    if ($ultima > 0 && ($ahora - $ultima) > TIEMPO_INACTIVIDAD) {
        cerrarSesion();
        redirigirLogin("expirada");
    }

    if (!cuentaSigueActiva()) {
        cerrarSesion();
        redirigirLogin("inactiva");
    }

    $_SESSION["ultima_actividad"] = $ahora;
}
