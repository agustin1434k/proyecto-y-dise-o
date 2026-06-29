/*
 * 5.2. Escribir una aplicación que solicite al usuario cuántos números desea
 * introducir. A continuación, se introducirá por teclado esa cantidad de
 * números enteros y, por último, los mostrará en el orden inverso al
 * introducido.
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("¿Cuántos números desea introducir?: ");
        int cantidad = sc.nextInt();

        int numeros[] = new int[cantidad];

        for (int i = 0; i < numeros.length; i++) {
            System.out.print("Ingrese un número: ");
            numeros[i] = sc.nextInt();
        }

        System.out.println("\nLos números en orden inverso son:");

        for (int i = numeros.length - 1; i >= 0; i--) {
            System.out.println(numeros[i]);
        }

        sc.close();
    }
}