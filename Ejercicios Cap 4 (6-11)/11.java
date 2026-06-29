import java.util.Scanner;

public class Main {

    static double potencia(double a, int n) {
        double res = 1;

        for (int i = 0; i < n; i++)
            res *= a;

        return res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println(potencia(sc.nextDouble(), sc.nextInt()));
    }
}