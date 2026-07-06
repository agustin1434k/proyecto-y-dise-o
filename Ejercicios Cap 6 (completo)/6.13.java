import java.util.Scanner;

public class Ejercicio613 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese una frase: ");
        String frase = sc.nextLine().toLowerCase();

        int[] contador = new int[26];

        for (int i = 0; i < frase.length(); i++) {

            char letra = frase.charAt(i);

            if (letra >= 'a' && letra <= 'z') {
                contador[letra - 'a']++;
            }

        }

        System.out.println("\nCantidad de veces que aparece cada letra:");

        for (int i = 0; i < contador.length; i++) {

            if (contador[i] > 0) {
                System.out.println((char) (i + 'a') + ": " + contador[i] + " veces");
            }

        }

        sc.close();
    }
}