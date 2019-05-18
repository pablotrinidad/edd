package mx.unam.ciencias.edd;

import java.text.NumberFormat;
import java.util.Random;

/**
 * Práctica 10: Diccionarios.
 */
public class Practica10 {

    /* Imprime el uso del programa y lo termina. */
    private static void uso() {
        System.err.println("Uso: java -jar practica10.jar N");
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

        if (N < 1)
            uso();

        Random random = new Random();
        long tiempoInicial, tiempoTotal;
        NumberFormat nf = NumberFormat.getIntegerInstance();

        String[] arreglo = new String[N];
        for (int i = 0; i < N; i++)
            arreglo[i] = String.valueOf(random.nextInt(N));

        ArbolBinarioOrdenado<String> abo = new ArbolBinarioOrdenado<String>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            abo.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "binario ordenado con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        ArbolRojinegro<String> arn = new ArbolRojinegro<String>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            arn.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "rojinegro con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        ArbolAVL<String> avl = new ArbolAVL<String>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            avl.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "AVL con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Diccionario<String, String> diccJava =
            new Diccionario<String, String>(N);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            diccJava.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario " +
                          "con %s elementos (dispersor Java).\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
        System.out.printf("\t%d colisiones, %d colisión máxima\n",
                          diccJava.colisiones(), diccJava.colisionMaxima());

        AlgoritmoDispersor a = AlgoritmoDispersor.BJ_STRING;
        Dispersor<String> bj = FabricaDispersores.dispersorCadena(a);
        Diccionario<String, String> diccBJ =
            new Diccionario<String, String>(N, bj);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            diccBJ.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario " +
                          "con %s elementos (dispersor Bob Jenkins).\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
        System.out.printf("\t%d colisiones, %d colisión máxima\n",
                          diccBJ.colisiones(), diccBJ.colisionMaxima());

        a = AlgoritmoDispersor.DJB_STRING;
        Dispersor<String> djb = FabricaDispersores.dispersorCadena(a);
        Diccionario<String, String> diccDJB =
            new Diccionario<String, String>(N, djb);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            diccDJB.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario " +
                          "con %s elementos (dispersor Daniel Bernstein).\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
        System.out.printf("\t%d colisiones, %d colisión máxima\n",
                          diccDJB.colisiones(), diccDJB.colisionMaxima());

        a = AlgoritmoDispersor.XOR_STRING;
        Dispersor<String> xor = FabricaDispersores.dispersorCadena(a);
        Diccionario<String, String> diccXOR =
            new Diccionario<String, String>(N, xor);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            diccXOR.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario " +
                          "con %s elementos (dispersor XOR).\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
        System.out.printf("\t%d colisiones, %d colisión máxima\n",
                          diccXOR.colisiones(), diccXOR.colisionMaxima());
    }
}
