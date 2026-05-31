package ejercicio3_7swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_7Swing extends JFrame {

    private JTextArea outputArea;

    public Ejercicio3_7Swing() {
        setTitle("Ejercicio 3.7 - Producto de Impares");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnCalcular = new JButton("Calcular Producto");
        outputArea = new JTextArea(10, 35);
        outputArea.setEditable(false);

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
                long producto = 1;
                int contadorImpares = 0;
                int numeroActual = 1;

                outputArea.append("Multiplicando los primeros 10 impares:\n");
                
                while (contadorImpares < 10) {
                    producto *= numeroActual;
                    outputArea.append(numeroActual + (contadorImpares < 9 ? " x " : " = "));
                    numeroActual += 2; 
                    contadorImpares++;
                }
                
                outputArea.append("\n\nResultado final: " + producto);
            }
        });

        JPanel inputPanel = new JPanel();
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
            new Ejercicio3_7Swing().setVisible(true);
        });
    }
}