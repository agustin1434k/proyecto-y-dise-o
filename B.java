public class EjercicioB extends JFrame {
    public EjercicioB() {
        JTextField input = new JTextField(10);
        JButton btn = new JButton("Calcular");
        JLabel res = new JLabel("m/s: ");

        btn.addActionListener(e -> {
            double kmh = Double.parseDouble(input.getText());
            res.setText("m/s: " + (kmh / 3.6));
        });

        JPanel p = new JPanel();
        p.add(new JLabel("Km/h: ")); p.add(input); p.add(btn); p.add(res);
        add(p); setSize(400, 100); setVisible(true);
    }
    public static void main(String[] args) { new EjercicioB(); }
}