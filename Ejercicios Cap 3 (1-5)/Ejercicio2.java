package ejercicio3_2swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_2Swing extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private JButton submitButton;
    
    private int sumaEdades = 0;
    private int contadorAlumnos = 0;
    private int mayoresDeEdad = 0;

    public Ejercicio3_2Swing() {
        setTitle("Ejercicio 3.2 - Estadísticas de Alumnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        submitButton = new JButton("Registrar Edad");
        outputArea = new JTextArea(12, 35);
        outputArea.setEditable(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int edad = Integer.parseInt(inputField.getText());
                    
                    if (edad < 0) {
                        outputArea.append("### PROCESO TERMINADO ###\n");
                        outputArea.append("Resultados Finales:\n");
                        outputArea.append("Suma total de edades: " + sumaEdades + "\n");
                        outputArea.append("Total de alumnos: " + contadorAlumnos + "\n");
                        
                        if (contadorAlumnos > 0) {
                            double media = (double) sumaEdades / contadorAlumnos;
                            outputArea.append("Media de edad: " + String.format("%.2f", media) + "\n");
                        } else {
                            outputArea.append("Media de edad: 0\n");
                        }
                        
                        outputArea.append("Alumnos mayores de edad (>=18): " + mayoresDeEdad + "\n");
                        
                        
                        inputField.setEnabled(false);
                        submitButton.setEnabled(false);
                    } else {
                        
                        sumaEdades += edad;
                        contadorAlumnos++;
                        if (edad >= 18) {
                            mayoresDeEdad++;
                        }
                        outputArea.append("Edad registrada: " + edad + " años.\n");
                    }
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce una edad válida\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Edad:"));
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
            new Ejercicio3_2Swing().setVisible(true);
        });
    }
}