import java.util.Scanner;

public class Ejercicio2_10 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int n, original, invertido = 0, resto;

        System.out.print("Ingrese un número: ");
        n = teclado.nextInt();

        original = n;

        while (n > 0) {

            resto = n % 10;
            invertido = invertido * 10 + resto;
            n = n / 10;
        }

        if (original == invertido) {
            System.out.println("Es capicúa");
        } else {
            System.out.println("No es capicúa");
        }
    }
}/