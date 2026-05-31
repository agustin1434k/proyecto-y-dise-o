package ejercicio3_6swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_6Swing extends JFrame {

    private JTextArea outputArea;

    public Ejercicio3_6Swing() {
        setTitle("Ejercicio 3.6 - Múltiplos de 7");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnMostrar = new JButton("Mostrar Múltiplos");
        outputArea = new JTextArea(12, 30);
        outputArea.setEditable(false);

        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("Múltiplos de 7 menores que 100:\n\n");
                
                for (int i = 7; i < 100; i += 7) {
                    outputArea.append(i + " ");
                }
                outputArea.append("\n\n¡Proceso completado!");
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(btnMostrar);

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
            new Ejercicio3_6Swing().setVisible(true);
        });
    }
}