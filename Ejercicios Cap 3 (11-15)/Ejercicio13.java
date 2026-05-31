package ejercicios_cap3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio13Swing extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton submitButton;

    private int notasIngresadas = 0;
    private int aprobados = 0;
    private int condicionados = 0;
    private int desaprobados = 0;

    public Ejercicio13Swing() {
        setTitle("Ejercicio 13 - Estadísticas de 6 Notas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        submitButton = new JButton("Cargar Nota");
        outputArea = new JTextArea(12, 30);
        outputArea.setEditable(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int nota = Integer.parseInt(inputField.getText());
                    if (nota < 1 || nota > 10) {
                        outputArea.append("Error: Nota inválida (Rango 1-10).\n");
                        return;
                    }

                    notasIngresadas++;
                    
                    if (nota > 4) {
                        aprobados++;
                    } else if (nota == 4) {
                        condicionados++;
                    } else {
                        desaprobados++;
                    }

                    outputArea.append("Nota #" + notasIngresadas + " registrada: " + nota + "\n");
                    inputField.setText("");
                    inputField.requestFocus();

                    if (notasIngresadas == 6) {
                        outputArea.append("\n=== RESUMEN DE NOTAS ===\n");
                        outputArea.append("Aprobados (> 4): " + aprobados + "\n");
                        outputArea.append("Condicionados (= 4): " + condicionados + "\n");
                        outputArea.append("Desaprobados (< 4): " + desaprobados + "\n");

                        inputField.setEnabled(false);
                        submitButton.setEnabled(false);
                    }

                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Ingrese un número entero.\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nota (1-10):"));
        inputPanel.add(inputField);
        inputPanel.add(submitButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputPanel);
        mainPanel.add(new JScrollPane(outputArea));

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ejercicio13Swing().setVisible(true));
    }
}