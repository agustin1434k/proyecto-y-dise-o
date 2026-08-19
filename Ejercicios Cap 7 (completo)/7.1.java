/*
 * 7.1. Diseñar la clase CuentaCorriente, sabiendo que los datos necesarios son:
 * saldo, límite de descubierto, nombre y DNI del titular.
 *
 * Las operaciones típicas con una cuenta corriente son:
 * - Crear la cuenta: se necesita el nombre y DNI del titular.
 *   El saldo inicial será 0 y el límite de descubierto será de -50 euros.
 * - Sacar dinero: solo se podrá sacar dinero hasta el límite de descubierto.
 *   El método debe indicar si ha sido posible llevar a cabo la operación.
 * - Ingresar dinero: se incrementa el saldo.
 * - Mostrar información: muestra la información disponible de la cuenta corriente.
 */

public class CuentaCorriente {

    double saldo;
    double limite;
    String nombre;
    String dni;

    // Constructor
    public CuentaCorriente(String nombre, String dni) {

        this.nombre = nombre;
        this.dni = dni;

        saldo = 0;
        limite = -50;
    }

    // Método para sacar dinero
    public boolean egreso(double cantidad) {

        if (saldo - cantidad >= limite) {

            saldo = saldo - cantidad;
            return true;

        } else {

            return false;
        }
    }

    // Método para ingresar dinero
    public void ingreso(double cantidad) {

        saldo = saldo + cantidad;
    }

    // Método para mostrar la información
    public void mostrarInformacion() {

        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Saldo: " + saldo);
        System.out.println("Límite de descubierto: " + limite);
    }
}