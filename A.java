public class EjercicioA extends JFrame {
    public EjercicioA() {
        JTextField input = new JTextField(10);
        JButton btn = new JButton("Convertir");
        JLabel res = new JLabel("Fahrenheit: ");

        btn.addActionListener(e -> {
            double c = Double.parseDouble(input.getText());
            res.setText("Fahrenheit: " + (32 + (9 * c / 5)));
        });

        JPanel p = new JPanel();
        p.add(new JLabel("Celsius: ")); p.add(input); p.add(btn); p.add(res);
        add(p); setSize(400, 100); setVisible(true);
    }
    public static void main(String[] args) { new EjercicioA(); }
}