/*
 * 5.6. Diseñar una aplicación para gestionar un campeonato de programación,
 * donde se introducen las puntuaciones de 5 programadores. La aplicación
 * debe mostrar las puntuaciones ordenadas. Luego se podrán agregar hasta
 * 3 programadores de exhibición. Para finalizar el ingreso se introduce -1.
 * Finalmente, se mostrarán todas las puntuaciones ordenadas.
 */

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int puntos[] = new int[8];

        for (int i = 0; i < 5; i++) {
            System.out.print("Puntos del programador " + (i + 1) + ": ");
            puntos[i] = sc.nextInt();
        }

        Arrays.sort(puntos, 0, 5);

        System.out.println("\nPuntuaciones ordenadas:");
        for (int i = 0; i < 5; i++) {
            System.out.print(puntos[i] + " ");
        }

        System.out.println();

        int numElem = 5;

        System.out.print("\nPuntos del programador de exhibición (-1 para terminar): ");
        int puntosExhibicion = sc.nextInt();

        while (puntosExhibicion != -1 && numElem < puntos.length) {

            int pos = Arrays.binarySearch(puntos, 0, numElem, puntosExhibicion);

            int indiceInsercion;

            if (pos < 0) {
                indiceInsercion = -pos - 1;
            } else {
                indiceInsercion = pos;
            }

            for (int i = numElem - 1; i >= indiceInsercion; i--) {
                puntos[i + 1] = puntos[i];
            }

            puntos[indiceInsercion] = puntosExhibicion;
            numElem++;

            if (numElem < puntos.length) {
                System.out.print("Puntos del programador de exhibición (-1 para terminar): ");
                puntosExhibicion = sc.nextInt();
            }
        }

        System.out.println("\nPuntuaciones finales ordenadas:");

        for (int i = 0; i < numElem; i++) {
            System.out.print(puntos[i] + " ");
        }

        sc.close();
    }
}