/*
 * 5.14. Definir una función que tome como parámetros dos tablas, la primera
 * con los 6 números de una apuesta de la primitiva y la segunda con los
 * 6 números de la combinación ganadora. La función devolverá el número
 * de aciertos.
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int apuesta[] = new int[6];
        int ganadora[] = new int[6];

        System.out.println("Ingrese la apuesta:");

        for (int i = 0; i < 6; i++) {
            apuesta[i] = sc.nextInt();
        }

        System.out.println("Ingrese la combinación ganadora:");

        for (int i = 0; i < 6; i++) {
            ganadora[i] = sc.nextInt();
        }

        int aciertos = contarAciertos(apuesta, ganadora);

        System.out.println("Cantidad de aciertos: " + aciertos);

        sc.close();
    }

    public static int contarAciertos(int apuesta[], int ganadora[]) {

        int aciertos = 0;

        for (int i = 0; i < apuesta.length; i++) {

            for (int j = 0; j < ganadora.length; j++) {

                if (apuesta[i] == ganadora[j]) {
                    aciertos++;
                    break;
                }

            }
        }

        return aciertos;
    }
}