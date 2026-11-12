<?php
declare(strict_types=1);

// auth.php inicia la sesión: tiene que ir antes de cualquier salida HTML
require_once __DIR__ . "/includes/auth.php";
require_once __DIR__ . "/includes/funciones.php";

// Si ya hay sesión iniciada, no hace falta volver a loguearse
if (estaLogueado()) {
    header("Location: panel.php");
    exit;
}

$mensaje = null; // [tipo, texto]
if (textoGet("error") === "1") {
    $mensaje = ["error", "Usuario o contraseña incorrectos."];
} elseif (textoGet("error") === "2") {
    $mensaje = ["error", "No se pudo iniciar sesión por un problema técnico. Intentá nuevamente en unos minutos."];
} elseif (textoGet("salio") === "1") {
    $mensaje = ["exito", "Cerraste sesión correctamente."];
} elseif (textoGet("aviso") === "login") {
    $mensaje = ["aviso", "Iniciá sesión para acceder a esa página."];
}

$tituloPagina = "Iniciar sesión";
$raiz         = "";
$paginaActiva = "login";

require __DIR__ . "/includes/header.php";
require __DIR__ . "/includes/menu.php";
?>
<main class="contenedor">
    <section class="tarjeta login">
        <h2>Iniciar sesión</h2>

        <?php if ($mensaje !== null): ?>
            <div class="alerta alerta-<?= e($mensaje[0]) ?>"><?= e($mensaje[1]) ?></div>
        <?php endif; ?>

        <form method="POST" action="autenticar.php" class="formulario">
            <div class="campo">
                <label for="usuario">Usuario</label>
                <input type="text" id="usuario" name="usuario" required maxlength="50"
                       autocomplete="username" autofocus>
            </div>
            <div class="campo">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" required
                       autocomplete="current-password">
            </div>
            <div class="acciones-form">
                <button type="submit" class="btn btn-primario">Ingresar</button>
            </div>
        </form>
    </section>
</main>
<?php require __DIR__ . "/includes/footer.php"; ?>
