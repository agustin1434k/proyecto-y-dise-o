import javax.swing.JOptionPane;

public class JuegoBuscador {

    public static void ejecutar(MenuPrincipal menu, Jugador jugador) {

        // Pedimos la frase
        String frase = JOptionPane.showInputDialog(
                menu,
                "Ingrese una frase:"
        );

        if (frase == null) {
            return;
        }

        // Pedimos la palabra que queremos buscar
        String palabra = JOptionPane.showInputDialog(
                menu,
                "Ingrese la palabra que desea buscar:"
        );

        if (palabra == null) {
            return;
        }

        // Quitamos espacios innecesarios
        frase = frase.trim();
        palabra = palabra.trim();

        // Validamos los campos
        if (frase.isEmpty() || palabra.isEmpty()) {

            JOptionPane.showMessageDialog(
                    menu,
                    "Los campos no pueden estar vacíos."
            );

            return;
        }

        // Pasamos todo a minúsculas
        // para que Java y java sean considerados iguales
        String fraseMinuscula = frase.toLowerCase();
        String palabraMinuscula = palabra.toLowerCase();

        int contador = 0;
        int posicion = 0;

        // Buscamos todas las apariciones
        while ((posicion = fraseMinuscula.indexOf(
                palabraMinuscula,
                posicion
        )) != -1) {

            contador++;

            // Avanzamos para seguir buscando
            posicion += palabraMinuscula.length();
        }

        // Sumamos los puntos
        jugador.sumarPuntos(50);

        // Mostramos el resultado
        JOptionPane.showMessageDialog(
                menu,
                "La palabra \""
                + palabra
                + "\" aparece "
                + contador
                + " veces.\n\n"
                + "+50 puntos"
        );

        // Actualizamos el puntaje
        menu.actualizarDatos();
    }
}