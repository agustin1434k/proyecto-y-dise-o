<?php
declare(strict_types=1);

require_once __DIR__ . "/includes/auth.php";
require_once __DIR__ . "/includes/funciones.php";

// El cierre de sesión se hace por POST con token CSRF (botón "Cerrar sesión" del menú).
// Si alguien abre esta URL directamente, no se cierra nada: se lo manda a donde corresponde.
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    header("Location: " . (estaLogueado() ? "panel.php" : "login.php"));
    exit;
}
if (!csrfValido()) {
    header("Location: " . (estaLogueado() ? "panel.php" : "login.php"));
    exit;
}

// 1) vaciar los datos de la sesión
$_SESSION = [];

// 2) borrar la cookie de sesión del navegador
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

// 3) destruir la sesión en el servidor
session_destroy();

header("Location: login.php?salio=1");
exit;
