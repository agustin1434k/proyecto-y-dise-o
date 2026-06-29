/*
 * 5.7. Leer una serie de 6 enteros que se almacenarán en una tabla que hay
 * que ordenar y mostrar. Leer otra serie de 6 enteros, que también se
 * guardarán en una tabla y se mostrarán ordenados. A continuación, fusionar
 * las dos tablas en una tercera, de forma que los 12 números sigan
 * ordenados.
 */

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        final int TAM = 6;

        int a[] = new int[TAM];
        int b[] = new int[TAM];

        System.out.println("Ingrese los 6 números de la primera tabla:");

        for (int i = 0; i < TAM; i++) {
            System.out.print("Número " + (i + 1) + ": ");
            a[i] = sc.nextInt();
        }

        Arrays.sort(a);

        System.out.println("Primera tabla ordenada:");
        System.out.println(Arrays.toString(a));

        System.out.println("\nIngrese los 6 números de la segunda tabla:");

        for (int i = 0; i < TAM; i++) {
            System.out.print("Número " + (i + 1) + ": ");
            b[i] = sc.nextInt();
        }

        Arrays.sort(b);

        System.out.println("Segunda tabla ordenada:");
        System.out.println(Arrays.toString(b));

        int c[] = new int[TAM * 2];

        int indA = 0;
        int indB = 0;
        int indC = 0;

        while (indA < TAM && indB < TAM) {

            if (a[indA] < b[indB]) {
                c[indC] = a[indA];
                indA++;
            } else {
                c[indC] = b[indB];
                indB++;
            }

            indC++;
        }

        while (indA < TAM) {
            c[indC] = a[indA];
            indA++;
            indC++;
        }

        while (indB < TAM) {
            c[indC] = b[indB];
            indB++;
            indC++;
        }

        System.out.println("\nTabla fusionada:");
        System.out.println(Arrays.toString(c));

        sc.close();
    }
}