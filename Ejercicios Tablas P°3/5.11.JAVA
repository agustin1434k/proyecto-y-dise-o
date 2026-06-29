/*
 * 5.11. Escribir un programa que solicite los elementos de una matriz de
 * tamaño 4x4. La aplicación debe decidir si la matriz introducida
 * corresponde a una matriz mágica, donde la suma de los elementos de
 * cualquier fila o columna es la misma.
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int matriz[][] = new int[4][4];

        System.out.println("Ingrese los elementos de la matriz:");

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("[" + i + "][" + j + "]: ");
                matriz[i][j] = sc.nextInt();
            }
        }

        int sumaReferencia = 0;

        for (int j = 0; j < 4; j++) {
            sumaReferencia += matriz[0][j];
        }

        boolean magica = true;

        for (int i = 0; i < 4 && magica; i++) {

            int sumaFila = 0;

            for (int j = 0; j < 4; j++) {
                sumaFila += matriz[i][j];
            }

            if (sumaFila != sumaReferencia) {
                magica = false;
            }
        }

        for (int j = 0; j < 4 && magica; j++) {

            int sumaColumna = 0;

            for (int i = 0; i < 4; i++) {
                sumaColumna += matriz[i][j];
            }

            if (sumaColumna != sumaReferencia) {
                magica = false;
            }
        }

        if (magica) {
            System.out.println("La matriz es mágica.");
        } else {
            System.out.println("La matriz NO es mágica.");
        }

        sc.close();
    }
}