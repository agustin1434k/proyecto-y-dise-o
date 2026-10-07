<?php

require_once "conexion.php";

$mensaje = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    $nombre = trim($_POST["nombre"]);
    $apellido = trim($_POST["apellido"]);
    $email = trim($_POST["email"]);
    $telefono = trim($_POST["telefono"]);

    if ($nombre == "" || $apellido == "" || $email == "" || $telefono == "") {
        $mensaje = "Todos los campos son obligatorios.";
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $mensaje = "El email no es válido.";
    } else {

        $sql = "INSERT INTO clientes (nombre, apellido, email, telefono)
                VALUES (?, ?, ?, ?)";

        $stmt = $conexion->prepare($sql);
        $stmt->bind_param("ssss", $nombre, $apellido, $email, $telefono);

        if ($stmt->execute()) {
            $mensaje = "Cliente agregado correctamente.";
        } else {
            $mensaje = "Error al agregar el cliente.";
        }

        $stmt->close();
    }
}

?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Alta de cliente</title>
</head>

<body>

    <h1>Alta de cliente</h1>

    <?php if ($mensaje != ""): ?>
        <p><strong><?php echo $mensaje; ?></strong></p>
    <?php endif; ?>

    <form method="POST" action="alta_cliente.php">

        <label>Nombre:</label><br>
        <input type="text" name="nombre" required>
        <br><br>

        <label>Apellido:</label><br>
        <input type="text" name="apellido" required>
        <br><br>

        <label>Email:</label><br>
        <input type="email" name="email" required>
        <br><br>

        <label>Teléfono:</label><br>
        <input type="text" name="telefono" required>
        <br><br>

        <button type="submit">Agregar cliente</button>

    </form>

    <br>

    <a href="index.php">Volver al menú</a>

</body>
</html>