import java.util.Scanner;

public class Main {

    static int sumaDivisores(int n) {
        int suma = 0;

        for (int i = 1; i < n; i++)
            if (n % i == 0)
                suma += i;

        return suma;
    }

    static boolean amigos(int a, int b) {
        return sumaDivisores(a) == b && sumaDivisores(b) == a;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println(amigos(sc.nextInt(), sc.nextInt()));
    }
}