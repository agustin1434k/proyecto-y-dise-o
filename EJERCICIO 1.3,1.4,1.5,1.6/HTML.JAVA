import java.util.Scanner;

public class Ejercicio1_3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Año actual: ");
        int anioActual = sc.nextInt();

        System.out.print("Año de nacimiento: ");
        int anioNacimiento = sc.nextInt();

        System.out.println("Tu edad es: " + (anioActual - anioNacimiento));
    }
}


public class Ejercicio1_4 {
    public static void main(String[] args) {
        short max = 32767;
        short min = -32768;

        short siguienteMax = (short)(max + 1); // después del máximo
        short anteriorMin = (short)(min - 1); // antes del mínimo

        System.out.println("Máximo de short: " + max);
        System.out.println("Siguiente al máximo: " + siguienteMax);
        System.out.println("Mínimo de short: " + min);
        System.out.println("Anterior al mínimo: " + anteriorMin);
    }
}


public class Ejercicio1_5 {
    public static void main(String[] args) {
        // Creamos el escáner para leer datos del usuario
        Scanner sc = new Scanner(System.in);

        // 1. Definimos las notas como enteras (int)
        System.out.print("Introduce la primera nota (entera): ");
        int nota1 = sc.nextInt();

        System.out.print("Introduce la segunda nota (entera): ");
        int nota2 = sc.nextInt();

        // 2. Calculamos la media. 
        // IMPORTANTE: Dividimos por 2.0 para que Java entienda que queremos decimales (double).
        // Si dividiéramos solo por 2, se perderían los decimales.
        double media = (nota1 + nota2) / 2.0;

        // 3. Mostramos los resultados
        System.out.println("--- Resultados ---");
        System.out.println("Nota 1: " + nota1);
        System.out.println("Nota 2: " + nota2);
        System.out.println("La media aritmética es: " + media);
        
        sc.close();
    }
}


public class Ejercicio1_6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Definimos las notas como decimales (double)
        System.out.print("Introduce la nota 1 (con decimales): ");
        double n1 = sc.nextDouble();

        System.out.print("Introduce la nota 2 (con decimales): ");
        double n2 = sc.nextDouble();

        System.out.print("Introduce la nota 3 (con decimales): ");
        double n3 = sc.nextDouble();

        // 2. Calculamos la media real
        double mediaCompleta = (n1 + n2 + n3) / 3.0;

        // 3. Obtenemos la parte entera usando un "cast" a int
        // Esto trunca los decimales (no redondea, simplemente los corta)
        int parteEntera = (int) mediaCompleta;

        // 4. Mostramos los resultados
        System.out.println("Media real: " + mediaCompleta);
        System.out.println("Parte entera de la media: " + parteEntera);

        sc.close();
    }
}