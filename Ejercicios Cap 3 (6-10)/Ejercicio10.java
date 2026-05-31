package ejercicio3_10swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_10Swing extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;

    public Ejercicio3_10Swing() {
        setTitle("Ejercicio 3.10 - Tablas de Multiplicar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        JButton btnCalcular = new JButton("Generar Tabla");
        outputArea = new JTextArea(14, 30);
        outputArea.setEditable(false);
        outputArea.append("Introduce un número del 1 al 10 para ver su tabla.\n\n");

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(inputField.getText());
                    outputArea.setText(""); 

                    if (num < 1 || num > 10) {
                        outputArea.append("Error: El número debe estar entre 1 y 10.\n");
                    } else {
                        outputArea.append("Tabla de multiplicar del " + num + ":\n");
                        outputArea.append("-----------------------------\n");
                        for (int i = 1; i <= 10; i++) {
                            outputArea.append(num + " x " + i + " = " + (num * i) + "\n");
                        }
                    }
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce un número entero válido.\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Número (1-10):"));
        inputPanel.add(inputField);
        inputPanel.add(btnCalcular);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputPanel);
        mainPanel.add(scrollPane);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Ejercicio3_10Swing().setVisible(true);
        });
    }
}