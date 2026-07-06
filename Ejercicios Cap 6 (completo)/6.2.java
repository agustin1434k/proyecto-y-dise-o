import java.util.Scanner;

public class Ejercicio62Version1 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Jugador 1, ingrese la contraseña: ");
        String contraseña = sc.nextLine();

        for (int i = 0; i < 30; i++) {
            System.out.println();
        }

        System.out.println("Jugador 2, intenta adivinar la contraseña.");
        System.out.println("Cantidad de caracteres: " + contraseña.length());
        System.out.println("Primer carácter: " + contraseña.charAt(0));
        System.out.println("Último carácter: " + contraseña.charAt(contraseña.length() - 1));

        String intento;

        do {
            System.out.print("Ingrese la contraseña: ");
            intento = sc.nextLine();

            if (!intento.equals(contraseña)) {
                System.out.println("Contraseña incorrecta.");
            }

        } while (!intento.equals(contraseña));

        System.out.println("¡Correcto! Adivinaste la contraseña.");

        sc.close();
    }
}


import java.util.Scanner;

public class Ejercicio62Version2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Jugador 1, ingrese la contraseña: ");
        String contraseña = sc.nextLine();

        for (int i = 0; i < 30; i++) {
            System.out.println();
        }

        System.out.println("Jugador 2, intenta adivinar la contraseña.");
        System.out.println("Cantidad de caracteres: " + contraseña.length());
        System.out.println("Primer carácter: " + contraseña.charAt(0));
        System.out.println("Último carácter: " + contraseña.charAt(contraseña.length() - 1));

        String intento;

        do {
            System.out.print("Ingrese una palabra: ");
            intento = sc.nextLine();

            if (!intento.equals(contraseña)) {

                if (intento.compareToIgnoreCase(contraseña) < 0) {
                    System.out.println("La palabra ingresada es MENOR alfabéticamente que la contraseña.");
                } else {
                    System.out.println("La palabra ingresada es MAYOR alfabéticamente que la contraseña.");
                }

            }

        } while (!intento.equalsIgnoreCase(contraseña));

        System.out.println("¡Correcto! Adivinaste la contraseña.");

        sc.close();
    }
}