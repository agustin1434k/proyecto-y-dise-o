import javax.swing.JOptionPane;

public class JuegoContadorLetras {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        String frase = JOptionPane.showInputDialog(
                menu,
                "Ingrese una frase:"
        );

        if (frase == null) {
            return;
        }

        frase = frase.toLowerCase();

        if (frase.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "La frase no puede estar vacía."
            );

            return;
        }

        int[] contador = new int[26];

        for (int i = 0; i < frase.length(); i++) {

            char letra = frase.charAt(i);

            if (letra >= 'a' && letra <= 'z') {

                contador[letra - 'a']++;
            }
        }

        String resultado = "";

        for (int i = 0; i < 26; i++) {

            if (contador[i] > 0) {

                resultado +=
                        (char) ('a' + i)
                        + ": "
                        + contador[i]
                        + "\n";
            }
        }

        jugador.sumarPuntos(50);

        JOptionPane.showMessageDialog(
                menu,
                "Cantidad de letras:\n\n"
                + resultado
                + "\n+50 puntos"
        );

        menu.actualizarDatos();
    }
}