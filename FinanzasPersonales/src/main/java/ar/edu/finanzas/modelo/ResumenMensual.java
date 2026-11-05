package ar.edu.finanzas.modelo;

import java.math.BigDecimal;

public class ResumenMensual {

    private BigDecimal totalIngresos;
    private BigDecimal totalGastos;
    private BigDecimal resultado;

    public ResumenMensual() {
        this(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public ResumenMensual(BigDecimal totalIngresos, BigDecimal totalGastos, BigDecimal resultado) {
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.resultado = resultado;
    }

    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }

    public BigDecimal getTotalGastos() { return totalGastos; }
    public void setTotalGastos(BigDecimal totalGastos) { this.totalGastos = totalGastos; }

    public BigDecimal getResultado() { return resultado; }
    public void setResultado(BigDecimal resultado) { this.resultado = resultado; }
}
