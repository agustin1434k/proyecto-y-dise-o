/*
 * 5.15. Escribir la función rellenaPares() a la que se le pasa como
 * parámetro una tabla que debe rellenar con números pares leídos por
 * teclado. Los números impares se ignoran. La función devuelve la
 * cantidad de impares desechados.
 */

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int tabla[] = new int[6];

        int descartados = rellenaPares(tabla);

        System.out.println("\nTabla de números pares:");
        System.out.println(Arrays.toString(tabla));

        System.out.println("Cantidad de impares descartados: " + descartados);
    }

    public static int rellenaPares(int tabla[]) {

        Scanner sc = new Scanner(System.in);

        int descartados = 0;
        int i = 0;

        while (i < tabla.length) {

            System.out.print("Ingrese un número: ");
            int num = sc.nextInt();

            if (num % 2 == 0) {

                tabla[i] = num;
                i++;

            } else {

                descartados++;

            }
        }

        return descartados;
    }
}