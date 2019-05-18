package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.AlgoritmoDispersor;
import mx.unam.ciencias.edd.Arreglos;
import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Dispersor;
import mx.unam.ciencias.edd.FabricaDispersores;
import mx.unam.ciencias.edd.Lista;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Diccionario}.
 */
public class TestDiccionario {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El diccionario. */
    private Diccionario<String, String> diccionario;

    /**
     * Crea un diccionario para cada prueba.
     */
    public TestDiccionario() {
        int N = 64;
        random = new Random();
        total = N + random.nextInt(N);
        diccionario = new Diccionario<String, String>(total);
    }

    /* Calcula la capacidad. */
    private int calculaCapacidad(int n) {
        n = (n < 64) ? 64 : n;
        int c = 1;
        while (c < n * 2)
            c *= 2;
        return c;
    }

    /**
     * Prueba unitaria para {@link Diccionario#Diccionario}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(diccionario.esVacia());
        Assert.assertTrue(diccionario.getElementos() == 0);
        Assert.assertTrue(diccionario.carga() == 0.0);
        Assert.assertTrue(diccionario.colisiones() == 0);
        Iterator<String> iteradorLlaves = diccionario.iteradorLlaves();
        Iterator<String> iteradorValores = diccionario.iterator();
        Assert.assertFalse(iteradorLlaves.hasNext());
        Assert.assertFalse(iteradorValores.hasNext());
        diccionario.agrega("a", "a");
        int c = calculaCapacidad(total);
        Assert.assertTrue(diccionario.carga() == 1.0 / c);
    }

    /**
     * Prueba unitaria para {@link Diccionario#agrega}.
     */
    @Test public void testAgrega() {
        boolean crecio = false;
        double l = 0.0;
        int ini = random.nextInt(10000);
        int cap = calculaCapacidad(total);
        for (int i = 0; i < total * 4; i++) {
            String s = String.format("%x", ini + i * 1000);
            Assert.assertFalse(diccionario.contiene(s));
            diccionario.agrega(s, s);
            if (diccionario.carga() < l) {
                crecio = true;
                cap *= 2;
            }
            Assert.assertTrue(diccionario.carga() == ((double)(i+1)) / cap);
            Assert.assertTrue(diccionario.getElementos() == i+1);
            Assert.assertTrue(diccionario.contiene(s));
            Assert.assertTrue(diccionario.get(s).equals(s));
            Assert.assertTrue(diccionario.carga() < Diccionario.MAXIMA_CARGA);
            l = diccionario.carga();
        }
        Assert.assertTrue(crecio);
        String k = String.format("%x", ini);
        String v = String.format("%x", ini+1);
        diccionario.agrega(k, v);
        Assert.assertTrue(diccionario.getElementos() == total*4);
        Assert.assertTrue(diccionario.contiene(k));
        Assert.assertTrue(diccionario.get(k).equals(v));
        Assert.assertTrue(diccionario.carga() < Diccionario.MAXIMA_CARGA);
        try {
            diccionario.agrega(null, "X");
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            diccionario.agrega("X", null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Diccionario#get}.
     */
    @Test public void testGet() {
        int ini = 1 + random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.get(s).equals(s));
        }
        try {
            diccionario.get("00000");
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            diccionario.get(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Diccionario#contiene}.
     */
    @Test public void testContiene() {
        Assert.assertFalse(diccionario.contiene(null));
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            Assert.assertFalse(diccionario.contiene(s));
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.contiene(s));
        }
        Assert.assertFalse(diccionario.contiene("00000"));
    }

    /**
     * Prueba unitaria para {@link Diccionario#elimina}.
     */
    @Test public void testElimina() {
        try {
            diccionario.elimina(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        String[] arreglo = new String[total];
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            arreglo[i] = String.format("%x", ini + i * 1000);
            diccionario.agrega(arreglo[i], arreglo[i]);
        }
        for (int i = 0; i < total; i++) {
            Assert.assertTrue(diccionario.contiene(arreglo[i]));
            diccionario.elimina(arreglo[i]);
            Assert.assertFalse(diccionario.contiene(arreglo[i]));
            Assert.assertTrue(diccionario.getElementos() == total - (i+1));
            try {
                diccionario.get(arreglo[i]);
                Assert.fail();
            } catch (NoSuchElementException nsee) {}
        }
    }

    /**
     * Prueba unitaria para {@link Diccionario#colisiones}.
     */
    @Test public void testColisiones() {
        Dispersor<String> hd;
        hd = FabricaDispersores.dispersorCadena(AlgoritmoDispersor.XOR_STRING);
        diccionario = new Diccionario<String, String>(total, hd);
        byte[] bs1 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        byte[] bs2 = { 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 0x03, 0x04 };
        String val1 = String.format("%x", random.nextInt(1000));
        String val2 = String.format("%x", random.nextInt(1000));
        diccionario.agrega(new String(bs1), val1);
        diccionario.agrega(new String(bs2), val2);
        Assert.assertTrue(diccionario.colisiones() == 1);
    }

    /**
     * Prueba unitaria para {@link Diccionario#colisionMaxima}.
     */
    @Test public void testColisionMaxima() {
        int r = random.nextInt(10000);
        String s = String.format("%x", r);
        diccionario.agrega(s, s);
        Assert.assertTrue(diccionario.colisionMaxima() == 0);
        Dispersor<String> hd;
        hd = FabricaDispersores.dispersorCadena(AlgoritmoDispersor.XOR_STRING);
        diccionario = new Diccionario<String, String>(total, hd);
        byte[] bs1 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        byte[] bs2 = { 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 0x03, 0x04 };
        String val1 = String.format("%x", random.nextInt(1000));
        String val2 = String.format("%x", random.nextInt(1000));
        diccionario.agrega(new String(bs1), val1);
        diccionario.agrega(new String(bs2), val2);
        Assert.assertTrue(diccionario.colisionMaxima() == 1);
    }

    /**
     * Prueba unitaria para {@link Diccionario#carga}.
     */
    @Test public void testCarga() {
        int ini = random.nextInt(10000);
        double c = 0.0;
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.carga() > c);
            c = diccionario.carga();
            Assert.assertTrue(diccionario.carga() < Diccionario.MAXIMA_CARGA);
        }
        for (int i = total; i < total*4; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.carga() < Diccionario.MAXIMA_CARGA);
        }
    }

    /**
     * Prueba unitaria para {@link Diccionario#getElementos}.
     */
    @Test public void testGetElementos() {
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link Diccionario#esVacia}.
     */
    @Test public void testEsVacia() {
        Assert.assertTrue(diccionario.esVacia());
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertFalse(diccionario.esVacia());
        }
        Iterator<String> iteradorLlaves = diccionario.iteradorLlaves();
        Lista<String> lista = new Lista<String>();
        while (iteradorLlaves.hasNext())
            lista.agrega(iteradorLlaves.next());
        int c = 0;
        for (String s : lista) {
            Assert.assertFalse(diccionario.esVacia());
            diccionario.elimina(s);
            c++;
        }
        Assert.assertTrue(c == total);
        Assert.assertTrue(diccionario.esVacia());
    }

    /**
     * Prueba unitaria para {@link Diccionario#limpia}.
     */
    @Test public void testLimpia() {
        Assert.assertTrue(diccionario.esVacia());
        Assert.assertTrue(diccionario.getElementos() == 0);
        for (int i = 0; i < total; i++)
            diccionario.agrega(String.valueOf(i), String.valueOf(i));
        Assert.assertFalse(diccionario.esVacia());
        Assert.assertTrue(diccionario.getElementos() == total);
        Assert.assertTrue(diccionario.carga() > 0.0);
        diccionario.limpia();
        Assert.assertTrue(diccionario.esVacia());
        Assert.assertTrue(diccionario.getElementos() == 0);
        Assert.assertTrue(diccionario.carga() == 0.0);
    }

    /**
     * Prueba unitaria para {@link Diccionario#toString}.
     */
    @Test public void testToString() {
        Diccionario<Integer, Integer> dicc =
            new Diccionario<Integer, Integer>();
        Assert.assertTrue(dicc.toString().equals("{}"));
        int[] entradas = new int[total];
        for (int i = 0; i < total; i++)
            entradas[i] = -1;
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(total);
            dicc.agrega(n, n);
            entradas[n] = n;
            String s = "{ ";
            for (int j = 0; j < total; j++) {
                if (entradas[j] == -1)
                    continue;
                s += String.format("'%d': '%d', ", j, j);
            }
            Assert.assertTrue(dicc.toString().equals(s + "}"));
        }
        String s = "{ ";
        for (int i = 0; i < total; i++) {
            if (entradas[i] == -1)
                continue;
            s += String.format("'%d': '%d', ", i, i);
        }
        Assert.assertTrue(dicc.toString().equals(s + "}"));
    }

    /**
     * Prueba unitaria para {@link Diccionario#equals}.
     */
    @Test public void testEquals() {
        Diccionario<String, String> d2 = new Diccionario<String, String>();
        Assert.assertTrue(diccionario.equals(d2));
        Assert.assertTrue(diccionario.getElementos() == d2.getElementos());
        for (String s : diccionario)
            Assert.assertTrue(d2.contiene(s));
        int ini = random.nextInt(10000);
        String[] a = new String[total];
        for (int i = 0; i < total; i++)
            a[i] = String.format("%x", ini + i * 1000);
        for (int i = 0; i < total; i++) {
            diccionario.agrega(a[i], a[i]);
            d2.agrega(a[total - i - 1], a[total - i - 1]);
        }
        Assert.assertFalse(diccionario == d2);
        Assert.assertTrue(diccionario.equals(d2));
        Assert.assertTrue(diccionario.getElementos() == d2.getElementos());
        for (String s : diccionario)
            Assert.assertTrue(d2.contiene(s));
        for (int i = 0; i < total; i++) {
            diccionario.elimina(a[i]);
            Assert.assertFalse(diccionario.equals(d2));
            Assert.assertFalse(diccionario.getElementos() == d2.getElementos());
            Assert.assertTrue(d2.contiene(a[i]));
            d2.elimina(a[i]);
            Assert.assertTrue(diccionario.equals(d2));
            Assert.assertTrue(diccionario.getElementos() == d2.getElementos());
            for (String s : diccionario)
                Assert.assertTrue(d2.contiene(s));
        }
        Assert.assertTrue(diccionario.esVacia());
        Assert.assertTrue(d2.esVacia());
        Assert.assertTrue(diccionario.equals(d2));
        ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
        }
        ini += total*2;
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            d2.agrega(s, s);
        }
        Assert.assertTrue(diccionario.getElementos() == d2.getElementos());
        Assert.assertFalse(diccionario.equals(d2));
    }

    /**
     * Prueba unitaria para {@link Diccionario#iteradorLlaves}.
     */
    @Test public void testIteradorLlaves() {
        int ini = random.nextInt(10000);
        Lista<String> lista = new Lista<String>();
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            lista.agregaFinal(s);
        }
        int c = 0;
        Iterator<String> iteradorLlaves = diccionario.iteradorLlaves();
        while (iteradorLlaves.hasNext()) {
            String s = iteradorLlaves.next();
            Assert.assertTrue(lista.contiene(s));
            c++;
        }
        Assert.assertTrue(c == total);
        c = 0;
        for (String s : lista) {
            diccionario.elimina(s);
            lista.elimina(s);
            c++;
        }
        Assert.assertTrue(c == total);
        Assert.assertTrue(lista.esVacia());
        Assert.assertTrue(diccionario.esVacia());
    }

    /**
     * Prueba unitaria para {@link Diccionario#iterator}.
     */
    @Test public void testIterator() {
        int ini = random.nextInt(10000);
        Lista<String> lista = new Lista<String>();
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            lista.agregaFinal(s);
        }
        int c = 0;
        for (String s : diccionario) {
            Assert.assertTrue(lista.contiene(s));
            lista.elimina(s);
            c++;
        }
        Assert.assertTrue(c == total);
        Assert.assertTrue(lista.esVacia());
        Iterator<String> iteradorLlaves = diccionario.iteradorLlaves();
        while (iteradorLlaves.hasNext())
            lista.agrega(iteradorLlaves.next());
        c = 0;
        for (String s : lista) {
            diccionario.elimina(s);
            c++;
        }
        Assert.assertTrue(c == total);
        c = 0;
        for (String s : diccionario)
            c++;
        Assert.assertTrue(c == 0);
    }
}
