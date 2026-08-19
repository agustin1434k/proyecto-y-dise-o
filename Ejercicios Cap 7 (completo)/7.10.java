// 7.10. Hemos recibido un encargo para definir los paquetes y las clases
// necesarias para gestionar una empresa ferroviaria.
// Se distinguen dos grandes grupos: el personal y la maquinaria.
//
// PERSONAL:
// Maquinistas: nombre completo, DNI, sueldo mensual y rango.
// Mecánicos: nombre completo, teléfono y especialidad.
// Jefes de estación: nombre completo y DNI.
//
// MAQUINARIA:
// Vagones: capacidad máxima de carga, capacidad actual y tipo de mercancía.
// Locomotoras: matrícula, potencia, año de fabricación y mecánico asignado.
// Trenes: una locomotora, máximo 5 vagones y un maquinista.
//
// Las clases del personal serán públicas.
// Solo se podrán construir desde clases externas objetos Tren y Locomotora.
// La clase Vagon será visible solamente por sus clases vecinas.

class Maquinista {

    private String nombre;
    private String dni;
    private double sueldo;
    private String rango;

    public Maquinista(String nombre, String dni,
                      double sueldo, String rango) {

        this.nombre = nombre;
        this.dni = dni;
        this.sueldo = sueldo;
        this.rango = rango;
    }
}

class Mecanico {

    private String nombre;
    private String telefono;
    private String especialidad;

    public Mecanico(String nombre, String telefono,
                    String especialidad) {

        this.nombre = nombre;
        this.telefono = telefono;
        this.especialidad = especialidad;
    }
}

class JefeEstacion {

    private String nombre;
    private String dni;

    public JefeEstacion(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
    }
}

// Clase Vagon
class Vagon {

    private double capacidadMaxima;
    private double cargaActual;
    private String tipoMercancia;

    public Vagon(double capacidadMaxima,
                 double cargaActual,
                 String tipoMercancia) {

        this.capacidadMaxima = capacidadMaxima;
        this.cargaActual = cargaActual;
        this.tipoMercancia = tipoMercancia;
    }
}

// Clase Locomotora
class Locomotora {

    private String matricula;
    private double potencia;
    private int añoFabricacion;
    private Mecanico mecanico;

    public Locomotora(String matricula,
                      double potencia,
                      int añoFabricacion,
                      Mecanico mecanico) {

        this.matricula = matricula;
        this.potencia = potencia;
        this.añoFabricacion = añoFabricacion;
        this.mecanico = mecanico;
    }
}

// Clase Tren
class Tren {

    private Locomotora locomotora;
    private Vagon[] vagones;
    private int cantidadVagones;
    private Maquinista maquinista;

    public Tren(Locomotora locomotora,
                Maquinista maquinista) {

        this.locomotora = locomotora;
        this.maquinista = maquinista;

        // Un tren puede tener como máximo 5 vagones
        vagones = new Vagon[5];
        cantidadVagones = 0;
    }

    public void añadirVagon(Vagon vagon) {

        if (cantidadVagones < 5) {
            vagones[cantidadVagones] = vagon;
            cantidadVagones++;
        }
    }
}

public class Main {

    public static void main(String[] args) {

        // Creamos un maquinista
        Maquinista maquinista = new Maquinista(
                "Agustin Ugalde",
                "12345678",
                500000,
                "Principal"
        );

        // Creamos un mecánico
        Mecanico mecanico = new Mecanico(
                "Juan Perez",
                "1122334455",
                "Electricidad"
        );

        // Creamos un jefe de estación
        JefeEstacion jefe = new JefeEstacion(
                "Carlos Gomez",
                "87654321"
        );

        // Creamos una locomotora
        Locomotora locomotora = new Locomotora(
                "ABC-123",
                5000,
                2015,
                mecanico
        );

        // Creamos un tren
        Tren tren = new Tren(
                locomotora,
                maquinista
        );

        // Creamos los vagones
        Vagon vagon1 = new Vagon(
                20000,
                15000,
                "Grano"
        );

        Vagon vagon2 = new Vagon(
                25000,
                20000,
                "Carbón"
        );

        // Añadimos los vagones al tren
        tren.añadirVagon(vagon1);
        tren.añadirVagon(vagon2);

        System.out.println("EMPRESA FERROVIARIA");
        System.out.println("-------------------");

        System.out.println("Maquinista creado correctamente.");
        System.out.println("Mecánico creado correctamente.");
        System.out.println("Jefe de estación creado correctamente.");
        System.out.println("Locomotora creada correctamente.");
        System.out.println("Tren creado correctamente.");
        System.out.println("Vagones añadidos correctamente.");
    }
}