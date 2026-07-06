import java.util.Scanner;

public class Ejercicio67 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese una frase: ");
        String frase = sc.nextLine();

        System.out.print("Ingrese la palabra a buscar: ");
        String palabra = sc.nextLine();

        String[] palabras = frase.split(" ");

        int contador = 0;

        for (int i = 0; i < palabras.length; i++) {

            if (palabras[i].equalsIgnoreCase(palabra)) {
                contador++;
            }

        }

        System.out.println("La palabra \"" + palabra + "\" aparece " + contador + " veces.");

        sc.close();
    }
}