package ar.edu.finanzas;

import ar.edu.finanzas.dao.MovimientoDAO;
import ar.edu.finanzas.modelo.Movimiento;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class PruebaInsercion {

    // AJUSTAR con IDs reales de tu base (verificar en phpMyAdmin)
    private static final int ID_CUENTA = 1;
    private static final int ID_CATEGORIA_INGRESO = 1;
    private static final int ID_CATEGORIA_GASTO = 2;

    public static void main(String[] args) {
        MovimientoDAO dao = new MovimientoDAO();

        try {
            Movimiento ingreso = new Movimiento(0, ID_CUENTA, ID_CATEGORIA_INGRESO, "INGRESO",
                    LocalDate.now(), new BigDecimal("15000.00"),
                    "Ingreso de prueba desde Java", "TRANSFERENCIA", false);
            System.out.println("Ingreso insertado. Filas afectadas: " + dao.insertar(ingreso));

            Movimiento gasto = new Movimiento(0, ID_CUENTA, ID_CATEGORIA_GASTO, "GASTO",
                    LocalDate.now(), new BigDecimal("2500.50"),
                    "Gasto de prueba desde Java", "EFECTIVO", false);
            System.out.println("Gasto insertado. Filas afectadas: " + dao.insertar(gasto));

        } catch (IllegalArgumentException e) {
            System.out.println("Validación fallida: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }
}
