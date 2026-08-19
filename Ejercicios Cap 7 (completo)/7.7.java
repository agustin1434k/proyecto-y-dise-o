// 7.7. Se quiere definir la clase Banco de la que interesa guardar su nombre,
// capital y la dirección central. Los bancos tienen las siguientes restricciones:
// a) Siempre tienen que tener un nombre, que no puede ser modificado.
// b) Si no se especifica, todos los bancos tienen un capital por defecto de
// 5.2 millones de euros al crearse.
// c) El capital y la dirección de un banco son modificables.
//
// Modificar la clase CuentaCorriente para que cada una esté vinculada a un
// objeto de tipo Banco. Escribir los métodos necesarios en la clase
// CuentaCorriente para gestionar el banco al que pertenece.
// Existe la posibilidad de que una cuenta corriente no esté vinculada
// a ningún banco.

class Banco {

    private final String nombre;
    private double capital;
    private String direccion;

    // Constructor con nombre
    public Banco(String nombre) {
        this.nombre = nombre;
        this.capital = 5200000;
        this.direccion = "";
    }

    // Constructor con nombre, capital y dirección
    public Banco(String nombre, double capital, String direccion) {
        this.nombre = nombre;
        this.capital = capital;
        this.direccion = direccion;
    }

    // Obtener nombre
    public String getNombre() {
        return nombre;
    }

    // Obtener capital
    public double getCapital() {
        return capital;
    }

    // Modificar capital
    public void setCapital(double capital) {
        this.capital = capital;
    }

    // Obtener dirección
    public String getDireccion() {
        return direccion;
    }

    // Modificar dirección
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Mostrar datos del banco
    public void mostrarDatos() {
        System.out.println("Nombre del banco: " + nombre);
        System.out.println("Capital: " + capital);
        System.out.println("Dirección: " + direccion);
    }
}

class CuentaCorriente {

    private String nombre;
    private String dni;
    private double saldo;
    private double limite;

    // Banco al que pertenece la cuenta
    private Banco banco;

    // Constructor
    public CuentaCorriente(String nombre, String dni,
                           double saldo, double limite) {

        this.nombre = nombre;
        this.dni = dni;
        this.saldo = saldo;
        this.limite = limite;

        // Inicialmente no pertenece a ningún banco
        this.banco = null;
    }

    // Asignar un banco a la cuenta
    public void asignarBanco(Banco banco) {
        this.banco = banco;
    }

    // Obtener el banco
    public Banco getBanco() {
        return banco;
    }

    // Quitar el banco
    public void quitarBanco() {
        banco = null;
    }

    // Mostrar datos de la cuenta
    public void mostrarDatos() {

        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Saldo: " + saldo);
        System.out.println("Límite: " + limite);

        if (banco != null) {
            System.out.println("Banco: " + banco.getNombre());
        } else {
            System.out.println("Banco: Ninguno");
        }
    }
}

public class Main {

    public static void main(String[] args) {

        // Creamos un banco con el capital por defecto
        Banco banco1 = new Banco("Banco Nación");

        System.out.println("DATOS DEL BANCO");
        banco1.mostrarDatos();

        // Modificamos el capital
        System.out.println("\nModificamos el capital:");
        banco1.setCapital(6000000);

        // Modificamos la dirección
        banco1.setDireccion("Av. Siempre Viva 123");

        banco1.mostrarDatos();

        // Creamos una cuenta corriente
        CuentaCorriente cuenta1 = new CuentaCorriente(
                "Agustin",
                "12345678",
                10000,
                -500
        );

        System.out.println("\nCUENTA SIN BANCO");
        cuenta1.mostrarDatos();

        // Vinculamos la cuenta con el banco
        System.out.println("\nVinculamos la cuenta al banco:");

        cuenta1.asignarBanco(banco1);

        cuenta1.mostrarDatos();

        // Creamos otro banco usando el constructor completo
        Banco banco2 = new Banco(
                "Banco Provincia",
                8000000,
                "Av. Libertad 500"
        );

        System.out.println("\nSEGUNDO BANCO");
        banco2.mostrarDatos();

        // Cambiamos el banco de la cuenta
        System.out.println("\nCambiamos el banco de la cuenta:");

        cuenta1.asignarBanco(banco2);

        cuenta1.mostrarDatos();

        // Quitamos el banco
        System.out.println("\nQuitamos el banco de la cuenta:");

        cuenta1.quitarBanco();

        cuenta1.mostrarDatos();
    }
}