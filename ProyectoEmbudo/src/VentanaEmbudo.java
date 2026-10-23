
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaEmbudo extends JFrame {

    private static final Color AZUL_OSCURO = new Color(22, 50, 79);
    private static final Color AZUL_CLARO = new Color(234, 242, 248);
    private static final Color VERDE = new Color(22, 101, 52);
    private static final Color FONDO = new Color(245, 247, 250);
    private static final Color TEXTO = new Color(45, 55, 72);

    private final JTextField txtAltura = new JTextField("12");
    private final JTextField txtRadioInferior = new JTextField("0.5");
    private final JTextField txtRadioSuperior = new JTextField("5.0");
    private final JTextField txtEspesor = new JTextField("0.1");
    private final JTextField txtDensidad = new JTextField("7.93");

    private final JTextArea resultados = new JTextArea();
    private final CalculadoraEmbudo calculadora = new CalculadoraEmbudo();

    public VentanaEmbudo() {
        setTitle("Calculadora de Embudo - Calculo II");
        setSize(900, 760);
        setMinimumSize(new Dimension(750, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);

        construirInterfaz();
        calcular();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(0, 0));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(AZUL_OSCURO);
        encabezado.setBorder(new EmptyBorder(22, 20, 20, 20));

        JLabel titulo = new JLabel(
                "CALCULADORA DE EMBUDO",
                SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel(
                "Proyecto de Calculo II | Ingenieria en Sistemas",
                SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(220, 234, 250));

        encabezado.add(titulo, BorderLayout.CENTER);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(12, 12));
        contenido.setBackground(FONDO);
        contenido.setBorder(new EmptyBorder(16, 18, 18, 18));

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
        btnCalcular.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalcular.addActionListener(e -> calcular());

        JButton btnRestablecer = new JButton("Datos originales");
        btnRestablecer.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRestablecer.setBackground(AZUL_CLARO);
        btnRestablecer.setForeground(AZUL_OSCURO);
        btnRestablecer.setFocusPainted(false);
        btnRestablecer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRestablecer.addActionListener(e -> restablecer());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        botones.setBackground(FONDO);
        botones.add(btnCalcular);
        botones.add(btnRestablecer);

        JPanel panelSuperior = new JPanel(new BorderLayout(8, 8));
        panelSuperior.setBackground(FONDO);
        panelSuperior.add(datos, BorderLayout.CENTER);
        panelSuperior.add(botones, BorderLayout.SOUTH);

        resultados.setEditable(false);
        resultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultados.setForeground(TEXTO);
        resultados.setBackground(Color.WHITE);
        resultados.setMargin(new Insets(14, 14, 14, 14));
        resultados.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(resultados);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Resultados y procedimiento matematico"));

        contenido.add(panelSuperior, BorderLayout.NORTH);
        contenido.add(scroll, BorderLayout.CENTER);
        add(contenido, BorderLayout.CENTER);
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
                    altura, radioInf, radioSup, espesor, densidad);

            resultados.setText(texto);
            resultados.setCaretPosition(0);

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
