package ejercicio3_8swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_8Swing extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;

    public Ejercicio3_8Swing() {
        setTitle("Ejercicio 3.8 - Calcular Factorial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        JButton btnCalcular = new JButton("Calcular Factorial");
        outputArea = new JTextArea(10, 35);
        outputArea.setEditable(false);

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(inputField.getText());
                    outputArea.setText("");

                    if (num < 0) {
                        outputArea.append("Error: No existe el factorial de un número negativo.\n");
                    } else {
                        long factorial = 1;
                        for (int i = 1; i <= num; i++) {
                            factorial *= i;
                        }
                        outputArea.append("El factorial de " + num + " (" + num + "!) es:\n");
                        outputArea.append(factorial + "\n");
                    }
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce un número entero válido.\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Número:"));
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
            new Ejercicio3_8Swing().setVisible(true);
        });
    }
}