import javax.swing.*;
import java.awt.*;

public class AplicacionCalculos extends JFrame {

    private JTextArea areaResultados;

    public AplicacionCalculos() {

        setTitle("Aplicación de Cálculos");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 2, 10, 10));

        JButton btn1 = new JButton("1. Redondeo");
        JButton btn2 = new JButton("2. Ventas Fijas");
        JButton btn3 = new JButton("3. Ventas Variables");
        JButton btn4 = new JButton("4. Circunferencia");
        JButton btn5 = new JButton("5. Celsius a Fahrenheit");
        JButton btn6 = new JButton("6. Km/h a m/s");
        JButton btn7 = new JButton("7. Hipotenusa");
        JButton btn8 = new JButton("8. Área Triángulo");
        JButton btn9 = new JButton("9. Descomposición");
        JButton btn10 = new JButton("10. Número de la Suerte");

        panelBotones.add(btn1);
        panelBotones.add(btn2);
        panelBotones.add(btn3);
        panelBotones.add(btn4);
        panelBotones.add(btn5);
        panelBotones.add(btn6);
        panelBotones.add(btn7);
        panelBotones.add(btn8);
        panelBotones.add(btn9);
        panelBotones.add(btn10);

        areaResultados = new JTextArea();
        areaResultados.setFont(new Font("Arial", Font.PLAIN, 16));
        areaResultados.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaResultados);

        add(panelBotones, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Eventos

        btn1.addActionListener(e -> redondeoNumero());
        btn2.addActionListener(e -> ventasFijas());
        btn3.addActionListener(e -> ventasVariables());
        btn4.addActionListener(e -> circunferencia());
        btn5.addActionListener(e -> convertirTemperatura());
        btn6.addActionListener(e -> convertirVelocidad());
        btn7.addActionListener(e -> calcularHipotenusa());
        btn8.addActionListener(e -> areaTriangulo());
        btn9.addActionListener(e -> descomponerNumero());
        btn10.addActionListener(e -> numeroSuerte());
    }

    
    // 1. Redondeo
    

    public void redondeoNumero() {

        double numero = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese un número decimal")
        );

        long redondeado = Math.round(numero);

        areaResultados.setText(
                "REDONDEO\n\n" +
                "Número original: " + numero +
                "\nNúmero redondeado: " + redondeado
        );
    }

    
    // 2. Ventas fijas
    

    public void ventasFijas() {

        double manzanas = Double.parseDouble(
                JOptionPane.showInputDialog("Ventas trimestrales de manzanas")
        );

        double peras = Double.parseDouble(
                JOptionPane.showInputDialog("Ventas trimestrales de peras")
        );

        double precioManzana = 500;
        double precioPera = 350;

        double total = (manzanas * precioManzana) +
                       (peras * precioPera);

        areaResultados.setText(
                "VENTAS CON PRECIOS FIJOS\n\n" +
                "Precio manzana: $" + precioManzana +
                "\nPrecio pera: $" + precioPera +
                "\nTotal: $" + total
        );
    }

    
    // 3. Ventas variables
    

    public void ventasVariables() {

        double manzanas = Double.parseDouble(
                JOptionPane.showInputDialog("Cantidad de manzanas")
        );

        double peras = Double.parseDouble(
                JOptionPane.showInputDialog("Cantidad de peras")
        );

        double precioManzana = Double.parseDouble(
                JOptionPane.showInputDialog("Precio de la manzana")
        );

        double precioPera = Double.parseDouble(
                JOptionPane.showInputDialog("Precio de la pera")
        );

        double total = (manzanas * precioManzana) +
                       (peras * precioPera);

        areaResultados.setText(
                "VENTAS CON PRECIOS VARIABLES\n\n" +
                "Total: $" + total
        );
    }

    
    // 4. Circunferencia
    

    public void circunferencia() {

        double radio = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese el radio")
        );

        double longitud = 2 * Math.PI * radio;
        double area = Math.PI * Math.pow(radio, 2);

        areaResultados.setText(
                "CIRCUNFERENCIA\n\n" +
                "Longitud: " + longitud +
                "\nÁrea: " + area
        );
    }

    
    // 5. Celsius a Fahrenheit
    

    public void convertirTemperatura() {

        double celsius = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese grados Celsius")
        );

        double fahrenheit = (celsius * 9 / 5) + 32;

        areaResultados.setText(
                "CONVERSIÓN DE TEMPERATURA\n\n" +
                celsius + " °C = " + fahrenheit + " °F"
        );
    }

    
    // 6. Km/h a m/s
    

    public void convertirVelocidad() {

        double kmh = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese velocidad en Km/h")
        );

        double ms = kmh / 3.6;

        areaResultados.setText(
                "CONVERSIÓN DE VELOCIDAD\n\n" +
                kmh + " Km/h = " + ms + " m/s"
        );
    }

    
    // 7. Hipotenusa
    

    public void calcularHipotenusa() {

        double cateto1 = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese el primer cateto")
        );

        double cateto2 = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese el segundo cateto")
        );

        double hipotenusa = Math.sqrt(
                Math.pow(cateto1, 2) +
                Math.pow(cateto2, 2)
        );

        areaResultados.setText(
                "HIPOTENUSA\n\n" +
                "Resultado: " + hipotenusa
        );
    }

    
    // 8. Área de triángulo
    

    public void areaTriangulo() {

        double lado1 = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese lado 1")
        );

        double lado2 = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese lado 2")
        );

        double lado3 = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese lado 3")
        );

        double s = (lado1 + lado2 + lado3) / 2;

        double area = Math.sqrt(
                s *
                (s - lado1) *
                (s - lado2) *
                (s - lado3)
        );

        areaResultados.setText(
                "ÁREA DE TRIÁNGULO\n\n" +
                "Área: " + area
        );
    }

    
    // 9. Descomposición
    

    public void descomponerNumero() {

        int numero = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese un número de 3 cifras")
        );

        int primera = numero / 100;
        int central = (numero / 10) % 10;
        int ultima = numero % 10;

        areaResultados.setText(
                "DESCOMPOSICIÓN\n\n" +
                "Primera cifra: " + primera +
                "\nCifra central: " + central +
                "\nÚltima cifra: " + ultima
        );
    }

    
    // 10. Número de la suerte


    public void numeroSuerte() {

        int dia = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese día")
        );

        int mes = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese mes")
        );

        int anio = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese año")
        );

        int sumaFecha = dia + mes + anio;

        int unidad = sumaFecha % 10;
        int decena = (sumaFecha / 10) % 10;
        int centena = (sumaFecha / 100) % 10;
        int millar = (sumaFecha / 1000) % 10;

        int suerte = unidad + decena + centena + millar;

        areaResultados.setText(
                "NÚMERO DE LA SUERTE\n\n" +
                "Número de la suerte: " + suerte
        );
    }

    
    // Main
    

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new AplicacionCalculos().setVisible(true);
        });
    }
}

