<?php
/**
 * includes/form_cliente.php
 * Formulario Bootstrap compartido por clientes/nuevo.php y clientes/editar.php.
 * Variables esperadas:
 *   $accionForm  archivo que procesa el formulario (guardar.php / actualizar.php)
 *   $cliente     arreglo con id (o null), nombre, email, ciudad
 *   $errores     arreglo campo => mensaje (errores de validación del SERVIDOR)
 *   $textoBoton  texto del botón principal
 * Las clases is-invalid / invalid-feedback solo MUESTRAN los errores que detectó PHP;
 * la validación real sigue estando en validarCliente() (includes/funciones.php).
 */
?>
<form method="POST" action="<?= e($accionForm) ?>" class="row g-3" novalidate>
    <?= campoCsrf() ?>
    <?php if ($cliente["id"] !== null): ?>
        <input type="hidden" name="id" value="<?= (int) $cliente["id"] ?>">
    <?php endif; ?>

    <div class="col-12">
        <label for="nombre" class="form-label">Nombre <span class="text-danger" aria-hidden="true">*</span></label>
        <input type="text" class="form-control<?= isset($errores["nombre"]) ? " is-invalid" : "" ?>" id="nombre" name="nombre"
               maxlength="100" required value="<?= e($cliente["nombre"]) ?>" aria-describedby="ayudaNombre">
        <div id="ayudaNombre" class="form-text">Obligatorio. Entre 2 y 100 caracteres.</div>
        <?php if (isset($errores["nombre"])): ?><div class="invalid-feedback d-block"><?= e($errores["nombre"]) ?></div><?php endif; ?>
    </div>

    <div class="col-12 col-md-6">
        <label for="email" class="form-label">Email</label>
        <input type="text" class="form-control<?= isset($errores["email"]) ? " is-invalid" : "" ?>" id="email" name="email"
               maxlength="120" inputmode="email" autocomplete="off" value="<?= e($cliente["email"]) ?>" aria-describedby="ayudaEmail">
        <div id="ayudaEmail" class="form-text">Opcional. Ejemplo: nombre@dominio.com</div>
        <?php if (isset($errores["email"])): ?><div class="invalid-feedback d-block"><?= e($errores["email"]) ?></div><?php endif; ?>
    </div>

    <div class="col-12 col-md-6">
        <label for="ciudad" class="form-label">Ciudad</label>
        <input type="text" class="form-control<?= isset($errores["ciudad"]) ? " is-invalid" : "" ?>" id="ciudad" name="ciudad"
               maxlength="80" value="<?= e($cliente["ciudad"]) ?>" aria-describedby="ayudaCiudad">
        <div id="ayudaCiudad" class="form-text">Opcional. Hasta 80 caracteres.</div>
        <?php if (isset($errores["ciudad"])): ?><div class="invalid-feedback d-block"><?= e($errores["ciudad"]) ?></div><?php endif; ?>
    </div>

    <div class="col-12 d-flex flex-wrap gap-2 pt-2">
        <button type="submit" class="btn btn-success"><i class="bi bi-floppy me-1" aria-hidden="true"></i><?= e($textoBoton) ?></button>
        <a class="btn btn-outline-secondary" href="listar.php"><i class="bi bi-x-circle me-1" aria-hidden="true"></i>Cancelar</a>
    </div>
</form>
