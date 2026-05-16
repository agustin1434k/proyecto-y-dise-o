import java.util.Scanner;

public class Ejercicio2_8 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        double a, b, c, d, x1, x2;

        System.out.print("Ingrese a: ");
        a = teclado.nextDouble();

        System.out.print("Ingrese b: ");
        b = teclado.nextDouble();

        System.out.print("Ingrese c: ");
        c = teclado.nextDouble();

        d = (b * b) - (4 * a * c);

        if (d < 0) {

            System.out.println("No tiene soluciones reales");

        } else {

            x1 = (-b + Math.sqrt(d)) / (2 * a);
            x2 = (-b - Math.sqrt(d)) / (2 * a);

            System.out.println("x1 = " + x1);
            System.out.println("x2 = " + x2);
        }
    }
}