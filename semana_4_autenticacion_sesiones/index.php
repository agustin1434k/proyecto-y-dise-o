<?php
declare(strict_types=1);

// Punto de entrada: según haya sesión o no, va al panel o al login.
require_once __DIR__ . "/includes/auth.php";

header("Location: " . (estaLogueado() ? "panel.php" : "login.php"));
exit;
