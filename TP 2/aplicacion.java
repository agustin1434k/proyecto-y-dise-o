import javax.swing.*;
import java.awt.*;

public class CondicionalesSwing extends JFrame {

    JTextArea resultado;

    public CondicionalesSwing() {

        setTitle("Estructuras Condicionales");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JButton b1 = new JButton("1 Par o Impar");
        JButton b2 = new JButton("2 Iguales o Distintos");
        JButton b3 = new JButton("3 Numero Mayor");
        JButton b4 = new JButton("4 Igual o Mayor");
        JButton b5 = new JButton("5 Casi Cero");
        JButton b6 = new JButton("6 Mayor y Menor");
        JButton b7 = new JButton("7 Ordenar 3 Numeros");
        JButton b8 = new JButton("8 Clasificar Nota");
        JButton b9 = new JButton("9 Validar Fecha");

        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);
        panel.add(b5);
        panel.add(b6);
        panel.add(b7);
        panel.add(b8);
        panel.add(b9);

        resultado = new JTextArea();
        resultado.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(resultado), BorderLayout.CENTER);

        b1.addActionListener(e -> parImpar());
        b2.addActionListener(e -> igualesDistintos());
        b3.addActionListener(e -> numeroMayor());
        b4.addActionListener(e -> igualOMayor());
        b5.addActionListener(e -> casiCero());
        b6.addActionListener(e -> mayorMenor());
        b7.addActionListener(e -> ordenarTres());
        b8.addActionListener(e -> clasificarNota());
        b9.addActionListener(e -> validarFecha());
    }

    public void parImpar() {

        int n = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese un numero")
        );

        String r = (n % 2 == 0) ? "Par" : "Impar";

        resultado.setText(
                "El numero es: " + r
        );
    }

    public void igualesDistintos() {

        int a = Integer.parseInt(
                JOptionPane.showInputDialog("Primer numero")
        );

        int b = Integer.parseInt(
                JOptionPane.showInputDialog("Segundo numero")
        );

        if (a == b) {
            resultado.setText("Los numeros son iguales");
        } else {
            resultado.setText("Los numeros son distintos");
        }
    }

    public void numeroMayor() {

        int a = Integer.parseInt(
                JOptionPane.showInputDialog("Primer numero")
        );

        int b = Integer.parseInt(
                JOptionPane.showInputDialog("Segundo numero")
        );

        if (a > b) {
            resultado.setText("El mayor es: " + a);
        } else {
            resultado.setText("El mayor es: " + b);
        }
    }

    public void igualOMayor() {

        int a = Integer.parseInt(
                JOptionPane.showInputDialog("Primer numero")
        );

        int b = Integer.parseInt(
                JOptionPane.showInputDialog("Segundo numero")
        );

        if (a == b) {
            resultado.setText("Los numeros son iguales");
        } else if (a > b) {
            resultado.setText("El mayor es: " + a);
        } else {
            resultado.setText("El mayor es: " + b);
        }
    }

    public void casiCero() {

        double n = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese un numero decimal")
        );

        if (n > -1 && n < 1 && n != 0) {
            resultado.setText("Es casi cero");
        } else {
            resultado.setText("No es casi cero");
        }
    }

    public void mayorMenor() {

        int a = Integer.parseInt(
                JOptionPane.showInputDialog("Primer numero")
        );

        int b = Integer.parseInt(
                JOptionPane.showInputDialog("Segundo numero")
        );

        int mayor = (a > b) ? a : b;
        int menor = (a < b) ? a : b;

        resultado.setText(
                "Mayor: " + mayor +
                "\nMenor: " + menor
        );
    }

    public void ordenarTres() {

        int a = Integer.parseInt(
                JOptionPane.showInputDialog("Primer numero")
        );

        int b = Integer.parseInt(
                JOptionPane.showInputDialog("Segundo numero")
        );

        int c = Integer.parseInt(
                JOptionPane.showInputDialog("Tercer numero")
        );

        if (a > b && b > c) {
            resultado.setText(a + " > " + b + " > " + c);
        } else if (a > c && c > b) {
            resultado.setText(a + " > " + c + " > " + b);
        } else if (b > a && a > c) {
            resultado.setText(b + " > " + a + " > " + c);
        } else if (b > c && c > a) {
            resultado.setText(b + " > " + c + " > " + a);
        } else if (c > a && a > b) {
            resultado.setText(c + " > " + a + " > " + b);
        } else {
            resultado.setText(c + " > " + b + " > " + a);
        }
    }

    public void clasificarNota() {

        int nota = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese nota")
        );

        if (nota < 4) {
            resultado.setText("Insuficiente");
        } else if (nota < 6) {
            resultado.setText("Suficiente");
        } else if (nota < 8) {
            resultado.setText("Bien");
        } else if (nota < 10) {
            resultado.setText("Notable");
        } else {
            resultado.setText("Sobresaliente");
        }
    }

    public void validarFecha() {

        int dia = Integer.parseInt(
                JOptionPane.showInputDialog("Dia")
        );

        int mes = Integer.parseInt(
                JOptionPane.showInputDialog("Mes")
        );

        int anio = Integer.parseInt(
                JOptionPane.showInputDialog("Anio")
        );

        if (dia >= 1 && dia <= 31 &&
            mes >= 1 && mes <= 12 &&
            anio > 0) {

            resultado.setText("Fecha correcta");

        } else {

            resultado.setText("Fecha incorrecta");
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new CondicionalesSwing().setVisible(true);
        });
    }
}