import java.util.Scanner;

public class Ejercicio69 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese una frase: ");
        String frase = sc.nextLine();

        if (frase.startsWith("Javalín, javalón")) {

            String mensaje = frase.substring("Javalín, javalón".length()).trim();

            System.out.println("La frase pertenece a Javalandia.");
            System.out.println("Mensaje: " + mensaje);

        } else if (frase.endsWith("javalén, len, len")) {

            String mensaje = frase.substring(0,
                    frase.length() - "javalén, len, len".length()).trim();

            System.out.println("La frase pertenece a Javalandia.");
            System.out.println("Mensaje: " + mensaje);

        } else {

            System.out.println("La frase NO pertenece al idioma de Javalandia.");

        }

        sc.close();
    }
}