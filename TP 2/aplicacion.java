import javax.swing.*;
import java.awt.*;

public class tp2 extends JFrame {

    private JTextField input1, input2, input3;
    private JLabel resultLabel;

    public tp2() {
        setTitle("Trabajo Práctico N°2 - Estructuras Condicionales");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        input1 = new JTextField(5);
        input2 = new JTextField(5);
        input3 = new JTextField(5);

        
        JPanel panelInputs = new JPanel();
        panelInputs.add(new JLabel("Número 1 / Día / Nota:")); panelInputs.add(input1);
        panelInputs.add(new JLabel("Número 2 / Mes:")); panelInputs.add(input2);
        panelInputs.add(new JLabel("Número 3 / Año:")); panelInputs.add(input3);

        
        JButton btn1 = new JButton("1. Par / Impar");
        JButton btn2 = new JButton("2. Iguales o Distintos");
        JButton btn3 = new JButton("3. Cuál es Mayor");
        JButton btn4 = new JButton("4. Combinado (Mayor/Igual)");
        JButton btn5 = new JButton("5. Casi Cero");
        JButton btn6 = new JButton("6. Mayor/Menor (Ternario)");
        JButton btn7 = new JButton("7. Evaluar Orden");
        JButton btn8 = new JButton("8. Clasificar Nota");
        JButton btn9 = new JButton("9. Validar Fecha");

        btn1.addActionListener(e -> ej1ParImpar());
        btn2.addActionListener(e -> ej2IgualesDistintos());
        btn3.addActionListener(e -> ej3CualMayor());
        btn4.addActionListener(e -> ej4Combinado());
        btn5.addActionListener(e -> ej5CasiCero());
        btn6.addActionListener(e -> ej6Ternario());
        btn7.addActionListener(e -> ej7OrdenTresNumeros());
        btn8.addActionListener(e -> ej8ClasificarNota());
        btn9.addActionListener(e -> ej9ValidarFecha());

        
        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 8, 8));
        panelBotones.add(btn1); panelBotones.add(btn2); panelBotones.add(btn3);
        panelBotones.add(btn4); panelBotones.add(btn5); panelBotones.add(btn6);
        panelBotones.add(btn7); panelBotones.add(btn8); panelBotones.add(btn9);

        
        resultLabel = new JLabel("Resultado: esperando operación...", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JPanel panelResult = new JPanel(new BorderLayout());
        panelResult.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelResult.add(resultLabel, BorderLayout.CENTER);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(panelInputs, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        panelPrincipal.add(panelResult, BorderLayout.SOUTH);

        add(panelPrincipal);
        setSize(650, 250);
        setLocationRelativeTo(null);
    }

    private void ej1ParImpar() {
        try {
            int n = Integer.parseInt(input1.getText().trim());
            if (n % 2 == 0) {
                resultLabel.setText("Resultado: El número " + n + " es PAR.");
            } else {
                resultLabel.setText("Resultado: El número " + n + " es IMPAR.");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Ingrese un número entero en la primera caja.");
        }
    }

    private void ej2IgualesDistintos() {
        try {
            int n1 = Integer.parseInt(input1.getText().trim());
            int n2 = Integer.parseInt(input2.getText().trim());
            if (n1 == n2) {
                resultLabel.setText("Resultado: Los números son IGUALES.");
            } else {
                resultLabel.setText("Resultado: Los números son DISTINTOS.");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Ingrese números enteros en las cajas 1 y 2.");
        }
    }

    private void ej3CualMayor() {
        try {
            int n1 = Integer.parseInt(input1.getText().trim());
            int n2 = Integer.parseInt(input2.getText().trim());
            if (n1 > n2) {
                resultLabel.setText("Resultado: El mayor es " + n1);
            } else if (n2 > n1) {
                resultLabel.setText("Resultado: El mayor es " + n2);
            } else {
                resultLabel.setText("Resultado: Ambos números son iguales.");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Ingrese números enteros en las cajas 1 y 2.");
        }
    }

    private void ej4Combinado() {
        try {
            int n1 = Integer.parseInt(input1.getText().trim());
            int n2 = Integer.parseInt(input2.getText().trim());
            if (n1 == n2) {
                resultLabel.setText("Resultado: Son iguales.");
            } else if (n1 > n2) {
                resultLabel.setText("Resultado: El primero (" + n1 + ") es mayor que el segundo (" + n2 + ").");
            } else {
                resultLabel.setText("Resultado: El segundo (" + n2 + ") es mayor que el primero (" + n1 + ").");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Ingrese números enteros en las cajas 1 y 2.");
        }
    }

    private void ej5CasiCero() {
        try {
            double n = Double.parseDouble(input1.getText().trim());
            if (n > -1 && n < 1 && n != 0) {
                resultLabel.setText("Resultado: El número " + n + " es CASI CERO.");
            } else {
                resultLabel.setText("Resultado: El número " + n + " NO es casi cero.");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Ingrese un número decimal en la primera caja.");
        }
    }

    private void ej6Ternario() {
        try {
            int n1 = Integer.parseInt(input1.getText().trim());
            int n2 = Integer.parseInt(input2.getText().trim());
            
            int mayor = (n1 > n2) ? n1 : n2;
            int menor = (n1 < n2) ? n1 : n2;

            if (n1 == n2) {
                resultLabel.setText("Resultado: Son iguales, no hay mayor ni menor.");
            } else {
                resultLabel.setText("Resultado (Ternario) -> Mayor: " + mayor + " | Menor: " + menor);
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Ingrese números enteros en las cajas 1 y 2.");
        }
    }

    private void ej7OrdenTresNumeros() {
        try {
            int a = Integer.parseInt(input1.getText().trim());
            int b = Integer.parseInt(input2.getText().trim());
            int c = Integer.parseInt(input3.getText().trim());

            if (a > b && b > c) {
                resultLabel.setText("Orden decreciente: " + a + ", " + b + ", " + c);
            } else if (a < b && b < c) {
                resultLabel.setText("Orden creciente: " + a + ", " + b + ", " + c);
            } else {
                resultLabel.setText("Resultado: Los números están desordenados.");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Llene las 3 cajas con números enteros.");
        }
    }

    private void ej8ClasificarNota() {
        try {
            int nota = Integer.parseInt(input1.getText().trim());
            if (nota < 0 || nota > 10) {
                resultLabel.setText("Error: La nota debe ser de 0 a 10.");
            } else if (nota <= 4) {
                resultLabel.setText("Nota: Insuficiente");
            } else if (nota == 5) {
                resultLabel.setText("Nota: Suficiente");
            } else if (nota == 6) {
                resultLabel.setText("Nota: Bien");
            } else if (nota == 7 || nota == 8) {
                resultLabel.setText("Nota: Notable");
            } else {
                resultLabel.setText("Nota: Sobresaliente");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Ingrese una nota entera en la primera caja.");
        }
    }

    private void ej9ValidarFecha() {
        try {
            int dia = Integer.parseInt(input1.getText().trim());
            int mes = Integer.parseInt(input2.getText().trim());
            int anio = Integer.parseInt(input3.getText().trim());

            boolean valida = false;

            if (anio > 0 && mes >= 1 && mes <= 12) {
                int diasMes = 0;
                switch (mes) {
                    case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                        diasMes = 31; break;
                    case 4: case 6: case 9: case 11:
                        diasMes = 30; break;
                    case 2:
                        diasMes = 28; break;
                }
                if (dia >= 1 && dia <= diasMes) {
                    valida = true;
                }
            }

            if (valida) {
                resultLabel.setText("Fecha Correcta: " + dia + "/" + mes + "/" + anio);
            } else {
                resultLabel.setText("Resultado: La fecha ingresada NO es válida.");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Llene las 3 cajas con números enteros (Día/Mes/Año).");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new tp2().setVisible(true);
        });
    }
}