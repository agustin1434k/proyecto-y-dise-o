public class EjercicioE extends JFrame {
    public EjercicioE() {
        JTextField ta = new JTextField(3), tb = new JTextField(3), tc = new JTextField(3);
        JButton btn = new JButton("Calcular");
        JLabel res = new JLabel("Área: ");

        btn.addActionListener(e -> {
            double a = Double.parseDouble(ta.getText()), b = Double.parseDouble(tb.getText()), c = Double.parseDouble(tc.getText());
            double p = (a + b + c) / 2;
            res.setText("Área: " + Math.sqrt(p * (p - a) * (p - b) * (p - c)));
        });

        JPanel p = new JPanel();
        p.add(new JLabel("Lados:")); p.add(ta); p.add(tb); p.add(tc); p.add(btn); p.add(res);
        add(p); setSize(500, 100); setVisible(true);
    }
    public static void main(String[] args) { new EjercicioE(); }
}