public class EjercicioC extends JFrame {
    public EjercicioC() {
        JTextField c1 = new JTextField(5), c2 = new JTextField(5);
        JButton btn = new JButton("Calcular");
        JLabel res = new JLabel("H: ");

        btn.addActionListener(e -> {
            double a = Double.parseDouble(c1.getText());
            double b = Double.parseDouble(c2.getText());
            res.setText("H: " + Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)));
        });

        JPanel p = new JPanel();
        p.add(new JLabel("C1: ")); p.add(c1); p.add(new JLabel("C2: ")); p.add(c2); p.add(btn); p.add(res);
        add(p); setSize(500, 100); setVisible(true);
    }
    public static void main(String[] args) { new EjercicioC(); }
}