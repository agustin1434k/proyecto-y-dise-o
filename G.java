public class EjercicioG extends JFrame {
    public EjercicioG() {
        JTextField input = new JTextField(6);
        JButton btn = new JButton("Mostrar");
        JTextArea res = new JTextArea(5, 10);

        btn.addActionListener(e -> {
            int n = Integer.parseInt(input.getText());
            res.setText( (n/10000) + "\n" + (n/1000) + "\n" + (n/100) + "\n" + (n/10) + "\n" + n );
        });

        JPanel p = new JPanel();
        p.add(new JLabel("N (5 cifras):")); p.add(input); p.add(btn); p.add(res);
        add(p); setSize(300, 250); setVisible(true);
    }
    public static void main(String[] args) { new EjercicioG(); }
}