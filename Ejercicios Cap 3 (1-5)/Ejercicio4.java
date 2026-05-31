package ejercicio3_4swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_4Swing extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;

    public Ejercicio3_4Swing() {
        setTitle("Ejercicio 3.4 - Aprender a Contar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        JButton submitButton = new JButton("Contar");
        outputArea = new JTextArea(15, 35);
        outputArea.setEditable(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int n = Integer.parseInt(inputField.getText());
                    outputArea.setText(""); 
                    
                    if (n < 1) {
                        outputArea.append("Por favor, ingresa un número mayor o igual a 1.\n");
                    } else {
                        outputArea.append("Contando desde 1 hasta " + n + ":\n");
                        for (int i = 1; i <= n; i++) {
                            outputArea.append(i + " ");
                            
                            if (i % 10 == 0) outputArea.append("\n"); 
                        }
                        outputArea.append("\n\n¡Listo!");
                    }
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce un número entero válido\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Valor de n:"));
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
            new Ejercicio3_4Swing().setVisible(true);
        });
    }
}