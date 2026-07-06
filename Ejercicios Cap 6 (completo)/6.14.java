import java.util.Random;
import java.util.Scanner;

public class Ejercicio614 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        String texto;

        do {
            System.out.print("Jugador 1, ingrese una palabra o frase: ");
            texto = sc.nextLine();

            if (texto.trim().isEmpty()) {
                System.out.println("No puede ingresar una cadena vacía.");
            }

        } while (texto.trim().isEmpty());

        char[] letras = texto.toCharArray();

        for (int i = 0; i < letras.length; i++) {

            int j = random.nextInt(letras.length);

            char aux = letras[i];
            letras[i] = letras[j];
            letras[j] = aux;
        }

        String anagrama = new String(letras);

        for (int i = 0; i < 30; i++) {
            System.out.println();
        }

        System.out.println("Jugador 2");
        System.out.println("Anagrama: " + anagrama);

        String intento;

        do {

            System.out.print("¿Cuál es el texto original?: ");
            intento = sc.nextLine();

            if (!intento.equals(texto)) {
                System.out.println("Incorrecto. Intente nuevamente.");
            }

        } while (!intento.equals(texto));

        System.out.println("¡Correcto! Adivinaste el texto original.");

        sc.close();
    }
}