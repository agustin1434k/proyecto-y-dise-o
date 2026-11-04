package modelo;

/**
 * POJO (Plain Old Java Object): clase simple con atributos privados,
 * constructores, getters y setters, sin lógica de base de datos.
 * Representa una fila de la tabla "alumnos".
 */
public class Alumno {

    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private int edad;
    private String curso;

    public Alumno() {
    }

    public Alumno(String nombre, String apellido, String email, int edad, String curso) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad = edad;
        this.curso = curso;
    }

    public Alumno(int id, String nombre, String apellido, String email, int edad, String curso) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad = edad;
        this.curso = curso;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    @Override
    public String toString() {
        return String.format("%-4d %-15s %-15s %-28s %-5d %s",
                id, nombre, apellido, email, edad, curso);
    }
}
