import javax.swing.JOptionPane;

public class JuegoPalindromo {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        // Pedimos una frase
        String frase = JOptionPane.showInputDialog(
                menu,
                "Ingrese una frase:"
        );

        // Si presiona Cancelar
        if (frase == null) {
            return;
        }

        // Eliminamos los espacios y pasamos a minúsculas
        String limpia = frase
                .replace(" ", "")
                .toLowerCase();

        // Validamos que no esté vacía
        if (limpia.isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "La frase no puede estar vacía."
            );

            return;
        }

        // Invertimos la cadena
        String invertida = "";

        for (int i = limpia.length() - 1; i >= 0; i--) {

            invertida += limpia.charAt(i);
        }

        // Comparamos la frase original con la invertida
        if (limpia.equals(invertida)) {

            jugador.sumarPuntos(50);

            JOptionPane.showMessageDialog(
                    menu,
                    "PALÍNDROMO\n\n"
                    + "La frase se lee igual al revés.\n\n"
                    + "+50 puntos"
            );

        } else {

            jugador.sumarPuntos(-10);

            JOptionPane.showMessageDialog(
                    menu,
                    "NO ES PALÍNDROMO\n\n"
                    + "-10 puntos"
            );
        }

        // Actualizamos el puntaje
        menu.actualizarDatos();
    }
}