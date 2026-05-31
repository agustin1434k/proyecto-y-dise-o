package ejercicio3_3swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Ejercicio3_3Swing extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private JButton submitButton;
    private int numeroSecreto;

    public Ejercicio3_3Swing() {
        setTitle("Ejercicio 3.3 - El Número Secreto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Random rand = new Random();
        numeroSecreto = rand.nextInt(100) + 1;

        inputField = new JTextField(10);
        submitButton = new JButton("Intentar");
        outputArea = new JTextArea(12, 35);
        outputArea.setEditable(false);
        outputArea.append("¡He pensado un número entre 1 y 100!\nIntenta adivinarlo o ingresa -1 para rendirte.\n\n");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int intento = Integer.parseInt(inputField.getText());

                    if (intento == -1) {
                        outputArea.append("Te has rendido. El número secreto era: " + numeroSecreto + "\n");
                        finalizarJuego();
                    } else if (intento == numeroSecreto) {
                        outputArea.append("¡FELICIDADES! ¡Acertaste el número " + numeroSecreto + "!\n");
                        finalizarJuego();
                    } else if (intento < numeroSecreto) {
                        outputArea.append("El número secreto es MAYOR que " + intento + "\n");
                    } else {
                        outputArea.append("El número secreto es MENOR que " + intento + "\n");
                    }
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce un número entero válido.\n");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Tu número:"));
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

    private void finalizarJuego() {
        inputField.setEnabled(false);
        submitButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Ejercicio3_3Swing().setVisible(true);
        });
    }
}