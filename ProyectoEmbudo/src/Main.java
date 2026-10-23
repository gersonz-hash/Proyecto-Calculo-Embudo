import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaEmbudo ventana = new VentanaEmbudo();
            ventana.setVisible(true);
        });
    }
}