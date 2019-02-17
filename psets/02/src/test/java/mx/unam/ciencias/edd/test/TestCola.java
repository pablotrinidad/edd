package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.MeteSaca;
import mx.unam.ciencias.edd.Pila;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Cola}.
 */
public class TestCola {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* La cola. */
    private Cola<Integer> cola;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestra cola, y una cola.
     */
    public TestCola() {
        random = new Random();
        total = 10 + random.nextInt(90);
        cola = new Cola<Integer>();
    }

    /**
     * Prueba unitaria para {@link Cola#Cola}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(cola != null);
        Assert.assertTrue(cola.esVacia());
        try {
            cola.saca();
            Assert.fail();
        } catch (NoSuchElementException nse) {}
    }

    /**
     * Prueba unitaria para {@link Cola#mete}.
     */
    @Test public void testMete() {
        try {
            cola.mete(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++)
            cola.mete(i);
        int c = 0;
        while (!cola.esVacia())
            Assert.assertTrue(cola.saca() == c++);
        Assert.assertTrue(c == total);
        try {
            cola.mete(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++)
            cola.mete(i);
        c = 0;
        while (!cola.esVacia())
            Assert.assertTrue(cola.saca() == c++);
        Assert.assertTrue(c == total);
    }

    /**
     * Prueba unitaria para {@link Cola#saca}.
     */
    @Test public void testSaca() {
        try {
            cola.saca();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = i;
            cola.mete(a[i]);
        }
        int c = 0;
        while (!cola.esVacia())
            Assert.assertTrue(cola.saca() == a[c++]);
        Assert.assertTrue(c == total);
        try {
            cola.saca();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++)
            cola.mete(a[i]);
        c = 0;
        while (!cola.esVacia())
            Assert.assertTrue(cola.saca() == a[c++]);
        Assert.assertTrue(c == total);
    }

    /**
     * Prueba unitaria para {@link MeteSaca#mira}.
     */
    @Test public void testMira() {
        try {
            cola.mira();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++) {
            cola.mete(i);
            Assert.assertTrue(cola.mira() == 0);
        }
    }

    /**
     * Prueba unitaria para {@link Cola#esVacia}.
     */
    @Test public void testEsVacia() {
        Assert.assertTrue(cola.esVacia());
        cola.mete(1);
        Assert.assertFalse(cola.esVacia());
        cola.saca();
        Assert.assertTrue(cola.esVacia());
    }

    /**
     * Prueba unitaria para {@link Cola#toString}.
     */
    @Test public void testToString() {
        Assert.assertTrue(cola.toString().equals(""));
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = i;
            cola.mete(i);
            String s = "";
            for (int j = 0; j <= i; j++)
                s += String.valueOf(a[j]) + ",";
            Assert.assertTrue(cola.toString().equals(s));
        }
    }

    /**
     * Prueba unitaria para {@link Cola#equals}.
     */
    @Test public void testEquals() {
        Assert.assertFalse(cola.equals(null));
        Assert.assertFalse(cola.equals(""));
        Assert.assertFalse(cola.equals(new Pila<Integer>()));
        Cola<Integer> cola2 = new Cola<Integer>();
        Assert.assertTrue(cola.equals(cola2));
        for (int i = 0; i < total; i++) {
            cola.mete(i);
            Assert.assertFalse(cola.equals(cola2));
            cola2.mete(i);
            Assert.assertTrue(cola.equals(cola2));
        }
    }
}
