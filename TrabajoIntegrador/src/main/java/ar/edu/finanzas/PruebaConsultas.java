package ar.edu.finanzas;

import ar.edu.finanzas.dao.MovimientoDAO;
import ar.edu.finanzas.dao.PresupuestoDAO;
import ar.edu.finanzas.modelo.ComparacionPresupuesto;
import ar.edu.finanzas.modelo.GastoCategoria;
import ar.edu.finanzas.modelo.Movimiento;
import ar.edu.finanzas.modelo.ResumenMensual;
import ar.edu.finanzas.servicio.FinanzasServicio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** Solo lectura: no modifica datos. Cambiar ANIO y MES según los datos cargados. */
public class PruebaConsultas {

    private static final int ANIO = 2026;
    private static final int MES = 3;

    public static void main(String[] args) {
        LocalDate desde = LocalDate.of(ANIO, MES, 1);
        LocalDate hasta = desde.plusMonths(1);
        MovimientoDAO dao = new MovimientoDAO();
        FinanzasServicio servicio = new FinanzasServicio();

        try {
            System.out.println("=== Gastos del mes (filtro tipo + fechas) ===");
            List<Movimiento> gastos = dao.listarPorTipoYFechas("GASTO", desde, hasta);
            for (Movimiento m : gastos) {
                System.out.println(m.getIdMovimiento() + " | " + m.getFecha() + " | "
                        + m.getImporte() + " | " + m.getDescripcion());
            }
            System.out.println("Cantidad: " + gastos.size());

            System.out.println("\n=== Buscar por descripción ('a') ===");
            System.out.println("Coincidencias: " + dao.buscarPorDescripcion("a").size());

            System.out.println("\n=== Resumen mensual " + MES + "/" + ANIO + " ===");
            ResumenMensual r = dao.resumenMensual(desde, hasta);
            System.out.println("Ingresos: " + r.getTotalIngresos());
            System.out.println("Gastos:   " + r.getTotalGastos());
            System.out.println("Resultado: " + r.getResultado());
            System.out.println("Tasa de ahorro: " + servicio.calcularTasaAhorro(r) + " %");

            System.out.println("\n=== Gastos por categoría ===");
            for (GastoCategoria g : dao.gastosPorCategoria(desde, hasta)) {
                System.out.println(g.getCategoria() + " | total " + g.getTotalGastado()
                        + " | movs " + g.getCantidad() + " | " + g.getPorcentaje() + " %");
            }

            System.out.println("\n=== Presupuesto vs gasto real ===");
            List<ComparacionPresupuesto> comp = new PresupuestoDAO().compararConGastoReal(ANIO, MES);
            if (comp.isEmpty()) {
                System.out.println("No hay presupuestos cargados para ese mes.");
            }
            for (ComparacionPresupuesto c : comp) {
                System.out.println("Usuario " + c.getIdUsuario() + " | " + c.getCategoria()
                        + " | plan " + c.getMontoPlanificado() + " | real " + c.getGastoReal()
                        + " | disp " + c.getDisponible() + " | " + c.getEstado());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Validación: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }
}
