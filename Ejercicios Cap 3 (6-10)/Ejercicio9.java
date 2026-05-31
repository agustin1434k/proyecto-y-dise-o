package ejercicio3_9swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_9Swing extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private JButton btnRegistrar;

    private int idActual = 0;
    private int idMasAlto = -1;
    private int alturaMaxima = -1;

    public Ejercicio3_9Swing() {
        setTitle("Ejercicio 3.9 - Control de Flora Urbana");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(10);
        btnRegistrar = new JButton("Registrar Altura");
        outputArea = new JTextArea(12, 40);
        outputArea.setEditable(false);
        
        outputArea.append("Ingrese la altura del Árbol ID: " + idActual + " (en cm):\n");
        outputArea.append("(Para finalizar, ingrese -1)\n----------------------------------------\n");

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int altura = Integer.parseInt(inputField.getText());

                    if (altura == -1) {
                        outputArea.append("\n### REGISTRO FINALIZADO ###\n");
                        if (idMasAlto != -1) {
                            outputArea.append("El árbol más alto registrado es el ID: " + idMasAlto + "\n");
                            outputArea.append("Con una altura de: " + alturaMaxima + " cm.\n");
                        } else {
                            outputArea.append("No se registraron árboles válidos.\n");
                        }
                        
                        inputField.setEnabled(false);
                        btnRegistrar.setEnabled(false);
                    } else if (altura < 0) {
                        outputArea.append("Error: La altura no puede ser negativa (use -1 para salir).\n");
                    } else {
                        outputArea.append("Árbol ID " + idActual + " -> Altura: " + altura + " cm.\n");
                        
                        if (altura > alturaMaxima) {
                            alturaMaxima = altura;
                            idMasAlto = idActual;
                        }
                        
                        idActual++;
                        outputArea.append("Ingrese la altura del Árbol ID: " + idActual + " (en cm):\n");
                    }
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Ingrese un valor numérico válido.\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Altura (cm):"));
        inputPanel.add(inputField);
        inputPanel.add(btnRegistrar);

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
            new Ejercicio3_9Swing().setVisible(true);
        });
    }
}