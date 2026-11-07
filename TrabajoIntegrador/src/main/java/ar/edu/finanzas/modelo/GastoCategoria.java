package ar.edu.finanzas.modelo;

import java.math.BigDecimal;

/** Fila del reporte "gastos por categoría". */
public class GastoCategoria {

    private final String categoria;
    private final BigDecimal totalGastado;
    private final int cantidad;
    private final BigDecimal porcentaje;

    public GastoCategoria(String categoria, BigDecimal totalGastado, int cantidad, BigDecimal porcentaje) {
        this.categoria = categoria;
        this.totalGastado = totalGastado;
        this.cantidad = cantidad;
        this.porcentaje = porcentaje;
    }

    public String getCategoria() { return categoria; }
    public BigDecimal getTotalGastado() { return totalGastado; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPorcentaje() { return porcentaje; }
}
