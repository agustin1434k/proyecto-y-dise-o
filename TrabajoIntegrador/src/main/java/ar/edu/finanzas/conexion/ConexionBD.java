package ar.edu.finanzas.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/finanzas_personales"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
