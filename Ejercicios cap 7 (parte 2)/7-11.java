import javax.swing.JOptionPane;

class Hora {

    private int hora;
    private int minuto;
    private int segundo;

    public Hora(int hora, int minuto, int segundo) {
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }

    public void siguienteSegundo() {
        segundo++;

        if (segundo == 60) {
            segundo = 0;
            minuto++;

            if (minuto == 60) {
                minuto = 0;
                hora++;

                if (hora == 24) {
                    hora = 0;
                }
            }
        }
    }

    public String mostrarHora() {
        return String.format("%02d:%02d:%02d", hora, minuto, segundo);
    }
}

public class Main {

    public static void main(String[] args) {

        int hora = Integer.parseInt(
            JOptionPane.showInputDialog("Ingrese la hora (0-23):")
        );

        int minuto = Integer.parseInt(
            JOptionPane.showInputDialog("Ingrese los minutos (0-59):")
        );

        int segundo = Integer.parseInt(
            JOptionPane.showInputDialog("Ingrese los segundos (0-59):")
        );

        int n = Integer.parseInt(
            JOptionPane.showInputDialog("Ingrese la cantidad de segundos:")
        );

        if (hora < 0 || hora > 23 ||
            minuto < 0 || minuto > 59 ||
            segundo < 0 || segundo > 59 ||
            n < 0) {

            JOptionPane.showMessageDialog(
                null,
                "Los datos ingresados no son válidos.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Hora reloj = new Hora(hora, minuto, segundo);

        String resultado = "Hora introducida: "
                + reloj.mostrarHora()
                + "\n\n";

        for (int i = 0; i < n; i++) {
            reloj.siguienteSegundo();
            resultado += reloj.mostrarHora() + "\n";
        }

        JOptionPane.showMessageDialog(
            null,
            resultado,
            "Resultado 7.11",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}