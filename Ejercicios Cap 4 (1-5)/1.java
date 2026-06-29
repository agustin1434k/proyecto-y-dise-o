import java.util.Scanner;

public class Main {

    static void eco(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("Eco...");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        eco(sc.nextInt());
    }
}