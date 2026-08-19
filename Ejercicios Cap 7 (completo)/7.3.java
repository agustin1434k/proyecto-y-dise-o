// 7.3. Para la clase CuentaCorriente escribir un programa que compruebe
// el funcionamiento de sus métodos, incluidos los constructores.

class CuentaCorriente {

    private String nombre;
    private String dni;
    private double saldo;
    private double limite;

    // Constructor sin parámetros
    public CuentaCorriente() {
        nombre = "";
        dni = "";
        saldo = 0;
        limite = -50;
    }

    // Constructor con nombre y DNI
    public CuentaCorriente(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
        saldo = 0;
        limite = -50;
    }

    // Constructor completo
    public CuentaCorriente(String nombre, String dni, double saldo, double limite) {
        this.nombre = nombre;
        this.dni = dni;
        this.saldo = saldo;
        this.limite = limite;
    }

    // Método para ingresar dinero
    public void ingresar(double cantidad) {
        saldo += cantidad;
    }

    // Método para retirar dinero
    public boolean retirar(double cantidad) {
        if (saldo - cantidad >= limite) {
            saldo -= cantidad;
            return true;
        }

        return false;
    }

    // Método para mostrar los datos
    public void mostrarDatos() {
        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Saldo: " + saldo);
        System.out.println("Límite: " + limite);
    }
}

public class Main {

    public static void main(String[] args) {

        // Probamos el constructor sin parámetros
        CuentaCorriente cuenta1 = new CuentaCorriente();

        // Probamos el constructor con nombre y DNI
        CuentaCorriente cuenta2 =
                new CuentaCorriente("Agustin", "12345678");

        // Probamos el constructor completo
        CuentaCorriente cuenta3 =
                new CuentaCorriente("Juan", "87654321", 1000, -500);

        System.out.println("CUENTA 1");
        cuenta1.mostrarDatos();

        System.out.println("\nCUENTA 2");
        cuenta2.mostrarDatos();

        System.out.println("\nCUENTA 3");
        cuenta3.mostrarDatos();

        // Probamos el método ingresar
        System.out.println("\nIngresamos $500 a la cuenta 2");
        cuenta2.ingresar(500);
        cuenta2.mostrarDatos();

        // Probamos el método retirar
        System.out.println("\nRetiramos $200 de la cuenta 2");

        if (cuenta2.retirar(200)) {
            System.out.println("Retiro realizado correctamente.");
        } else {
            System.out.println("No se puede realizar el retiro.");
        }

        cuenta2.mostrarDatos();
    }
}