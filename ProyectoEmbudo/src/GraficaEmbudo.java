import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GraficaEmbudo extends JPanel {

    private double altura = 12.0;
    private double radioInf = 0.5;
    private double radioSup = 5.0;
    private double espesor = 0.1;

    private final Color azul = new Color(35, 100, 200);
    private final Color verde = new Color(30, 150, 90);
    private final Color acero = new Color(180, 190, 200);
    private final Color gris = new Color(225, 225, 225);

    private final DecimalFormat formato = new DecimalFormat(
            "0.00",
            DecimalFormatSymbols.getInstance(Locale.US)
    );

    public GraficaEmbudo() {
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    public void actualizarDatos(double altura, double radioInf,
                                double radioSup, double espesor) {

        if (altura <= 0 || radioInf <= espesor
                || radioSup <= espesor || espesor <= 0
                || radioSup <= 0) {
            return;
        }

        this.altura = altura;
        this.radioInf = radioInf;
        this.radioSup = radioSup;
        this.espesor = espesor;

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

        dibujarGrafica(g2);

        g2.dispose();
    }

    private void dibujarGrafica(Graphics2D g2) {
        int ancho = getWidth();
        int alto = getHeight();

        if (ancho < 250 || alto < 300) {
            return;
        }

        int margenIzq = 55;
        int margenDer = 55;
        int margenSup = 145;
        int margenInf = 145;

        double areaAncho = ancho - margenIzq - margenDer;
        double areaAlto = alto - margenSup - margenInf;

        if (areaAncho <= 0 || areaAlto <= 0) {
            return;
        }

        // Una misma escala para ambos ejes.
        double escalaX = areaAncho / (2.0 * (radioSup + 0.8));
        double escalaY = areaAlto / altura;
        double escala = Math.min(escalaX, escalaY);

        int centroX = ancho / 2;
        int yBase = alto - margenInf;
        int yTope = (int) Math.round(yBase - altura * escala);

        int xBaseExtDer =
                (int) Math.round(centroX + radioInf * escala);
        int xBaseIntDer =
                (int) Math.round(centroX + (radioInf - espesor) * escala);

        int xTopeExtDer =
                (int) Math.round(centroX + radioSup * escala);
        int xTopeIntDer =
                (int) Math.round(centroX + (radioSup - espesor) * escala);

        int xBaseExtIzq =
                (int) Math.round(centroX - radioInf * escala);
        int xBaseIntIzq =
                (int) Math.round(centroX - (radioInf - espesor) * escala);

        int xTopeExtIzq =
                (int) Math.round(centroX - radioSup * escala);
        int xTopeIntIzq =
                (int) Math.round(centroX - (radioSup - espesor) * escala);

        // Cuadrícula y números de los ejes.
        dibujarCuadricula(g2, centroX, yBase, escala, ancho, alto);

        // Zona de acero del lado izquierdo.
        Path2D.Double aceroIzquierdo = new Path2D.Double();
        aceroIzquierdo.moveTo(xBaseExtIzq, yBase);
        aceroIzquierdo.lineTo(xTopeExtIzq, yTope);
        aceroIzquierdo.lineTo(xTopeIntIzq, yTope);
        aceroIzquierdo.lineTo(xBaseIntIzq, yBase);
        aceroIzquierdo.closePath();

        // Zona de acero del lado derecho.
        Path2D.Double aceroDerecho = new Path2D.Double();
        aceroDerecho.moveTo(xBaseExtDer, yBase);
        aceroDerecho.lineTo(xTopeExtDer, yTope);
        aceroDerecho.lineTo(xTopeIntDer, yTope);
        aceroDerecho.lineTo(xBaseIntDer, yBase);
        aceroDerecho.closePath();

        g2.setColor(acero);
        g2.fill(aceroIzquierdo);
        g2.fill(aceroDerecho);

        // Contorno exterior.
        g2.setColor(azul);
        g2.setStroke(new BasicStroke(2.5f));

        g2.drawLine(xBaseExtIzq, yBase, xTopeExtIzq, yTope);
        g2.drawLine(xBaseExtDer, yBase, xTopeExtDer, yTope);
        g2.drawLine(xTopeExtIzq, yTope, xTopeExtDer, yTope);
        g2.drawLine(xBaseExtIzq, yBase, xBaseExtDer, yBase);

        // Contorno interior.
        g2.setColor(verde);
        g2.setStroke(new BasicStroke(2.5f));

        g2.drawLine(xBaseIntIzq, yBase, xTopeIntIzq, yTope);
        g2.drawLine(xBaseIntDer, yBase, xTopeIntDer, yTope);

        // Eje central.
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(centroX, 20, centroX, alto - 20);

        // Puntos inferiores.
        dibujarPunto(g2, xBaseExtDer, yBase, azul);
        dibujarPunto(g2, xBaseIntDer, yBase, verde);

        // Puntos superiores.
        dibujarPunto(g2, xTopeExtDer, yTope, azul);
        dibujarPunto(g2, xTopeIntDer, yTope, verde);

        // Altura a la derecha del embudo.
        String textoAltura =
                "Altura = " + formato.format(altura) + " cm";

        g2.setFont(new Font("Arial", Font.BOLD, 13));

        int xAltura = xTopeExtDer + 12;
        int yAltura = (yBase + yTope) / 2;
        int anchoTexto = g2.getFontMetrics().stringWidth(textoAltura);

        // Fondo blanco para que no se cruce visualmente con la cuadrícula.
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(
                xAltura - 5,
                yAltura - 17,
                anchoTexto + 10,
                25,
                8,
                8
        );

        g2.setColor(Color.BLACK);
        g2.drawString(textoAltura, xAltura, yAltura);

        // Valores inferiores, debajo de los números del eje horizontal.
        int etiquetaX = centroX + 15;

        g2.setFont(new Font("Arial", Font.BOLD, 13));

        g2.setColor(azul);
        g2.drawString(
                "R(0) = " + formato.format(radioInf) + " cm",
                etiquetaX,
                yBase + 38
        );

        g2.setColor(verde);
        g2.drawString(
                "r(0) = " + formato.format(radioInf - espesor) + " cm",
                etiquetaX,
                yBase + 57
        );

        // Valores superiores.
        g2.setFont(new Font("Arial", Font.BOLD, 12));

        g2.setColor(azul);
        g2.drawString(
                "R(h) = " + formato.format(radioSup) + " cm",
                xTopeExtDer + 8,
                yTope - 12
        );

        g2.setColor(verde);
        g2.drawString(
                "r(h) = " + formato.format(radioSup - espesor) + " cm",
                xTopeIntDer + 8,
                yTope + 18
        );

        dibujarDatos(g2, ancho);
        dibujarLeyenda(g2, ancho, alto);
    }

    private void dibujarCuadricula(Graphics2D g2, int centroX,
                                   int yBase, double escala,
                                   int ancho, int alto) {

        g2.setFont(new Font("Arial", Font.PLAIN, 11));

        // Líneas verticales y valores del eje horizontal.
        for (int i = -5; i <= 5; i++) {
            int x = (int) Math.round(centroX + i * escala);

            if (x < 10 || x > ancho - 10) {
                continue;
            }

            g2.setColor(gris);
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(x, 20, x, alto - 20);

            g2.setColor(Color.DARK_GRAY);
            String numero = String.valueOf(i);
            int anchoNumero = g2.getFontMetrics().stringWidth(numero);

            g2.drawString(numero, x - anchoNumero / 2, yBase + 16);
        }

        // Líneas horizontales y valores del eje vertical.
        int maxAltura = (int) Math.floor(altura);

        for (int i = 0; i <= maxAltura; i += 2) {
            int y = (int) Math.round(yBase - i * escala);

            if (y < 20 || y > alto - 20) {
                continue;
            }

            g2.setColor(gris);
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(15, y, ancho - 15, y);

            g2.setColor(Color.DARK_GRAY);
            String numero = String.valueOf(i);

            g2.drawString(numero, centroX + 6, y - 5);
        }

        // Eje horizontal principal.
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(15, yBase, ancho - 15, yBase);
    }

    private void dibujarPunto(Graphics2D g2, int x, int y,
                              Color color) {
        g2.setColor(color);
        g2.fillOval(x - 4, y - 4, 8, 8);
    }

    private void dibujarDatos(Graphics2D g2, int ancho) {
        int x = ancho - 225;
        int y = 30;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, 205, 110, 12, 12);

        g2.setColor(Color.GRAY);
        g2.drawRoundRect(x, y, 205, 110, 12, 12);

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(Color.BLACK);
        g2.drawString("Datos del embudo", x + 12, y + 21);

        g2.setFont(new Font("Arial", Font.PLAIN, 12));

        g2.drawString(
                "Altura: " + formato.format(altura) + " cm",
                x + 12, y + 43
        );

        g2.drawString(
                "Radio inferior: " + formato.format(radioInf) + " cm",
                x + 12, y + 61
        );

        g2.drawString(
                "Radio superior: " + formato.format(radioSup) + " cm",
                x + 12, y + 79
        );

        g2.drawString(
                "Grosor: " + formato.format(espesor) + " cm",
                x + 12, y + 97
        );
    }

    private void dibujarLeyenda(Graphics2D g2, int ancho, int alto) {
        int y = alto - 25;
        int x = 25;

        g2.setFont(new Font("Arial", Font.PLAIN, 12));

        g2.setColor(azul);
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(x, y, x + 25, y);
        g2.drawString("Radio exterior", x + 32, y + 5);

        x += 150;

        g2.setColor(verde);
        g2.drawLine(x, y, x + 25, y);
        g2.drawString("Radio interior", x + 32, y + 5);

        x += 145;

        g2.setColor(acero);
        g2.fillRect(x, y - 7, 22, 14);

        g2.setColor(Color.BLACK);
        g2.drawString("Acero", x + 30, y + 5);
    }
}