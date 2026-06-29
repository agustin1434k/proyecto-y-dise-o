/*
 * 5.5. Desarrollar el juego la cámara secreta, que consiste en abrir una
 * cámara mediante su combinación secreta, formada por una combinación de
 * dígitos del 1 al 5. El jugador especificará cuál es la longitud de la
 * combinación. La aplicación genera una combinación secreta de forma
 * aleatoria. En cada intento se muestra como pista si cada número ingresado
 * es mayor, menor o igual al correspondiente de la combinación secreta.
 */

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Longitud de la combinación secreta: ");
        int longitud = sc.nextInt();

        int combSecreta[] = new int[longitud];
        int combJugador[] = new int[longitud];

        generarCombinacion(combSecreta);

        System.out.println("Combinación secreta: " + Arrays.toString(combSecreta));

        System.out.println("\nIngrese una combinación:");
        leerCombinacion(combJugador);

        while (!Arrays.equals(combSecreta, combJugador)) {

            mostrarPistas(combSecreta, combJugador);

            System.out.println("\nIntente nuevamente:");
            leerCombinacion(combJugador);
        }

        System.out.println("\n¡La cámara está abierta!");
        sc.close();
    }

    public static void generarCombinacion(int t[]) {

        final int MAX = 5;

        for (int i = 0; i < t.length; i++) {
            t[i] = (int) (Math.random() * MAX + 1);
        }
    }

    public static void leerCombinacion(int t[]) {

        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < t.length; i++) {
            System.out.print("Número " + (i + 1) + ": ");
            t[i] = sc.nextInt();
        }
    }

    public static void mostrarPistas(int secreta[], int jugador[]) {

        System.out.println("\nPistas:");

        for (int i = 0; i < jugador.length; i++) {

            System.out.print(jugador[i] + " -> ");

            if (jugador[i] < secreta[i]) {
                System.out.println("Mayor");
            } else if (jugador[i] > secreta[i]) {
                System.out.println("Menor");
            } else {
                System.out.println("Igual");
            }
        }
    }
}