import javax.swing.*;

public class Ejercicio2_1 extends JFrame {
    public Ejercicio2_1() {
        JTextField input = new JTextField(10);
        JLabel resultado = new JLabel("Resultado:");
        JButton boton = new JButton("Verificar");

        boton.addActionListener(e -> {
            try {
                int num = Integer.parseInt(input.getText());
                if (num % 2 == 0) {
                    resultado.setText("Es PAR");
                } else {
                    resultado.setText("Es IMPAR");
                }
            } catch (Exception ex) {
                resultado.setText("Entrada inválida");
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Número:"));
        panel.add(input);
        panel.add(boton);
        panel.add(resultado);

        add(panel);
        setSize(300,120);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Ejercicio2_1();
    }
}