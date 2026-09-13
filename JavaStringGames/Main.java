import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MenuPrincipal ventana =
                    new MenuPrincipal();

            ventana.setVisible(true);
        });
    }
}