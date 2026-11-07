package ar.edu.finanzas.dao;

import ar.edu.finanzas.conexion.ConexionBD;
import ar.edu.finanzas.vista.OpcionCombo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/** Listas para los combos: cuentas, categorías y usuarios. */
public class CatalogoDAO {

    private static String columnaNombreCuenta;

    /**
     * Detecta cómo se llama la columna de texto con el nombre de la cuenta
     * (nombre, nombre_cuenta, alias...). Así no se depende de un nombre supuesto.
     */
    public static synchronized String columnaNombreCuenta() throws SQLException {
        if (columnaNombreCuenta == null) {
            try (Connection con = ConexionBD.obtenerConexion();
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM cuentas LIMIT 1");
                 ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                String[] preferidas = {"nombre", "nombre_cuenta", "alias", "descripcion", "titulo"};
                String elegida = null;
                for (String p : preferidas) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        if (md.getColumnName(i).equalsIgnoreCase(p)) {
                            elegida = md.getColumnName(i);
                            break;
                        }
                    }
                    if (elegida != null) {
                        break;
                    }
                }
                if (elegida == null) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        String n = md.getColumnName(i).toLowerCase();
                        int t = md.getColumnType(i);
                        if ((t == Types.VARCHAR || t == Types.CHAR)
                                && !n.startsWith("tipo") && !n.contains("moneda")) {
                            elegida = md.getColumnName(i);
                            break;
                        }
                    }
                }
                if (elegida == null) {
                    throw new SQLException("No se encontró una columna con el nombre en la tabla cuentas.");
                }
                columnaNombreCuenta = elegida;
            }
        }
        return columnaNombreCuenta;
    }

    public List<OpcionCombo> listarCuentas() throws SQLException {
        String col = columnaNombreCuenta();
        String sql = "SELECT id_cuenta, `" + col + "` AS nombre FROM cuentas ORDER BY `" + col + "`";
        List<OpcionCombo> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new OpcionCombo(rs.getInt("id_cuenta"), rs.getString("nombre")));
            }
        }
        return lista;
    }

    /** El campo extra de cada opción guarda tipo_permitido (INGRESO, GASTO o AMBOS). */
    public List<OpcionCombo> listarCategorias() throws SQLException {
        String sql = "SELECT id_categoria, nombre, tipo_permitido FROM categorias ORDER BY nombre";
        List<OpcionCombo> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new OpcionCombo(rs.getInt("id_categoria"), rs.getString("nombre"),
                        rs.getString("tipo_permitido")));
            }
        }
        return lista;
    }

    public List<OpcionCombo> listarUsuarios() throws SQLException {
        String sql = "SELECT id_usuario FROM usuarios ORDER BY id_usuario";
        List<OpcionCombo> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                lista.add(new OpcionCombo(id, "Usuario " + id));
            }
        }
        return lista;
    }
}
