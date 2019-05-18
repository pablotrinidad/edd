package mx.unam.ciencias.edd;

import java.text.NumberFormat;
import java.util.Random;

/**
 * Práctica 11: Conjuntos.
 */
public class Practica11 {

    /* Imprime el uso del programa y lo termina. */
    private static void uso() {
        System.err.println("Uso: java -jar practica11.jar N");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1)
            uso();

        int N = -1;
        try {
            N = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            uso();
        }

        if (N < 0)
            uso();

        Random random = new Random();
        long tiempoInicial, tiempoTotal;
        NumberFormat nf = NumberFormat.getIntegerInstance();

        Integer[] arreglo = new Integer[N];
        for (int i = 0; i < N; i++)
            arreglo[i] = random.nextInt(N);

        int b = arreglo[N/2];

        ArbolBinarioOrdenado<Integer> abo = new ArbolBinarioOrdenado<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            abo.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "binario ordenado con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        ArbolRojinegro<Integer> arn = new ArbolRojinegro<Integer>();
        for (int i = 0; i < N; i++)
            arn.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "rojinegro con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        ArbolAVL<Integer> avl = new ArbolAVL<Integer>();
        for (int i = 0; i < N; i++)
            avl.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "AVL con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Diccionario<Integer, Integer> diccionario =
            new Diccionario<Integer, Integer>(N);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            diccionario.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario " +
                          "con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Conjunto<Integer> conjunto = new Conjunto<Integer>(N);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            conjunto.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un conjunto " +
                          "con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        abo.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en buscar en un árbol " +
                          "binario ordenado con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        arn.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en buscar en un árbol " +
                          "rojinegro con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        avl.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en buscar en un árbol " +
                          "AVL con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        diccionario.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en buscar en un " +
                          "diccionario con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        conjunto.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en buscar en un " +
                          "conjunto con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
    }
}
