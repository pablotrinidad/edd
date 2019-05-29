package mx.unam.ciencias.edd.test;

import java.util.Random;
import mx.unam.ciencias.edd.Arreglos;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Arreglos}.
 */
public class TestArreglos {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El arreglo. */
    private Integer[] arreglo;

    /* Nos dice si el arreglo recibido está ordenado. */
    private static <T extends Comparable<T>>
                      boolean estaOrdenado(T[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i-1].compareTo(a[i]) > 0)
                return false;
        return true;
    }

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestro arreglo, y un arreglo.
     */
    public TestArreglos() {
        random = new Random();
        total = 10 + random.nextInt(90);
        arreglo = new Integer[total];
    }

    /**
     * Prueba unitaria para {@link Arreglos#quickSort(Comparable[])}.
     */
    @Test public void testQuickSort() {
        arreglo[0] = 1;
        arreglo[1] = 0;
        for (int i = 2; i < total; i++)
            arreglo[i] = random.nextInt(total);
        Assert.assertFalse(estaOrdenado(arreglo));
        Arreglos.quickSort(arreglo);
        Assert.assertTrue(estaOrdenado(arreglo));
    }

    /**
     * Prueba unitaria para {@link Arreglos#quickSort(Object[],Comparator)}.
     */
    @Test public void testQuickSortComparator() {
        arreglo[0] = 1;
        arreglo[1] = 0;
        for (int i = 2; i < total; i++)
            arreglo[i] = random.nextInt(total);
        Assert.assertFalse(estaOrdenado(arreglo));
        Arreglos.quickSort(arreglo, (a, b) -> a.compareTo(b));
        Assert.assertTrue(estaOrdenado(arreglo));
    }

    /**
     * Prueba unitaria para {@link Arreglos#selectionSort(Comparable[])}.
     */
    @Test public void testSelectionSort() {
        arreglo[0] = 1;
        arreglo[1] = 0;
        for (int i = 2; i < total; i++)
            arreglo[i] = random.nextInt(total);
        Assert.assertFalse(estaOrdenado(arreglo));
        Arreglos.selectionSort(arreglo);
        Assert.assertTrue(estaOrdenado(arreglo));
    }

    /**
     * Prueba unitaria para {@link Arreglos#selectionSort(Object[],Comparator)}.
     */
    @Test public void testSelectionSortComparator() {
        arreglo[0] = 1;
        arreglo[1] = 0;
        for (int i = 2; i < total; i++)
            arreglo[i] = random.nextInt(total);
        Assert.assertFalse(estaOrdenado(arreglo));
        Arreglos.selectionSort(arreglo, (a, b) -> a.compareTo(b));
        Assert.assertTrue(estaOrdenado(arreglo));
    }

    /**
     * Prueba unitaria para {@link
     * Arreglos#busquedaBinaria(Comparable[],Comparable)}.
     */
    @Test public void testBusquedaBinaria() {
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++)
            arreglo[i] = ini + i;
        for (int i = 0; i < total; i++)
            Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, arreglo[i]) == i);
        Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, ini - 1) == -1);
        Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, ini + total) == -1);
    }

    /**
     * Prueba unitaria para
     * {@link Arreglos#busquedaBinaria(Object[],Object,Comparator)}.
     */
    @Test public void testBusquedaBinariaCompartor() {
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++)
            arreglo[i] = ini + i;
        for (int i = 0; i < total; i++)
            Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, arreglo[i]) == i);
        Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, ini - 1) == -1);
        Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, ini + total) == -1);
    }
}
