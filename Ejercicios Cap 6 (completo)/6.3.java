import java.util.Scanner;

public class Ejercicio63 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String palabra;
        String frase = "";

        System.out.println("Ingrese palabras para formar una frase.");
        System.out.println("Escriba 'fin' para terminar.");

        do {
            System.out.print("Palabra: ");
            palabra = sc.nextLine();

            if (!palabra.equalsIgnoreCase("fin")) {
                frase += palabra + " ";
            }

        } while (!palabra.equalsIgnoreCase("fin"));

        System.out.println("\nFrase completa:");
        System.out.println(frase.trim());

        sc.close();
    }
}