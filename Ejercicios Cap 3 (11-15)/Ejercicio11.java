package ejercicios_cap3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio11Swing extends JFrame {
    private JTextArea outputArea;

    public Ejercicio11Swing() {
        setTitle("Ejercicio 11 - Tablas de Multiplicar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton showButton = new JButton("Mostrar Tablas del 1 al 10");
        outputArea = new JTextArea(20, 30);
        outputArea.setEditable(false);

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText(""); 
                for (int i = 1; i <= 10; i++) {
                    outputArea.append("=== TABLA DEL " + i + " ===\n");
                    for (int j = 1; j <= 10; j++) {
                        outputArea.append(i + " x " + j + " = " + (i * j) + "\n");
                    }
                    outputArea.append("\n"); 
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(showButton);
        panel.add(new JScrollPane(outputArea));

        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ejercicio11Swing().setVisible(true));
    }
}