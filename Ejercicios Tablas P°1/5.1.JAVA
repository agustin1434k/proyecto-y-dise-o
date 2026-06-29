/*
 * 5.1. Diseñar un programa que solicite al usuario que introduzca por teclado
 * 5 números decimales. A continuación, debe mostrar los números en el mismo
 * orden que se han introducido.
 */

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US); 

        double numeros[] = new double[5];

        for (int i = 0; i < numeros.length; i++) {
            System.out.print("Ingrese un número decimal: ");
            numeros[i] = sc.nextDouble();
        }

        System.out.println("\nLos números ingresados son:");

        for (int i = 0; i < numeros.length; i++) {
            System.out.println(numeros[i]);
        }

        System.out.println("\nMostrados con Arrays:");
        System.out.println(Arrays.toString(numeros));

        sc.close();
    }
}