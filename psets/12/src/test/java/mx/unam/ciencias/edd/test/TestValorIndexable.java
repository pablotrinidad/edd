package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ValorIndexable;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link ValorIndexable}.
 */
public class TestValorIndexable {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* El elemento. */
    private int elemento;
    /* El valor. */
    private double valor;
    /* El indexable. */
    private ValorIndexable<Integer> valorIndexable;

    /**
     * Crea un valor indexable para cada prueba.
     */
    public TestValorIndexable() {
        random = new Random();
        elemento = random.nextInt(100);
        valor = 0.1 + 0.9 * random.nextDouble();
        valorIndexable = new ValorIndexable<Integer>(elemento, valor);
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#ValorIndexable}.
     */
    @Test public void testIndexable() {
        Assert.assertTrue(elemento == valorIndexable.getElemento());
        Assert.assertTrue(valor == valorIndexable.getValor());
        Assert.assertTrue(-1 == valorIndexable.getIndice());
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#getElemento}.
     */
    @Test public void testGetElemento() {
        Assert.assertTrue(elemento == valorIndexable.getElemento());
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#compareTo}.
     */
    @Test public void testCompareTo() {
        int e = valorIndexable.getElemento();
        double v = valorIndexable.getValor();
        ValorIndexable<Integer> idx = new ValorIndexable<Integer>(e, v);
        Assert.assertFalse(valorIndexable.compareTo(idx) < 0);
        Assert.assertTrue(valorIndexable.compareTo(idx) == 0);
        Assert.assertFalse(valorIndexable.compareTo(idx) > 0);
        v = valorIndexable.getValor() + 0.00001;
        idx = new ValorIndexable<Integer>(e, v);
        Assert.assertTrue(valorIndexable.compareTo(idx) < 0);
        Assert.assertFalse(valorIndexable.compareTo(idx) == 0);
        Assert.assertFalse(valorIndexable.compareTo(idx) > 0);
        v = 1.0 / 1000000000.0;
        idx = new ValorIndexable<Integer>(e, v);
        Assert.assertFalse(valorIndexable.compareTo(idx) < 0);
        Assert.assertFalse(valorIndexable.compareTo(idx) == 0);
        Assert.assertTrue(valorIndexable.compareTo(idx) > 0);
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#setIndice}.
     */
    @Test public void testSetIndice() {
        int i = random.nextInt(100);
        Assert.assertFalse(i == valorIndexable.getIndice());
        valorIndexable.setIndice(i);
        Assert.assertTrue(i == valorIndexable.getIndice());
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#getIndice}.
     */
    @Test public void testGetIndice() {
        Assert.assertTrue(valorIndexable.getIndice() == -1);
        int i = random.nextInt(100);
        valorIndexable.setIndice(i);
        Assert.assertTrue(i == valorIndexable.getIndice());
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#setValor}.
     */
    @Test public void testSetValor() {
        double v = valorIndexable.getValor() + 10.0;
        Assert.assertFalse(v == valorIndexable.getValor());
        valorIndexable.setValor(v);
        Assert.assertTrue(v == valorIndexable.getValor());
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#getValor}.
     */
    @Test public void testGetValor() {
        Assert.assertTrue(valorIndexable.getValor() == valor);
        double v = random.nextDouble() * 100.0;
        valorIndexable.setValor(v);
        Assert.assertTrue(v == valorIndexable.getValor());
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#equals}.
     */
    @Test public void testEquals() {
        int e = valorIndexable.getElemento() + 1;
        double v = valorIndexable.getValor() + 10.0;
        ValorIndexable<Integer> otro = new ValorIndexable<Integer>(e, v);
        Assert.assertFalse(valorIndexable.equals(otro));
        otro = new ValorIndexable<Integer>(elemento, valor);
        Assert.assertTrue(valorIndexable.equals(otro));
    }

    /**
     * Prueba unitaria para {@link ValorIndexable#toString}.
     */
    @Test public void testToString() {
        String s = String.format("%d:%2.9f", elemento, valor);
        Assert.assertTrue(s.equals(valorIndexable.toString()));
    }
}
