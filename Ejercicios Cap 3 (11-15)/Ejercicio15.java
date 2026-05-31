package ejercicios_cap3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio15Swing extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;

    public Ejercicio15Swing() {
        setTitle("Ejercicio 15 - Contador de Primos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        JButton calculateButton = new JButton("Calcular Primos");
        outputArea = new JTextArea(15, 30);
        outputArea.setEditable(false);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    outputArea.setText(""); 
                    int n = Integer.parseInt(inputField.getText());

                    if (n < 1) {
                        outputArea.append("Introduce un número mayor o igual a 1.\n");
                        return;
                    }

                    int totalPrimos = 0;

                    for (int i = 1; i <= n; i++) {
                        if (esPrimo(i)) {
                            outputArea.append(i + " --+ primo\n");
                            totalPrimos++;
                        } else {
                            outputArea.append(i + " --+ no primo\n");
                        }
                    }

                    outputArea.append("\nResultando un total de " + totalPrimos + " números primos.\n");

                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Ingresa un número entero válido.\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Número (n):"));
        inputPanel.add(inputField);
        inputPanel.add(calculateButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputPanel);
        mainPanel.add(new JScrollPane(outputArea));

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private boolean esPrimo(int numero) {
        if (numero <= 1) return false; 
        if (numero == 2) return true;  
        if (numero % 2 == 0) return false; 

        for (int i = 3; i * i <= numero; i += 2) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ejercicio15Swing().setVisible(true));
    }
}