package ar.edu.finanzas.dao;

import ar.edu.finanzas.conexion.ConexionBD;
import ar.edu.finanzas.modelo.Movimiento;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    public List<Movimiento> listarTodos() throws SQLException {
        String sql = "SELECT id_movimiento, id_cuenta, id_categoria, tipo, fecha, importe, "
                + "descripcion, medio_pago, recurrente FROM movimientos ORDER BY fecha DESC, id_movimiento DESC";
        List<Movimiento> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setIdMovimiento(rs.getInt("id_movimiento"));
                m.setIdCuenta(rs.getInt("id_cuenta"));
                m.setIdCategoria(rs.getInt("id_categoria"));
                m.setTipo(rs.getString("tipo"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setImporte(rs.getBigDecimal("importe"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setMedioPago(rs.getString("medio_pago"));
                m.setRecurrente(rs.getBoolean("recurrente"));
                lista.add(m);
            }
        }
        return lista;
    }

    public void validarMovimiento(Movimiento m) {
        if (m == null) {
            throw new IllegalArgumentException("El movimiento no puede ser nulo.");
        }
        if (m.getIdCuenta() <= 0) {
            throw new IllegalArgumentException("La cuenta no es válida.");
        }
        if (m.getIdCategoria() <= 0) {
            throw new IllegalArgumentException("La categoría no es válida.");
        }
        if (m.getTipo() == null
                || !(m.getTipo().equals("INGRESO") || m.getTipo().equals("GASTO"))) {
            throw new IllegalArgumentException("El tipo debe ser INGRESO o GASTO.");
        }
        if (m.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (m.getImporte() == null || m.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que cero.");
        }
        if (m.getDescripcion() == null || m.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        if (m.getMedioPago() == null || m.getMedioPago().trim().isEmpty()) {
            throw new IllegalArgumentException("El medio de pago no puede estar vacío.");
        }
    }

    public int insertar(Movimiento m) throws SQLException {
        validarMovimiento(m);

        String sql = "INSERT INTO movimientos (id_cuenta, id_categoria, tipo, fecha, importe, "
                + "descripcion, medio_pago, recurrente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, m.getIdCuenta());
            ps.setInt(2, m.getIdCategoria());
            ps.setString(3, m.getTipo());
            ps.setDate(4, Date.valueOf(m.getFecha()));
            ps.setBigDecimal(5, m.getImporte());
            ps.setString(6, m.getDescripcion().trim());
            ps.setString(7, m.getMedioPago().trim());
            ps.setBoolean(8, m.isRecurrente());

            return ps.executeUpdate();
        }
    }
}
