package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Si MySQL usa otro puerto en XAMPP, cambiá el 3306 por el que corresponda
    private static final String URL = "jdbc:mysql://localhost:3306/escuela_java";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
