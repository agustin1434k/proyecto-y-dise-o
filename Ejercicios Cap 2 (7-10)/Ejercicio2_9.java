import java.util.Scanner;

public class Ejercicio2_9 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int n;

        System.out.print("Ingrese un número: ");
        n = teclado.nextInt();

        if (n < 10) {
            System.out.println("1 cifra");
        } else if (n < 100) {
            System.out.println("2 cifras");
        } else if (n < 1000) {
            System.out.println("3 cifras");
        } else if (n < 10000) {
            System.out.println("4 cifras");
        } else {
            System.out.println("5 cifras");
        }
    }
}