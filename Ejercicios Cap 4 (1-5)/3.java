import java.util.Scanner;

public class Main {

    static void areaVolumen(double radio, double altura, int opcion) {
        if (opcion == 1)
            System.out.println(2 * Math.PI * radio * (altura + radio));
        else if (opcion == 2)
            System.out.println(Math.PI * radio * radio * altura);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        areaVolumen(sc.nextDouble(), sc.nextDouble(), sc.nextInt());
    }
}