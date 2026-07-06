import java.util.Scanner;

public class Ejercicio64 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese una frase: ");
        String frase = sc.nextLine();

        int espacios = 0;

        for (int i = 0; i < frase.length(); i++) {

            if (frase.charAt(i) == ' ') {
                espacios++;
            }

        }

        System.out.println("La frase tiene " + espacios + " espacios en blanco.");

        sc.close();
    }
}