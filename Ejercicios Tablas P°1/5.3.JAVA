/*
 * 5.3. Introducir por teclado un número n; a continuación solicitar al usuario
 * que teclee n números. Realizar la media de los números positivos, la media
 * de los negativos y contar el número de ceros introducidos.
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese la cantidad de números (n): ");
        int n = sc.nextInt();

        int numeros[] = new int[n];

        int sumaPositivos = 0;
        int sumaNegativos = 0;
        int contPositivos = 0;
        int contNegativos = 0;
        int contCeros = 0;

        for (int i = 0; i < numeros.length; i++) {
            System.out.print("Ingrese un número: ");
            numeros[i] = sc.nextInt();
        }

        for (int num : numeros) {

            if (num == 0) {
                contCeros++;
            } else if (num > 0) {
                sumaPositivos += num;
                contPositivos++;
            } else {
                sumaNegativos += num;
                contNegativos++;
            }
        }

        if (contPositivos > 0) {
            double mediaPositivos = (double) sumaPositivos / contPositivos;
            System.out.println("Media de los positivos: " + mediaPositivos);
        } else {
            System.out.println("No se ingresaron números positivos.");
        }

        if (contNegativos > 0) {
            double mediaNegativos = (double) sumaNegativos / contNegativos;
            System.out.println("Media de los negativos: " + mediaNegativos);
        } else {
            System.out.println("No se ingresaron números negativos.");
        }

        System.out.println("Cantidad de ceros: " + contCeros);

        sc.close();
    }
}