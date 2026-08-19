// 7.4. Modificar la visibilidad de la clase CuentaCorriente para que sea
// visible desde clases externas y la visibilidad de sus atributos para que:
// saldo y limite no sean visibles para otras clases.
// nombre sea público para cualquier clase.
// dni solo sea visible por clases vecinas.
// Realizar un programa para comprobar la visibilidad de los atributos.

class CuentaCorriente {

    public String nombre;
    protected String dni;
    private double saldo;
    private double limite;

    public CuentaCorriente(String nombre, String dni,
                           double saldo, double limite) {

        this.nombre = nombre;
        this.dni = dni;
        this.saldo = saldo;
        this.limite = limite;
    }

    public void mostrarDatos() {
        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Saldo: " + saldo);
        System.out.println("Límite: " + limite);
    }
}

public class Main {

    public static void main(String[] args) {

        CuentaCorriente cuenta = new CuentaCorriente(
                "Agustin",
                "12345678",
                1000,
                -500
        );

        // El atributo nombre es PUBLIC,
        // por lo tanto podemos acceder desde esta clase.
        cuenta.nombre = "Juan";

        System.out.println("Nombre: " + cuenta.nombre);

        // El atributo dni es PROTECTED.
        // Se puede utilizar desde clases del mismo paquete,
        // pero no desde una clase externa de otro paquete.
        //
        // cuenta.dni = "87654321";

        // El atributo saldo es PRIVATE.
        // No podemos acceder directamente desde Main.
        //
        // cuenta.saldo = 5000;

        // El atributo limite es PRIVATE.
        // Tampoco podemos acceder directamente desde Main.
        //
        // cuenta.limite = -1000;

        System.out.println("\nDatos de la cuenta:");
        cuenta.mostrarDatos();
    }
}