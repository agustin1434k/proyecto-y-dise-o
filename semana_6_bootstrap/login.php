<?php
declare(strict_types=1);

// auth.php inicia la sesión: tiene que ir antes de cualquier salida HTML
require_once __DIR__ . "/includes/auth.php";
require_once __DIR__ . "/includes/funciones.php";

// Si ya hay sesión iniciada, no hace falta volver a loguearse
if (estaLogueado()) {
    header("Location: index.php");
    exit;
}

$mensaje = null; // [tipo, texto]
$error = textoGet("error");
$aviso = textoGet("aviso");
if ($error === "1") {
    $mensaje = ["error", "Usuario o contraseña incorrectos."];
} elseif ($error === "2") {
    $mensaje = ["error", "No se pudo iniciar sesión por un problema técnico. Intentá nuevamente en unos minutos."];
} elseif ($error === "3") {
    $mensaje = ["error", "El formulario venció. Volvé a intentarlo."];
} elseif ($error === "4") {
    $mensaje = ["error", "Demasiados intentos fallidos. Esperá unos minutos antes de volver a intentar."];
} elseif (textoGet("salio") === "1") {
    $mensaje = ["exito", "Cerraste sesión correctamente."];
} elseif ($aviso === "expirada") {
    $mensaje = ["aviso", "Tu sesión venció por inactividad. Iniciá sesión nuevamente."];
} elseif ($aviso === "inactiva") {
    $mensaje = ["aviso", "Tu cuenta ya no está habilitada. Consultá con el administrador."];
} elseif ($aviso === "login") {
    $mensaje = ["aviso", "Iniciá sesión para acceder a esa página."];
}

$tituloPagina = "Iniciar sesión";
$raiz         = "";
$paginaActiva = "login";

require __DIR__ . "/includes/header.php";
require __DIR__ . "/includes/menu.php";
?>
<main class="container py-5 flex-grow-1">
    <div class="row justify-content-center">
        <div class="col-12 col-sm-10 col-md-7 col-lg-5 col-xl-4">
            <div class="text-center mb-4">
                <i class="bi bi-people-fill display-4 text-primary" aria-hidden="true"></i>
                <h1 class="h3 mt-2 titulo-seccion">Gestión de Clientes</h1>
                <p class="text-body-secondary mb-0">Iniciá sesión para continuar</p>
            </div>

            <?php if ($mensaje !== null): ?>
                <?= alertaHtml($mensaje[0], $mensaje[1], false) ?>
            <?php endif; ?>

            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4">
                    <form method="POST" action="autenticar.php" class="row g-3">
                        <?= campoCsrf() ?>
                        <div class="col-12">
                            <label for="usuario" class="form-label">Usuario</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="bi bi-person" aria-hidden="true"></i></span>
                                <input type="text" class="form-control" id="usuario" name="usuario" required maxlength="50"
                                       autocomplete="username" autofocus>
                            </div>
                        </div>
                        <div class="col-12">
                            <label for="password" class="form-label">Contraseña</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="bi bi-lock" aria-hidden="true"></i></span>
                                <input type="password" class="form-control" id="password" name="password" required
                                       autocomplete="current-password">
                            </div>
                        </div>
                        <div class="col-12 d-grid">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-box-arrow-in-right me-1" aria-hidden="true"></i>Ingresar
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>
<?php require __DIR__ . "/includes/footer.php"; ?>
