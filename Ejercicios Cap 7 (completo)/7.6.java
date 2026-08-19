// 7.6. Diseñar la clase Texto que gestiona una cadena de caracteres con
// algunas características:
// La cadena de caracteres tendrá una longitud máxima, que se especifica
// en el constructor.
// Permite añadir un carácter, al principio o al final, siempre y cuando
// exista espacio disponible.
// Igualmente, permite añadir una cadena, al principio o al final del texto,
// siempre y cuando no se rebase el tamaño máximo establecido.
// Es necesario saber cuántas vocales (mayúsculas y minúsculas) hay en el texto.

class Texto {

    private String cadena;
    private int longitudMaxima;

    // Constructor
    public Texto(int longitudMaxima) {
        this.longitudMaxima = longitudMaxima;
        cadena = "";
    }

    // Añadir un carácter al principio
    public boolean añadirPrincipio(char caracter) {

        if (cadena.length() < longitudMaxima) {
            cadena = caracter + cadena;
            return true;
        }

        return false;
    }

    // Añadir un carácter al final
    public boolean añadirFinal(char caracter) {

        if (cadena.length() < longitudMaxima) {
            cadena = cadena + caracter;
            return true;
        }

        return false;
    }

    // Añadir una cadena al principio
    public boolean añadirPrincipio(String texto) {

        if (cadena.length() + texto.length() <= longitudMaxima) {
            cadena = texto + cadena;
            return true;
        }

        return false;
    }

    // Añadir una cadena al final
    public boolean añadirFinal(String texto) {

        if (cadena.length() + texto.length() <= longitudMaxima) {
            cadena = cadena + texto;
            return true;
        }

        return false;
    }

    // Contar vocales
    public int contarVocales() {

        int contador = 0;

        for (int i = 0; i < cadena.length(); i++) {

            char letra = Character.toLowerCase(cadena.charAt(i));

            if (letra == 'a' ||
                letra == 'e' ||
                letra == 'i' ||
                letra == 'o' ||
                letra == 'u') {

                contador++;
            }
        }

        return contador;
    }

    // Mostrar el texto
    public void mostrar() {
        System.out.println("Texto: " + cadena);
        System.out.println("Longitud: " + cadena.length());
        System.out.println("Vocales: " + contarVocales());
    }
}

public class Main {

    public static void main(String[] args) {

        // Creamos un texto con una longitud máxima de 20 caracteres
        Texto texto = new Texto(20);

        // Añadimos una cadena al final
        texto.añadirFinal("Hola");

        // Añadimos un carácter al final
        texto.añadirFinal(' ');

        // Añadimos otra cadena al final
        texto.añadirFinal("Agustin");

        System.out.println("Texto inicial:");
        texto.mostrar();

        // Añadimos una cadena al principio
        System.out.println("\nAñadimos \"Java \" al principio:");

        texto.añadirPrincipio("Java ");

        texto.mostrar();

        // Añadimos un carácter al final
        System.out.println("\nAñadimos ! al final:");

        texto.añadirFinal('!');

        texto.mostrar();

        // Comprobamos el límite de caracteres
        System.out.println("\nIntentamos añadir una cadena demasiado larga:");

        if (texto.añadirFinal(" Esto es demasiado largo")) {
            System.out.println("Cadena añadida.");
        } else {
            System.out.println("No se puede añadir: supera la longitud máxima.");
        }

        texto.mostrar();
    }
}