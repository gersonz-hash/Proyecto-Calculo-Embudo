
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Line2D;

public class GraficaEmbudo extends JPanel {

    private double altura = 12.0;
    private double radioBase = 0.5;
    private double radioSuperior = 5.0;
    private double grosor = 0.1;

    private final int margenIzquierdo = 85;
    private final int margenDerecho = 55;
    private final int margenSuperior = 100;
    private final int margenInferior = 90;

    public GraficaEmbudo() {
        setPreferredSize(new Dimension(900, 650));
        setBackground(Color.WHITE);
    }

    public GraficaEmbudo(double altura, double radioBase,
                         double radioSuperior, double grosor) {
        this();
        actualizarMedidas(altura, radioBase, radioSuperior, grosor);
    }

    public void actualizarDatos(double altura, double radioBase,
                                double radioSuperior, double grosor) {
        actualizarMedidas(altura, radioBase, radioSuperior, grosor);
    }

    public void actualizarMedidas(double altura, double radioBase,
                                  double radioSuperior, double grosor) {
        this.altura = altura;
        this.radioBase = radioBase;
        this.radioSuperior = radioSuperior;
        this.grosor = grosor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int ancho = getWidth();
        int alto = getHeight();

        // Título
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        String titulo = "Grafica del Embudo respecto al eje X";
        FontMetrics fmTitulo = g2.getFontMetrics();
        g2.drawString(
                titulo,
                (ancho - fmTitulo.stringWidth(titulo)) / 2,
                30
        );

        // Información superior distribuida en dos filas
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString(
                String.format("Altura: %.2f", altura),
                margenIzquierdo,
                55
        );
        g2.drawString(
                String.format("Radio superior: %.2f", radioSuperior),
                margenIzquierdo + 190,
                55
        );

        g2.drawString(
                String.format("Radio base: %.2f", radioBase),
                margenIzquierdo,
                77
        );
        g2.drawString(
                String.format("Grosor: %.2f", grosor),
                margenIzquierdo + 190,
                77
        );

        // Área de dibujo
        int xInicio = margenIzquierdo;
        int xFin = ancho - margenDerecho;
        int yInicio = margenSuperior;
        int yFin = alto - margenInferior;

        int anchoGrafica = xFin - xInicio;
        int altoGrafica = yFin - yInicio;

        if (anchoGrafica <= 0 || altoGrafica <= 0) {
            g2.dispose();
            return;
        }

        // Escala: altura horizontal y radio vertical
        double radioMaximo = Math.max(radioSuperior, radioBase);
        radioMaximo = Math.max(radioMaximo, radioSuperior + grosor);
        radioMaximo = Math.max(radioMaximo, radioBase + grosor);

        if (altura <= 0 || radioMaximo <= 0) {
            g2.setColor(Color.RED);
            g2.drawString(
                    "Ingrese medidas mayores que cero.",
                    xInicio,
                    yInicio + 20
            );
            g2.dispose();
            return;
        }

        double escalaX = anchoGrafica / altura;
        double escalaY = (altoGrafica * 0.80) / (2.0 * radioMaximo);

        double escala = Math.min(escalaX, escalaY);

        // Centrar el embudo dentro del área
        double largoDibujo = altura * escala;
        double altoDibujo = 2.0 * radioMaximo * escala;

        double inicioX = xInicio + (anchoGrafica - largoDibujo) / 2.0;
        double centroY = yInicio + (altoGrafica - altoDibujo) / 2.0
                + radioMaximo * escala;

        // Cuadrícula
        g2.setColor(new Color(230, 230, 230));
        g2.setStroke(new BasicStroke(1));

        for (int i = 0; i <= 10; i++) {
            int x = xInicio + i * anchoGrafica / 10;
            int y = yInicio + i * altoGrafica / 10;

            g2.drawLine(x, yInicio, x, yFin);
            g2.drawLine(xInicio, y, xFin, y);
        }

        // Ejes
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(xInicio, (int) centroY, xFin, (int) centroY);
        g2.drawLine(xInicio, yInicio, xInicio, yFin);

        // Coordenadas del exterior
        double x0 = inicioX;
        double x1 = inicioX + altura * escala;

        double yBaseArriba = centroY - radioBase * escala;
        double yBaseAbajo = centroY + radioBase * escala;

        double ySuperiorArriba = centroY - radioSuperior * escala;
        double ySuperiorAbajo = centroY + radioSuperior * escala;

        // Grosor de la pared
        double radioBaseInterior = Math.max(0, radioBase - grosor);
        double radioSuperiorInterior = Math.max(0, radioSuperior - grosor);

        double yBaseInteriorArriba =
                centroY - radioBaseInterior * escala;
        double yBaseInteriorAbajo =
                centroY + radioBaseInterior * escala;

        double ySuperiorInteriorArriba =
                centroY - radioSuperiorInterior * escala;
        double ySuperiorInteriorAbajo =
                centroY + radioSuperiorInterior * escala;

        // Cuerpo exterior
        Path2D exterior = new Path2D.Double();
        exterior.moveTo(x0, yBaseArriba);
        exterior.lineTo(x1, ySuperiorArriba);
        exterior.lineTo(x1, ySuperiorAbajo);
        exterior.lineTo(x0, yBaseAbajo);
        exterior.closePath();

        g2.setColor(new Color(100, 160, 230));
        g2.fill(exterior);

        // Interior del embudo
        Path2D interior = new Path2D.Double();
        interior.moveTo(x0, yBaseInteriorArriba);
        interior.lineTo(x1, ySuperiorInteriorArriba);
        interior.lineTo(x1, ySuperiorInteriorAbajo);
        interior.lineTo(x0, yBaseInteriorAbajo);
        interior.closePath();

        g2.setColor(Color.WHITE);
        g2.fill(interior);

        // Bordes exteriores
        g2.setColor(new Color(30, 80, 160));
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(new Line2D.Double(x0, yBaseArriba, x1, ySuperiorArriba));
        g2.draw(new Line2D.Double(x0, yBaseAbajo, x1, ySuperiorAbajo));
        g2.draw(new Line2D.Double(x1, ySuperiorArriba,
                x1, ySuperiorAbajo));

        // Bordes interiores
        g2.setColor(new Color(30, 140, 80));
        g2.setStroke(new BasicStroke(1.8f));
        g2.draw(new Line2D.Double(
                x0, yBaseInteriorArriba,
                x1, ySuperiorInteriorArriba
        ));
        g2.draw(new Line2D.Double(
                x0, yBaseInteriorAbajo,
                x1, ySuperiorInteriorAbajo
        ));

        // Marcas de altura en el eje X
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 11));

        for (int i = 0; i <= 5; i++) {
            double valor = altura * i / 5.0;
            int x = (int) (inicioX + valor * escala);

            g2.drawLine(x, (int) centroY - 4,
                    x, (int) centroY + 4);

            String texto = String.format("%.1f", valor);
            g2.drawString(texto, x - 10, (int) centroY + 20);
        }

        // Etiquetas de radios a los extremos, sin amontonarlas
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.setColor(new Color(30, 80, 160));

        g2.drawString(
                String.format("r = %.2f", radioBase),
                (int) x0 - 5,
                (int) yBaseArriba - 10
        );

        g2.drawString(
                String.format("R = %.2f", radioSuperior),
                (int) x1 - 65,
                (int) ySuperiorArriba - 10
        );

        // Nombre del eje X
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(Color.BLACK);
        g2.drawString(
                "Altura (x)",
                (xInicio + xFin) / 2 - 30,
                yFin + 35
        );

        // Nombre del eje Y
        g2.rotate(-Math.PI / 2);
        g2.drawString(
                "Radio (y)",
                -(yInicio + yFin) / 2 - 30,
                xInicio - 55
        );
        g2.rotate(Math.PI / 2);

        // Leyenda inferior
        int leyendaY = alto - 25;

        g2.setColor(new Color(100, 160, 230));
        g2.fillRect(xInicio, leyendaY - 10, 15, 12);
        g2.setColor(Color.BLACK);
        g2.drawString("Material", xInicio + 22, leyendaY);

        g2.setColor(Color.WHITE);
        g2.fillRect(xInicio + 120, leyendaY - 10, 15, 12);
        g2.setColor(Color.BLACK);
        g2.drawRect(xInicio + 120, leyendaY - 10, 15, 12);
        g2.drawString("Interior", xInicio + 142, leyendaY);

        g2.dispose();
    }
}
