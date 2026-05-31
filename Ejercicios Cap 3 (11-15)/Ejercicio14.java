package ejercicios_cap3;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio14Swing extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;

    public Ejercicio14Swing() {
        setTitle("Ejercicio 14 - Triángulo de Asteriscos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        JButton drawButton = new JButton("Dibujar Triángulo");
        outputArea = new JTextArea(15, 30);
        outputArea.setEditable(false);
        
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    outputArea.setText(""); 
                    int n = Integer.parseInt(inputField.getText());

                    if (n <= 0) {
                        outputArea.setText("Por favor, introduce un número mayor a 0.");
                        return;
                    }

                    for (int fila = n; fila >= 1; fila--) {
                        for (int columna = 1; columna <= fila; columna++) {
                            outputArea.append("* ");
                        }
                        outputArea.append("\n"); 
                    }

                } catch (NumberFormatException ex) {
                    outputArea.setText("Error: Introduce un número entero válido.");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Lado (n):"));
        inputPanel.add(inputField);
        inputPanel.add(drawButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputPanel);
        mainPanel.add(new JScrollPane(outputArea));

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ejercicio14Swing().setVisible(true));
    }
}