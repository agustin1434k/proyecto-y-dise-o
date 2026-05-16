import java.util.Scanner;

public class Ejercicio2_17 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        double comidaDiaria, kilosPorAnimal;
        int numAnimales;

        System.out.print("Comida diaria: ");
        comidaDiaria = teclado.nextDouble();

        System.out.print("Número de animales: ");
        numAnimales = teclado.nextInt();

        System.out.print("Kilos por animal: ");
        kilosPorAnimal = teclado.nextDouble();

        if (numAnimales == 0) {

            System.out.println("No hay animales");

        } else {

            double comidaNecesaria = numAnimales * kilosPorAnimal;

            if (comidaDiaria >= comidaNecesaria) {

                System.out.println("Hay comida suficiente");

            } else {

                System.out.println("Falta comida");
                System.out.println("Cada animal recibe: " + (comidaDiaria / numAnimales));
            }
        }
    }
}