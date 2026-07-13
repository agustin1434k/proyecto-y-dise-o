package Tp5;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class TP5 extends JFrame {

    public TP5() {

        setTitle("TP5 - Tablas");
        setSize(650, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridLayout(15, 1, 5, 5));


        JButton b51 = new JButton("5.1 - Ingresar cinco números reales");
        JButton b52 = new JButton("5.2 - Mostrar números en orden inverso");
        JButton b53 = new JButton("5.3 - Medias y cantidad de ceros");
        JButton b54 = new JButton("5.4 - Eliminar elemento por índice");
        JButton b55 = new JButton("5.5 - Insertar en arreglo ordenado");
        JButton b56 = new JButton("5.6 - Fusionar tablas");
        JButton b57 = new JButton("5.7 - Merge de tablas");
        JButton b58 = new JButton("5.8 - Combinación secreta");
        JButton b59 = new JButton("5.9 - Buscar elemento");
        JButton b510 = new JButton("5.10 - Orden ascendente");
        JButton b511 = new JButton("5.11 - Orden descendente");
        JButton b512 = new JButton("5.12 - Mayor y menor");
        JButton b513 = new JButton("5.13 - Repeticiones");
        JButton b514 = new JButton("5.14 - Eliminar repetidos");
        JButton b515 = new JButton("5.15 - Estadísticas");


        add(b51);
        add(b52);
        add(b53);
        add(b54);
        add(b55);
        add(b56);
        add(b57);
        add(b58);
        add(b59);
        add(b510);
        add(b511);
        add(b512);
        add(b513);
        add(b514);
        add(b515);


        b51.addActionListener(e -> ejercicio51());
        b52.addActionListener(e -> ejercicio52());
        b53.addActionListener(e -> ejercicio53());
        b54.addActionListener(e -> ejercicio54());
        b55.addActionListener(e -> ejercicio55());
        b56.addActionListener(e -> ejercicio56());
        b57.addActionListener(e -> ejercicio57());
        b58.addActionListener(e -> ejercicio58());
        b59.addActionListener(e -> ejercicio59());
        b510.addActionListener(e -> ejercicio510());
        b511.addActionListener(e -> ejercicio511());
        b512.addActionListener(e -> ejercicio512());
        b513.addActionListener(e -> ejercicio513());
        b514.addActionListener(e -> ejercicio514());
        b515.addActionListener(e -> ejercicio515());

    }


    public static void main(String[] args) {

        new TP5().setVisible(true);

    }



    /*
     * 5.1
     * Ingresar cinco números reales y mostrarlos.
     */
    public void ejercicio51() {


        double[] numeros = new double[5];


        for(int i = 0; i < numeros.length; i++) {

            numeros[i] = Double.parseDouble(
                    JOptionPane.showInputDialog(
                            "Ingrese número real " + (i + 1)
                    )
            );

        }


        String resultado = "Números ingresados:\n";


        for(double numero : numeros) {

            resultado += numero + "\n";

        }


        JOptionPane.showMessageDialog(null, resultado);

    }



    /*
     * 5.2
     * Ingresar N números enteros y mostrarlos en orden inverso.
     */
    public void ejercicio52() {


        int cantidad = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Cantidad de números"
                )
        );


        int[] numeros = new int[cantidad];


        for(int i = 0; i < numeros.length; i++) {


            numeros[i] = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            "Ingrese número " + (i + 1)
                    )
            );

        }


        String resultado = "Orden inverso:\n";


        for(int i = numeros.length - 1; i >= 0; i--) {


            resultado += numeros[i] + "\n";

        }


        JOptionPane.showMessageDialog(null, resultado);

    }




    /*
     * 5.3
     * Calcular media de positivos,
     * media de negativos y cantidad de ceros.
     */
    public void ejercicio53() {


        int cantidad = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Cantidad de números"
                )
        );


        int positivos = 0;
        int negativos = 0;
        int ceros = 0;


        double sumaPositivos = 0;
        double sumaNegativos = 0;



        for(int i = 0; i < cantidad; i++) {


            int numero = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            "Ingrese número " + (i + 1)
                    )
            );


            if(numero > 0) {

                sumaPositivos += numero;
                positivos++;

            } 
            else if(numero < 0) {

                sumaNegativos += numero;
                negativos++;

            } 
            else {

                ceros++;

            }

        }



        double mediaPositivos =
                positivos > 0 ? sumaPositivos / positivos : 0;


        double mediaNegativos =
                negativos > 0 ? sumaNegativos / negativos : 0;



        String resultado =
                "Media positivos: " + mediaPositivos +
                "\nMedia negativos: " + mediaNegativos +
                "\nCantidad de ceros: " + ceros;



        JOptionPane.showMessageDialog(null, resultado);

    }
        /*
     * 5.4
     * Eliminar elementos de una tabla mediante su índice.
     */
    public void ejercicio54() {


        int[] tabla = {10, 20, 30, 40, 50};


        while(tabla.length > 0) {


            String mostrar = "Tabla actual:\n";


            for(int i = 0; i < tabla.length; i++) {

                mostrar += "Índice " + i + " -> " + tabla[i] + "\n";

            }



            int indice = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            mostrar + 
                            "\nIngrese índice a eliminar (-1 para salir)"
                    )
            );



            if(indice == -1) {

                break;

            }



            if(indice >= 0 && indice < tabla.length) {


                int[] nuevaTabla = new int[tabla.length - 1];


                int j = 0;


                for(int i = 0; i < tabla.length; i++) {


                    if(i != indice) {

                        nuevaTabla[j] = tabla[i];
                        j++;

                    }

                }


                tabla = nuevaTabla;


            }
            else {


                JOptionPane.showMessageDialog(
                        null,
                        "Índice inválido"
                );

            }


        }



        JOptionPane.showMessageDialog(
                null,
                "Tabla final: " + Arrays.toString(tabla)
        );

    }





    /*
     * 5.5
     * Insertar números en un arreglo ordenado manteniendo el orden.
     */
    public void ejercicio55() {


        int[] tabla = {5, 10, 20, 30, 40};



        int numero = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Número a insertar"
                )
        );



        int[] nuevaTabla = new int[tabla.length + 1];



        int posicion = 0;



        while(posicion < tabla.length &&
              tabla[posicion] < numero) {


            nuevaTabla[posicion] = tabla[posicion];
            posicion++;


        }



        nuevaTabla[posicion] = numero;



        for(int i = posicion; i < tabla.length; i++) {


            nuevaTabla[i + 1] = tabla[i];


        }



        JOptionPane.showMessageDialog(
                null,
                "Tabla ordenada:\n" +
                Arrays.toString(nuevaTabla)
        );


    }





    /*
     * 5.6
     * Fusionar dos tablas ordenadas.
     */
    public void ejercicio56() {


        int[] tabla1 = {1, 3, 5, 7, 9};

        int[] tabla2 = {2, 4, 6, 8, 10};



        int[] resultado = new int[
                tabla1.length + tabla2.length
        ];



        int i = 0;
        int j = 0;
        int k = 0;



        while(i < tabla1.length && j < tabla2.length) {



            if(tabla1[i] < tabla2[j]) {


                resultado[k] = tabla1[i];
                i++;


            }
            else {


                resultado[k] = tabla2[j];
                j++;


            }


            k++;


        }



        while(i < tabla1.length) {


            resultado[k] = tabla1[i];

            i++;
            k++;


        }



        while(j < tabla2.length) {


            resultado[k] = tabla2[j];

            j++;
            k++;


        }



        JOptionPane.showMessageDialog(
                null,
                "Tabla fusionada:\n" +
                Arrays.toString(resultado)
        );


    }
        /*
     * 5.7
     * Fusionar dos tablas ordenadas utilizando algoritmo Merge.
     */
    public void ejercicio57() {


        int[] tabla1 = {2, 6, 8, 12, 15, 20};

        int[] tabla2 = {1, 3, 5, 10, 14, 18};



        int[] fusion = new int[
                tabla1.length + tabla2.length
        ];



        int i = 0;
        int j = 0;
        int k = 0;



        while(i < tabla1.length && j < tabla2.length) {


            if(tabla1[i] <= tabla2[j]) {


                fusion[k] = tabla1[i];
                i++;


            }
            else {


                fusion[k] = tabla2[j];
                j++;


            }


            k++;


        }



        while(i < tabla1.length) {


            fusion[k] = tabla1[i];

            i++;
            k++;


        }



        while(j < tabla2.length) {


            fusion[k] = tabla2[j];

            j++;
            k++;


        }



        JOptionPane.showMessageDialog(
                null,
                "Resultado Merge:\n" +
                Arrays.toString(fusion)
        );


    }





    /*
     * 5.8
     * Juego de combinación secreta.
     * La combinación está formada por números del 1 al 5.
     */
    public void ejercicio58() {


        int longitud = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Ingrese longitud de combinación"
                )
        );



        int[] secreta = new int[longitud];

        int[] jugador = new int[longitud];



        Random random = new Random();



        for(int i = 0; i < longitud; i++) {


            secreta[i] = random.nextInt(5) + 1;


        }



        boolean acertado = false;



        while(!acertado) {



            for(int i = 0; i < longitud; i++) {


                jugador[i] = Integer.parseInt(
                        JOptionPane.showInputDialog(
                                "Ingrese número " + (i + 1)
                        )
                );


            }



            String pista = "";



            acertado = true;



            for(int i = 0; i < longitud; i++) {



                if(jugador[i] > secreta[i]) {


                    pista += "Posición " + i +
                            ": menor\n";

                    acertado = false;


                }
                else if(jugador[i] < secreta[i]) {


                    pista += "Posición " + i +
                            ": mayor\n";

                    acertado = false;


                }
                else {


                    pista += "Posición " + i +
                            ": correcto\n";


                }


            }



            JOptionPane.showMessageDialog(
                    null,
                    pista
            );


        }



        JOptionPane.showMessageDialog(
                null,
                "¡Combinación correcta!"
        );


    }





    /*
     * 5.9
     * Buscar elementos en una tabla.
     */
    public void ejercicio59() {


        int[] tabla = {4, 8, 15, 16, 23, 42};



        int buscar = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Número a buscar"
                )
        );



        boolean encontrado = false;

        int posicion = -1;



        for(int i = 0; i < tabla.length; i++) {



            if(tabla[i] == buscar) {


                encontrado = true;

                posicion = i;

                break;


            }


        }



        if(encontrado) {


            JOptionPane.showMessageDialog(
                    null,
                    "Número encontrado en índice: " 
                    + posicion
            );


        }
        else {


            JOptionPane.showMessageDialog(
                    null,
                    "El número no existe en la tabla"
            );


        }


    }
        /*
     * 5.10
     * Ordenación ascendente de una tabla.
     */
    public void ejercicio510() {


        int cantidad = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Cantidad de números"
                )
        );


        int[] tabla = new int[cantidad];



        for(int i = 0; i < tabla.length; i++) {


            tabla[i] = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            "Ingrese número " + (i + 1)
                    )
            );


        }



        for(int i = 0; i < tabla.length - 1; i++) {


            for(int j = 0; j < tabla.length - 1 - i; j++) {



                if(tabla[j] > tabla[j + 1]) {


                    int aux = tabla[j];

                    tabla[j] = tabla[j + 1];

                    tabla[j + 1] = aux;


                }


            }


        }



        JOptionPane.showMessageDialog(
                null,
                "Orden ascendente:\n" +
                Arrays.toString(tabla)
        );


    }





    /*
     * 5.11
     * Ordenación descendente de una tabla.
     */
    public void ejercicio511() {


        int cantidad = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Cantidad de números"
                )
        );



        int[] tabla = new int[cantidad];



        for(int i = 0; i < tabla.length; i++) {


            tabla[i] = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            "Ingrese número " + (i + 1)
                    )
            );


        }




        for(int i = 0; i < tabla.length - 1; i++) {


            for(int j = 0; j < tabla.length - 1 - i; j++) {



                if(tabla[j] < tabla[j + 1]) {



                    int aux = tabla[j];

                    tabla[j] = tabla[j + 1];

                    tabla[j + 1] = aux;


                }


            }


        }



        JOptionPane.showMessageDialog(
                null,
                "Orden descendente:\n" +
                Arrays.toString(tabla)
        );


    }





    /*
     * 5.12
     * Buscar el mayor y menor elemento de una tabla.
     */
    public void ejercicio512() {


        int cantidad = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Cantidad de números"
                )
        );



        int[] tabla = new int[cantidad];



        for(int i = 0; i < tabla.length; i++) {


            tabla[i] = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            "Ingrese número " + (i + 1)
                    )
            );


        }




        int mayor = tabla[0];

        int menor = tabla[0];



        for(int i = 1; i < tabla.length; i++) {



            if(tabla[i] > mayor) {


                mayor = tabla[i];


            }



            if(tabla[i] < menor) {


                menor = tabla[i];


            }


        }




        JOptionPane.showMessageDialog(
                null,
                "Mayor: " + mayor +
                "\nMenor: " + menor
        );


    }
        /*
     * 5.13
     * Contar repeticiones de elementos.
     */
    public void ejercicio513() {


        int[] tabla = {5, 3, 5, 8, 3, 5, 10};



        String resultado = "Repeticiones:\n";



        boolean[] revisado = new boolean[tabla.length];



        for(int i = 0; i < tabla.length; i++) {


            if(!revisado[i]) {


                int contador = 1;


                for(int j = i + 1; j < tabla.length; j++) {


                    if(tabla[i] == tabla[j]) {


                        contador++;

                        revisado[j] = true;


                    }


                }



                resultado += tabla[i] + 
                        " aparece " + contador +
                        " veces\n";


            }


        }



        JOptionPane.showMessageDialog(
                null,
                resultado
        );


    }





    /*
     * 5.14
     * Eliminar elementos repetidos de una tabla.
     */
    public void ejercicio514() {


        int[] tabla = {2, 4, 2, 6, 8, 4, 10};



        ArrayList<Integer> nuevaTabla = new ArrayList<>();



        for(int i = 0; i < tabla.length; i++) {


            if(!nuevaTabla.contains(tabla[i])) {


                nuevaTabla.add(tabla[i]);


            }


        }



        JOptionPane.showMessageDialog(
                null,
                "Tabla sin repetidos:\n" +
                nuevaTabla
        );


    }





    /*
     * 5.15
     * Realizar estadísticas sobre una tabla.
     */
    public void ejercicio515() {


        int cantidad = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Cantidad de números"
                )
        );



        int[] tabla = new int[cantidad];



        for(int i = 0; i < tabla.length; i++) {


            tabla[i] = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            "Ingrese número " + (i + 1)
                    )
            );


        }



        int suma = 0;

        int mayor = tabla[0];

        int menor = tabla[0];



        for(int i = 0; i < tabla.length; i++) {


            suma += tabla[i];



            if(tabla[i] > mayor) {


                mayor = tabla[i];


            }



            if(tabla[i] < menor) {


                menor = tabla[i];


            }


        }



        double promedio = (double)suma / tabla.length;



        String resultado =
                "Suma: " + suma +
                "\nPromedio: " + promedio +
                "\nMayor: " + mayor +
                "\nMenor: " + menor;



        JOptionPane.showMessageDialog(
                null,
                resultado
        );


    }


}