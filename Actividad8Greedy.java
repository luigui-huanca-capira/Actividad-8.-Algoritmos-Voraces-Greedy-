import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Actividad8Greedy {

    // =========================
    // 1) MOCHILA FRACCIONAL
    // =========================
    static class Objeto {
        String nombre;
        double valor;
        double peso;
        double ratio; // valor/peso

        Objeto(String nombre, double valor, double peso) {
            this.nombre = nombre;
            this.valor = valor;
            this.peso = peso;
            this.ratio = valor / peso;
        }
    }

    static class SeleccionMochila {
        String nombre;
        double fraccionTomada; // 1.0 = completo
        double valorAportado;
        double pesoUsado;

        SeleccionMochila(String nombre, double fraccionTomada, double valorAportado, double pesoUsado) {
            this.nombre = nombre;
            this.fraccionTomada = fraccionTomada;
            this.valorAportado = valorAportado;
            this.pesoUsado = pesoUsado;
        }
    }

    static class ResultadoMochila {
        List<SeleccionMochila> seleccionados = new ArrayList<>();
        double valorTotal;
        double pesoTotal;
    }

    public static ResultadoMochila resolverMochilaFraccional(List<Objeto> objetos, double capacidadMaxima) {
        // Estrategia Greedy: ordenar por valor/peso (ratio) de mayor a menor
        Collections.sort(objetos, new Comparator<Objeto>() {
            @Override
            public int compare(Objeto o1, Objeto o2) {
                return Double.compare(o2.ratio, o1.ratio);
            }
        });

        ResultadoMochila resultado = new ResultadoMochila();
        double capacidadRestante = capacidadMaxima;

        for (Objeto obj : objetos) {
            if (capacidadRestante <= 0) break;

            if (obj.peso <= capacidadRestante) {
                // Tomar objeto completo
                resultado.seleccionados.add(
                        new SeleccionMochila(obj.nombre, 1.0, obj.valor, obj.peso)
                );
                capacidadRestante -= obj.peso;
                resultado.valorTotal += obj.valor;
                resultado.pesoTotal += obj.peso;
            } else {
                // Tomar fracción
                double fraccion = capacidadRestante / obj.peso;
                double valorAportado = obj.valor * fraccion;
                double pesoUsado = obj.peso * fraccion;

                resultado.seleccionados.add(
                        new SeleccionMochila(obj.nombre, fraccion, valorAportado, pesoUsado)
                );
                resultado.valorTotal += valorAportado;
                resultado.pesoTotal += pesoUsado;
                capacidadRestante = 0;
            }
        }

        return resultado;
    }

    public static void imprimirResultadoMochila(String titulo, List<Objeto> objetos, double capacidad) {
        System.out.println("==============================================");
        System.out.println(titulo);
        System.out.println("Capacidad máxima: " + capacidad);
        System.out.println("Objetos (nombre, valor, peso):");
        for (Objeto o : objetos) {
            System.out.printf("- %s (%.2f, %.2f)%n", o.nombre, o.valor, o.peso);
        }

        ResultadoMochila r = resolverMochilaFraccional(new ArrayList<>(objetos), capacidad);

        System.out.println("Objetos seleccionados:");
        for (SeleccionMochila s : r.seleccionados) {
            if (Math.abs(s.fraccionTomada - 1.0) < 1e-9) {
                System.out.printf("- %s completo (peso usado: %.2f, valor aportado: %.2f)%n",
                        s.nombre, s.pesoUsado, s.valorAportado);
            } else {
                System.out.printf("- %.2f%% de %s (peso usado: %.2f, valor aportado: %.2f)%n",
                        (s.fraccionTomada * 100.0), s.nombre, s.pesoUsado, s.valorAportado);
            }
        }

        System.out.printf("Peso total usado: %.2f%n", r.pesoTotal);
        System.out.printf("Valor total obtenido: %.2f%n", r.valorTotal);
    }

    // =========================
    // 2) COBERTURA DE ANTENAS
    // =========================
    static class ResultadoAntenas {
        List<Integer> posicionesAntenas = new ArrayList<>();
    }

    public static ResultadoAntenas resolverCoberturaAntenas(int[] casas, int R) {
        Arrays.sort(casas);
        ResultadoAntenas res = new ResultadoAntenas();

        int i = 0;
        int n = casas.length;

        while (i < n) {
            // Primera casa no cubierta
            int primeraCasa = casas[i];

            // Colocamos antena greedy en primeraCasa + R
            int posicionAntena = primeraCasa + R;
            res.posicionesAntenas.add(posicionAntena);

            // Saltamos todas las casas cubiertas por esta antena: [posicionAntena - R, posicionAntena + R]
            int limiteCoberturaDerecha = posicionAntena + R;
            i++;
            while (i < n && casas[i] <= limiteCoberturaDerecha) {
                i++;
            }
        }

        return res;
    }

    public static void imprimirResultadoAntenas(String titulo, int[] casas, int R) {
        System.out.println("==============================================");
        System.out.println(titulo);
        System.out.println("Casas: " + Arrays.toString(casas));
        System.out.println("Cobertura R: " + R);

        int[] copia = Arrays.copyOf(casas, casas.length);
        ResultadoAntenas r = resolverCoberturaAntenas(copia, R);

        System.out.println("Antenas colocadas aproximadamente en:");
        for (int pos : r.posicionesAntenas) {
            System.out.println("- " + pos);
        }
        System.out.println("Cantidad total: " + r.posicionesAntenas.size());
    }

    // =========================
    // MAIN - EJEMPLOS DE LA GUÍA
    // =========================
    public static void main(String[] args) {

        // ----- Mochila Fraccional: Ejemplo 1 -----
        // Capacidad: 50
        // A(60,10), B(100,20), C(120,30)
        List<Objeto> objetos1 = Arrays.asList(
                new Objeto("A", 60, 10),
                new Objeto("B", 100, 20),
                new Objeto("C", 120, 30)
        );
        imprimirResultadoMochila("MOCHILA FRACCIONAL - EJEMPLO 1", objetos1, 50);

        // ----- Mochila Fraccional: Ejemplo 2 -----
        // Capacidad: 25
        // A(80,20), B(100,10), C(120,30)
        List<Objeto> objetos2 = Arrays.asList(
                new Objeto("A", 80, 20),
                new Objeto("B", 100, 10),
                new Objeto("C", 120, 30)
        );
        imprimirResultadoMochila("MOCHILA FRACCIONAL - EJEMPLO 2", objetos2, 25);

        // ----- Cobertura de antenas: Ejemplo 1 -----
        // Casas: [1,2,7,11,20,21,30], R=5
        int[] casas1 = {1, 2, 7, 11, 20, 21, 30};
        imprimirResultadoAntenas("COBERTURA DE ANTENAS - EJEMPLO 1", casas1, 5);

        // ----- Cobertura de antenas: Ejemplo 2 -----
        // Casas: [2,4,8,15,18,22], R=3
        int[] casas2 = {2, 4, 8, 15, 18, 22};
        imprimirResultadoAntenas("COBERTURA DE ANTENAS - EJEMPLO 2", casas2, 3);

        System.out.println("==============================================");
        System.out.println("Actividad 8 completada con enfoque Greedy.");
    }
}
