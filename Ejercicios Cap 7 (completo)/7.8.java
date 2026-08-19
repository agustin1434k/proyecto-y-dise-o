// 7.8. Se quiere definir una clase que permita controlar un sintonizador
// digital de emisoras FM. El controlador debe permitir subir (up) o bajar
// (down) la frecuencia en saltos de 0.5 MHz y mostrar la frecuencia
// sintonizada en un momento dado (display).
// El rango de frecuencias es de 80 MHz a 108 MHz.
// Al inicio, el controlador sintoniza a 80 MHz.
// Si durante una operación se sobrepasa uno de los límites,
// la frecuencia pasa a ser la del extremo contrario.

class SintonizadorFM {

    private double frecuencia;

    // Constructor
    public SintonizadorFM() {
        frecuencia = 80;
    }

    // Subir la frecuencia 0.5 MHz
    public void up() {

        frecuencia += 0.5;

        // Si supera 108 MHz, vuelve a 80 MHz
        if (frecuencia > 108) {
            frecuencia = 80;
        }
    }

    // Bajar la frecuencia 0.5 MHz
    public void down() {

        frecuencia -= 0.5;

        // Si baja de 80 MHz, vuelve a 108 MHz
        if (frecuencia < 80) {
            frecuencia = 108;
        }
    }

    // Mostrar la frecuencia actual
    public void display() {
        System.out.println("Frecuencia actual: " + frecuencia + " MHz");
    }
}

public class Main {

    public static void main(String[] args) {

        // Creamos el sintonizador
        SintonizadorFM radio = new SintonizadorFM();

        System.out.println("Frecuencia inicial:");
        radio.display();

        // Subimos la frecuencia
        System.out.println("\nSubimos la frecuencia 3 veces:");

        radio.up();
        radio.up();
        radio.up();

        radio.display();

        // Bajamos la frecuencia
        System.out.println("\nBajamos la frecuencia 2 veces:");

        radio.down();
        radio.down();

        radio.display();

        // Comprobamos el límite superior
        System.out.println("\nComprobamos el límite superior:");

        for (int i = 0; i < 54; i++) {
            radio.up();
        }

        radio.display();

        // Subimos una vez más para comprobar que vuelve a 80
        System.out.println("\nSubimos una vez más:");

        radio.up();

        radio.display();

        // Comprobamos el límite inferior
        System.out.println("\nComprobamos el límite inferior:");

        radio.down();

        radio.display();
    }
}