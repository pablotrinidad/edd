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
 * Clase para pruebas unitarias de la clase {@link Pila}.
 */
public class TestPila {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* La pila. */
    private Pila<Integer> pila;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestra pila, y una pila.
     */
    public TestPila() {
        random = new Random();
        total = 10 + random.nextInt(90);
        pila = new Pila<Integer>();
    }

    /**
     * Prueba unitaria para {@link Pila#Pila}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(pila != null);
        Assert.assertTrue(pila.esVacia());
        try {
            pila.saca();
            Assert.fail();
        } catch (NoSuchElementException nse) {}
    }

    /**
     * Prueba unitaria para {@link Pila#mete}.
     */
    @Test public void testMete() {
        try {
            pila.mete(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++)
            pila.mete(i);
        int c = total - 1;
        int t = 0;
        while (!pila.esVacia()) {
            Assert.assertTrue(pila.saca() == c--);
            t++;
        }
        Assert.assertTrue(t == total);
        try {
            pila.mete(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++)
            pila.mete(i);
        c = total - 1;
        t = 0;
        while (!pila.esVacia()) {
            Assert.assertTrue(pila.saca() == c--);
            t++;
        }
        Assert.assertTrue(t == total);
    }

    /**
     * Prueba unitaria para {@link Pila#saca}.
     */
    @Test public void testSaca() {
        try {
            pila.saca();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = i;
            pila.mete(a[i]);
        }
        int c = 0;
        while (!pila.esVacia())
            Assert.assertTrue(pila.saca() == a[total - ++c]);
        try {
            pila.saca();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++)
            pila.mete(a[i]);
        c = 0;
        while (!pila.esVacia())
            Assert.assertTrue(pila.saca() == a[total - ++c]);
    }

    /**
     * Prueba unitaria para {@link MeteSaca#mira}.
     */
    @Test public void testMira() {
        try {
            pila.mira();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++) {
            pila.mete(i);
            Assert.assertTrue(pila.mira() == i);
        }
    }

    /**
     * Prueba unitaria para {@link Pila#esVacia}.
     */
    @Test public void testEsVacia() {
        Assert.assertTrue(pila.esVacia());
        pila.mete(1);
        Assert.assertFalse(pila.esVacia());
        pila.saca();
        Assert.assertTrue(pila.esVacia());
    }

    /**
     * Prueba unitaria para {@link Pila#toString}.
     */
    @Test public void testToString() {
        Assert.assertTrue(pila.toString().equals(""));
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = i;
            pila.mete(i);
            String s = "";
            for (int j = 0; j <= i; j++)
                s += String.valueOf(a[i-j]) + "\n";
            Assert.assertTrue(pila.toString().equals(s));
        }
    }

    /**
     * Prueba unitaria para {@link Pila#equals}.
     */
    @Test public void testEquals() {
        Assert.assertFalse(pila.equals(null));
        Assert.assertFalse(pila.equals(""));
        Assert.assertFalse(pila.equals(new Cola<Integer>()));
        Pila<Integer> pila2 = new Pila<Integer>();
        Assert.assertTrue(pila.equals(pila2));
        for (int i = 0; i < total; i++) {
            pila.mete(i);
            Assert.assertFalse(pila.equals(pila2));
            pila2.mete(i);
            Assert.assertTrue(pila.equals(pila2));
        }
    }
}
