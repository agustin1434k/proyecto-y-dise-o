/*
 * 5.12. Crear una tabla bidimensional de tamaño 5x5 y rellenarla de forma
 * que la posición [n][m] contenga n + m. Luego mostrar su contenido.
 */

public class Main {

    public static void main(String[] args) {

        int tabla[][] = new int[5][5];

        for (int i = 0; i < tabla.length; i++) {

            for (int j = 0; j < tabla[i].length; j++) {

                tabla[i][j] = i + j;

            }
        }

        System.out.println("Contenido de la tabla:");

        for (int i = 0; i < tabla.length; i++) {

            for (int j = 0; j < tabla[i].length; j++) {

                System.out.print(tabla[i][j] + "\t");

            }

            System.out.println();
        }
    }
}