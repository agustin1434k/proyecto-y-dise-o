import javax.swing.*;

public class JuegoContrasena {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        // Campo especial para ocultar la contraseña
        JPasswordField campoContrasena = new JPasswordField();

        int opcion = JOptionPane.showConfirmDialog(
                menu,
                campoContrasena,
                "Jugador 1: Ingrese una contraseña",
                JOptionPane.OK_CANCEL_OPTION
        );

        // Si cancela
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        // Obtenemos la contraseña
        String contrasena =
                new String(campoContrasena.getPassword());

        // Validamos que no esté vacía
        if (contrasena.isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "La contraseña no puede estar vacía."
            );

            return;
        }

        // Mostramos las pistas
        JOptionPane.showMessageDialog(
                menu,
                "PISTAS:\n\n"
                + "Cantidad de caracteres: "
                + contrasena.length()
                + "\nPrimer carácter: "
                + contrasena.charAt(0)
                + "\nÚltimo carácter: "
                + contrasena.charAt(
                        contrasena.length() - 1
                )
        );

        int intentos = 0;
        boolean acerto = false;

        // Mientras tenga menos de 3 intentos
        while (intentos < 3 && !acerto) {

            String intento = JOptionPane.showInputDialog(
                    menu,
                    "Intento " + (intentos + 1)
                    + " de 3\n\n"
                    + "Ingrese la contraseña:"
            );

            // Si abandona
            if (intento == null) {
                return;
            }

            // Comparamos las contraseñas
            if (intento.equals(contrasena)) {

                acerto = true;

                jugador.sumarPuntos(50);

                JOptionPane.showMessageDialog(
                        menu,
                        "¡CONTRASEÑA CORRECTA!\n\n"
                        + "+50 puntos"
                );

            } else {

                intentos++;

                // Pista avanzada: comparación alfabética
                if (intento.compareTo(contrasena) > 0) {

                    JOptionPane.showMessageDialog(
                            menu,
                            "Incorrecto.\n\n"
                            + "Tu palabra es alfabéticamente "
                            + "MAYOR que la contraseña."
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            menu,
                            "Incorrecto.\n\n"
                            + "Tu palabra es alfabéticamente "
                            + "MENOR que la contraseña."
                    );
                }
            }
        }

        // Si después de los intentos no acertó
        if (!acerto) {

            jugador.sumarPuntos(-10);

            JOptionPane.showMessageDialog(
                    menu,
                    "Se terminaron los 3 intentos.\n\n"
                    + "La contraseña era: "
                    + contrasena
                    + "\n\n-10 puntos"
            );
        }

        // Actualizamos el puntaje
        menu.actualizarDatos();
    }
}