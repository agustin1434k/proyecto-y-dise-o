<?php
declare(strict_types=1);

/**
 * pedidos/nuevo.php
 * ADAPTAR: asume la tabla pedidos(id, cliente_id, fecha, total).
 * Muestra el formulario y, cuando llega por POST, lo procesa en este mismo archivo.
 */
require_once __DIR__ . "/../includes/auth.php";
requiereLogin();
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
<main class="container-fluid px-3 px-lg-4 py-4">
    <div class="row justify-content-center">
        <div class="col-12 col-lg-8 col-xl-6">
            <?php if (isset($errores["general"])): ?>
                <?= alertaHtml("error", $errores["general"], false) ?>
            <?php endif; ?>

            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white border-0 pt-3">
                    <h1 class="h4 mb-0 titulo-seccion"><i class="bi bi-cart-plus me-2" aria-hidden="true"></i>Registrar nuevo pedido</h1>
                </div>
                <div class="card-body">
                    <form method="POST" action="nuevo.php" class="row g-3" novalidate>
                        <?= campoCsrf() ?>

                        <div class="col-12">
                            <label for="cliente_id" class="form-label">Cliente <span class="text-danger" aria-hidden="true">*</span></label>
                            <select class="form-select<?= isset($errores["cliente_id"]) ? " is-invalid" : "" ?>" id="cliente_id" name="cliente_id" required>
                                <option value="">Seleccioná un cliente</option>
                                <?php foreach ($clientes as $c): ?>
                                    <option value="<?= (int) $c["id"] ?>" <?= $viejo["cliente_id"] === (string) $c["id"] ? "selected" : "" ?>>
                                        <?= e($c["nombre"]) ?>
                                    </option>
                                <?php endforeach; ?>
                            </select>
                            <?php if (isset($errores["cliente_id"])): ?><div class="invalid-feedback d-block"><?= e($errores["cliente_id"]) ?></div><?php endif; ?>
                        </div>

                        <div class="col-12 col-md-6">
                            <label for="fecha" class="form-label">Fecha <span class="text-danger" aria-hidden="true">*</span></label>
                            <input type="date" class="form-control<?= isset($errores["fecha"]) ? " is-invalid" : "" ?>" id="fecha" name="fecha"
                                   value="<?= e($viejo["fecha"]) ?>" required>
                            <?php if (isset($errores["fecha"])): ?><div class="invalid-feedback d-block"><?= e($errores["fecha"]) ?></div><?php endif; ?>
                        </div>

                        <div class="col-12 col-md-6">
                            <label for="total" class="form-label">Total <span class="text-danger" aria-hidden="true">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text">$</span>
                                <input type="text" class="form-control<?= isset($errores["total"]) ? " is-invalid" : "" ?>" id="total" name="total"
                                       inputmode="decimal" value="<?= e($viejo["total"]) ?>" required aria-describedby="ayudaTotal">
                            </div>
                            <div id="ayudaTotal" class="form-text">Hasta 2 decimales. Ejemplo: 1500,50</div>
                            <?php if (isset($errores["total"])): ?><div class="invalid-feedback d-block"><?= e($errores["total"]) ?></div><?php endif; ?>
                        </div>

                        <div class="col-12 d-flex flex-wrap gap-2 pt-2">
                            <button type="submit" class="btn btn-success"><i class="bi bi-floppy me-1" aria-hidden="true"></i>Guardar pedido</button>
                            <a class="btn btn-outline-secondary" href="listar.php"><i class="bi bi-x-circle me-1" aria-hidden="true"></i>Cancelar</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>
<?php require __DIR__ . "/../includes/footer.php"; ?>
