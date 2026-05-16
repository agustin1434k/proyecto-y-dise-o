import java.util.Scanner;

public class Ejercicio2_12 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int dia, mes, año;

        System.out.print("Día: ");
        dia = teclado.nextInt();

        System.out.print("Mes: ");
        mes = teclado.nextInt();

        System.out.print("Año: ");
        año = teclado.nextInt();

        boolean correcta = false;

        if (mes >= 1 && mes <= 12) {

            if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia >= 1 && dia <= 30) {
                correcta = true;

            } else if (mes == 2 && dia >= 1 && dia <= 28) {
                correcta = true;

            } else if (dia >= 1 && dia <= 31) {
                correcta = true;
            }
        }

        if (correcta) {
            System.out.println("Fecha correcta");
        } else {
            System.out.println("Fecha incorrecta");
        }
    }
}