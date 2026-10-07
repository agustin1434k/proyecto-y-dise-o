<?php

require_once "conexion.php";

$sql = "SELECT 
            clientes.nombre,
            clientes.apellido,
            clientes.email,
            pedidos.producto,
            pedidos.fecha,
            pedidos.importe
        FROM clientes
        INNER JOIN pedidos
        ON clientes.id_cliente = pedidos.id_cliente
        ORDER BY pedidos.fecha";

$resultado = $conexion->query($sql);

?>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Clientes y pedidos</title>
</head>

<body>

    <h1>Clientes y pedidos</h1>

    <table border="1" cellpadding="8">

        <tr>
            <th>Cliente</th>
            <th>Email</th>
            <th>Producto</th>
            <th>Fecha</th>
            <th>Importe</th>
        </tr>

        <?php while ($pedido = $resultado->fetch_assoc()): ?>

            <tr>
                <td>
                    <?php 
                    echo $pedido["nombre"] . " " . $pedido["apellido"]; 
                    ?>
                </td>

                <td>
                    <?php echo $pedido["email"]; ?>
                </td>

                <td>
                    <?php echo $pedido["producto"]; ?>
                </td>

                <td>
                    <?php echo $pedido["fecha"]; ?>
                </td>

                <td>
                    $<?php echo number_format($pedido["importe"], 2, ',', '.'); ?>
                </td>
            </tr>

        <?php endwhile; ?>

    </table>

    <br>

    <a href="index.php">Volver al menú</a>

</body>

</html>