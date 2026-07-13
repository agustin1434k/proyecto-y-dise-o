/*
Ejercicio 4.1
Solicitar un número entero y mostrar la palabra "Eco..." tantas veces
como indique el número.
*/

import javax.swing.JOptionPane;

public class Main {

    public static void eco(int n) {
        String mensaje = "";

        for (int i = 1; i <= n; i++) {
            mensaje += "Eco...\n";
        }

        JOptionPane.showMessageDialog(null, mensaje);
    }

    public static void main(String[] args) {

        int numero = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese un número entero"));

        eco(numero);
    }
}

/*
Ejercicio 4.2
Solicitar dos números y mostrar todos los números comprendidos entre ellos.
*/

import javax.swing.JOptionPane;

public class Main {

    public static void mostrarNumeros(int a, int b) {
        String resultado = "";

        if (a < b) {
            for (int i = a; i <= b; i++) {
                resultado += i + "\n";
            }
        } else {
            for (int i = b; i <= a; i++) {
                resultado += i + "\n";
            }
        }

        JOptionPane.showMessageDialog(null, resultado);
    }

    public static void main(String[] args) {

        int num1 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el primer número"));

        int num2 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el segundo número"));

        mostrarNumeros(num1, num2);
    }
}

/*
Ejercicio 4.3
Calcular el área y el volumen de un cilindro.
*/

import javax.swing.JOptionPane;

public class Main {

    public static double areaCilindro(double radio, double altura) {
        return 2 * Math.PI * radio * (altura + radio);
    }

    public static double volumenCilindro(double radio, double altura) {
        return Math.PI * Math.pow(radio, 2) * altura;
    }

    public static void main(String[] args) {

        double radio = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese el radio del cilindro"));

        double altura = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese la altura del cilindro"));

        double area = areaCilindro(radio, altura);
        double volumen = volumenCilindro(radio, altura);

        JOptionPane.showMessageDialog(null,
                "Área: " + area +
                "\nVolumen: " + volumen);
    }
}

/*
Ejercicio 4.4
Determinar el mayor entre dos números.
*/

import javax.swing.JOptionPane;

public class Main {

    public static int mayor(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    public static void main(String[] args) {

        int num1 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el primer número"));

        int num2 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el segundo número"));

        int resultado = mayor(num1, num2);

        JOptionPane.showMessageDialog(null,
                "El mayor es: " + resultado);
    }
}

/*
Ejercicio 4.5
Determinar el mayor entre tres números utilizando
sobrecarga de métodos.
*/

import javax.swing.JOptionPane;

public class Main {

    public static int mayor(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    public static int mayor(int a, int b, int c) {
        return mayor(mayor(a, b), c);
    }

    public static void main(String[] args) {

        int num1 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el primer número"));

        int num2 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el segundo número"));

        int num3 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el tercer número"));

        int resultado = mayor(num1, num2, num3);

        JOptionPane.showMessageDialog(null,
                "El mayor es: " + resultado);
    }
}

/*
Ejercicio 4.6
Determinar si un carácter ingresado es una vocal.
*/

import javax.swing.JOptionPane;

public class Main {

    public static boolean esVocal(char letra) {

        letra = Character.toLowerCase(letra);

        return letra == 'a' || letra == 'e' || letra == 'i'
                || letra == 'o' || letra == 'u';
    }

    public static void main(String[] args) {

        char letra = JOptionPane.showInputDialog("Ingrese una letra")
                .charAt(0);

        if (esVocal(letra)) {
            JOptionPane.showMessageDialog(null,
                    "La letra es una vocal.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "La letra no es una vocal.");
        }
    }
}

/*
Ejercicio 4.7
Determinar si un número es primo.
*/

import javax.swing.JOptionPane;

public class Main {

    public static boolean esPrimo(int numero) {

        if (numero <= 1) {
            return false;
        }

        for (int i = 2; i < numero; i++) {
            if (numero % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        int numero = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese un número"));

        if (esPrimo(numero)) {
            JOptionPane.showMessageDialog(null,
                    "El número es primo.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "El número no es primo.");
        }
    }
}

/*
Ejercicio 4.8
Escribir una función a la que se le pase un número entero
y devuelva la cantidad de divisores primos que tiene.
*/

import javax.swing.JOptionPane;

public class Main {

    public static boolean esPrimo(int n) {

        if (n <= 1) {
            return false;
        }

        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static int cantidadDivisoresPrimos(int numero) {

        int contador = 0;

        for (int i = 2; i <= numero; i++) {

            if (numero % i == 0 && esPrimo(i)) {
                contador++;
            }

        }

        return contador;
    }

    public static void main(String[] args) {

        int numero = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese un número"));

        int cantidad = cantidadDivisoresPrimos(numero);

        JOptionPane.showMessageDialog(null,
                "Cantidad de divisores primos: " + cantidad);

    }
}

/*
Ejercicio 4.9
Implementar la función divisoresPrimos() que muestre
todos los divisores primos de un número.
*/

import javax.swing.JOptionPane;

public class Main {

    public static boolean esPrimo(int n) {

        if (n <= 1) {
            return false;
        }

        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static void divisoresPrimos(int numero) {

        String resultado = "";

        for (int i = 2; i <= numero; i++) {

            if (numero % i == 0 && esPrimo(i)) {
                resultado += i + "\n";
            }

        }

        JOptionPane.showMessageDialog(null,
                "Divisores primos:\n" + resultado);
    }

    public static void main(String[] args) {

        int numero = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese un número"));

        divisoresPrimos(numero);

    }
}

/*
Ejercicio 4.10
Determinar si dos números son amigos.
*/

import javax.swing.JOptionPane;

public class Main {

    public static int sumaDivisores(int numero) {

        int suma = 0;

        for (int i = 1; i < numero; i++) {
            if (numero % i == 0) {
                suma += i;
            }
        }

        return suma;
    }

    public static boolean sonAmigos(int a, int b) {
        return sumaDivisores(a) == b && sumaDivisores(b) == a;
    }

    public static void main(String[] args) {

        int num1 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el primer número"));

        int num2 = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el segundo número"));

        if (sonAmigos(num1, num2)) {
            JOptionPane.showMessageDialog(null,
                    "Los números son amigos.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Los números no son amigos.");
        }
    }
}

/*
Ejercicio 4.11
Calcular la potencia de un número real elevado
a un exponente entero positivo.
*/

import javax.swing.JOptionPane;

public class Main {

    public static double potencia(double base, int exponente) {

        double resultado = 1;

        for (int i = 1; i <= exponente; i++) {
            resultado *= base;
        }

        return resultado;
    }

    public static void main(String[] args) {

        double base = Double.parseDouble(
                JOptionPane.showInputDialog("Ingrese la base"));

        int exponente = Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese el exponente"));

        double resultado = potencia(base, exponente);

        JOptionPane.showMessageDialog(null,
                base + " elevado a " + exponente + " = " + resultado);
    }
}

