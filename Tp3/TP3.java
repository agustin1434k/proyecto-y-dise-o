package Tp3;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TP3 extends JFrame {

    public TP3() {

        setTitle("TP3 Estructuras Repetitivas");
        setSize(500, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(15, 1));

JButton b31 = new JButton("Ingresar números (par/impar/cuadrado)");
JButton b32 = new JButton("Edades (suma, promedio, mayores)");
JButton b33 = new JButton("Adivinar número (1-100)");
JButton b34 = new JButton("Mostrar números hasta N");
JButton b35 = new JButton("Número dentro de rango");
JButton b36 = new JButton("Múltiplos de 7");
JButton b37 = new JButton("Producto primeros impares");
JButton b38 = new JButton("Factorial");
JButton b39 = new JButton("Árbol más alto");
JButton b310 = new JButton("Tabla de multiplicar");
JButton b311 = new JButton("Tablas del 1 al 10");
JButton b312 = new JButton("Detectar desaprobados");
JButton b313 = new JButton("Positivos / Negativos / Ceros");
JButton b314 = new JButton("Números primos hasta N");
JButton b315 = new JButton("Contar primos hasta N");

        add(b31); add(b32); add(b33); add(b34); add(b35);
        add(b36); add(b37); add(b38); add(b39); add(b310);
        add(b311); add(b312); add(b313); add(b314); add(b315);

        b31.addActionListener(e -> ejercicio31());
        b32.addActionListener(e -> ejercicio32());
        b33.addActionListener(e -> ejercicio33());
        b34.addActionListener(e -> ejercicio34());
        b35.addActionListener(e -> ejercicio35());
        b36.addActionListener(e -> ejercicio36());
        b37.addActionListener(e -> ejercicio37());
        b38.addActionListener(e -> ejercicio38());
        b39.addActionListener(e -> ejercicio39());
        b310.addActionListener(e -> ejercicio310());
        b311.addActionListener(e -> ejercicio311());
        b312.addActionListener(e -> ejercicio312());
        b313.addActionListener(e -> ejercicio313());
        b314.addActionListener(e -> ejercicio314());
        b315.addActionListener(e -> ejercicio315());
    }

    public static void main(String[] args) {
        new TP3().setVisible(true);
    }

    // =========================
    // EJERCICIOS
    // =========================

    void ejercicio31() {
        int n;
        String r = "";

        do {
            n = Integer.parseInt(JOptionPane.showInputDialog("Número (0 salir)"));

            if (n != 0) {
                r += (n % 2 == 0 ? "Par" : "Impar") + "\n";
                r += (n > 0 ? "Positivo" : "Negativo") + "\n";
                r += "Cuadrado: " + (n * n) + "\n\n";
            }
        } while (n != 0);

        JOptionPane.showMessageDialog(null, r);
    }

    void ejercicio32() {
        int e, s = 0, c = 0, m = 0;

        do {
            e = Integer.parseInt(JOptionPane.showInputDialog("Edad (-1 salir)"));

            if (e >= 0) {
                s += e;
                c++;
                if (e >= 18) m++;
            }
        } while (e >= 0);

        JOptionPane.showMessageDialog(null,
                "Suma: " + s +
                "\nPromedio: " + (c == 0 ? 0 : (double) s / c) +
                "\nCantidad: " + c +
                "\nMayores: " + m);
    }

    void ejercicio33() {
        int secreto = new Random().nextInt(100) + 1;
        int n;

        do {
            n = Integer.parseInt(JOptionPane.showInputDialog("Adivina 1-100"));

            if (n < secreto) JOptionPane.showMessageDialog(null, "Más alto");
            if (n > secreto) JOptionPane.showMessageDialog(null, "Más bajo");

        } while (n != secreto);

        JOptionPane.showMessageDialog(null, "Ganaste");
    }

    void ejercicio34() {
        int n = Integer.parseInt(JOptionPane.showInputDialog("Número"));
        String r = "";

        for (int i = 1; i <= n; i++) r += i + "\n";

        JOptionPane.showMessageDialog(null, r);
    }

    void ejercicio35() {
        int n;
        do {
            n = Integer.parseInt(JOptionPane.showInputDialog("Entre 10 y 20"));
        } while (n < 10 || n > 20);

        JOptionPane.showMessageDialog(null, "OK " + n);
    }

    void ejercicio36() {
        String r = "";
        for (int i = 7; i < 100; i += 7) r += i + "\n";
        JOptionPane.showMessageDialog(null, r);
    }

    void ejercicio37() {
        int p = 1, n = 1, c = 0;

        while (c < 10) {
            if (n % 2 != 0) {
                p *= n;
                c++;
            }
            n++;
        }

        JOptionPane.showMessageDialog(null, "Producto: " + p);
    }

    void ejercicio38() {
        int n = Integer.parseInt(JOptionPane.showInputDialog("Factorial"));
        int f = 1;

        for (int i = 1; i <= n; i++) f *= i;

        JOptionPane.showMessageDialog(null, f);
    }

    void ejercicio39() {
        int max = 0;

        for (int i = 1; i <= 5; i++) {
            int h = Integer.parseInt(JOptionPane.showInputDialog("Altura árbol " + i));
            if (h > max) max = h;
        }

        JOptionPane.showMessageDialog(null, "Más alto: " + max);
    }

    void ejercicio310() {
        int n = Integer.parseInt(JOptionPane.showInputDialog("Número"));
        String r = "";

        for (int i = 1; i <= 10; i++)
            r += n + " x " + i + " = " + (n * i) + "\n";

        JOptionPane.showMessageDialog(null, r);
    }

    void ejercicio311() {
        String r = "";

        for (int i = 1; i <= 10; i++) {
            r += "Tabla del " + i + "\n";
            for (int j = 1; j <= 10; j++) {
                r += i + " x " + j + " = " + (i * j) + "\n";
            }
            r += "\n";
        }

        JOptionPane.showMessageDialog(null, r);
    }

    void ejercicio312() {
        boolean desaprobado = false;

        for (int i = 1; i <= 5; i++) {
            int n = Integer.parseInt(JOptionPane.showInputDialog("Nota " + i));
            if (n < 6) desaprobado = true;
        }

        JOptionPane.showMessageDialog(null,
                desaprobado ? "Hay desaprobados" : "Todos aprobados");
    }

    void ejercicio313() {
        int pos = 0, neg = 0, cer = 0;

        for (int i = 0; i < 10; i++) {
            int n = Integer.parseInt(JOptionPane.showInputDialog("Número"));

            if (n > 0) pos++;
            else if (n < 0) neg++;
            else cer++;
        }

        JOptionPane.showMessageDialog(null,
                "Positivos: " + pos +
                "\nNegativos: " + neg +
                "\nCeros: " + cer);
    }

    void ejercicio314() {
        int n = Integer.parseInt(JOptionPane.showInputDialog("N"));
        String r = "";

        for (int i = 2; i <= n; i++) {
            if (esPrimo(i)) r += i + "\n";
        }

        JOptionPane.showMessageDialog(null, r);
    }

    void ejercicio315() {
        int n = Integer.parseInt(JOptionPane.showInputDialog("N"));
        int c = 0;

        for (int i = 1; i <= n; i++) {
            if (esPrimo(i)) c++;
        }

        JOptionPane.showMessageDialog(null, "Primos: " + c);
    }

    boolean esPrimo(int n) {
        if (n < 2) return false;

        for (int i = 2; i < n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}