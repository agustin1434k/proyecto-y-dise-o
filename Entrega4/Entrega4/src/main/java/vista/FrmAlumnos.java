package vista;

import dao.AlumnoDAO;
import modelo.Alumno;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class FrmAlumnos extends JFrame {

    private static final String[] CURSOS = {
        "Programación", "Bases de Datos", "Redes", "Sistemas", "Desarrollo Web"
    };
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final AlumnoDAO dao = new AlumnoDAO();

    // Formulario
    private final JTextField txtId = new JTextField(6);
    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtApellido = new JTextField(15);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtEdad = new JTextField(6);
    private final JComboBox<String> cboCurso = new JComboBox<>(CURSOS);

    // Botones
    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnModificar = new JButton("Modificar");
    private final JButton btnEliminar = new JButton("Eliminar");

    // Búsqueda y filtro
    private final JTextField txtBuscar = new JTextField(15);
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnMostrarTodos = new JButton("Mostrar todos");
    private final JButton btnLimpiarBusqueda = new JButton("Limpiar búsqueda");
    private final JComboBox<String> cboFiltroCurso = new JComboBox<>();
    private final JButton btnFiltrar = new JButton("Filtrar por curso");

    // Tabla y estadísticas
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Apellido", "Email", "Edad", "Curso"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false; // la tabla es solo de lectura
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final JLabel lblEstadisticas = new JLabel(" ");

    public FrmAlumnos() {
        super("Gestión de Alumnos");
        armarVentana();
        configurarEventos();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 620);
        setLocationRelativeTo(null); // centrada en la pantalla
        cargarTabla();
    }

    // ---------- Armado de la interfaz ----------

    private void armarVentana() {
        txtId.setEditable(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cboFiltroCurso.addItem("Todos");
        for (String c : CURSOS) {
            cboFiltroCurso.addItem(c);
        }

        // Panel del formulario
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Datos del alumno"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        agregarFila(pnlForm, g, 0, "ID:", txtId, "Edad:", txtEdad);
        agregarFila(pnlForm, g, 1, "Nombre:", txtNombre, "Apellido:", txtApellido);
        agregarFila(pnlForm, g, 2, "Email:", txtEmail, "Curso:", cboCurso);

        // Panel de botones
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlBotones.setBorder(BorderFactory.createTitledBorder("Acciones"));
        pnlBotones.add(btnNuevo);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnModificar);
        pnlBotones.add(btnEliminar);

        // Panel de búsqueda
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda y filtros"));
        pnlBusqueda.add(new JLabel("Apellido:"));
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        pnlBusqueda.add(btnMostrarTodos);
        pnlBusqueda.add(btnLimpiarBusqueda);
        pnlBusqueda.add(new JLabel("Curso:"));
        pnlBusqueda.add(cboFiltroCurso);
        pnlBusqueda.add(btnFiltrar);

        JPanel pnlNorte = new JPanel();
        pnlNorte.setLayout(new BoxLayout(pnlNorte, BoxLayout.Y_AXIS));
        pnlNorte.add(pnlForm);
        pnlNorte.add(pnlBotones);
        pnlNorte.add(pnlBusqueda);

        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSur.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        pnlSur.add(lblEstadisticas);

        JPanel raiz = new JPanel(new BorderLayout(5, 5));
        raiz.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        raiz.add(pnlNorte, BorderLayout.NORTH);
        raiz.add(new JScrollPane(tabla), BorderLayout.CENTER);
        raiz.add(pnlSur, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private void agregarFila(JPanel p, GridBagConstraints g, int fila,
                             String et1, java.awt.Component c1, String et2, java.awt.Component c2) {
        g.gridy = fila;
        g.gridx = 0; p.add(new JLabel(et1), g);
        g.gridx = 1; p.add(c1, g);
        g.gridx = 2; p.add(new JLabel(et2), g);
        g.gridx = 3; p.add(c2, g);
    }

    private void configurarEventos() {
        btnNuevo.addActionListener(e -> nuevo());
        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
        btnMostrarTodos.addActionListener(e -> mostrarTodos());
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());
        btnFiltrar.addActionListener(e -> filtrarPorCurso());

        // Al seleccionar una fila, se cargan sus datos en el formulario
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                cargarFormularioDesdeTabla();
            }
        });
    }

    // ---------- Tabla ----------

    private void cargarTabla() {
        mostrarEnTabla(dao.listar());
        actualizarEstadisticas();
    }

    private void mostrarEnTabla(List<Alumno> lista) {
        modelo.setRowCount(0);
        for (Alumno a : lista) {
            modelo.addRow(new Object[]{
                a.getId(), a.getNombre(), a.getApellido(),
                a.getEmail(), a.getEdad(), a.getCurso()});
        }
    }

    private void actualizarEstadisticas() {
        lblEstadisticas.setText(String.format("Total de alumnos: %d   |   Promedio de edad: %.2f",
                dao.cantidadAlumnos(), dao.promedioEdad()));
    }

    private void cargarFormularioDesdeTabla() {
        int f = tabla.getSelectedRow();
        txtId.setText(String.valueOf(modelo.getValueAt(f, 0)));
        txtNombre.setText(String.valueOf(modelo.getValueAt(f, 1)));
        txtApellido.setText(String.valueOf(modelo.getValueAt(f, 2)));
        txtEmail.setText(String.valueOf(modelo.getValueAt(f, 3)));
        txtEdad.setText(String.valueOf(modelo.getValueAt(f, 4)));
        String curso = String.valueOf(modelo.getValueAt(f, 5));
        // Si el curso guardado no está en la lista, se agrega para no perder el dato
        boolean existe = false;
        for (int i = 0; i < cboCurso.getItemCount(); i++) {
            if (cboCurso.getItemAt(i).equals(curso)) {
                existe = true;
                break;
            }
        }
        if (!existe && !curso.equals("null")) {
            cboCurso.addItem(curso);
        }
        cboCurso.setSelectedItem(curso);
    }

    // ---------- Acciones de los botones ----------

    private void nuevo() {
        limpiarFormulario();
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    private void guardar() {
        if (!txtId.getText().isEmpty()) {
            aviso("Hay un alumno seleccionado. Use «Modificar» para cambiarlo\no presione «Nuevo» para cargar uno nuevo.");
            return;
        }
        Alumno a = leerFormulario(0);
        if (a == null) {
            return;
        }
        if (dao.existeEmail(a.getEmail(), 0)) {
            aviso("El email ya existe. Ingrese otro.");
            return;
        }
        if (dao.insertar(a)) {
            info("Alumno guardado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            error("No se pudo guardar el alumno. Verifique la conexión con la base de datos.");
        }
    }

    private void modificar() {
        if (txtId.getText().isEmpty()) {
            aviso("Seleccione un alumno de la tabla para modificarlo.");
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        Alumno a = leerFormulario(id);
        if (a == null) {
            return;
        }
        if (dao.existeEmail(a.getEmail(), id)) {
            aviso("El email ya existe en otro alumno. Ingrese otro.");
            return;
        }
        if (dao.actualizar(a)) {
            info("Alumno modificado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            error("No se encontró el alumno o no se pudo modificar.");
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            aviso("Seleccione un alumno de la tabla para eliminarlo.");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = modelo.getValueAt(fila, 1) + " " + modelo.getValueAt(fila, 2);
        int r = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar a " + nombre + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        if (dao.eliminar(id)) {
            info("Alumno eliminado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            error("No se encontró el alumno o no se pudo eliminar.");
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            aviso("Escriba un apellido (o parte del apellido) para buscar.");
            return;
        }
        List<Alumno> lista = dao.buscarPorApellido(texto);
        mostrarEnTabla(lista);
        if (lista.isEmpty()) {
            info("No se encontraron alumnos con ese apellido.");
        }
    }

    private void mostrarTodos() {
        txtBuscar.setText("");
        cboFiltroCurso.setSelectedIndex(0);
        cargarTabla();
    }

    private void limpiarBusqueda() {
        txtBuscar.setText("");
        cargarTabla();
    }

    private void filtrarPorCurso() {
        String curso = (String) cboFiltroCurso.getSelectedItem();
        if (curso == null || curso.equals("Todos")) {
            cargarTabla();
            return;
        }
        List<Alumno> lista = dao.buscarPorCurso(curso);
        mostrarEnTabla(lista);
        if (lista.isEmpty()) {
            info("No hay alumnos en ese curso.");
        }
    }

    // ---------- Validaciones y utilidades ----------

    // Devuelve el Alumno armado con los datos del formulario, o null si hay un error.
    private Alumno leerFormulario(int id) {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String textoEdad = txtEdad.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || textoEdad.isEmpty()) {
            aviso("Complete todos los campos obligatorios: nombre, apellido, email y edad.");
            return null;
        }
        if (!EMAIL.matcher(email).matches()) {
            aviso("El email no tiene un formato válido (ejemplo: nombre@dominio.com).");
            return null;
        }
        int edad;
        try {
            edad = Integer.parseInt(textoEdad);
        } catch (NumberFormatException ex) {
            aviso("La edad debe ser un número entero.");
            return null;
        }
        if (edad <= 0 || edad > 120) {
            aviso("La edad debe ser mayor que 0 y no superar 120.");
            return null;
        }
        String curso = (String) cboCurso.getSelectedItem();
        if (curso == null || curso.isEmpty()) {
            aviso("Seleccione un curso.");
            return null;
        }
        return new Alumno(id, nombre, apellido, email, edad, curso);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtEdad.setText("");
        cboCurso.setSelectedIndex(0);
    }

    private void info(String m) {
        JOptionPane.showMessageDialog(this, m, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void aviso(String m) {
        JOptionPane.showMessageDialog(this, m, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    private void error(String m) {
        JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
