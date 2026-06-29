import java.util.Scanner;

public class Main {

    static boolean esPrimo(int n) {
        if (n < 2) return false;

        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;

        return true;
    }

    static int divisoresPrimos(int n) {
        int cont = 0;

        for (int i = 2; i <= n; i++)
            if (esPrimo(i) && n % i == 0)
                cont++;

        return cont;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println(divisoresPrimos(sc.nextInt()));
    }
}