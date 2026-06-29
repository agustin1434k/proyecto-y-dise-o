/*
 * 5.4. Implementar un programa que inicialice una tabla con nuestros números
 * favoritos. A continuación, pedir al usuario el índice de un elemento que
 * será eliminado de la tabla. Continuaremos eliminando elementos hasta que
 * el índice introducido sea negativo o hasta que no existan más elementos
 * que borrar. Es imprescindible controlar que el índice leído corresponde
 * a un dato válido.
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int numeros[] = {23, 8, 1, -3, 17, 5, 99};

        int numElem = numeros.length;

        mostrarTabla(numeros, numElem);

        System.out.print("Ingrese el índice del elemento a borrar: ");
        int indice = sc.nextInt();

        while (indice >= 0 && numElem > 0) {

            if (indice < numElem) {
                numeros[indice] = numeros[numElem - 1];
                numElem--;

                System.out.println("\nTabla actual:");
                mostrarTabla(numeros, numElem);

            } else {

                System.out.println("Índice inválido.");
            }

            if (numElem > 0) {
                System.out.print("\nIngrese otro índice (-1 para salir): ");
                indice = sc.nextInt();
            }
        }

        System.out.println("\nPrograma finalizado.");
        sc.close();
    }

    public static void mostrarTabla(int t[], int n) {

        System.out.print("[");

        for (int i = 0; i < n; i++) {

            System.out.print(t[i]);

            if (i < n - 1) {
                System.out.print(", ");
            }
        }

        System.out.println("]");
    }
}