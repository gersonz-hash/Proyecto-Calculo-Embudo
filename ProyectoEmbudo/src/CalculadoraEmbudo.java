
import java.util.Locale;
import java.util.function.DoubleUnaryOperator;

public class CalculadoraEmbudo {

    public String calcular(
            double altura,
            double radioInf,
            double radioSup,
            double espesor,
            double densidad) {

        validarDatos(altura, radioInf, radioSup, espesor, densidad);

        double pendiente = (radioSup - radioInf) / altura;

        double radioInteriorInf = radioInf - espesor;
        double radioInteriorSup = radioSup - espesor;

        DoubleUnaryOperator radioExterior =
                x -> radioInf + pendiente * x;

        DoubleUnaryOperator radioInterior =
                x -> radioInteriorInf + pendiente * x;

        // Area entre las curvas del perfil
        double areaPerfil = integrar(
                x -> radioExterior.applyAsDouble(x)
                        - radioInterior.applyAsDouble(x),
                0, altura);

        // Volumen del acero mediante arandelas
        double volumenAcero = Math.PI * integrar(
                x -> Math.pow(radioExterior.applyAsDouble(x), 2)
                        - Math.pow(radioInterior.applyAsDouble(x), 2),
                0, altura);

        // Capacidad interior mediante discos
        double capacidadDiscos = Math.PI * integrar(
                x -> Math.pow(radioInterior.applyAsDouble(x), 2),
                0, altura);

        // Comprobacion mediante capas cilindricas
        double capacidadCapas = 2 * Math.PI * (
                integrar(y -> altura * y, 0, radioInteriorInf)
                        + integrar(
                        y -> y * (altura
                                - (y - radioInteriorInf) / pendiente),
                        radioInteriorInf, radioInteriorSup)
        );

        // Masa del acero
        double masa = volumenAcero * densidad;

        // Areas laterales interior y exterior
        double factor = Math.sqrt(1 + pendiente * pendiente);

        double areaExterior = 2 * Math.PI * factor
                * integrar(radioExterior::applyAsDouble, 0, altura);

        double areaInterior = 2 * Math.PI * factor
                * integrar(radioInterior::applyAsDouble, 0, altura);

        StringBuilder salida = new StringBuilder();

        salida.append("MODELO MATEMATICO\n");
        salida.append(String.format(Locale.US,
                "R(x) = %.4f + %.4fx%n", radioInf, pendiente));
        salida.append(String.format(Locale.US,
                "r(x) = %.4f + %.4fx%n%n",
                radioInteriorInf, pendiente));

        salida.append("1. VOLUMEN DE ACERO\n");
        salida.append("V = pi * integral (R(x)^2 - r(x)^2) dx\n");
        salida.append(String.format(Locale.US,
                "Resultado: %.4f cm3%n%n", volumenAcero));

        salida.append("2. MASA DEL ACERO\n");
        salida.append("Masa = densidad * volumen de acero\n");
        salida.append(String.format(Locale.US,
                "Resultado: %.4f gramos%n%n", masa));

        salida.append("3. CAPACIDAD POR DISCOS\n");
        salida.append("V = pi * integral r(x)^2 dx\n");
        salida.append(String.format(Locale.US,
                "Resultado: %.4f cm3%n", capacidadDiscos));
        salida.append(String.format(Locale.US,
                "Capacidad: %.5f litros%n%n", capacidadDiscos / 1000));

        salida.append("4. COMPROBACION POR CAPAS CILINDRICAS\n");
        salida.append("V = 2*pi * integral (radio * altura) dy\n");
        salida.append(String.format(Locale.US,
                "Resultado: %.4f cm3%n", capacidadCapas));
        salida.append(String.format(Locale.US,
                "Diferencia entre metodos: %.8f cm3%n%n",
                Math.abs(capacidadDiscos - capacidadCapas)));

        salida.append("5. AREA SUPERFICIAL\n");
        salida.append(String.format(Locale.US,
                "Area exterior: %.4f cm2%n", areaExterior));
        salida.append(String.format(Locale.US,
                "Area interior: %.4f cm2%n", areaInterior));
        salida.append(String.format(Locale.US,
                "Area total de ambas caras: %.4f cm2%n%n",
                areaExterior + areaInterior));

        salida.append("6. AREA ENTRE CURVAS DEL PERFIL\n");
        salida.append(String.format(Locale.US,
                "Resultado: %.4f cm2%n", areaPerfil));

        salida.append("\nMetodo numerico: regla de Simpson.");
        salida.append("\nLos bordes circulares no estan incluidos");
        salida.append(" en el area superficial.");

        return salida.toString();
    }

    private void validarDatos(
            double altura,
            double radioInf,
            double radioSup,
            double espesor,
            double densidad) {

        if (!Double.isFinite(altura)
                || !Double.isFinite(radioInf)
                || !Double.isFinite(radioSup)
                || !Double.isFinite(espesor)
                || !Double.isFinite(densidad)
                || altura <= 0
                || radioInf <= 0
                || radioSup <= radioInf
                || espesor <= 0
                || densidad <= 0
                || radioInf <= espesor
                || radioSup <= espesor) {

            throw new IllegalArgumentException(
                    "Revisa las dimensiones ingresadas.");
        }
    }

    private double integrar(
            DoubleUnaryOperator funcion,
            double a,
            double b) {

        int n = 10000;

        if (b <= a) {
            return 0;
        }

        double h = (b - a) / n;
        double suma = funcion.applyAsDouble(a)
                + funcion.applyAsDouble(b);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;

            if (i % 2 == 0) {
                suma += 2 * funcion.applyAsDouble(x);
            } else {
                suma += 4 * funcion.applyAsDouble(x);
            }
        }

        return suma * h / 3.0;
    }
}
