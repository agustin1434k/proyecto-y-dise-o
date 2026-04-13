package ejercicio1_10swing;

import javax.swing.*;

public class Ejercicio1_10Swing extends JFrame {

    public Ejercicio1_10Swing() {
        JTextField radio = new JTextField(5);
        JButton boton = new JButton("Calcular");
        JLabel resultado = new JLabel("Resultado: ");

        boton.addActionListener(e -> {
            double r = Double.parseDouble(radio.getText());

            double longitud = 2 * Math.PI * r;
            double area = Math.PI * r * r;

            resultado.setText("Longitud: " + longitud + " Área: " + area);
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Radio:"));
        panel.add(radio);
        panel.add(boton);
        panel.add(resultado);

        add(panel);
        setSize(350, 120);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Ejercicio1_10Swing();
    }
}