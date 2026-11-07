package ar.edu.finanzas;

import ar.edu.finanzas.conexion.ConexionBD;
import ar.edu.finanzas.vista.FinanzasFrame;
import java.awt.Font;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            configurarEstilo();
            try (Connection con = ConexionBD.obtenerConexion()) {
                // Si llega acá, la conexión funciona.
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo conectar con la base de datos.\n"
                        + "Verificá que MySQL esté iniciado en XAMPP y que los datos de ConexionBD sean correctos.\n\n"
                        + "Detalle: " + e.getMessage(),
                        "Error de conexión", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            new FinanzasFrame().setVisible(true);
        });
    }

    private static void configurarEstilo() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // se usa el estilo por defecto
        }
        Enumeration<Object> claves = UIManager.getDefaults().keys();
        while (claves.hasMoreElements()) {
            Object clave = claves.nextElement();
            Object valor = UIManager.get(clave);
            if (valor instanceof FontUIResource) {
                UIManager.put(clave, new FontUIResource("Segoe UI", Font.PLAIN, 13));
            }
        }
    }
}
