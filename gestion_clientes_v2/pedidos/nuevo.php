<?php
declare(strict_types=1);

/**
 * pedidos/nuevo.php
 * ADAPTAR: asume la tabla pedidos(id, cliente_id, fecha, total).
 * Muestra el formulario y, cuando llega por POST, lo procesa en este mismo archivo.
 */
require_once __DIR__ . "/../config/conexion.php";
require_once __DIR__ . "/../includes/funciones.php";

$errores = [];
$viejo = ["cliente_id" => "", "fecha" => date("Y-m-d"), "total" => ""];

try {
    $clientes = $pdo->query("SELECT id, nombre FROM clientes ORDER BY nombre")->fetchAll();
} catch (PDOException $e) {
    error_log("[gestion_clientes] pedidos/nuevo.php (clientes): " . $e->getMessage());
    $clientes = [];
    $errores["general"] = "No se pudo cargar la lista de clientes.";
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    if (!csrfValido()) {
        $errores["general"] = "El formulario venció o no es válido. Volvé a intentarlo.";
    } else {
        $viejo = [
            "cliente_id" => textoPost("cliente_id"),
            "fecha"      => textoPost("fecha"),
            "total"      => textoPost("total"),
        ];

        $clienteId = idPositivo($viejo["cliente_id"]);
        if ($clienteId === null) {
            $errores["cliente_id"] = "Elegí un cliente.";
        }

        $fecha = DateTime::createFromFormat("Y-m-d", $viejo["fecha"]);
        if (!$fecha || $fecha->format("Y-m-d") !== $viejo["fecha"]) {
            $errores["fecha"] = "La fecha no es válida.";
        }

        if (!preg_match('/^\d{1,8}([.,]\d{1,2})?$/', $viejo["total"])) {
            $errores["total"] = "El total debe ser un número positivo con hasta 2 decimales (ej.: 1500,50).";
        }

        if (empty($errores)) {
            $total = str_replace(",", ".", $viejo["total"]);
            try {
                $existe = $pdo->prepare("SELECT 1 FROM clientes WHERE id = :id");
                $existe->bindValue(":id", $clienteId, PDO::PARAM_INT);
                $existe->execute();

                if (!$existe->fetchColumn()) {
                    $errores["cliente_id"] = "El cliente seleccionado no existe.";
                } else {
                    $stmt = $pdo->prepare(
                        "INSERT INTO pedidos (cliente_id, fecha, total) VALUES (:cliente_id, :fecha, :total)"
                    );
                    $stmt->bindValue(":cliente_id", $clienteId, PDO::PARAM_INT);
                    $stmt->bindValue(":fecha", $viejo["fecha"]);
                    $stmt->bindValue(":total", $total);
                    $stmt->execute();

                    flash("exito", "Pedido registrado correctamente.");
                    redirigir("listar.php");
                }
            } catch (PDOException $e) {
                error_log("[gestion_clientes] pedidos/nuevo.php: " . $e->getMessage());
                $errores["general"] = "No se pudo guardar el pedido. Verificá que exista la tabla pedidos.";
            }
        }
    }
}

$tituloPagina = "Nuevo pedido";
$raiz         = "../";
$paginaActiva = "pedidos";

require __DIR__ . "/../includes/header.php";
require __DIR__ . "/../includes/menu.php";
?>
<main class="contenedor">
    <?php if (isset($errores["general"])): ?>
        <div class="alerta alerta-error"><?= e($errores["general"]) ?></div>
    <?php endif; ?>

    <section class="tarjeta">
        <h2>Registrar nuevo pedido</h2>
        <form method="POST" action="nuevo.php" class="formulario" novalidate>
            <?= campoCsrf() ?>

            <div class="campo">
                <label for="cliente_id">Cliente *</label>
                <select id="cliente_id" name="cliente_id" required>
                    <option value="">Seleccioná un cliente</option>
                    <?php foreach ($clientes as $c): ?>
                        <option value="<?= (int) $c["id"] ?>" <?= $viejo["cliente_id"] === (string) $c["id"] ? "selected" : "" ?>>
                            <?= e($c["nombre"]) ?>
                        </option>
                    <?php endforeach; ?>
                </select>
                <?php if (isset($errores["cliente_id"])): ?><span class="error-campo"><?= e($errores["cliente_id"]) ?></span><?php endif; ?>
            </div>

            <div class="campo">
                <label for="fecha">Fecha *</label>
                <input type="date" id="fecha" name="fecha" value="<?= e($viejo["fecha"]) ?>" required>
                <?php if (isset($errores["fecha"])): ?><span class="error-campo"><?= e($errores["fecha"]) ?></span><?php endif; ?>
            </div>

            <div class="campo">
                <label for="total">Total ($) *</label>
                <input type="text" id="total" name="total" inputmode="decimal" value="<?= e($viejo["total"]) ?>" required>
                <?php if (isset($errores["total"])): ?><span class="error-campo"><?= e($errores["total"]) ?></span><?php endif; ?>
            </div>

            <div class="acciones-form">
                <button type="submit" class="btn btn-verde">Guardar pedido</button>
                <a class="btn btn-secundario" href="listar.php">Cancelar</a>
            </div>
        </form>
    </section>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
