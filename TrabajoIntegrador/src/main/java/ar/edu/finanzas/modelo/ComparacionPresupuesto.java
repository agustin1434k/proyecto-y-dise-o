package ar.edu.finanzas.modelo;

import java.math.BigDecimal;

/** Fila de la comparación presupuesto vs. gasto real. */
public class ComparacionPresupuesto {

    private final int idUsuario;
    private final String categoria;
    private final int anio;
    private final int mes;
    private final BigDecimal montoPlanificado;
    private final BigDecimal gastoReal;
    private final BigDecimal disponible;
    private final String estado;

    public ComparacionPresupuesto(int idUsuario, String categoria, int anio, int mes,
            BigDecimal montoPlanificado, BigDecimal gastoReal, BigDecimal disponible, String estado) {
        this.idUsuario = idUsuario;
        this.categoria = categoria;
        this.anio = anio;
        this.mes = mes;
        this.montoPlanificado = montoPlanificado;
        this.gastoReal = gastoReal;
        this.disponible = disponible;
        this.estado = estado;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getCategoria() { return categoria; }
    public int getAnio() { return anio; }
    public int getMes() { return mes; }
    public BigDecimal getMontoPlanificado() { return montoPlanificado; }
    public BigDecimal getGastoReal() { return gastoReal; }
    public BigDecimal getDisponible() { return disponible; }
    public String getEstado() { return estado; }
}
