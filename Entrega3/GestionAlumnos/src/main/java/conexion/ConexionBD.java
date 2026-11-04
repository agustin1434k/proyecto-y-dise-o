package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Si tu MySQL/MariaDB usa otro puerto (ver XAMPP), cambialo acá.
    private static final String URL =
            "jdbc:mysql://localhost:3306/escuela_java?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // XAMPP por defecto: vacía

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
