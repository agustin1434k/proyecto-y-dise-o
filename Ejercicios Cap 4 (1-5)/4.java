import java.util.Scanner;

public class Main {

    static int maximo(int a, int b) {
        return (a > b) ? a : b;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println(maximo(sc.nextInt(), sc.nextInt()));
    }
}