/*
 * 5.10. Leer y almacenar n números enteros en una tabla. Construir otras
 * dos tablas: una con los números pares y otra con los números impares.
 */

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("¿Cuántos números desea ingresar?: ");
        int n = sc.nextInt();

        int numeros[] = new int[n];

        int contPares = 0;
        int contImpares = 0;

        for (int i = 0; i < n; i++) {

            System.out.print("Número " + (i + 1) + ": ");
            numeros[i] = sc.nextInt();

            if (numeros[i] % 2 == 0) {
                contPares++;
            } else {
                contImpares++;
            }
        }

        int pares[] = new int[contPares];
        int impares[] = new int[contImpares];

        int iPar = 0;
        int iImpar = 0;

        for (int num : numeros) {

            if (num % 2 == 0) {
                pares[iPar] = num;
                iPar++;
            } else {
                impares[iImpar] = num;
                iImpar++;
            }
        }

        System.out.println("\nTabla original:");
        System.out.println(Arrays.toString(numeros));

        System.out.println("Tabla de pares:");
        System.out.println(Arrays.toString(pares));

        System.out.println("Tabla de impares:");
        System.out.println(Arrays.toString(impares));

        sc.close();
    }
}