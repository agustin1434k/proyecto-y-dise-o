import java.util.Scanner;

public class Main {

    static void mostrar(int a, int b) {
        if (a > b) {
            int aux = a;
            a = b;
            b = aux;
        }

        for (int i = a; i <= b; i++) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        mostrar(sc.nextInt(), sc.nextInt());
    }
}