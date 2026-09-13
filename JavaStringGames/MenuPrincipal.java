import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private Jugador jugador;
    private JLabel lblJugador;
    private JLabel lblPuntaje;

    public MenuPrincipal() {

        String nombre = JOptionPane.showInputDialog(
                null,
                "Ingrese el nombre del jugador:"
        );

        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "Jugador";
        }

        jugador = new Jugador(nombre);

        crearVentana();
    }

    private void crearVentana() {

        setTitle("JAVA STRING GAMES");
        setSize(650, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Título
        JLabel titulo = new JLabel(
                "JAVA STRING GAMES",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        // Información del jugador
        JPanel panelInfo = new JPanel(
                new GridLayout(2, 1)
        );

        lblJugador = new JLabel(
                "Jugador: " + jugador.getNombre()
        );

        lblPuntaje = new JLabel(
                "Puntaje: " + jugador.getPuntaje()
        );

        lblJugador.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        lblPuntaje.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        panelInfo.add(lblJugador);
        panelInfo.add(lblPuntaje);

        // Botones de juegos
        JPanel panelJuegos = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        JButton btnPalabraCorta =
                new JButton("Palabra más corta");
                btnPalabraCorta.addActionListener(e -> {

    JuegoPalabraCorta.ejecutar(
            this,
            jugador
    );

});
        JButton btnContrasena =
                new JButton("Acierta la contraseña");
                btnContrasena.addActionListener(e -> {

    JuegoContrasena.ejecutar(
            this,
            jugador
    );

});

        JButton btnEspejo =
                new JButton("Espejo");
                btnEspejo.addActionListener(e -> {

    JuegoEspejo.ejecutar(
            this,
            jugador
    );

});

        JButton btnBuscador =
                new JButton("Buscador de palabras");
                btnBuscador.addActionListener(e -> {

    JuegoBuscador.ejecutar(
            this,
            jugador
    );

});

        JButton btnPalindromo =
                new JButton("Palíndromo");
                btnPalindromo.addActionListener(e -> {

    JuegoPalindromo.ejecutar(
            this,
            jugador
    );

});

        JButton btnJavalandia =
                new JButton("Traductor Javalandia");
                btnJavalandia.addActionListener(e -> {

    JuegoJavalandia.ejecutar(
            this,
            jugador
    );

});

        JButton btnAnagramas =
                new JButton("Anagramas");
                btnAnagramas.addActionListener(e -> {

    JuegoAnagrama.ejecutar(
            this,
            jugador
    );

});

        JButton btnContador =
                new JButton("Contador de letras");
                btnContador.addActionListener(e -> {

    JuegoContadorLetras.ejecutar(
            this,
            jugador
    );

});

        JButton btnAnagramaFinal =
                new JButton("Juego del anagrama");
                btnAnagramaFinal.addActionListener(e -> {

    JuegoAnagramaFinal.ejecutar(
            this,
            jugador
    );

});

        // Agregamos los botones
        panelJuegos.add(btnPalabraCorta);
        panelJuegos.add(btnContrasena);
        panelJuegos.add(btnEspejo);
        panelJuegos.add(btnBuscador);
        panelJuegos.add(btnPalindromo);
        panelJuegos.add(btnJavalandia);
        panelJuegos.add(btnAnagramas);
        panelJuegos.add(btnContador);
        panelJuegos.add(btnAnagramaFinal);

        // Botón Nuevo jugador
        JButton btnNuevoJugador =
                new JButton("Nuevo jugador");

        btnNuevoJugador.addActionListener(e -> {

            String nombre = JOptionPane.showInputDialog(
                    this,
                    "Ingrese el nombre del nuevo jugador:"
            );

            if (nombre != null &&
                    !nombre.trim().isEmpty()) {

                jugador = new Jugador(nombre.trim());
                actualizarDatos();
            }
        });

        // Botón Ayuda
        JButton btnAyuda =
                new JButton("Ayuda");

        btnAyuda.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "JAVA STRING GAMES\n\n" +
                    "Elegí uno de los juegos para comenzar.\n\n" +
                    "Respuesta correcta: +50 puntos\n" +
                    "Desafío difícil correcto: +100 puntos\n" +
                    "Respuesta incorrecta: -10 puntos\n" +
                    "Abandonar: 0 puntos",
                    "Ayuda",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Botón Salir
        JButton btnSalir =
                new JButton("SALIR");

        btnSalir.addActionListener(e -> {
            System.exit(0);
        });

        // Panel inferior
        JPanel panelInferior =
                new JPanel(new FlowLayout());

        panelInferior.add(btnNuevoJugador);
        panelInferior.add(btnAyuda);
        panelInferior.add(btnSalir);

        // Agregamos todo
        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                panelInfo,
                BorderLayout.WEST
        );

        panelPrincipal.add(
                panelJuegos,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelInferior,
                BorderLayout.SOUTH
        );

        setContentPane(panelPrincipal);
    }

    // Actualizar jugador y puntaje
    public void actualizarDatos() {

        lblJugador.setText(
                "Jugador: " + jugador.getNombre()
        );

        lblPuntaje.setText(
                "Puntaje: " + jugador.getPuntaje()
        );
    }

    public Jugador getJugador() {
        return jugador;
    }
}