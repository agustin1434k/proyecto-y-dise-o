import javax.swing.JOptionPane;
import java.util.Arrays;

public class JuegoAnagrama {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        String palabra1 = JOptionPane.showInputDialog(
                menu,
                "Ingrese la primera palabra:"
        );

        if (palabra1 == null) {
            return;
        }

        String palabra2 = JOptionPane.showInputDialog(
                menu,
                "Ingrese la segunda palabra:"
        );

        if (palabra2 == null) {
            return;
        }

        palabra1 = palabra1.replace(" ", "").toLowerCase();
        palabra2 = palabra2.replace(" ", "").toLowerCase();

        if (palabra1.isEmpty() || palabra2.isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "Las palabras no pueden estar vacías."
            );

            return;
        }

        char[] letras1 = palabra1.toCharArray();
        char[] letras2 = palabra2.toCharArray();

        Arrays.sort(letras1);
        Arrays.sort(letras2);

        if (Arrays.equals(letras1, letras2)) {

            jugador.sumarPuntos(50);

            JOptionPane.showMessageDialog(
                    menu,
                    "¡SON ANAGRAMAS!\n\n"
                    + "+50 puntos"
            );

        } else {

            jugador.sumarPuntos(-10);

            JOptionPane.showMessageDialog(
                    menu,
                    "NO SON ANAGRAMAS.\n\n"
                    + "-10 puntos"
            );
        }

        menu.actualizarDatos();
    }
}