import java.util.Scanner;

public class Ejercicio2_11 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int nota;

        System.out.print("Ingrese una nota: ");
        nota = teclado.nextInt();

        if (nota <= 4) {
            System.out.println("Insuficiente");
        } else if (nota == 5) {
            System.out.println("Suficiente");
        } else if (nota == 6) {
            System.out.println("Bien");
        } else if (nota == 7 || nota == 8) {
            System.out.println("Notable");
        } else {
            System.out.println("Sobresaliente");
        }
    }
}