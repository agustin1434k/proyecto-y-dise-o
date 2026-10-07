<?php

require_once "conexion.php";

$sql = "SELECT * FROM clientes";
$resultado = $conexion->query($sql);

?>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Listado de clientes</title>
</head>

<body>

    <h1>Listado de clientes</h1>

    <table border="1" cellpadding="8">

        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Email</th>
            <th>Teléfono</th>
            <th>Acciones</th>
        </tr>

        <?php while ($cliente = $resultado->fetch_assoc()): ?>

            <tr>

                <td>
                    <?php echo $cliente["id_cliente"]; ?>
                </td>

                <td>
                    <?php echo htmlspecialchars($cliente["nombre"]); ?>
                </td>

                <td>
                    <?php echo htmlspecialchars($cliente["apellido"]); ?>
                </td>

                <td>
                    <?php echo htmlspecialchars($cliente["email"]); ?>
                </td>

                <td>
                    <?php echo htmlspecialchars($cliente["telefono"]); ?>
                </td>

                <td>

                    <a href="modificar_cliente.php?id=<?php echo $cliente['id_cliente']; ?>">
                        Modificar
                    </a>

                    |

                    <a href="eliminar_cliente.php?id=<?php echo $cliente['id_cliente']; ?>"
                       onclick="return confirm('¿Seguro que desea eliminar este cliente?');">
                        Eliminar
                    </a>

                </td>

            </tr>

        <?php endwhile; ?>

    </table>

    <br>

    <a href="alta_cliente.php">Agregar cliente</a>

    <br><br>

    <a href="index.php">Volver al menú</a>

</body>

</html>