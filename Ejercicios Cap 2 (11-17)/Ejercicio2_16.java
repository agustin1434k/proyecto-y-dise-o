import java.util.Scanner;

public class Ejercicio2_16 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int n;

        System.out.print("Ingrese un número del 1 al 9: ");
        n = teclado.nextInt();

        switch (n) {

            case 1:
                System.out.println("Uno");
                break;

            case 2:
                System.out.println("Dos");
                break;

            case 3:
                System.out.println("Tres");
                break;

            case 4:
                System.out.println("Cuatro");
                break;

            case 5:
                System.out.println("Cinco");
                break;

            case 6:
                System.out.println("Seis");
                break;

            case 7:
                System.out.println("Siete");
                break;

            case 8:
                System.out.println("Ocho");
                break;

            case 9:
                System.out.println("Nueve");
                break;

            default:
                System.out.println("Número incorrecto");
        }
    }
}