package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;

import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.ComparableIndexable;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MonticuloArreglo;
import mx.unam.ciencias.edd.ValorIndexable;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link MonticuloArreglo}.
 */
public class TestMonticuloArreglo {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El montículo mínimo. */
    private MonticuloArreglo<ValorIndexable<String>> monticulo;
    /* Arreglo auxiliar. */
    private ValorIndexable<String>[] arreglo;

    /* Método que verifica que un montículo de arreglo cumpla con sus
     * propiedades. */
    private static <T extends ComparableIndexable<T>> void
    verificaMonticuloArreglo(T[] arreglo, MonticuloArreglo<T> monticulo) {
        for (int i = 0; i < monticulo.getElementos(); i++) {
            T e1 = monticulo.get(i);
            T e2 = arreglo[i];
            Assert.assertTrue(e1 == e2);
            if (e1 != null) {
                Assert.assertTrue(e1.getIndice() == i);
                Assert.assertTrue(e1.equals(e2));
            } else {
                Assert.assertTrue(e2 == null);
            }
        }
    }

    /**
     * Crea un montículo de arreglo para cada prueba.
     */
    public TestMonticuloArreglo() {
        random = new Random();
        total = 10 + random.nextInt(90);
        @SuppressWarnings("unchecked")
            ValorIndexable<String>[] a =
            (ValorIndexable<String>[])new ValorIndexable[total];
        arreglo = a;
        Lista<ValorIndexable<String>> lista = new Lista<ValorIndexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
            arreglo[i] = idx;
            lista.agrega(idx);
        }
        monticulo = new MonticuloArreglo<ValorIndexable<String>>(lista);
        verificaMonticuloArreglo(arreglo, monticulo);
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#MonticuloArreglo}.
     */
    @Test public void testConstructor() {
        Lista<ValorIndexable<String>> lista = new Lista<ValorIndexable<String>>();
        for (int i = 0; i < total; i++)
            lista.agrega(monticulo.get(i));
        MonticuloArreglo<ValorIndexable<String>> m2;
        m2 = new MonticuloArreglo<ValorIndexable<String>>(lista, total);
        for (int i = 0; i < total; i++)
            Assert.assertTrue(monticulo.get(i).equals(m2.get(i)));
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#elimina}.
     */
    @Test public void testElimina() {
        Lista<ValorIndexable<String>> ordenada =
            new Lista<ValorIndexable<String>>();
        for (int i = 0; i < arreglo.length; i++)
            ordenada.agrega(arreglo[i]);
        ordenada = Lista.mergeSort(ordenada);
        while (!monticulo.esVacia()) {
            ValorIndexable<String> a = monticulo.elimina();
            Assert.assertTrue(a.getIndice() == -1);
            ValorIndexable<String> b = ordenada.eliminaPrimero();
            Assert.assertTrue(a.equals(b));
            for (int i = 0; i < arreglo.length; i++)
                if (a.equals(arreglo[i]))
                    arreglo[i] = null;
            verificaMonticuloArreglo(arreglo, monticulo);
            Assert.assertTrue(monticulo.getElementos() == --total);
        }
        try {
            monticulo.elimina();
            Assert.fail();
        } catch (IllegalStateException ise) {}
        for (int i = 0; i < arreglo.length; i++)
            Assert.assertTrue(arreglo[i] == null);
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#esVacia}.
     */
    @Test public void testEsVacio() {
        Assert.assertFalse(monticulo.esVacia());
        Lista<ValorIndexable<String>> lista = new Lista<ValorIndexable<String>>();
        monticulo = new MonticuloArreglo<ValorIndexable<String>>(lista);
        Assert.assertTrue(monticulo.esVacia());
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#reordena}.
     */
    @Test public void testReordena() {
        int n = monticulo.getElementos();
        for (int i = 0; i < n; i++) {
            verificaMonticuloArreglo(arreglo, monticulo);
            ValorIndexable<String> idx = monticulo.get(random.nextInt(n));
            idx.setValor(idx.getValor() / 10.0);
            monticulo.reordena(idx);
            verificaMonticuloArreglo(arreglo, monticulo);
            for (int j = 0; j < arreglo.length; j++)
                Assert.assertTrue(monticulo.get(j).equals(arreglo[j]));
        }
        for (int i = 0; i < n; i++) {
            verificaMonticuloArreglo(arreglo, monticulo);
            ValorIndexable<String> idx = monticulo.get(random.nextInt(n));
            idx.setValor(idx.getValor() * 10.0);
            monticulo.reordena(idx);
            verificaMonticuloArreglo(arreglo, monticulo);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#getElementos}.
     */
    @Test public void testGetElementos() {
        while (!monticulo.esVacia()) {
            Assert.assertTrue(monticulo.getElementos() == total--);
            monticulo.elimina();
        }
        Assert.assertTrue(monticulo.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#get}.
     */
    @Test public void testGet() {
        try {
            monticulo.get(-1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            monticulo.get(total);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < arreglo.length; i++)
            Assert.assertTrue(monticulo.get(i).equals(arreglo[i]));
    }
}
