package ar.edu.finanzas.dao;

import ar.edu.finanzas.conexion.ConexionBD;
import ar.edu.finanzas.modelo.ComparacionPresupuesto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PresupuestoDAO {

    public List<ComparacionPresupuesto> compararConGastoReal(int anio, int mes) throws SQLException {
        return compararConGastoReal(anio, mes, null);
    }

    /**
     * Compara cada presupuesto del mes con el gasto real (solo movimientos GASTO).
     * El gasto se suma ANTES del LEFT JOIN, en una subconsulta agrupada por
     * usuario + categoría + año + mes, para que los importes no se dupliquen.
     * idUsuario null = todos los usuarios. Se asume cuentas.id_usuario.
     */
    public List<ComparacionPresupuesto> compararConGastoReal(int anio, int mes, Integer idUsuario)
            throws SQLException {
        if (anio < 2000 || mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Año o mes no válidos.");
        }
        String sql = "SELECT p.id_usuario, c.nombre AS categoria, p.anio, p.mes, p.monto_planificado, "
                + "COALESCE(g.total_gastado, 0) AS gasto_real, "
                + "p.monto_planificado - COALESCE(g.total_gastado, 0) AS disponible, "
                + "CASE WHEN COALESCE(g.total_gastado, 0) > p.monto_planificado THEN 'EXCEDIDO' "
                + "WHEN COALESCE(g.total_gastado, 0) = p.monto_planificado THEN 'AL LIMITE' "
                + "ELSE 'DENTRO DEL PRESUPUESTO' END AS estado "
                + "FROM presupuestos p "
                + "JOIN categorias c ON c.id_categoria = p.id_categoria "
                + "LEFT JOIN (SELECT cu.id_usuario, m.id_categoria, YEAR(m.fecha) AS anio, "
                + "MONTH(m.fecha) AS mes, SUM(m.importe) AS total_gastado "
                + "FROM movimientos m JOIN cuentas cu ON cu.id_cuenta = m.id_cuenta "
                + "WHERE m.tipo = 'GASTO' "
                + "GROUP BY cu.id_usuario, m.id_categoria, YEAR(m.fecha), MONTH(m.fecha)) g "
                + "ON g.id_usuario = p.id_usuario AND g.id_categoria = p.id_categoria "
                + "AND g.anio = p.anio AND g.mes = p.mes "
                + "WHERE p.anio = ? AND p.mes = ? "
                + (idUsuario != null ? "AND p.id_usuario = ? " : "")
                + "ORDER BY p.id_usuario, c.nombre";
        List<ComparacionPresupuesto> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            if (idUsuario != null) {
                ps.setInt(3, idUsuario);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ComparacionPresupuesto(rs.getInt("id_usuario"),
                            rs.getString("categoria"), rs.getInt("anio"), rs.getInt("mes"),
                            rs.getBigDecimal("monto_planificado"), rs.getBigDecimal("gasto_real"),
                            rs.getBigDecimal("disponible"), rs.getString("estado")));
                }
            }
        }
        return lista;
    }

    /**
     * Guarda un presupuesto. Si ya existe uno para el mismo usuario, categoría,
     * año y mes, actualiza el monto (no crea duplicados).
     */
    public int guardar(int idUsuario, int idCategoria, int anio, int mes,
            BigDecimal monto, String observaciones) throws SQLException {
        if (anio < 2000 || mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Año o mes no válidos.");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto planificado debe ser mayor que cero.");
        }

        try (Connection con = ConexionBD.obtenerConexion()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT 1 FROM usuarios WHERE id_usuario = ?")) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("El usuario " + idUsuario + " no existe.");
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT tipo_permitido FROM categorias WHERE id_categoria = ?")) {
                ps.setInt(1, idCategoria);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("La categoría " + idCategoria + " no existe.");
                    }
                    if ("INGRESO".equals(rs.getString("tipo_permitido"))) {
                        throw new IllegalArgumentException(
                                "Un presupuesto solo puede asignarse a categorías de gasto.");
                    }
                }
            }
            String sql = "INSERT INTO presupuestos (id_usuario, id_categoria, anio, mes, "
                    + "monto_planificado, observaciones) VALUES (?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE monto_planificado = VALUES(monto_planificado), "
                    + "observaciones = VALUES(observaciones)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idCategoria);
                ps.setInt(3, anio);
                ps.setInt(4, mes);
                ps.setBigDecimal(5, monto);
                if (observaciones == null || observaciones.trim().isEmpty()) {
                    ps.setNull(6, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(6, observaciones.trim());
                }
                return ps.executeUpdate();
            }
        }
    }
}
