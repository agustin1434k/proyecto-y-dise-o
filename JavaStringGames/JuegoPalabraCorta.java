import javax.swing.JOptionPane;

public class JuegoPalabraCorta {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        // Pedimos la primera palabra
        String palabra1 = JOptionPane.showInputDialog(
                menu,
                "Ingrese la primera palabra:"
        );

        // Si presiona Cancelar
        if (palabra1 == null) {
            return;
        }

        // Pedimos la segunda palabra
        String palabra2 = JOptionPane.showInputDialog(
                menu,
                "Ingrese la segunda palabra:"
        );

        if (palabra2 == null) {
            return;
        }

        // Quitamos espacios al principio y al final
        palabra1 = palabra1.trim();
        palabra2 = palabra2.trim();

        // Validamos que no estén vacías
        if (palabra1.isEmpty() || palabra2.isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "No puede dejar los campos vacíos."
            );

            return;
        }

        // Comparamos la cantidad de caracteres
        if (palabra1.length() < palabra2.length()) {

            jugador.sumarPuntos(50);

            JOptionPane.showMessageDialog(
                    menu,
                    palabra1 + " posee menos caracteres.\n\n"
                    + "+50 puntos"
            );

        } else if (palabra2.length() < palabra1.length()) {

            jugador.sumarPuntos(50);

            JOptionPane.showMessageDialog(
                    menu,
                    palabra2 + " posee menos caracteres.\n\n"
                    + "+50 puntos"
            );

        } else {

            JOptionPane.showMessageDialog(
                    menu,
                    "Las dos palabras tienen la misma cantidad "
                    + "de caracteres.\n\n"
                    + "0 puntos"
            );
        }

        // Actualizamos el puntaje del menú
        menu.actualizarDatos();
    }
}