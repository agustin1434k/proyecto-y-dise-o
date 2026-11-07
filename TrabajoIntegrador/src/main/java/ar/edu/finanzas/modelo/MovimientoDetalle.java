package ar.edu.finanzas.modelo;

/** Movimiento con los nombres de cuenta y categoría (para mostrar en la tabla). */
public class MovimientoDetalle extends Movimiento {

    private String cuentaNombre;
    private String categoriaNombre;

    public String getCuentaNombre() { return cuentaNombre; }
    public void setCuentaNombre(String cuentaNombre) { this.cuentaNombre = cuentaNombre; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
}
