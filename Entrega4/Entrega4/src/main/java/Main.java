import conexion.ConexionBD;
import vista.FrmAlumnos;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Se comprueba la conexión antes de abrir la ventana.
            try (Connection con = ConexionBD.getConnection()) {
                new FrmAlumnos().setVisible(true);
            } catch (SQLException e) {
                System.out.println("Detalle técnico: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "No se pudo conectar con MySQL.\n"
                        + "Verifique que MySQL esté iniciado en XAMPP\n"
                        + "y que el puerto y la base de datos sean correctos.",
                        "Error de conexión", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
