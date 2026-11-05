package ar.edu.finanzas.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Movimiento {

    private int idMovimiento;
    private int idCuenta;
    private int idCategoria;
    private String tipo;
    private LocalDate fecha;
    private BigDecimal importe;
    private String descripcion;
    private String medioPago;
    private boolean recurrente;

    public Movimiento() {
    }

    public Movimiento(int idMovimiento, int idCuenta, int idCategoria, String tipo,
            LocalDate fecha, BigDecimal importe, String descripcion,
            String medioPago, boolean recurrente) {
        this.idMovimiento = idMovimiento;
        this.idCuenta = idCuenta;
        this.idCategoria = idCategoria;
        this.tipo = tipo;
        this.fecha = fecha;
        this.importe = importe;
        this.descripcion = descripcion;
        this.medioPago = medioPago;
        this.recurrente = recurrente;
    }

    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public int getIdCuenta() { return idCuenta; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public boolean isRecurrente() { return recurrente; }
    public void setRecurrente(boolean recurrente) { this.recurrente = recurrente; }
}
