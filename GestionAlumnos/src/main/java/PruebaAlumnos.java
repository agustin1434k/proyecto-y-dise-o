import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PruebaAlumnos {

    public static void main(String[] args) {
        // Ejercicio 2: listar todos
        System.out.println("=== Todos los alumnos ===");
        listarTodos();

        // Ejercicio 3: buscar por curso (cambiá el valor por uno que exista en tu tabla)
        String curso = "6° A";
        System.out.println("\n=== Alumnos del curso " + curso + " ===");
        buscarPorCurso(curso);

        // Ejercicio 4: buscar por apellido (coincidencia parcial)
        String apellido = "Pérez";
        System.out.println("\n=== Alumnos con apellido que contiene \"" + apellido + "\" ===");
        buscarPorApellido(apellido);
    }

    public static void listarTodos() {
        String sql = "SELECT id, nombre, apellido, curso FROM alumnos";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            mostrar(rs);
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    public static void buscarPorCurso(String curso) {
        String sql = "SELECT * FROM alumnos WHERE curso = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, curso);
            try (ResultSet rs = ps.executeQuery()) {
                mostrar(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    public static void buscarPorApellido(String texto) {
        String sql = "SELECT * FROM alumnos WHERE apellido LIKE ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                mostrar(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    private static void mostrar(ResultSet rs) throws SQLException {
        boolean hayRegistros = false;
        while (rs.next()) {
            hayRegistros = true;
            System.out.println(rs.getInt("id") + " - "
                    + rs.getString("nombre") + " "
                    + rs.getString("apellido") + " - "
                    + rs.getString("curso"));
        }
        if (!hayRegistros) {
            System.out.println("No se encontraron alumnos.");
        }
    }
}
