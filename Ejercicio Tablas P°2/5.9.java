/*
 * 5.9. Desarrollar una aplicación que permita gestionar las notas de un
 * grupo de 5 alumnos. Leer las notas del primer, segundo y tercer
 * trimestre. Mostrar el promedio del grupo en cada trimestre y el
 * promedio del alumno indicado por el usuario.
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        final int ALUMNOS = 5;

        int primer[] = new int[ALUMNOS];
        int segundo[] = new int[ALUMNOS];
        int tercero[] = new int[ALUMNOS];

        int suma1 = 0;
        int suma2 = 0;
        int suma3 = 0;

        for (int i = 0; i < ALUMNOS; i++) {

            System.out.println("Alumno " + (i + 1));

            System.out.print("Nota 1° trimestre: ");
            primer[i] = sc.nextInt();
            suma1 += primer[i];

            System.out.print("Nota 2° trimestre: ");
            segundo[i] = sc.nextInt();
            suma2 += segundo[i];

            System.out.print("Nota 3° trimestre: ");
            tercero[i] = sc.nextInt();
            suma3 += tercero[i];
        }

        System.out.println("\nPromedio del grupo:");

        System.out.println("Primer trimestre: " + (double) suma1 / ALUMNOS);
        System.out.println("Segundo trimestre: " + (double) suma2 / ALUMNOS);
        System.out.println("Tercer trimestre: " + (double) suma3 / ALUMNOS);

        System.out.print("\nIngrese la posición del alumno (1-5): ");
        int pos = sc.nextInt();

        if (pos >= 1 && pos <= ALUMNOS) {

            double promedio = (primer[pos - 1] + segundo[pos - 1] + tercero[pos - 1]) / 3.0;

            System.out.println("Promedio del alumno " + pos + ": " + promedio);

        } else {

            System.out.println("Posición inválida.");

        }

        sc.close();
    }
}