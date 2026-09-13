import javax.swing.JOptionPane;
import java.util.Random;

public class JuegoAnagramaFinal {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        String original = JOptionPane.showInputDialog(
                menu,
                "Jugador 1: ingrese una palabra o frase:"
        );

        if (original == null) {
            return;
        }

        original = original.trim();

        if (original.isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "La palabra o frase no puede estar vacía."
            );

            return;
        }

        String anagrama = generarAnagrama(original);

        JOptionPane.showMessageDialog(
                menu,
                "Jugador 2 debe descubrir el texto original.\n\n"
                + "ANAGRAMA:\n"
                + anagrama
        );

        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {

            String respuesta = JOptionPane.showInputDialog(
                    menu,
                    "Intento " + (intentos + 1)
                    + " de " + MAX_INTENTOS
                    + "\n\nAnagrama: "
                    + anagrama
            );

            if (respuesta == null) {
                return;
            }

            if (respuesta.trim().equalsIgnoreCase(original)) {

                jugador.sumarPuntos(100);

                JOptionPane.showMessageDialog(
                        menu,
                        "¡ACERTASTE!\n\n"
                        + "+100 puntos"
                );

                menu.actualizarDatos();

                int nuevaPartida = JOptionPane.showConfirmDialog(
                        menu,
                        "¿Querés comenzar una nueva partida?",
                        "Nueva partida",
                        JOptionPane.YES_NO_OPTION
                );

                if (nuevaPartida == JOptionPane.YES_OPTION) {

                    ejecutar(menu, jugador);
                }

                return;
            }

            intentos++;

            if (intentos < MAX_INTENTOS) {

                JOptionPane.showMessageDialog(
                        menu,
                        "Incorrecto.\n"
                        + "Te quedan "
                        + (MAX_INTENTOS - intentos)
                        + " intentos."
                );
            }
        }

        jugador.sumarPuntos(-10);

        JOptionPane.showMessageDialog(
                menu,
                "Se terminaron los intentos.\n\n"
                + "La respuesta era: "
                + original
                + "\n\n"
                + "-10 puntos"
        );

        menu.actualizarDatos();
    }

    private static String generarAnagrama(String texto) {

        char[] letras = texto.toCharArray();

        Random random = new Random();

        for (int i = letras.length - 1; i > 0; i--) {

            int j = random.nextInt(i + 1);

            char auxiliar = letras[i];

            letras[i] = letras[j];
            letras[j] = auxiliar;
        }

        return new String(letras);
    }
}