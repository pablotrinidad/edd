package mx.unam.ciencias.edd;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 2: Pilas, colas, ordenamientos y búsquedas.
 */
public class Practica2 {

    /* Imprime el uso del programa y lo termina. */
    private static void uso() {
        System.err.println("Uso: java -jar practica3.jar N");
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
        NumberFormat nf = NumberFormat.getIntegerInstance();
        long tiempoInicial, tiempoTotal;

        int[] arreglo = new int[N];
        for (int i = 0; i < N; i++)
            arreglo[i] = random.nextInt();

        Integer[] is = new Integer[N];
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            is[i] = arreglo[i];
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un arreglo con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        Arreglos.selectionSort(is);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar un arreglo con %s elementos " +
                          "usando SelectionSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Integer[] qs = new Integer[N];
        for (int i = 0; i < N; i++)
            qs[i] = arreglo[i];

        tiempoInicial = System.nanoTime();
        Arreglos.quickSort(qs);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar un arreglo con %s elementos " +
                          "usando QuickSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        int b = qs[random.nextInt(N)];

        tiempoInicial = System.nanoTime();
        int idx = Arreglos.busquedaBinaria(qs, b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un arreglo " +
                          "con %s elementos usando búsqueda binaria.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Lista<Integer> ms = new Lista<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            ms.agregaFinal(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear una lista con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        ms = Lista.mergeSort(ms);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar una lista con %s elementos " +
                          "usando MergeSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
    }
}
