package ejercicio3_5swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejercicio3_5Swing extends JFrame {

    private JTextField minField, maxField, numField;
    private JButton btnConfigurar, btnValidar;
    private JTextArea outputArea;
    
    private int minimo;
    private int maximo;
    private boolean rangoConfigurado = false;

    public Ejercicio3_5Swing() {
        setTitle("Ejercicio 3.5 - Validador de Rango");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        minField = new JTextField(5);
        maxField = new JTextField(5);
        numField = new JTextField(5);
        numField.setEnabled(false); 

        btnConfigurar = new JButton("Definir Rango");
        btnValidar = new JButton("Validar Número");
        btnValidar.setEnabled(false);

        outputArea = new JTextArea(12, 40);
        outputArea.setEditable(false);
        outputArea.append("Paso 1: Introduce los valores mínimo y máximo de tu rango.\n\n");

        btnConfigurar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    minimo = Integer.parseInt(minField.getText());
                    maximo = Integer.parseInt(maxField.getText());

                    if (minimo > maximo) {
                        outputArea.append("Error: El mínimo no puede ser mayor que el máximo.\n");
                    } else {
                        rangoConfigurado = true;
                        outputArea.append("Rango establecido: [" + minimo + " , " + maximo + "]\n");
                        outputArea.append("Paso 2: Introduce un número que pertenezca a este rango.\n\n");
                        
                        minField.setEnabled(false);
                        maxField.setEnabled(false);
                        btnConfigurar.setEnabled(false);
                        numField.setEnabled(true);
                        btnValidar.setEnabled(true);
                    }
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce límites numéricos válidos.\n");
                }
            }
        });

        btnValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numero = Integer.parseInt(numField.getText());

                    if (numero >= minimo && numero <= maximo) {
                        outputArea.append("¡Excelente! El número " + numero + " está dentro del rango.\n");
                        outputArea.append("Proceso finalizado con éxito.\n");
                        
                        // Finalizar todo
                        numField.setEnabled(false);
                        btnValidar.setEnabled(false);
                    } else {
                        outputArea.append("El número " + numero + " NO pertenece al rango [" + minimo + " , " + maximo + "]. Volvé a intentar.\n");
                    }
                    numField.setText("");
                } catch (NumberFormatException ex) {
                    outputArea.append("Error: Introduce un número válido.\n");
                }
            }
        });

        JPanel configPanel = new JPanel();
        configPanel.add(new JLabel("Mín:"));
        configPanel.add(minField);
        configPanel.add(new JLabel("Máx:"));
        configPanel.add(maxField);
        configPanel.add(btnConfigurar);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Número a evaluar:"));
        inputPanel.add(numField);
        inputPanel.add(btnValidar);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(configPanel);
        mainPanel.add(inputPanel);
        mainPanel.add(scrollPane);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Ejercicio3_5Swing().setVisible(true);
        });
    }
}