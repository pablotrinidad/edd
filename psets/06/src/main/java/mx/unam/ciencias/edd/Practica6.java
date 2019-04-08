package mx.unam.ciencias.edd;

import java.util.Random;

/**
 * Práctica 6: Gráficas.
 */
public class Practica6 {

    /* Cadena para que usen las lambdas. */
    private static String cadena;

    public static void main(String[] args) {
        /*
         *    b─────d
         *   ╱│╲    │╲
         *  ╱ │ ╲   │ ╲
         * a  │  ╲  │  f
         *  ╲ │   ╲ │ ╱
         *   ╲│    ╲│╱
         *    c─────e
         */
        Grafica<String> grafica = new Grafica<String>();
        grafica.agrega("a");
        grafica.agrega("b");
        grafica.agrega("c");
        grafica.agrega("d");
        grafica.agrega("e");
        grafica.agrega("f");

        grafica.conecta("a", "b");
        grafica.conecta("a", "c");
        grafica.conecta("b", "c");
        grafica.conecta("b", "d");
        grafica.conecta("b", "e");
        grafica.conecta("c", "e");
        grafica.conecta("d", "e");
        grafica.conecta("d", "f");
        grafica.conecta("e", "f");

        System.out.println(grafica);

        /* BFS */
        grafica.paraCadaVertice(v -> grafica.setColor(v, Color.ROJO));
        Cola<VerticeGrafica<String>> c = new Cola<VerticeGrafica<String>>();
        VerticeGrafica<String> vertice = grafica.vertice("a");
        grafica.setColor(vertice, Color.NEGRO);
        c.mete(vertice);
        cadena = "BFS 1: ";
        while (!c.esVacia()) {
            vertice = c.saca();
            cadena += vertice.get() + ", ";
            for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                if (vecino.getColor() == Color.ROJO) {
                    grafica.setColor(vecino, Color.NEGRO);
                    c.mete(vecino);
                }
            }
        }
        System.out.println(cadena);

        /* BFS de la clase */
        cadena = "BFS 2: ";
        grafica.bfs("a", v -> cadena += v.get() + ", ");
        System.out.println(cadena);

        /* DFS */
        grafica.paraCadaVertice(v -> grafica.setColor(v, Color.ROJO));
        Pila<VerticeGrafica<String>> p = new Pila<VerticeGrafica<String>>();
        vertice = grafica.vertice("a");
        grafica.setColor(vertice, Color.NEGRO);
        p.mete(vertice);
        cadena = "DFS 1: ";
        while (!p.esVacia()) {
            vertice = p.saca();
            cadena += vertice.get() + ", ";
            for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                if (vecino.getColor() == Color.ROJO) {
                    grafica.setColor(vecino, Color.NEGRO);
                    p.mete(vecino);
                }
            }
        }
        System.out.println(cadena);

        /* DFS de la clase */
        cadena = "DFS 2: ";
        grafica.dfs("a", v -> cadena += v.get() + ", ");
        System.out.println(cadena);
    }
}
