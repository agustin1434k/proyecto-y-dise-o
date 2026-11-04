package dao;

import conexion.ConexionBD;
import modelo.Alumno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object): concentra todo el acceso a la tabla "alumnos".
 */
public class AlumnoDAO {

    // Convierte la fila actual del ResultSet en un objeto Alumno.
    private Alumno mapear(ResultSet rs) throws SQLException {
        return new Alumno(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("email"),
                rs.getInt("edad"),
                rs.getString("curso"));
    }

    // A. INSERT
    public boolean insertar(Alumno a) {
        String sql = "INSERT INTO alumnos (nombre, apellido, email, edad, curso) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getEmail());
            ps.setInt(4, a.getEdad());
            ps.setString(5, a.getCurso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    // B. SELECT de todos, ordenados por ID
    public List<Alumno> listar() {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumnos ORDER BY id";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    // C. UPDATE (siempre con WHERE id=?)
    public boolean actualizar(Alumno a) {
        String sql = "UPDATE alumnos SET nombre=?, apellido=?, email=?, edad=?, curso=? WHERE id=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getEmail());
            ps.setInt(4, a.getEdad());
            ps.setString(5, a.getCurso());
            ps.setInt(6, a.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    // D. DELETE (siempre con WHERE id=?)
    public boolean eliminar(int id) {
        String sql = "DELETE FROM alumnos WHERE id=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // E. Búsqueda parcial por apellido (LIKE)
    public List<Alumno> buscarPorApellido(String texto) {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumnos WHERE apellido LIKE ? ORDER BY id";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por apellido: " + e.getMessage());
        }
        return lista;
    }

    // F. Búsqueda por curso
    public List<Alumno> buscarPorCurso(String curso) {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumnos WHERE curso = ? ORDER BY id";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, curso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por curso: " + e.getMessage());
        }
        return lista;
    }

    // G. COUNT(*)
    public int cantidadAlumnos() {
        String sql = "SELECT COUNT(*) AS cantidad FROM alumnos";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            System.out.println("Error al contar alumnos: " + e.getMessage());
        }
        return 0;
    }

    // H. AVG(edad). Si no hay registros, AVG devuelve NULL y se retorna 0.
    public double promedioEdad() {
        String sql = "SELECT AVG(edad) AS promedio FROM alumnos";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("promedio"); // NULL -> 0.0
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular el promedio: " + e.getMessage());
        }
        return 0;
    }

    // Auxiliar (Entrega 4): comprueba si un email ya existe en otro alumno.
    // idExcluir = id del alumno que se está modificando (0 si es un alta).
    public boolean existeEmail(String email, int idExcluir) {
        String sql = "SELECT COUNT(*) AS cantidad FROM alumnos WHERE email = ? AND id <> ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad") > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el email: " + e.getMessage());
        }
        return false;
    }

    // Auxiliar: buscar un alumno por ID (usado en modificar y eliminar)
    public Alumno buscarPorId(int id) {
        String sql = "SELECT * FROM alumnos WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por ID: " + e.getMessage());
        }
        return null;
    }
}
