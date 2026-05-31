package ejercicio3_1swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_1Swing extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private JButton submitButton;

    public Ejercicio3_1Swing() {
        setTitle("Ejercicio 3.1 - Analizador de Números");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        submitButton = new JButton("Introducir número");
        outputArea = new JTextArea(12, 35);
        outputArea.setEditable(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(inputField.getText());
                    
                    if (num == 0) {
                        outputArea.append("Se ha introducido un 0. Proceso finalizado.\n");
                        inputField.setEnabled(false);
                        submitButton.setEnabled(false);
                    } else {
                        boolean esPar = num % 2 == 0;
                        boolean esPositivo = num >= 0;
                        
                        outputArea.append("Número: " + num + "\n");
                        outputArea.append("¿Es Par?: " + (esPar ? "Sí" : "No") + "\n");
                        outputArea.append("¿Es Positivo?: " + (esPositivo ? "Sí" : "No") + "\n");
                        outputArea.append("Cuadrado: " + (num * num) + "\n");
                        outputArea.append("-----------------------------\n");
                    }
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce un número válido\n\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Número:"));
        inputPanel.add(inputField);
        inputPanel.add(submitButton);

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
            new Ejercicio3_1Swing().setVisible(true);
        });
    }
}