<?php

require_once "conexion.php";

$mensaje = "";

// Si se envió el formulario para actualizar
if ($_SERVER["REQUEST_METHOD"] == "POST") {

    $id = $_POST["id_cliente"];
    $nombre = trim($_POST["nombre"]);
    $apellido = trim($_POST["apellido"]);
    $email = trim($_POST["email"]);
    $telefono = trim($_POST["telefono"]);

    if ($nombre == "" || $apellido == "" || $email == "" || $telefono == "") {
        $mensaje = "Todos los campos son obligatorios.";
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $mensaje = "El email no es válido.";
    } else {

        $sql = "UPDATE clientes 
                SET nombre = ?, apellido = ?, email = ?, telefono = ?
                WHERE id_cliente = ?";

        $stmt = $conexion->prepare($sql);
        $stmt->bind_param("ssssi", $nombre, $apellido, $email, $telefono, $id);

        if ($stmt->execute()) {
            $mensaje = "Cliente modificado correctamente.";
        } else {
            $mensaje = "Error al modificar el cliente.";
        }

        $stmt->close();
    }
}

// Obtener cliente para mostrar en el formulario
if (isset($_GET["id"])) {

    $id = $_GET["id"];

    $stmt = $conexion->prepare("SELECT * FROM clientes WHERE id_cliente = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();

    $resultado = $stmt->get_result();
    $cliente = $resultado->fetch_assoc();

    $stmt->close();

} else {
    $cliente = null;
}

?>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Modificar cliente</title>
</head>

<body>

    <h1>Modificar cliente</h1>

    <?php if ($mensaje != ""): ?>
        <p><strong><?php echo $mensaje; ?></strong></p>
    <?php endif; ?>

    <?php if ($cliente): ?>

        <form method="POST" action="modificar_cliente.php">

            <input type="hidden" name="id_cliente"
                   value="<?php echo $cliente['id_cliente']; ?>">

            <label>Nombre:</label><br>
            <input type="text" name="nombre"
                   value="<?php echo htmlspecialchars($cliente['nombre']); ?>"
                   required>
            <br><br>

            <label>Apellido:</label><br>
            <input type="text" name="apellido"
                   value="<?php echo htmlspecialchars($cliente['apellido']); ?>"
                   required>
            <br><br>

            <label>Email:</label><br>
            <input type="email" name="email"
                   value="<?php echo htmlspecialchars($cliente['email']); ?>"
                   required>
            <br><br>

            <label>Teléfono:</label><br>
            <input type="text" name="telefono"
                   value="<?php echo htmlspecialchars($cliente['telefono']); ?>"
                   required>
            <br><br>

            <button type="submit">Guardar cambios</button>

        </form>

    <?php else: ?>

        <p>No se encontró el cliente.</p>

    <?php endif; ?>

    <br>

    <a href="clientes.php">Volver al listado</a>

</body>
</html>