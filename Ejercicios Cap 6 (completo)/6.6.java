import java.util.Scanner;

public class Ejercicio66 {

    public static String invertirCadena(String texto) {

        String invertida = "";

        for (int i = texto.length() - 1; i >= 0; i--) {
            invertida += texto.charAt(i);
        }

        return invertida;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese una frase: ");
        String frase = sc.nextLine();

        String resultado = invertirCadena(frase);

        System.out.println("Cadena invertida: " + resultado);

        sc.close();
    }
}