public class EjercicioF extends JFrame {
    public EjercicioF() {
        JTextField input = new JTextField(5);
        JButton btn = new JButton("Ver Cifras");
        JLabel res = new JLabel("Resultado: ");

        btn.addActionListener(e -> {
            int n = Integer.parseInt(input.getText());
            res.setText("Cifras: " + (n/100) + " - " + (n/10)%10 + " - " + (n%10));
        });

        JPanel p = new JPanel();
        p.add(new JLabel("N (3 cifras):")); p.add(input); p.add(btn); p.add(res);
        add(p); setSize(400, 100); setVisible(true);
    }
    public static void main(String[] args) { new EjercicioF(); }
}