public class EjercicioD extends JFrame {
    public EjercicioD() {
        JTextField radio = new JTextField(10);
        JButton btn = new JButton("Calcular");
        JLabel res = new JLabel("Volumen: ");

        btn.addActionListener(e -> {
            double r = Double.parseDouble(radio.getText());
            res.setText("Volumen: " + (4.0/3.0 * Math.PI * Math.pow(r, 3)));
        });

        JPanel p = new JPanel();
        p.add(new JLabel("Radio: ")); p.add(radio); p.add(btn); p.add(res);
        add(p); setSize(400, 100); setVisible(true);
    }
    public static void main(String[] args) { new EjercicioD(); }