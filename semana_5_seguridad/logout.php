<?php
declare(strict_types=1);

require_once __DIR__ . "/includes/auth.php";
require_once __DIR__ . "/includes/funciones.php";

// El cierre de sesión se hace por POST con token CSRF (botón "Cerrar sesión").
// Si alguien abre esta URL directamente, no se cierra nada.
if ($_SERVER["REQUEST_METHOD"] !== "POST" || !csrfValido()) {
    header("Location: " . (estaLogueado() ? "panel.php" : "login.php"));
    exit;
}

cerrarSesion(); // vacía $_SESSION, borra la cookie y destruye la sesión

header("Location: login.php?salio=1");
exit;
