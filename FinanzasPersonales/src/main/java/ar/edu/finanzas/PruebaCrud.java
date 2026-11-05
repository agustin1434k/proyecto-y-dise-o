package ar.edu.finanzas;

import ar.edu.finanzas.dao.MovimientoDAO;
import ar.edu.finanzas.modelo.Movimiento;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Crea un movimiento ficticio propio, lo busca, lo actualiza y lo elimina.
 * Solo toca el registro que ella misma crea (identificado por su descripción).
 * AJUSTAR los IDs con valores reales de tu base.
 */
public class PruebaCrud {

    private static final int ID_CUENTA = 1;
    private static final int ID_CATEGORIA_GASTO = 2;
    private static final String MARCA = "PRUEBA CRUD E3";

    public static void main(String[] args) {
        MovimientoDAO dao = new MovimientoDAO();

        try {
            if (!dao.buscarPorDescripcion(MARCA).isEmpty()) {
                System.out.println("Ya existe un registro de prueba. Eliminalo en phpMyAdmin o corré de nuevo tras borrarlo.");
                return;
            }

            // CREATE
            dao.insertar(new Movimiento(0, ID_CUENTA, ID_CATEGORIA_GASTO, "GASTO", LocalDate.now(),
                    new BigDecimal("100.00"), MARCA, "EFECTIVO", false));
            System.out.println("1) Insertado.");

            // READ
            List<Movimiento> encontrados = dao.buscarPorDescripcion(MARCA);
            Movimiento m = dao.buscarPorId(encontrados.get(0).getIdMovimiento());
            System.out.println("2) Buscado por ID " + m.getIdMovimiento() + ": " + m.getDescripcion()
                    + " - " + m.getImporte());

            // UPDATE
            m.setImporte(new BigDecimal("250.75"));
            System.out.println("3) Actualizado. Filas afectadas: " + dao.actualizar(m));
            System.out.println("   Nuevo importe: " + dao.buscarPorId(m.getIdMovimiento()).getImporte());

            // DELETE (solo el registro de prueba)
            System.out.println("4) Eliminado. Filas afectadas: " + dao.eliminar(m.getIdMovimiento()));
            System.out.println("   ¿Sigue existiendo? " + (dao.buscarPorId(m.getIdMovimiento()) != null));
        } catch (IllegalArgumentException e) {
            System.out.println("Validación: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }
}
