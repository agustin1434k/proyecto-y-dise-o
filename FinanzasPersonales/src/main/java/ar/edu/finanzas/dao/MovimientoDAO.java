package ar.edu.finanzas.dao;

import ar.edu.finanzas.conexion.ConexionBD;
import ar.edu.finanzas.modelo.GastoCategoria;
import ar.edu.finanzas.modelo.Movimiento;
import ar.edu.finanzas.modelo.ResumenMensual;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    private static final String COLUMNAS = "id_movimiento, id_cuenta, id_categoria, tipo, fecha, "
            + "importe, descripcion, medio_pago, recurrente";

    // ---------------------------------------------------------------- READ

    public List<Movimiento> listarTodos() throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM movimientos ORDER BY fecha DESC, id_movimiento DESC";
        List<Movimiento> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        }
        return lista;
    }

    /** Devuelve el movimiento o null si el id no existe. */
    public Movimiento buscarPorId(int id) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM movimientos WHERE id_movimiento = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMovimiento(rs);
                }
            }
        }
        return null;
    }

    public List<Movimiento> listarPorTipoYFechas(String tipo, LocalDate desde, LocalDate hastaExclusiva)
            throws SQLException {
        if (!tipoValido(tipo)) {
            throw new IllegalArgumentException("El tipo debe ser INGRESO o GASTO.");
        }
        if (desde == null || hastaExclusiva == null || !desde.isBefore(hastaExclusiva)) {
            throw new IllegalArgumentException("El rango de fechas no es válido.");
        }
        String sql = "SELECT " + COLUMNAS + " FROM movimientos "
                + "WHERE tipo = ? AND fecha >= ? AND fecha < ? ORDER BY fecha, id_movimiento";
        List<Movimiento> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ps.setDate(2, Date.valueOf(desde));
            ps.setDate(3, Date.valueOf(hastaExclusiva));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        }
        return lista;
    }

    public List<Movimiento> buscarPorDescripcion(String texto) throws SQLException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de búsqueda no puede estar vacío.");
        }
        String sql = "SELECT " + COLUMNAS + " FROM movimientos WHERE descripcion LIKE ? "
                + "ORDER BY fecha DESC, id_movimiento DESC";
        List<Movimiento> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + texto.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        }
        return lista;
    }

    private Movimiento mapearMovimiento(ResultSet rs) throws SQLException {
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
        return m;
    }

    // -------------------------------------------------------------- CREATE

    public int insertar(Movimiento m) throws SQLException {
        validarMovimiento(m);

        String sql = "INSERT INTO movimientos (id_cuenta, id_categoria, tipo, fecha, importe, "
                + "descripcion, medio_pago, recurrente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion()) {
            validarCompatibilidadCategoria(con, m);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
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

    // -------------------------------------------------------------- UPDATE

    /** Devuelve las filas modificadas (0 si el id no existe). */
    public int actualizar(Movimiento m) throws SQLException {
        validarMovimiento(m);
        if (m.getIdMovimiento() <= 0) {
            throw new IllegalArgumentException("El id del movimiento no es válido.");
        }

        String sql = "UPDATE movimientos SET id_cuenta = ?, id_categoria = ?, tipo = ?, fecha = ?, "
                + "importe = ?, descripcion = ?, medio_pago = ?, recurrente = ? "
                + "WHERE id_movimiento = ?";

        try (Connection con = ConexionBD.obtenerConexion()) {
            validarCompatibilidadCategoria(con, m);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, m.getIdCuenta());
                ps.setInt(2, m.getIdCategoria());
                ps.setString(3, m.getTipo());
                ps.setDate(4, Date.valueOf(m.getFecha()));
                ps.setBigDecimal(5, m.getImporte());
                ps.setString(6, m.getDescripcion().trim());
                ps.setString(7, m.getMedioPago().trim());
                ps.setBoolean(8, m.isRecurrente());
                ps.setInt(9, m.getIdMovimiento());
                return ps.executeUpdate();
            }
        }
    }

    // -------------------------------------------------------------- DELETE

    /** Devuelve las filas eliminadas (0 si el id no existe). */
    public int eliminar(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del movimiento no es válido.");
        }
        String sql = "DELETE FROM movimientos WHERE id_movimiento = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    // --------------------------------------------------------- VALIDACIONES

    private boolean tipoValido(String tipo) {
        return "INGRESO".equals(tipo) || "GASTO".equals(tipo);
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
        if (!tipoValido(m.getTipo())) {
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

    /**
     * Comprueba que la categoría exista y que su campo tipo_permitido
     * (INGRESO, GASTO o AMBOS) sea compatible con el tipo del movimiento.
     */
    public void validarCompatibilidadCategoria(Connection con, Movimiento m) throws SQLException {
        String sql = "SELECT tipo_permitido FROM categorias WHERE id_categoria = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getIdCategoria());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("La categoría " + m.getIdCategoria() + " no existe.");
                }
                String permitido = rs.getString("tipo_permitido");
                if (!"AMBOS".equals(permitido) && !m.getTipo().equals(permitido)) {
                    throw new IllegalArgumentException("La categoría " + m.getIdCategoria()
                            + " solo admite movimientos de tipo " + permitido
                            + " y se intentó registrar un " + m.getTipo() + ".");
                }
            }
        }
    }

    // ------------------------------------------------------------- REPORTES

    public ResumenMensual resumenMensual(LocalDate desde, LocalDate hastaExclusiva) throws SQLException {
        if (desde == null || hastaExclusiva == null || !desde.isBefore(hastaExclusiva)) {
            throw new IllegalArgumentException("El rango de fechas no es válido.");
        }
        String sql = "SELECT "
                + "COALESCE(SUM(CASE WHEN tipo = 'INGRESO' THEN importe ELSE 0 END), 0) AS total_ingresos, "
                + "COALESCE(SUM(CASE WHEN tipo = 'GASTO' THEN importe ELSE 0 END), 0) AS total_gastos "
                + "FROM movimientos WHERE fecha >= ? AND fecha < ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hastaExclusiva));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                BigDecimal ingresos = rs.getBigDecimal("total_ingresos");
                BigDecimal gastos = rs.getBigDecimal("total_gastos");
                return new ResumenMensual(ingresos, gastos, ingresos.subtract(gastos));
            }
        }
    }

    public List<GastoCategoria> gastosPorCategoria(LocalDate desde, LocalDate hastaExclusiva)
            throws SQLException {
        if (desde == null || hastaExclusiva == null || !desde.isBefore(hastaExclusiva)) {
            throw new IllegalArgumentException("El rango de fechas no es válido.");
        }
        String sql = "SELECT c.nombre AS categoria, SUM(m.importe) AS total_gastado, "
                + "COUNT(*) AS cantidad, "
                + "ROUND(SUM(m.importe) * 100 / (SELECT SUM(importe) FROM movimientos "
                + "WHERE tipo = 'GASTO' AND fecha >= ? AND fecha < ?), 2) AS porcentaje "
                + "FROM movimientos m JOIN categorias c ON c.id_categoria = m.id_categoria "
                + "WHERE m.tipo = 'GASTO' AND m.fecha >= ? AND m.fecha < ? "
                + "GROUP BY c.id_categoria, c.nombre ORDER BY total_gastado DESC";
        List<GastoCategoria> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hastaExclusiva));
            ps.setDate(3, Date.valueOf(desde));
            ps.setDate(4, Date.valueOf(hastaExclusiva));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new GastoCategoria(rs.getString("categoria"),
                            rs.getBigDecimal("total_gastado"), rs.getInt("cantidad"),
                            rs.getBigDecimal("porcentaje")));
                }
            }
        }
        return lista;
    }
}
