import java.util.Scanner;

public class Ejercicio68 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese una frase: ");
        String frase = sc.nextLine();

        // Pasar a minúsculas y eliminar espacios
        frase = frase.toLowerCase().replace(" ", "");

        String invertida = "";

        for (int i = frase.length() - 1; i >= 0; i--) {
            invertida += frase.charAt(i);
        }

        if (frase.equals(invertida)) {
            System.out.println("La frase es palíndroma.");
        } else {
            System.out.println("La frase NO es palíndroma.");
        }

        sc.close();
    }
}