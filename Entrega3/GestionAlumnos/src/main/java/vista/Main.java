package vista;

import dao.AlumnoDAO;
import modelo.Alumno;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final AlumnoDAO dao = new AlumnoDAO();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elija una opción: ");
            switch (opcion) {
                case 1: listarAlumnos(); break;
                case 2: agregarAlumno(); break;
                case 3: modificarAlumno(); break;
                case 4: eliminarAlumno(); break;
                case 5: buscarAlumno(); break;
                case 6: System.out.println("Programa finalizado."); break;
                default: System.out.println("Opción inválida. Elija un número del 1 al 6.");
            }
        } while (opcion != 6);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=================================");
        System.out.println("GESTIÓN DE ALUMNOS");
        System.out.println("=================================");
        System.out.println("1. Listar alumnos");
        System.out.println("2. Agregar alumno");
        System.out.println("3. Modificar alumno");
        System.out.println("4. Eliminar alumno");
        System.out.println("5. Buscar alumno");
        System.out.println("6. Salir");
    }

    // ---------- Opciones del menú ----------

    private static void listarAlumnos() {
        List<Alumno> lista = dao.listar();
        mostrarLista(lista, "No hay alumnos registrados.");
        if (!lista.isEmpty()) {
            System.out.println("\nCantidad total de alumnos: " + dao.cantidadAlumnos());
            System.out.printf("Promedio de edad: %.2f%n", dao.promedioEdad());
        }
    }

    private static void agregarAlumno() {
        System.out.println("\n--- Agregar alumno ---");
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String email = leerTextoObligatorio("Email: ");
        int edad = leerEdad("Edad: ");
        String curso = leerTextoObligatorio("Curso: ");

        Alumno a = new Alumno(nombre, apellido, email, edad, curso);
        if (dao.insertar(a)) {
            System.out.println("Alumno agregado correctamente.");
        } else {
            System.out.println("No se pudo agregar el alumno.");
        }
    }

    private static void modificarAlumno() {
        System.out.println("\n--- Modificar alumno ---");
        int id = leerEntero("ID del alumno a modificar: ");
        if (id <= 0 || dao.buscarPorId(id) == null) {
            System.out.println("No se encontró un alumno con ese ID.");
            return;
        }
        String nombre = leerTextoObligatorio("Nuevo nombre: ");
        String apellido = leerTextoObligatorio("Nuevo apellido: ");
        String email = leerTextoObligatorio("Nuevo email: ");
        int edad = leerEdad("Nueva edad: ");
        String curso = leerTextoObligatorio("Nuevo curso: ");

        Alumno a = new Alumno(id, nombre, apellido, email, edad, curso);
        if (dao.actualizar(a)) {
            System.out.println("Alumno modificado correctamente.");
        } else {
            System.out.println("No se pudo modificar el alumno.");
        }
    }

    private static void eliminarAlumno() {
        System.out.println("\n--- Eliminar alumno ---");
        int id = leerEntero("ID del alumno a eliminar: ");
        Alumno a = (id > 0) ? dao.buscarPorId(id) : null;
        if (a == null) {
            System.out.println("No se encontró un alumno con ese ID.");
            return;
        }
        System.out.println("Alumno seleccionado: " + a.getNombre() + " " + a.getApellido());

        String respuesta;
        while (true) {
            System.out.print("¿Está seguro de que desea eliminar este alumno? (S/N): ");
            respuesta = sc.nextLine().trim();
            if (respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("N")) {
                break;
            }
            System.out.println("Respuesta inválida. Escriba S o N.");
        }

        if (respuesta.equalsIgnoreCase("S")) {
            if (dao.eliminar(id)) {
                System.out.println("Alumno eliminado correctamente.");
            } else {
                System.out.println("No se pudo eliminar el alumno.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void buscarAlumno() {
        System.out.println("\n--- Buscar alumno ---");
        System.out.println("1. Por apellido (o parte del apellido)");
        System.out.println("2. Por curso");
        int tipo = leerEntero("Elija el tipo de búsqueda: ");
        if (tipo == 1) {
            String texto = leerTextoObligatorio("Apellido a buscar: ");
            mostrarLista(dao.buscarPorApellido(texto), "No se encontraron coincidencias.");
        } else if (tipo == 2) {
            String curso = leerTextoObligatorio("Curso a buscar: ");
            mostrarLista(dao.buscarPorCurso(curso), "No hay alumnos en ese curso.");
        } else {
            System.out.println("Opción inválida.");
        }
    }

    // ---------- Utilidades de entrada y salida ----------

    private static void mostrarLista(List<Alumno> lista, String mensajeVacio) {
        if (lista.isEmpty()) {
            System.out.println(mensajeVacio);
            return;
        }
        System.out.println();
        System.out.printf("%-4s %-15s %-15s %-28s %-5s %s%n",
                "ID", "Nombre", "Apellido", "Email", "Edad", "Curso");
        System.out.println("-".repeat(85));
        for (Alumno a : lista) {
            System.out.println(a);
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    private static int leerEdad(String mensaje) {
        while (true) {
            int edad = leerEntero(mensaje);
            if (edad > 0) {
                return edad;
            }
            System.out.println("La edad debe ser mayor que cero.");
        }
    }

    private static String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este campo no puede estar vacío.");
        }
    }
}
