import javax.swing.JOptionPane;

public class JuegoJavalandia {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        String frase = JOptionPane.showInputDialog(
                menu,
                "Ingrese una frase en Javalandia:"
        );

        if (frase == null) {
            return;
        }

        if (frase.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "La frase no puede estar vacía."
            );

            return;
        }

        String inicio = "Javalin, javalón";
        String finalJavalandia = "javalén, len, len";

        String resultado;

        // Variante 1: comienza con Javalin, javalón
        if (frase.startsWith(inicio)) {

            resultado = frase.substring(inicio.length()).trim();

        // Variante 2: termina con javalén, len, len
        } else if (frase.endsWith(finalJavalandia)) {

            resultado = frase.substring(
                    0,
                    frase.length() - finalJavalandia.length()
            ).trim();

        } else {

            jugador.sumarPuntos(-10);

            JOptionPane.showMessageDialog(
                    menu,
                    "La frase no pertenece al idioma Javalandia.\n\n"
                    + "-10 puntos"
            );

            menu.actualizarDatos();
            return;
        }

        jugador.sumarPuntos(50);

        JOptionPane.showMessageDialog(
                menu,
                "Mensaje traducido:\n\n"
                + resultado
                + "\n\n"
                + "+50 puntos"
        );

        menu.actualizarDatos();
    }
}