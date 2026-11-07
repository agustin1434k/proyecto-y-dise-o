package ar.edu.finanzas.vista;

import ar.edu.finanzas.dao.CatalogoDAO;
import ar.edu.finanzas.dao.MovimientoDAO;
import ar.edu.finanzas.dao.PresupuestoDAO;
import ar.edu.finanzas.modelo.ComparacionPresupuesto;
import ar.edu.finanzas.modelo.GastoCategoria;
import ar.edu.finanzas.modelo.Movimiento;
import ar.edu.finanzas.modelo.MovimientoDetalle;
import ar.edu.finanzas.modelo.ResumenMensual;
import ar.edu.finanzas.servicio.FinanzasServicio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal. Solo contiene componentes Swing y presentación:
 * los datos se piden a los DAO y los cálculos al servicio.
 */
public class FinanzasFrame extends JFrame {

    private static final Color PRIMARIO = new Color(0x1F3A5F);
    private static final Color ACENTO = new Color(0x2A9D8F);
    private static final Color FONDO = new Color(0xF4F6F8);
    private static final Color BORDE = new Color(0xD9DEE3);
    private static final Color GRIS = new Color(0x5F6B7A);
    private static final Color VERDE = new Color(0x2E7D32);
    private static final Color ROJO = new Color(0xC62828);
    private static final Color AMBAR = new Color(0xD98200);
    private static final String[] MESES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final PresupuestoDAO presupuestoDAO = new PresupuestoDAO();
    private final CatalogoDAO catalogoDAO = new CatalogoDAO();
    private final FinanzasServicio servicio = new FinanzasServicio();
    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));

    // Catálogos
    private List<OpcionCombo> categoriasTodas = new ArrayList<>();

    // Pestaña Movimientos
    private JTextField txtFecha;
    private JTextField txtImporte;
    private JTextField txtMedioPago;
    private JTextField txtDescripcion;
    private JComboBox<String> cmbTipo;
    private JComboBox<OpcionCombo> cmbCuenta;
    private JComboBox<OpcionCombo> cmbCategoria;
    private JCheckBox chkRecurrente;
    private JLabel lblEdicion;
    private JComboBox<String> fltTipo;
    private JTextField fltDesde;
    private JTextField fltHasta;
    private JTextField fltTexto;
    private JLabel lblContador;
    private DefaultTableModel modeloMov;
    private JTable tblMov;
    private final List<MovimientoDetalle> listaMov = new ArrayList<>();
    private int idSeleccionado = 0;

    // Pestaña Resumen
    private JSpinner resAnio;
    private JComboBox<String> resMes;
    private JLabel valIngresos;
    private JLabel valGastos;
    private JLabel valResultado;
    private JLabel valTasa;
    private JLabel lblNotaResumen;
    private DefaultTableModel modeloGastosCat;

    // Pestaña Presupuestos
    private JSpinner preAnio;
    private JComboBox<String> preMes;
    private JComboBox<OpcionCombo> preUsuario;
    private JComboBox<OpcionCombo> preCategoria;
    private JTextField preMonto;
    private JTextField preObs;
    private JLabel lblNotaPre;
    private DefaultTableModel modeloPre;

    public FinanzasFrame() {
        super("FINANZAS PERSONALES INTELIGENTES - Trabajo Integrador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1180, 780);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());

        add(crearEncabezado(), BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pestanas.addTab("Movimientos", crearPestanaMovimientos());
        pestanas.addTab("Resumen mensual", crearPestanaResumen());
        pestanas.addTab("Presupuestos", crearPestanaPresupuestos());
        add(pestanas, BorderLayout.CENTER);

        cargarCatalogos();
        fijarPeriodoInicial();
        limpiarFormulario();
        recargarTodo();
    }

    // ===================================================================
    //  ESTRUCTURA GENERAL
    // ===================================================================

    private JPanel crearEncabezado() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PRIMARIO);
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel("FINANZAS PERSONALES INTELIGENTES");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel sub = new JLabel("Trabajo Integrador  |  Java Swing + JDBC + MySQL/MariaDB");
        sub.setForeground(new Color(0xBFD0E3));
        p.add(titulo, BorderLayout.CENTER);
        p.add(sub, BorderLayout.SOUTH);
        return p;
    }

    private void cargarCatalogos() {
        try {
            DefaultComboBoxModel<OpcionCombo> mc = new DefaultComboBoxModel<>();
            for (OpcionCombo o : catalogoDAO.listarCuentas()) {
                mc.addElement(o);
            }
            cmbCuenta.setModel(mc);

            categoriasTodas = catalogoDAO.listarCategorias();
            filtrarCategoriasPorTipo();

            DefaultComboBoxModel<OpcionCombo> mp = new DefaultComboBoxModel<>();
            for (OpcionCombo o : categoriasTodas) {
                if (!"INGRESO".equals(o.getExtra())) {
                    mp.addElement(o);
                }
            }
            preCategoria.setModel(mp);

            DefaultComboBoxModel<OpcionCombo> mu = new DefaultComboBoxModel<>();
            for (OpcionCombo o : catalogoDAO.listarUsuarios()) {
                mu.addElement(o);
            }
            preUsuario.setModel(mu);
        } catch (SQLException | RuntimeException e) {
            manejar(e);
        }
    }

    /** Período inicial = mes del movimiento más reciente (o el mes actual si no hay datos). */
    private void fijarPeriodoInicial() {
        LocalDate ref = LocalDate.now();
        try {
            List<Movimiento> todos = movimientoDAO.listarTodos();
            if (!todos.isEmpty()) {
                ref = todos.get(0).getFecha();
            }
        } catch (SQLException e) {
            // se usa el mes actual
        }
        resAnio.setValue(ref.getYear());
        resMes.setSelectedIndex(ref.getMonthValue() - 1);
        preAnio.setValue(ref.getYear());
        preMes.setSelectedIndex(ref.getMonthValue() - 1);
    }

    private void recargarTodo() {
        recargarMovimientos();
        actualizarResumen();
        actualizarPresupuestos();
    }

    // ===================================================================
    //  PESTAÑA 1: MOVIMIENTOS
    // ===================================================================

    private JPanel crearPestanaMovimientos() {
        JPanel raiz = new JPanel(new BorderLayout(0, 10));
        raiz.setBackground(FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        raiz.add(crearFormulario(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setOpaque(false);
        centro.add(crearFiltros(), BorderLayout.NORTH);

        modeloMov = crearModelo("ID", "Fecha", "Tipo", "Cuenta", "Categoría", "Importe",
                "Medio de pago", "Descripción", "Recurrente");
        tblMov = new JTable(modeloMov);
        estilizarTabla(tblMov);
        tblMov.getColumnModel().getColumn(0).setMaxWidth(60);
        tblMov.getColumnModel().getColumn(1).setPreferredWidth(90);
        tblMov.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblMov.getColumnModel().getColumn(7).setPreferredWidth(240);
        tblMov.getColumnModel().getColumn(8).setMaxWidth(90);
        Map<String, Color> colores = new HashMap<>();
        colores.put("INGRESO", VERDE);
        colores.put("GASTO", ROJO);
        tblMov.getColumnModel().getColumn(2).setCellRenderer(new RenderizadorColor(colores, true));
        tblMov.getColumnModel().getColumn(5).setCellRenderer(derecha());
        tblMov.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tblMov.getSelectedRow();
                if (fila >= 0 && fila < listaMov.size()) {
                    cargarFormulario(listaMov.get(fila));
                }
            }
        });
        centro.add(new JScrollPane(tblMov), BorderLayout.CENTER);

        lblContador = new JLabel(" ");
        lblContador.setForeground(GRIS);
        centro.add(lblContador, BorderLayout.SOUTH);

        raiz.add(centro, BorderLayout.CENTER);
        return raiz;
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),
                        "Datos del movimiento"),
                BorderFactory.createEmptyBorder(6, 10, 10, 10)));

        txtFecha = new JTextField(10);
        cmbTipo = new JComboBox<>(new String[]{"INGRESO", "GASTO"});
        cmbTipo.addActionListener(e -> filtrarCategoriasPorTipo());
        cmbCuenta = new JComboBox<>();
        cmbCategoria = new JComboBox<>();
        txtImporte = new JTextField(10);
        txtMedioPago = new JTextField(12);
        txtDescripcion = new JTextField(24);
        chkRecurrente = new JCheckBox("Movimiento recurrente");
        chkRecurrente.setOpaque(false);

        agregar(p, "Fecha (AAAA-MM-DD)", txtFecha, 0, 0, 1);
        agregar(p, "Tipo", cmbTipo, 1, 0, 1);
        agregar(p, "Cuenta", cmbCuenta, 2, 0, 1);
        agregar(p, "Categoría", cmbCategoria, 3, 0, 1);
        agregar(p, "Importe", txtImporte, 0, 1, 1);
        agregar(p, "Medio de pago", txtMedioPago, 1, 1, 1);
        agregar(p, "Descripción", txtDescripcion, 2, 1, 2);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(8, 4, 0, 4);
        p.add(chkRecurrente, g);

        lblEdicion = new JLabel("Nuevo movimiento");
        lblEdicion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEdicion.setForeground(ACENTO);
        g = new GridBagConstraints();
        g.gridx = 2;
        g.gridy = 4;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(8, 4, 0, 4);
        p.add(lblEdicion, g);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        botones.add(crearBoton("Nuevo", GRIS, e -> nuevo()));
        botones.add(crearBoton("Guardar", ACENTO, e -> guardar()));
        botones.add(crearBoton("Modificar", PRIMARIO, e -> modificar()));
        botones.add(crearBoton("Eliminar", ROJO, e -> eliminar()));
        botones.add(crearBoton("Limpiar", GRIS, e -> limpiarFormulario()));
        g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 5;
        g.gridwidth = 4;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(10, 0, 0, 0);
        p.add(botones, g);
        return p;
    }

    private JPanel crearFiltros() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),
                "Filtros (se combinan entre sí; vacío = sin filtrar)"));

        fltTipo = new JComboBox<>(new String[]{"TODOS", "INGRESO", "GASTO"});
        fltDesde = new JTextField(9);
        fltHasta = new JTextField(9);
        fltTexto = new JTextField(14);
        ActionListener aplicar = e -> recargarMovimientos();
        fltDesde.addActionListener(aplicar);
        fltHasta.addActionListener(aplicar);
        fltTexto.addActionListener(aplicar);

        p.add(new JLabel("Tipo:"));
        p.add(fltTipo);
        p.add(new JLabel("Desde:"));
        p.add(fltDesde);
        p.add(new JLabel("Hasta:"));
        p.add(fltHasta);
        p.add(new JLabel("Descripción:"));
        p.add(fltTexto);
        p.add(crearBoton("Aplicar filtros", PRIMARIO, aplicar));
        p.add(crearBoton("Quitar filtros", GRIS, e -> {
            fltTipo.setSelectedIndex(0);
            fltDesde.setText("");
            fltHasta.setText("");
            fltTexto.setText("");
            recargarMovimientos();
        }));
        return p;
    }

    // ---- acciones de movimientos

    private void nuevo() {
        limpiarFormulario();
        txtImporte.requestFocusInWindow();
    }

    private void limpiarFormulario() {
        idSeleccionado = 0;
        tblMov.clearSelection();
        txtFecha.setText(LocalDate.now().toString());
        cmbTipo.setSelectedIndex(0);
        if (cmbCuenta.getItemCount() > 0) {
            cmbCuenta.setSelectedIndex(0);
        }
        filtrarCategoriasPorTipo();
        txtImporte.setText("");
        txtMedioPago.setText("");
        txtDescripcion.setText("");
        chkRecurrente.setSelected(false);
        lblEdicion.setText("Nuevo movimiento");
    }

    private void cargarFormulario(MovimientoDetalle m) {
        idSeleccionado = m.getIdMovimiento();
        txtFecha.setText(m.getFecha().toString());
        cmbTipo.setSelectedItem(m.getTipo());
        seleccionarPorId(cmbCuenta, m.getIdCuenta());
        seleccionarPorId(cmbCategoria, m.getIdCategoria());
        txtImporte.setText(m.getImporte().toPlainString());
        txtMedioPago.setText(m.getMedioPago());
        txtDescripcion.setText(m.getDescripcion());
        chkRecurrente.setSelected(m.isRecurrente());
        lblEdicion.setText("Editando movimiento #" + idSeleccionado);
    }

    private void guardar() {
        try {
            Movimiento m = leerFormulario();
            if (idSeleccionado != 0) {
                int r = JOptionPane.showConfirmDialog(this,
                        "Hay un movimiento cargado para edición (#" + idSeleccionado + ").\n"
                        + "Guardar creará un movimiento NUEVO con estos datos.\n¿Continuar?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (r != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            movimientoDAO.insertar(m);
            mostrarInfo("Movimiento guardado correctamente.");
            limpiarFormulario();
            recargarTodo();
        } catch (SQLException | RuntimeException e) {
            manejar(e);
        }
    }

    private void modificar() {
        if (idSeleccionado == 0) {
            mostrarAviso("Primero seleccioná un movimiento en la tabla.");
            return;
        }
        try {
            Movimiento m = leerFormulario();
            m.setIdMovimiento(idSeleccionado);
            int filas = movimientoDAO.actualizar(m);
            if (filas == 0) {
                mostrarAviso("El movimiento #" + idSeleccionado + " ya no existe.");
            } else {
                mostrarInfo("Movimiento modificado correctamente.");
            }
            limpiarFormulario();
            recargarTodo();
        } catch (SQLException | RuntimeException e) {
            manejar(e);
        }
    }

    private void eliminar() {
        if (idSeleccionado == 0) {
            mostrarAviso("Primero seleccioná un movimiento en la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Seguro que querés eliminar el movimiento #" + idSeleccionado + "?\n"
                + "Esta acción no se puede deshacer.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            int filas = movimientoDAO.eliminar(idSeleccionado);
            if (filas == 0) {
                mostrarAviso("El movimiento #" + idSeleccionado + " ya no existe.");
            } else {
                mostrarInfo("Movimiento eliminado correctamente.");
            }
            limpiarFormulario();
            recargarTodo();
        } catch (SQLException | RuntimeException e) {
            manejar(e);
        }
    }

    /** Convierte lo escrito en el formulario en un Movimiento, validando cada campo. */
    private Movimiento leerFormulario() {
        LocalDate fecha = parsearFecha(txtFecha.getText(), "La fecha");
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria (formato AAAA-MM-DD).");
        }
        OpcionCombo cuenta = (OpcionCombo) cmbCuenta.getSelectedItem();
        if (cuenta == null) {
            throw new IllegalArgumentException("Seleccioná una cuenta.");
        }
        OpcionCombo categoria = (OpcionCombo) cmbCategoria.getSelectedItem();
        if (categoria == null) {
            throw new IllegalArgumentException("Seleccioná una categoría compatible con el tipo elegido.");
        }
        Movimiento m = new Movimiento();
        m.setFecha(fecha);
        m.setTipo((String) cmbTipo.getSelectedItem());
        m.setIdCuenta(cuenta.getId());
        m.setIdCategoria(categoria.getId());
        m.setImporte(parsearImporte(txtImporte.getText()));
        m.setMedioPago(txtMedioPago.getText());
        m.setDescripcion(txtDescripcion.getText());
        m.setRecurrente(chkRecurrente.isSelected());
        return m;
    }

    /** Muestra solo las categorías cuyo tipo_permitido sirve para el tipo elegido. */
    private void filtrarCategoriasPorTipo() {
        if (cmbCategoria == null || cmbTipo == null) {
            return;
        }
        Object previa = cmbCategoria.getSelectedItem();
        String tipo = (String) cmbTipo.getSelectedItem();
        DefaultComboBoxModel<OpcionCombo> modelo = new DefaultComboBoxModel<>();
        for (OpcionCombo o : categoriasTodas) {
            if ("AMBOS".equals(o.getExtra()) || (tipo != null && tipo.equals(o.getExtra()))) {
                modelo.addElement(o);
            }
        }
        cmbCategoria.setModel(modelo);
        if (previa != null && modelo.getIndexOf(previa) >= 0) {
            cmbCategoria.setSelectedItem(previa);
        }
    }

    private void recargarMovimientos() {
        try {
            String tipo = (String) fltTipo.getSelectedItem();
            if ("TODOS".equals(tipo)) {
                tipo = null;
            }
            LocalDate desde = parsearFecha(fltDesde.getText(), "La fecha desde");
            LocalDate hasta = parsearFecha(fltHasta.getText(), "La fecha hasta");
            List<MovimientoDetalle> datos = movimientoDAO.listarFiltrado(tipo, desde, hasta,
                    fltTexto.getText());

            listaMov.clear();
            listaMov.addAll(datos);
            modeloMov.setRowCount(0);
            for (MovimientoDetalle m : datos) {
                modeloMov.addRow(new Object[]{m.getIdMovimiento(), m.getFecha(), m.getTipo(),
                    m.getCuentaNombre(), m.getCategoriaNombre(), moneda.format(m.getImporte()),
                    m.getMedioPago(), m.getDescripcion(), m.isRecurrente() ? "Sí" : "No"});
            }
            lblContador.setText(datos.size() + " movimiento(s) mostrado(s)");
        } catch (SQLException | RuntimeException e) {
            manejar(e);
        }
    }

    // ===================================================================
    //  PESTAÑA 2: RESUMEN MENSUAL
    // ===================================================================

    private JPanel crearPestanaResumen() {
        JPanel raiz = new JPanel(new BorderLayout(0, 12));
        raiz.setBackground(FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel periodo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        periodo.setBackground(Color.WHITE);
        periodo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),
                "Período"));
        resAnio = crearSpinnerAnio();
        resMes = new JComboBox<>(MESES);
        periodo.add(new JLabel("Mes:"));
        periodo.add(resMes);
        periodo.add(new JLabel("Año:"));
        periodo.add(resAnio);
        periodo.add(crearBoton("Actualizar", ACENTO, e -> actualizarResumen()));

        valIngresos = new JLabel("-");
        valGastos = new JLabel("-");
        valResultado = new JLabel("-");
        valTasa = new JLabel("-");
        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 12, 0));
        tarjetas.setOpaque(false);
        tarjetas.add(tarjeta("Total de ingresos", valIngresos));
        tarjetas.add(tarjeta("Total de gastos", valGastos));
        tarjetas.add(tarjeta("Resultado del mes", valResultado));
        tarjetas.add(tarjeta("Tasa de ahorro", valTasa));

        lblNotaResumen = new JLabel(" ");
        lblNotaResumen.setForeground(GRIS);

        JPanel norte = new JPanel(new BorderLayout(0, 10));
        norte.setOpaque(false);
        norte.add(periodo, BorderLayout.NORTH);
        norte.add(tarjetas, BorderLayout.CENTER);
        norte.add(lblNotaResumen, BorderLayout.SOUTH);
        raiz.add(norte, BorderLayout.NORTH);

        modeloGastosCat = crearModelo("Categoría", "Total gastado", "Cantidad de movimientos",
                "% del total de gastos");
        JTable t = new JTable(modeloGastosCat);
        estilizarTabla(t);
        t.getColumnModel().getColumn(1).setCellRenderer(derecha());
        t.getColumnModel().getColumn(2).setCellRenderer(derecha());
        t.getColumnModel().getColumn(3).setCellRenderer(derecha());
        JPanel abajo = new JPanel(new BorderLayout(0, 6));
        abajo.setOpaque(false);
        JLabel titulo = new JLabel("Gastos por categoría en el período");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(PRIMARIO);
        abajo.add(titulo, BorderLayout.NORTH);
        abajo.add(new JScrollPane(t), BorderLayout.CENTER);
        raiz.add(abajo, BorderLayout.CENTER);
        return raiz;
    }

    private void actualizarResumen() {
        try {
            LocalDate desde = LocalDate.of((Integer) resAnio.getValue(), resMes.getSelectedIndex() + 1, 1);
            LocalDate hasta = desde.plusMonths(1);

            ResumenMensual r = movimientoDAO.resumenMensual(desde, hasta);
            BigDecimal tasa = servicio.calcularTasaAhorro(r);

            valIngresos.setText(moneda.format(r.getTotalIngresos()));
            valGastos.setText(moneda.format(r.getTotalGastos()));
            valResultado.setText(moneda.format(r.getResultado()));
            valResultado.setForeground(r.getResultado().signum() >= 0 ? VERDE : ROJO);
            valTasa.setText(tasa.toPlainString().replace('.', ',') + " %");
            valTasa.setForeground(tasa.signum() >= 0 ? VERDE : ROJO);

            if (r.getTotalIngresos().signum() == 0 && r.getTotalGastos().signum() == 0) {
                lblNotaResumen.setText("No hay movimientos en este período.");
            } else if (r.getTotalIngresos().signum() == 0) {
                lblNotaResumen.setText("No hubo ingresos: la tasa de ahorro se muestra como 0,00 %.");
            } else {
                lblNotaResumen.setText("Tasa de ahorro = (ingresos - gastos) / ingresos x 100");
            }

            modeloGastosCat.setRowCount(0);
            for (GastoCategoria g : movimientoDAO.gastosPorCategoria(desde, hasta)) {
                modeloGastosCat.addRow(new Object[]{g.getCategoria(), moneda.format(g.getTotalGastado()),
                    g.getCantidad(), g.getPorcentaje().toPlainString().replace('.', ',') + " %"});
            }
        } catch (SQLException | RuntimeException e) {
            manejar(e);
        }
    }

    // ===================================================================
    //  PESTAÑA 3: PRESUPUESTOS
    // ===================================================================

    private JPanel crearPestanaPresupuestos() {
        JPanel raiz = new JPanel(new BorderLayout(0, 10));
        raiz.setBackground(FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),
                        "Presupuesto mensual por categoría"),
                BorderFactory.createEmptyBorder(6, 10, 10, 10)));

        preAnio = crearSpinnerAnio();
        preMes = new JComboBox<>(MESES);
        preUsuario = new JComboBox<>();
        preCategoria = new JComboBox<>();
        preMonto = new JTextField(10);
        preObs = new JTextField(24);

        agregar(form, "Usuario", preUsuario, 0, 0, 1);
        agregar(form, "Mes", preMes, 1, 0, 1);
        agregar(form, "Año", preAnio, 2, 0, 1);
        agregar(form, "Categoría", preCategoria, 3, 0, 1);
        agregar(form, "Monto planificado", preMonto, 0, 1, 1);
        agregar(form, "Observaciones", preObs, 1, 1, 2);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        botones.add(crearBoton("Guardar presupuesto", ACENTO, e -> guardarPresupuesto()));
        botones.add(crearBoton("Consultar / comparar", PRIMARIO, e -> actualizarPresupuestos()));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 4;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(10, 0, 0, 0);
        form.add(botones, g);
        raiz.add(form, BorderLayout.NORTH);

        modeloPre = crearModelo("Categoría", "Monto planificado", "Gasto real", "Disponible", "Estado");
        JTable t = new JTable(modeloPre);
        estilizarTabla(t);
        t.getColumnModel().getColumn(1).setCellRenderer(derecha());
        t.getColumnModel().getColumn(2).setCellRenderer(derecha());
        t.getColumnModel().getColumn(3).setCellRenderer(derecha());
        Map<String, Color> colores = new HashMap<>();
        colores.put("EXCEDIDO", ROJO);
        colores.put("AL LIMITE", AMBAR);
        colores.put("DENTRO DEL PRESUPUESTO", VERDE);
        t.getColumnModel().getColumn(4).setCellRenderer(new RenderizadorColor(colores, true));
        raiz.add(new JScrollPane(t), BorderLayout.CENTER);

        lblNotaPre = new JLabel(" ");
        lblNotaPre.setForeground(GRIS);
        raiz.add(lblNotaPre, BorderLayout.SOUTH);
        return raiz;
    }

    private void guardarPresupuesto() {
        try {
            OpcionCombo usuario = (OpcionCombo) preUsuario.getSelectedItem();
            OpcionCombo categoria = (OpcionCombo) preCategoria.getSelectedItem();
            if (usuario == null) {
                throw new IllegalArgumentException("Seleccioná un usuario.");
            }
            if (categoria == null) {
                throw new IllegalArgumentException("Seleccioná una categoría.");
            }
            BigDecimal monto = parsearImporte(preMonto.getText());
            presupuestoDAO.guardar(usuario.getId(), categoria.getId(), (Integer) preAnio.getValue(),
                    preMes.getSelectedIndex() + 1, monto, preObs.getText());
            mostrarInfo("Presupuesto guardado correctamente.\n"
                    + "(Si ya existía uno para esa categoría y mes, se actualizó el monto.)");
            preMonto.setText("");
            preObs.setText("");
            actualizarPresupuestos();
        } catch (SQLException | RuntimeException e) {
            manejar(e);
        }
    }

    private void actualizarPresupuestos() {
        try {
            OpcionCombo usuario = (OpcionCombo) preUsuario.getSelectedItem();
            Integer idUsuario = usuario == null ? null : usuario.getId();
            List<ComparacionPresupuesto> lista = presupuestoDAO.compararConGastoReal(
                    (Integer) preAnio.getValue(), preMes.getSelectedIndex() + 1, idUsuario);
            modeloPre.setRowCount(0);
            for (ComparacionPresupuesto c : lista) {
                modeloPre.addRow(new Object[]{c.getCategoria(), moneda.format(c.getMontoPlanificado()),
                    moneda.format(c.getGastoReal()), moneda.format(c.getDisponible()), c.getEstado()});
            }
            lblNotaPre.setText(lista.isEmpty()
                    ? "No hay presupuestos cargados para el usuario y período elegidos."
                    : lista.size() + " presupuesto(s). Disponible = planificado - gasto real (solo movimientos GASTO).");
        } catch (SQLException | RuntimeException e) {
            manejar(e);
        }
    }

    // ===================================================================
    //  UTILIDADES DE INTERFAZ
    // ===================================================================

    private void agregar(JPanel p, String etiqueta, java.awt.Component campo, int columna, int fila, int ancho) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = columna;
        g.gridy = fila * 2;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(4, 4, 0, 8);
        JLabel l = new JLabel(etiqueta);
        l.setForeground(GRIS);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(l, g);

        g = new GridBagConstraints();
        g.gridx = columna;
        g.gridy = fila * 2 + 1;
        g.gridwidth = ancho;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.insets = new Insets(0, 4, 4, 8);
        p.add(campo, g);
    }

    private JButton crearBoton(String texto, Color color, ActionListener accion) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        b.addActionListener(accion);
        return b;
    }

    private JPanel tarjeta(String titulo, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        JLabel t = new JLabel(titulo);
        t.setForeground(GRIS);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valor.setForeground(PRIMARIO);
        p.add(t, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        return p;
    }

    private JSpinner crearSpinnerAnio() {
        JSpinner s = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
        s.setEditor(new JSpinner.NumberEditor(s, "#"));
        return s;
    }

    private DefaultTableModel crearModelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void estilizarTabla(JTable t) {
        t.setRowHeight(26);
        t.setGridColor(BORDE);
        t.setSelectionBackground(new Color(0xCFE8E5));
        t.setSelectionForeground(Color.BLACK);
        t.setFillsViewportHeight(true);
        t.getTableHeader().setBackground(PRIMARIO);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setReorderingAllowed(false);
    }

    private DefaultTableCellRenderer derecha() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        return r;
    }

    /** Pinta el texto de una celda según su valor (por ejemplo INGRESO verde, GASTO rojo). */
    private static class RenderizadorColor extends DefaultTableCellRenderer {

        private final Map<String, Color> colores;
        private final boolean negrita;

        RenderizadorColor(Map<String, Color> colores, boolean negrita) {
            this.colores = colores;
            this.negrita = negrita;
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                boolean foco, int fila, int columna) {
            Component c = super.getTableCellRendererComponent(tabla, valor, seleccionada, foco, fila, columna);
            Color color = valor == null ? null : colores.get(valor.toString());
            if (!seleccionada && color != null) {
                c.setForeground(color);
            }
            if (negrita) {
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            }
            return c;
        }
    }

    private void seleccionarPorId(JComboBox<OpcionCombo> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    // ===================================================================
    //  CONVERSIÓN DE DATOS Y MENSAJES
    // ===================================================================

    /** Devuelve null si el texto está vacío; lanza IllegalArgumentException si el formato es malo. */
    private LocalDate parsearFecha(String texto, String nombreCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(nombreCampo
                    + " no es válida. Usá el formato AAAA-MM-DD (por ejemplo 2026-03-15).");
        }
    }

    /** Acepta 1500, 1500.50 o 1.500,50. Máximo 2 decimales. */
    private BigDecimal parsearImporte(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El importe es obligatorio.");
        }
        String t = texto.trim().replace("$", "").replace(" ", "");
        if (t.contains(",")) {
            t = t.replace(".", "").replace(",", ".");
        }
        BigDecimal valor;
        try {
            valor = new BigDecimal(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El importe no es un número válido: " + texto.trim());
        }
        if (valor.scale() > 2) {
            throw new IllegalArgumentException("El importe admite como máximo 2 decimales.");
        }
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que cero.");
        }
        return valor;
    }

    private void manejar(Exception e) {
        if (e instanceof IllegalArgumentException) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Dato incorrecto", JOptionPane.WARNING_MESSAGE);
        } else if (e instanceof SQLException) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            String ayuda = "";
            if (msg.contains("presupuestos") && msg.contains("doesn't exist")) {
                ayuda = "\n\nLa tabla presupuestos no existe: ejecutá sql/presupuestos.sql en phpMyAdmin.";
            }
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + msg + ayuda,
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarInfo(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAviso(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
