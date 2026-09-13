import javax.swing.JOptionPane;

public class JuegoEspejo {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        // Pedimos una cadena
        String texto = JOptionPane.showInputDialog(
                menu,
                "Ingrese una palabra o frase:"
        );

        // Si presiona Cancelar
        if (texto == null) {
            return;
        }

        // Validamos que no esté vacío
        if (texto.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "La cadena no puede estar vacía."
            );

            return;
        }

        // Variable donde guardaremos la cadena invertida
        String invertida = "";

        // Recorremos la cadena desde el último carácter
        for (int i = texto.length() - 1; i >= 0; i--) {

            invertida += texto.charAt(i);
        }

        // Sumamos los puntos
        jugador.sumarPuntos(50);

        // Mostramos el resultado
        JOptionPane.showMessageDialog(
                menu,
                "Entrada:\n"
                + texto
                + "\n\nResultado:\n"
                + invertida
                + "\n\n+50 puntos"
        );

        // Actualizamos el puntaje
        menu.actualizarDatos();
    }
}