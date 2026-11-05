package ar.edu.finanzas.servicio;

import ar.edu.finanzas.modelo.ResumenMensual;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinanzasServicio {

    private static final BigDecimal CIEN = new BigDecimal("100");

    /** Tasa de ahorro = resultado / ingresos * 100. Sin ingresos devuelve 0.00. */
    public BigDecimal calcularTasaAhorro(ResumenMensual resumen) {
        if (resumen == null || resumen.getTotalIngresos() == null || resumen.getResultado() == null) {
            throw new IllegalArgumentException("El resumen mensual no es válido.");
        }
        if (resumen.getTotalIngresos().signum() < 0) {
            throw new IllegalArgumentException("Los ingresos no pueden ser negativos.");
        }
        if (resumen.getTotalIngresos().signum() == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return resumen.getResultado().multiply(CIEN)
                .divide(resumen.getTotalIngresos(), 2, RoundingMode.HALF_UP);
    }

    /** Porcentaje = gastoCategoria / gastosTotales * 100. Sin gastos totales devuelve 0.00. */
    public BigDecimal calcularPorcentajeGasto(BigDecimal gastoCategoria, BigDecimal gastosTotales) {
        if (gastoCategoria == null || gastosTotales == null) {
            throw new IllegalArgumentException("Los importes no pueden ser nulos.");
        }
        if (gastoCategoria.signum() < 0 || gastosTotales.signum() < 0) {
            throw new IllegalArgumentException("Los importes no pueden ser negativos.");
        }
        if (gastoCategoria.compareTo(gastosTotales) > 0) {
            throw new IllegalArgumentException("El gasto de la categoría no puede superar el total.");
        }
        if (gastosTotales.signum() == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return gastoCategoria.multiply(CIEN).divide(gastosTotales, 2, RoundingMode.HALF_UP);
    }
}
