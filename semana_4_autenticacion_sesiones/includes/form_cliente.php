<?php
/**
 * includes/form_cliente.php
 * Formulario compartido por clientes/nuevo.php y clientes/editar.php.
 * Variables esperadas:
 *   $accionForm  archivo que procesa el formulario (guardar.php / actualizar.php)
 *   $cliente     arreglo con id (o null), nombre, email, ciudad
 *   $errores     arreglo campo => mensaje
 *   $textoBoton  texto del botón principal
 */
?>
<form method="POST" action="<?= e($accionForm) ?>" class="formulario" novalidate>
    <?= campoCsrf() ?>
    <?php if ($cliente["id"] !== null): ?>
        <input type="hidden" name="id" value="<?= (int) $cliente["id"] ?>">
    <?php endif; ?>

    <div class="campo">
        <label for="nombre">Nombre *</label>
        <input type="text" id="nombre" name="nombre" maxlength="100" required
               value="<?= e($cliente["nombre"]) ?>">
        <?php if (isset($errores["nombre"])): ?><span class="error-campo"><?= e($errores["nombre"]) ?></span><?php endif; ?>
    </div>

    <div class="campo">
        <label for="email">Email</label>
        <input type="text" id="email" name="email" maxlength="120" inputmode="email"
               value="<?= e($cliente["email"]) ?>">
        <?php if (isset($errores["email"])): ?><span class="error-campo"><?= e($errores["email"]) ?></span><?php endif; ?>
    </div>

    <div class="campo">
        <label for="ciudad">Ciudad</label>
        <input type="text" id="ciudad" name="ciudad" maxlength="80"
               value="<?= e($cliente["ciudad"]) ?>">
        <?php if (isset($errores["ciudad"])): ?><span class="error-campo"><?= e($errores["ciudad"]) ?></span><?php endif; ?>
    </div>

    <div class="acciones-form">
        <button type="submit" class="btn btn-verde"><?= e($textoBoton) ?></button>
        <a class="btn btn-secundario" href="listar.php">Cancelar</a>
    </div>
</form>
