import java.util.Scanner;

public class Main {

    static boolean esPrimo(int n) {
        if (n < 2) return false;

        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;

        return true;
    }

    static void divisoresPrimos(int n) {
        for (int i = 2; i <= n; i++)
            if (esPrimo(i) && n % i == 0)
                System.out.println(i);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        divisoresPrimos(sc.nextInt());
    }
}