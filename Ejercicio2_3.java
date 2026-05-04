import javax.swing.*;

public class Ejercicio2_3 extends JFrame {
    public Ejercicio2_3() {
        JTextField a = new JTextField(5);
        JTextField b = new JTextField(5);
        JLabel resultado = new JLabel("Resultado:");
        JButton boton = new JButton("Calcular");

        boton.addActionListener(e -> {
            try {
                int n1 = Integer.parseInt(a.getText());
                int n2 = Integer.parseInt(b.getText());

                resultado.setText("Mayor: " + (n1 > n2 ? n1 : n2));
            } catch (Exception ex) {
                resultado.setText("Entrada inválida");
            }
        });

        JPanel p = new JPanel();
        p.add(new JLabel("N1:")); p.add(a);
        p.add(new JLabel("N2:")); p.add(b);
        p.add(boton);
        p.add(resultado);

        add(p);
        setSize(300,120);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Ejercicio2_3();
    }
}