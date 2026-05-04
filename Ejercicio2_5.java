import javax.swing.*;

public class Ejercicio2_5 extends JFrame {
    public Ejercicio2_5() {
        JTextField input = new JTextField(10);
        JLabel resultado = new JLabel("Resultado:");
        JButton boton = new JButton("Verificar");

        boton.addActionListener(e -> {
            try {
                double num = Double.parseDouble(input.getText());

                if (num != 0 && Math.abs(num) < 1) {
                    resultado.setText("Es casi-cero");
                } else {
                    resultado.setText("No es casi-cero");
                }
            } catch (Exception ex) {
                resultado.setText("Entrada inválida");
            }
        });

        JPanel p = new JPanel();
        p.add(new JLabel("Número:"));
        p.add(input);
        p.add(boton);
        p.add(resultado);

        add(p);
        setSize(300,120);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Ejercicio2_5();
    }
}