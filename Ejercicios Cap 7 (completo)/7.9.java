// 7.9. Modelar una casa con muchas bombillas, de forma que cada bombilla
// se pueda encender o apagar individualmente. Para ello hacer una clase
// Bombilla con una variable privada que indique si está encendida o apagada,
// así como un método que nos diga el estado de una bombilla concreta.
// Además, queremos poner un interruptor general, de forma que si saltan
// los fusibles, todas las bombillas quedan apagadas. Cuando el fusible se
// repara, las bombillas vuelven a estar encendidas o apagadas, según
// estuvieran antes del percance. Cada bombilla se enciende y se apaga
// individualmente, pero solo responde que está encendida si su interruptor
// particular está activado y además hay luz general.

class Bombilla {

    private boolean encendida;

    // Luz general de la casa
    private static boolean luzGeneral = true;

    // Constructor
    public Bombilla() {
        encendida = false;
    }

    // Encender la bombilla
    public void encender() {
        encendida = true;
    }

    // Apagar la bombilla
    public void apagar() {
        encendida = false;
    }

    // Comprobar si la bombilla está encendida
    public boolean estaEncendida() {
        return encendida && luzGeneral;
    }

    // Cortar la luz general
    public static void cortarLuzGeneral() {
        luzGeneral = false;
    }

    // Reparar los fusibles
    public static void repararFusibles() {
        luzGeneral = true;
    }

    // Mostrar el estado
    public void mostrarEstado() {

        if (estaEncendida()) {
            System.out.println("La bombilla está ENCENDIDA.");
        } else {
            System.out.println("La bombilla está APAGADA.");
        }
    }
}

public class Main {

    public static void main(String[] args) {

        // Creamos tres bombillas
        Bombilla bombilla1 = new Bombilla();
        Bombilla bombilla2 = new Bombilla();
        Bombilla bombilla3 = new Bombilla();

        // Encendemos la bombilla 1 y la 3
        bombilla1.encender();
        bombilla3.encender();

        System.out.println("ESTADO INICIAL:");

        System.out.print("Bombilla 1: ");
        bombilla1.mostrarEstado();

        System.out.print("Bombilla 2: ");
        bombilla2.mostrarEstado();

        System.out.print("Bombilla 3: ");
        bombilla3.mostrarEstado();

        // Se cortan los fusibles
        System.out.println("\nSE CORTAN LOS FUSIBLES:");

        Bombilla.cortarLuzGeneral();

        System.out.print("Bombilla 1: ");
        bombilla1.mostrarEstado();

        System.out.print("Bombilla 2: ");
        bombilla2.mostrarEstado();

        System.out.print("Bombilla 3: ");
        bombilla3.mostrarEstado();

        // Se reparan los fusibles
        System.out.println("\nSE REPARAN LOS FUSIBLES:");

        Bombilla.repararFusibles();

        System.out.print("Bombilla 1: ");
        bombilla1.mostrarEstado();

        System.out.print("Bombilla 2: ");
        bombilla2.mostrarEstado();

        System.out.print("Bombilla 3: ");
        bombilla3.mostrarEstado();

        // Apagamos individualmente la bombilla 1
        System.out.println("\nAPAGAMOS LA BOMBILLA 1:");

        bombilla1.apagar();

        System.out.print("Bombilla 1: ");
        bombilla1.mostrarEstado();

        System.out.print("Bombilla 2: ");
        bombilla2.mostrarEstado();

        System.out.print("Bombilla 3: ");
        bombilla3.mostrarEstado();
    }
}