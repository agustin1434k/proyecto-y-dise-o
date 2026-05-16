import java.util.Scanner;

public class Ejercicio2_14 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int dia, mes, año;

        System.out.print("Día: ");
        dia = teclado.nextInt();

        System.out.print("Mes: ");
        mes = teclado.nextInt();

        System.out.print("Año: ");
        año = teclado.nextInt();

        dia++;

        if (dia > 30) {
            dia = 1;
            mes++;
        }

        if (mes > 12) {
            mes = 1;
            año++;
        }

        System.out.println(dia + "/" + mes + "/" + año);
    }
}