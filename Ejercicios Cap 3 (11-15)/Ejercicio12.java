package ejercicios_cap3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio12Swing extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton submitButton;
    
    private int contadorNotas = 0;
    private boolean hayDesaprobados = false;

    public Ejercicio12Swing() {
        setTitle("Ejercicio 12 - Control de 5 Notas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        submitButton = new JButton("Ingresar Nota");
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double nota = Double.parseDouble(inputField.getText());
                    
                    if (nota < 1 || nota > 10) {
                        outputArea.append("Error: La nota debe estar entre 1 y 10.\n");
                        return;
                    }

                    contadorNotas++;
                    outputArea.append("Nota " + contadorNotas + ": " + nota + "\n");

                    if (nota < 4) {
                        hayDesaprobados = true;
                    }

                    inputField.setText("");
                    inputField.requestFocus();

                    if (contadorNotas == 5) {
                        outputArea.append("\n=== RESULTADO FINAL ===\n");
                        if (hayDesaprobados) {
                            outputArea.append("ATENCIÓN: Sí hay alumnos desaprobados (nota < 4).\n");
                        } else {
                            outputArea.append("¡Buenas noticias! No hay ningún alumno desaprobado.\n");
                        }
                        
                        inputField.setEnabled(false);
                        submitButton.setEnabled(false);
                    }

                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce una nota numérica válida.\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nota:"));
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
        SwingUtilities.invokeLater(() -> new Ejercicio12Swing().setVisible(true));
    }
}