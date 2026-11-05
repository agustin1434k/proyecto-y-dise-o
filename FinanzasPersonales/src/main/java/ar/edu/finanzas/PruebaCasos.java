package ar.edu.finanzas;

import ar.edu.finanzas.dao.MovimientoDAO;
import ar.edu.finanzas.modelo.Movimiento;
import ar.edu.finanzas.modelo.ResumenMensual;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

/** Casos de prueba negativos. Ninguno debe modificar datos reales. */
public class PruebaCasos {

    private static final int ID_CUENTA = 1;
    private static final int ID_CATEGORIA = 2;

    public static void main(String[] args) {
        MovimientoDAO dao = new MovimientoDAO();

        probar("Importe cero (debe fallar)", () -> dao.insertar(nuevo("GASTO", ID_CATEGORIA, "0.00")));
        probar("Tipo incorrecto (debe fallar)", () -> dao.insertar(nuevo("OTRO", ID_CATEGORIA, "10.00")));
        probar("Categoría inexistente (debe fallar)", () -> dao.insertar(nuevo("GASTO", 999999, "10.00")));

        probar("Actualizar ID inexistente (0 filas)", () -> {
            Movimiento m = nuevo("GASTO", ID_CATEGORIA, "10.00");
            m.setIdMovimiento(999999);
            System.out.println("   Filas afectadas: " + dao.actualizar(m));
        });
        probar("Eliminar ID inexistente (0 filas)", () ->
                System.out.println("   Filas afectadas: " + dao.eliminar(999999)));

        probar("Mes sin movimientos (todo en 0)", () -> {
            ResumenMensual r = dao.resumenMensual(LocalDate.of(1999, 1, 1), LocalDate.of(1999, 2, 1));
            System.out.println("   Ingresos " + r.getTotalIngresos() + " | Gastos " + r.getTotalGastos()
                    + " | Resultado " + r.getResultado());
        });
    }

    private static Movimiento nuevo(String tipo, int idCategoria, String importe) {
        return new Movimiento(0, ID_CUENTA, idCategoria, tipo, LocalDate.now(),
                new BigDecimal(importe), "Caso de prueba", "EFECTIVO", false);
    }

    private interface Accion {
        void ejecutar() throws SQLException;
    }

    private static void probar(String nombre, Accion accion) {
        System.out.println("> " + nombre);
        try {
            accion.ejecutar();
            System.out.println("   Resultado: OK (sin error)");
        } catch (IllegalArgumentException e) {
            System.out.println("   Rechazado por validación: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("   Error SQL: " + e.getMessage());
        }
    }
}
