public class EjercicioH extends JFrame {
    public EjercicioH() {
        JTextField d = new JTextField(2), m = new JTextField(2), a = new JTextField(4);
        JButton btn = new JButton("Calcular");
        JLabel res = new JLabel("Nº Suerte: ");

        btn.addActionListener(e -> {
            int suma = Integer.parseInt(d.getText()) + Integer.parseInt(m.getText()) + Integer.parseInt(a.getText());
            int suerte = (suma/1000) + (suma/100%10) + (suma/10%10) + (suma%10);
            res.setText("Nº Suerte: " + suerte);
        });

        JPanel p = new JPanel();
        p.add(new JLabel("Fecha (D/M/A):")); p.add(d); p.add(m); p.add(a); p.add(btn); p.add(res);
        add(p); setSize(500, 100); setVisible(true);
    }
    public static void main(String[] args) { new EjercicioH(); }
}