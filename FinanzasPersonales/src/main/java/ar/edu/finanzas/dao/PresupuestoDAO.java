package ar.edu.finanzas.dao;

import ar.edu.finanzas.conexion.ConexionBD;
import ar.edu.finanzas.modelo.ComparacionPresupuesto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PresupuestoDAO {

    /**
     * Compara cada presupuesto del mes con el gasto real.
     * El gasto se suma ANTES del LEFT JOIN (subconsulta agrupada) para que
     * los importes no se dupliquen. Se asume cuentas.id_usuario.
     */
    public List<ComparacionPresupuesto> compararConGastoReal(int anio, int mes) throws SQLException {
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
                + "WHERE p.anio = ? AND p.mes = ? ORDER BY p.id_usuario, c.nombre";
        List<ComparacionPresupuesto> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
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
}
