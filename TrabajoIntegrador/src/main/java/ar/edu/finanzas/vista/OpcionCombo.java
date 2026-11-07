package ar.edu.finanzas.vista;

/** Elemento de un JComboBox: muestra un texto y conserva el ID interno. */
public class OpcionCombo {

    private final int id;
    private final String texto;
    private final String extra;

    public OpcionCombo(int id, String texto) {
        this(id, texto, null);
    }

    public OpcionCombo(int id, String texto, String extra) {
        this.id = id;
        this.texto = texto;
        this.extra = extra;
    }

    public int getId() { return id; }
    public String getTexto() { return texto; }
    /** Dato adicional (por ejemplo, tipo_permitido de una categoría). */
    public String getExtra() { return extra; }

    @Override
    public String toString() {
        return texto;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof OpcionCombo && ((OpcionCombo) o).id == id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
