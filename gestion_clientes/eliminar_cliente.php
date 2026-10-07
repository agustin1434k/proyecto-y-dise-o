<?php

require_once "conexion.php";

if (isset($_GET["id"])) {

    $id = $_GET["id"];

    // Primero eliminamos los pedidos del cliente
    $sqlPedidos = "DELETE FROM pedidos WHERE id_cliente = ?";
    $stmtPedidos = $conexion->prepare($sqlPedidos);
    $stmtPedidos->bind_param("i", $id);

    if ($stmtPedidos->execute()) {

        $stmtPedidos->close();

        // Después eliminamos el cliente
        $sqlCliente = "DELETE FROM clientes WHERE id_cliente = ?";
        $stmtCliente = $conexion->prepare($sqlCliente);
        $stmtCliente->bind_param("i", $id);

        if ($stmtCliente->execute()) {
            echo "Cliente eliminado correctamente.";
        } else {
            echo "Error al eliminar el cliente.";
        }

        $stmtCliente->close();

    } else {

        echo "Error al eliminar los pedidos del cliente.";

        $stmtPedidos->close();
    }

} else {

    echo "No se indicó ningún cliente.";

}

?>

<br><br>

<a href="clientes.php">Volver al listado</a>