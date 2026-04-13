package ejercicio1_7swing;

import javax.swing.*;

public class Ejercicio1_7Swing extends JFrame {

    public Ejercicio1_7Swing() {
        JTextField input = new JTextField(10);
        JButton boton = new JButton("Redondear");
        JLabel resultado = new JLabel();

        boton.addActionListener(e -> {
            double num = Double.parseDouble(input.getText());
            resultado.setText("Resultado: " + Math.round(num));
        });

        JPanel panel = new JPanel();
        panel.add(input);
        panel.add(boton);
        panel.add(resultado);

        add(panel);
        setSize(300, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Ejercicio1_7Swing();
    }
}