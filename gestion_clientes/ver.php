<?php
/**
 * ver.php - Muestra los datos de un cliente (solo lectura).
 * OPCIONAL: usalo únicamente si tu proyecto todavía no tiene un ver.php.
 */
require_once "conexion.php";

function h($valor): string
{
    return htmlspecialchars((string) $valor, ENT_QUOTES, "UTF-8");
}

$id = filter_input(INPUT_GET, "id", FILTER_VALIDATE_INT);
$cliente = null;
$error = "";

if ($id && $id > 0) {
    try {
        $stmt = $pdo->prepare("SELECT id, nombre, email, ciudad FROM clientes WHERE id = :id");
        $stmt->bindValue(":id", $id, PDO::PARAM_INT);
        $stmt->execute();
        $cliente = $stmt->fetch() ?: null;
    } catch (PDOException $e) {
        $error = $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Ver cliente</title>
<style>
    body { margin: 0; font-family: "Segoe UI", Arial, sans-serif; background: #f1f4f9; color: #1c2733; }
    header { background: #1f3a5f; color: #fff; padding: 22px 30px; }
    header h1 { margin: 0; font-size: 1.6rem; }
    main { max-width: 600px; margin: 24px auto; padding: 0 16px; }
    .tarjeta { background: #fff; border: 1px solid #d9dee7; border-radius: 10px; padding: 22px; }
    dl { display: grid; grid-template-columns: 110px 1fr; gap: 10px 14px; margin: 0 0 18px; }
    dt { font-weight: 600; color: #5f6b7a; }
    dd { margin: 0; }
    .btn { display: inline-block; padding: 9px 16px; border-radius: 6px; text-decoration: none; font-weight: 600; font-size: .9rem; }
    .volver { background: #e7ebf1; color: #1f3a5f; }
    .editar { background: #2f6fb0; color: #fff; }
    .aviso { background: #fff8e5; border: 1px solid #f0d58a; color: #7a5a00; padding: 14px; border-radius: 8px; margin-bottom: 14px; }
</style>
</head>
<body>
<header><h1>Gestión de Clientes</h1></header>
<main>
    <div class="tarjeta">
        <?php if ($error !== ""): ?>
            <div class="aviso">No se pudo consultar la base de datos: <?= h($error) ?></div>
        <?php elseif ($cliente === null): ?>
            <div class="aviso">El cliente solicitado no existe.</div>
        <?php else: ?>
            <dl>
                <dt>ID</dt><dd><?= (int) $cliente["id"] ?></dd>
                <dt>Nombre</dt><dd><?= h($cliente["nombre"]) ?></dd>
                <dt>Email</dt><dd><?= h($cliente["email"]) ?></dd>
                <dt>Ciudad</dt><dd><?= h($cliente["ciudad"]) ?></dd>
            </dl>
            <a class="btn editar" href="editar.php?id=<?= (int) $cliente["id"] ?>">Editar</a>
        <?php endif; ?>
        <a class="btn volver" href="listar.php">&larr; Volver al listado</a>
    </div>
</main>
</body>
</html>
