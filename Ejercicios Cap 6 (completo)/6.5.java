import java.util.Scanner;

public class Ejercicio65 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese su nombre completo: ");
        String nombre = sc.nextLine();

        String resultado = "";

        for (int i = 0; i < nombre.length(); i++) {

            char letra = nombre.charAt(i);

            if (letra != 'a' && letra != 'e' && letra != 'i' && letra != 'o' && letra != 'u' &&
                letra != 'A' && letra != 'E' && letra != 'I' && letra != 'O' && letra != 'U' &&
                letra != 'á' && letra != 'é' && letra != 'í' && letra != 'ó' && letra != 'ú' &&
                letra != 'Á' && letra != 'É' && letra != 'Í' && letra != 'Ó' && letra != 'Ú') {

                resultado += letra;
            }
        }

        System.out.println("Nombre sin vocales: " + resultado);

        sc.close();
    }
}