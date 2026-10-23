
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaEmbudo extends JFrame {

    private static final Color AZUL_OSCURO = new Color(22, 50, 79);
    private static final Color AZUL_CLARO = new Color(234, 242, 248);
    private static final Color FONDO = new Color(245, 247, 250);
    private static final Color TEXTO = new Color(45, 55, 72);

    private final JTextField txtAltura = new JTextField("12");
    private final JTextField txtRadioInferior = new JTextField("0.5");
    private final JTextField txtRadioSuperior = new JTextField("5.0");
    private final JTextField txtEspesor = new JTextField("0.1");
    private final JTextField txtDensidad = new JTextField("7.93");

    private final JTextArea resultados = new JTextArea();

    private final CalculadoraEmbudo calculadora = new CalculadoraEmbudo();
    private final GraficaEmbudo grafica = new GraficaEmbudo();

    public VentanaEmbudo() {
        setTitle("Calculadora de Embudo - Calculo II");
        setSize(950, 780);
        setMinimumSize(new Dimension(780, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        construirInterfaz();
        calcular();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "CALCULADORA DE EMBUDO",
                SwingConstants.CENTER);

        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setOpaque(true);
        titulo.setBackground(AZUL_OSCURO);
        titulo.setBorder(new EmptyBorder(18, 10, 18, 10));

        add(titulo, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 14));

        pestanas.addTab("Calculadora", crearPanelCalculadora());
        pestanas.addTab("Grafica", crearPanelGrafica());

        add(pestanas, BorderLayout.CENTER);
    }

    private JPanel crearPanelCalculadora() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(FONDO);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel datos = new JPanel(new GridLayout(0, 2, 12, 12));
        datos.setBackground(Color.WHITE);
        datos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos del embudo"),
                new EmptyBorder(12, 12, 12, 12)));

        agregarCampo(datos, "Altura (cm):", txtAltura);
        agregarCampo(datos, "Radio exterior inferior (cm):",
                txtRadioInferior);
        agregarCampo(datos, "Radio exterior superior (cm):",
                txtRadioSuperior);
        agregarCampo(datos, "Espesor (cm):", txtEspesor);
        agregarCampo(datos, "Densidad (g/cm3):", txtDensidad);

        JButton btnCalcular = new JButton("Calcular resultados");
        btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCalcular.setBackground(AZUL_OSCURO);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFocusPainted(false);
        btnCalcular.setOpaque(true);
        btnCalcular.setBorderPainted(false);
        btnCalcular.addActionListener(e -> calcular());

        JButton btnRestablecer = new JButton("Datos originales");
        btnRestablecer.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRestablecer.setBackground(AZUL_CLARO);
        btnRestablecer.setForeground(AZUL_OSCURO);
        btnRestablecer.setFocusPainted(false);
        btnRestablecer.addActionListener(e -> restablecer());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        botones.setBackground(FONDO);
        botones.add(btnCalcular);
        botones.add(btnRestablecer);

        JPanel superior = new JPanel(new BorderLayout(8, 8));
        superior.setBackground(FONDO);
        superior.add(datos, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        resultados.setEditable(false);
        resultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultados.setForeground(TEXTO);
        resultados.setBackground(Color.WHITE);
        resultados.setMargin(new Insets(14, 14, 14, 14));

        JScrollPane scroll = new JScrollPane(resultados);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Resultados y procedimiento matematico"));

        panel.add(superior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelGrafica() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(FONDO);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel descripcion = new JLabel(
                "<html><center>"
                        + "Perfil longitudinal del embudo<br>"
                        + "Azul: radio exterior | Verde: radio interior | Gris: acero"
                        + "</center></html>",
                SwingConstants.CENTER);

        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descripcion.setForeground(AZUL_OSCURO);
        descripcion.setBorder(new EmptyBorder(5, 5, 10, 5));

        panel.add(descripcion, BorderLayout.NORTH);
        panel.add(grafica, BorderLayout.CENTER);

        JLabel nota = new JLabel(
                "La grafica representa las funciones R(x) y r(x) "
                        + "en el intervalo de 0 a la altura.",
                SwingConstants.CENTER);

        nota.setForeground(TEXTO);
        nota.setBorder(new EmptyBorder(8, 5, 5, 5));
        panel.add(nota, BorderLayout.SOUTH);

        return panel;
    }

    private void agregarCampo(
            JPanel panel, String etiqueta, JTextField campo) {

        JLabel label = new JLabel(etiqueta);
        label.setForeground(AZUL_OSCURO);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));

        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setForeground(TEXTO);
        campo.setBackground(AZUL_CLARO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 216, 228)),
                new EmptyBorder(7, 8, 7, 8)));

        panel.add(label);
        panel.add(campo);
    }

    private void restablecer() {
        txtAltura.setText("12");
        txtRadioInferior.setText("0.5");
        txtRadioSuperior.setText("5.0");
        txtEspesor.setText("0.1");
        txtDensidad.setText("7.93");

        calcular();
    }

    private void calcular() {
        try {
            double altura = leer(txtAltura);
            double radioInf = leer(txtRadioInferior);
            double radioSup = leer(txtRadioSuperior);
            double espesor = leer(txtEspesor);
            double densidad = leer(txtDensidad);

            String texto = calculadora.calcular(
                    altura,
                    radioInf,
                    radioSup,
                    espesor,
                    densidad);

            resultados.setText(texto);
            resultados.setCaretPosition(0);

            // Actualizar la grafica con los datos ingresados
            grafica.actualizarDatos(
                    altura, radioInf, radioSup, espesor);

        } catch (NumberFormatException ex) {
            mostrarError(
                    "Ingresa numeros validos en todos los campos.",
                    "Datos incorrectos");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage(), "Revisa los datos");
        }
    }

    private double leer(JTextField campo) {
        return Double.parseDouble(
                campo.getText().trim().replace(',', '.'));
    }

    private void mostrarError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                titulo,
                JOptionPane.WARNING_MESSAGE);
    }
}
