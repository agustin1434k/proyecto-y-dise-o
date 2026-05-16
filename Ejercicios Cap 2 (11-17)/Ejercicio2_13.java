import java.util.Scanner;

public class Ejercicio2_13 {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int h, m, s;

        System.out.print("Hora: ");
        h = teclado.nextInt();

        System.out.print("Minutos: ");
        m = teclado.nextInt();

        System.out.print("Segundos: ");
        s = teclado.nextInt();

        s++;

        if (s == 60) {
            s = 0;
            m++;
        }

        if (m == 60) {
            m = 0;
            h++;
        }

        if (h == 24) {
            h = 0;
        }

        System.out.println(h + ":" + m + ":" + s);
    }
}