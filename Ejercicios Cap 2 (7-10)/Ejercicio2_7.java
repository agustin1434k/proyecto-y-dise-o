import java.util.Scanner;

public class Ejercicio2_7 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int a, b, c;

        System.out.print("Ingrese el primer número: ");
        a = teclado.nextInt();

        System.out.print("Ingrese el segundo número: ");
        b = teclado.nextInt();

        System.out.print("Ingrese el tercer número: ");
        c = teclado.nextInt();

        if (a >= b && a >= c) {

            if (b >= c) {
                System.out.println(a + " " + b + " " + c);
            } else {
                System.out.println(a + " " + c + " " + b);
            }

        } else if (b >= a && b >= c) {

            if (a >= c) {
                System.out.println(b + " " + a + " " + c);
            } else {
                System.out.println(b + " " + c + " " + a);
            }

        } else {

            if (a >= b) {
                System.out.println(c + " " + a + " " + b);
            } else {
                System.out.println(c + " " + b + " " + a);
            }
        }
    }
}