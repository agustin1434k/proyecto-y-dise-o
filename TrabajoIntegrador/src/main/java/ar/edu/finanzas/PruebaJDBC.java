package ar.edu.finanzas;

import ar.edu.finanzas.conexion.ConexionBD;
import ar.edu.finanzas.dao.MovimientoDAO;
import ar.edu.finanzas.modelo.Movimiento;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PruebaJDBC {

    public static void main(String[] args) {
        try (Connection con = ConexionBD.obtenerConexion()) {
            System.out.println("Conexión establecida correctamente.");
            System.out.println("Base: " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return;
        }

        try {
            MovimientoDAO dao = new MovimientoDAO();
            List<Movimiento> lista = dao.listarTodos();

            System.out.println("\n--- Movimientos (primeros 10) ---");
            int limite = Math.min(10, lista.size());
            for (int i = 0; i < limite; i++) {
                Movimiento m = lista.get(i);
                System.out.println("ID: " + m.getIdMovimiento()
                        + " | Fecha: " + m.getFecha()
                        + " | Tipo: " + m.getTipo()
                        + " | Importe: " + m.getImporte()
                        + " | Descripción: " + m.getDescripcion());
            }
            System.out.println("Total en la base: " + lista.size());
        } catch (SQLException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
    }
}
