import java.util.Arrays;
import java.util.Scanner;

public class Ejercicio612 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese la primera palabra: ");
        String palabra1 = sc.nextLine().toLowerCase().replace(" ", "");

        System.out.print("Ingrese la segunda palabra: ");
        String palabra2 = sc.nextLine().toLowerCase().replace(" ", "");

        if (palabra1.length() != palabra2.length()) {

            System.out.println("No son anagramas.");

        } else {

            char[] letras1 = palabra1.toCharArray();
            char[] letras2 = palabra2.toCharArray();

            Arrays.sort(letras1);
            Arrays.sort(letras2);

            if (Arrays.equals(letras1, letras2)) {
                System.out.println("Las palabras son anagramas.");
            } else {
                System.out.println("Las palabras NO son anagramas.");
            }

        }

        sc.close();
    }
}