// 7.5. Todas las cuentas corrientes con las que vamos a trabajar pertenecen
// al mismo banco. Añadir un atributo que almacene el nombre del banco
// (que es único) en la clase CuentaCorriente. Diseñar un método que permita
// modificar el nombre del banco (al que pertenecen todas las cuentas corrientes).

class CuentaCorriente {

    private String nombre;
    private String dni;
    private double saldo;
    private double limite;

    // El banco es común para todas las cuentas
    private static String banco;

    public CuentaCorriente(String nombre, String dni,
                           double saldo, double limite) {

        this.nombre = nombre;
        this.dni = dni;
        this.saldo = saldo;
        this.limite = limite;
    }

    // Método para modificar el nombre del banco
    public static void modificarBanco(String nuevoBanco) {
        banco = nuevoBanco;
    }

    // Método para mostrar los datos
    public void mostrarDatos() {
        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Saldo: " + saldo);
        System.out.println("Límite: " + limite);
        System.out.println("Banco: " + banco);
    }
}

public class Main {

    public static void main(String[] args) {

        // Establecemos el banco para todas las cuentas
        CuentaCorriente.modificarBanco("Banco Nación");

        // Creamos dos cuentas
        CuentaCorriente cuenta1 = new CuentaCorriente(
                "Agustin",
                "12345678",
                1000,
                -500
        );

        CuentaCorriente cuenta2 = new CuentaCorriente(
                "Juan",
                "87654321",
                2000,
                -300
        );

        System.out.println("CUENTA 1");
        cuenta1.mostrarDatos();

        System.out.println("\nCUENTA 2");
        cuenta2.mostrarDatos();

        // Cambiamos el banco
        System.out.println("\nCambiamos el banco...");

        CuentaCorriente.modificarBanco("Banco Provincia");

        System.out.println("\nCUENTA 1 DESPUÉS DEL CAMBIO");
        cuenta1.mostrarDatos();

        System.out.println("\nCUENTA 2 DESPUÉS DEL CAMBIO");
        cuenta2.mostrarDatos();
    }
}