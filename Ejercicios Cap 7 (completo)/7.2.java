/*
 * 7.2. En la clase CuentaCorriente sobrecargar los constructores para que
 * permitan crear objetos:
 *
 * - Solo con el saldo inicial, no serán necesarios los datos del titular.
 *   Por defecto el límite de descubierto será 0 euros.
 *
 * - Con un saldo inicial, con un límite de descubierto y con el DNI del
 *   titular de la cuenta.
 */

public class CuentaCorriente {

    double saldo;
    double limite;
    String nombre;
    String dni;

    // Constructor del ejercicio 7.1
    // Crea la cuenta con nombre y DNI.
    // Saldo inicial: 0
    // Límite de descubierto: -50
    public CuentaCorriente(String nombre, String dni) {

        this.nombre = nombre;
        this.dni = dni;

        saldo = 0;
        limite = -50;
    }

    // Constructor con solo saldo inicial.
    // El límite de descubierto será 0.
    public CuentaCorriente(double saldoInicial) {

        saldo = saldoInicial;
        limite = 0;

        nombre = null;
        dni = null;
    }

    // Constructor con saldo inicial,
    // límite de descubierto y DNI.
    public CuentaCorriente(double saldoInicial, double limite, String dni) {

        saldo = saldoInicial;
        this.limite = limite;
        this.dni = dni;

        nombre = null;
    }

    // Método para sacar dinero
    public boolean egreso(double cantidad) {

        if (saldo - cantidad >= limite) {

            saldo -= cantidad;
            return true;

        } else {

            return false;
        }
    }

    // Método para ingresar dinero
    public void ingreso(double cantidad) {

        saldo += cantidad;
    }

    // Método para mostrar la información
    public void mostrarInformacion() {

        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Saldo: " + saldo);
        System.out.println("Límite de descubierto: " + limite);
    }
}