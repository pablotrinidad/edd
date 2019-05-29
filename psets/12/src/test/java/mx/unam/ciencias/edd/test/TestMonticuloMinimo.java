package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.ComparableIndexable;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MonticuloMinimo;
import mx.unam.ciencias.edd.ValorIndexable;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link MonticuloMinimo}.
 */
public class TestMonticuloMinimo {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El montículo mínimo. */
    private MonticuloMinimo<ValorIndexable<String>> monticulo;

    /* Método que verifica que un montículo mínimo cumpla con sus
     * propiedades. */
    private static <T extends ComparableIndexable<T>> void
    verificaMonticuloMinimo(MonticuloMinimo<T> monticulo) {
        int n = monticulo.getElementos();
        if (n == 0)
            return;
        for (int i = 0; i < n; i++)
            Assert.assertTrue(monticulo.get(i).getIndice() == i);
        for (int i = 0; i < n; i++) {
            T e = monticulo.get(i);
            if (e == null)
                continue;
            int izq = 2*i + 1;
            int der = 2*i + 2;
            if (izq >= n)
                continue;
            Assert.assertTrue(monticulo.get(izq).compareTo(e) >= 0);
            if (der >= n)
                continue;
            Assert.assertTrue(monticulo.get(der).compareTo(e) >= 0);
        }
    }

    /**
     * Crea un montículo mínimo para cada prueba.
     */
    public TestMonticuloMinimo() {
        random = new Random();
        total = 10 + random.nextInt(90);
        Lista<ValorIndexable<String>> l = new Lista<ValorIndexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
            l.agregaFinal(idx);
        }
        monticulo = new MonticuloMinimo<ValorIndexable<String>>(l);
        verificaMonticuloMinimo(monticulo);
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#MonticuloMinimo(Coleccion)} y
     * {@link MonticuloMinimo#MonticuloMinimo(Iterable,int)}.
     */
    @Test public void testConstructores() {
        int n = monticulo.getElementos();
        MonticuloMinimo<ValorIndexable<String>> monticulo2 =
            new MonticuloMinimo<ValorIndexable<String>>(monticulo, n);
        Assert.assertTrue(monticulo.equals(monticulo2));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#agrega}.
     */
    @Test public void testAgrega() {
        for (int i = 0; i < total * 4; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
            monticulo.agrega(idx);
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == total + i + 1);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#elimina}.
     */
    @Test public void testElimina() {
        while (!monticulo.esVacia()) {
            ValorIndexable<String> a = monticulo.elimina();
            Assert.assertTrue(a.getIndice() == -1);
            for (int i = 0; i < monticulo.getElementos(); i++) {
                ValorIndexable<String> b = monticulo.get(i);
                Assert.assertTrue(a.getValor() <= b.getValor());
            }
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == --total);
        }
        try {
            monticulo.elimina();
            Assert.fail();
        } catch (IllegalStateException ise) {}
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#elimina(Object)}.
     */
    @Test public void testEliminaElemento() {
        while (!monticulo.esVacia()) {
            int n = monticulo.getElementos();
            ValorIndexable<String> a = monticulo.get(random.nextInt(n));
            monticulo.elimina(a);
            Assert.assertTrue(a.getIndice() == -1);
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == --total);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#contiene}.
     */
    @Test public void testContiene() {
        for (int i = 0; i < monticulo.getElementos(); i++) {
            ValorIndexable<String> a = monticulo.get(i);
            Assert.assertTrue(monticulo.contiene(a));
        }
        ValorIndexable<String> a = new ValorIndexable<String>("a", 0);
        Assert.assertFalse(monticulo.contiene(a));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#esVacia}.
     */
    @Test public void testEsVacio() {
        monticulo = new MonticuloMinimo<ValorIndexable<String>>();
        Assert.assertTrue(monticulo.esVacia());
        String s = Integer.toString(random.nextInt());
        double p = random.nextDouble();
        ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
        monticulo.agrega(idx);
        Assert.assertFalse(monticulo.esVacia());
        idx = monticulo.elimina();
        Assert.assertTrue(monticulo.esVacia());
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#limpia}.
     */
    @Test public void testLimpia() {
        Assert.assertFalse(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == total);
        monticulo.limpia();
        Assert.assertTrue(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == 0);
        monticulo = new MonticuloMinimo<ValorIndexable<String>>();
        Assert.assertTrue(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == 0);
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(i);
            monticulo.agrega(new ValorIndexable<String>(s, i));
        }
        Assert.assertFalse(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == total);
        monticulo.limpia();
        Assert.assertTrue(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == 0);
        for (int i = 0; i < total; i++) {
            try {
                monticulo.get(i);
                Assert.fail();
            } catch (NoSuchElementException nsee) {}
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#reordena}.
     */
    @Test public void testReordena() {
        int n = monticulo.getElementos();
        for (int i = 0; i < n; i++) {
            verificaMonticuloMinimo(monticulo);
            ValorIndexable<String> idx = monticulo.get(random.nextInt(n));
            idx.setValor(idx.getValor() / 10.0);
            monticulo.reordena(idx);
            verificaMonticuloMinimo(monticulo);
        }
        for (int i = 0; i < n; i++) {
            verificaMonticuloMinimo(monticulo);
            ValorIndexable<String> idx = monticulo.get(random.nextInt(n));
            idx.setValor(idx.getValor() * 10.0);
            monticulo.reordena(idx);
            verificaMonticuloMinimo(monticulo);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#getElementos}.
     */
    @Test public void testGetElementos() {
        monticulo = new MonticuloMinimo<ValorIndexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
            Assert.assertTrue(monticulo.getElementos() == i);
            monticulo.agrega(idx);
            Assert.assertTrue(monticulo.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#get}.
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
        monticulo = new MonticuloMinimo<ValorIndexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
            monticulo.agrega(idx);
            Assert.assertTrue(monticulo.getElementos() == i + 1);
            Assert.assertTrue(monticulo.get(idx.getIndice()) == idx);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#toString}.
     */
    @Test public void testToString() {
        Lista<ValorIndexable<String>> lista = new Lista<ValorIndexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
            lista.agrega(idx);
        }
        monticulo = new MonticuloMinimo<ValorIndexable<String>>(lista);
        String s = "";
        for (int i = 0; i < monticulo.getElementos(); i++) {
            Assert.assertFalse(s.equals(monticulo.toString()));
            s += String.format("%s, ", monticulo.get(i).toString());
        }
        Assert.assertTrue(s.equals(monticulo.toString()));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#equals}.
     */
    @Test public void testEquals() {
        Lista<ValorIndexable<String>> lista = new Lista<ValorIndexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
            lista.agrega(idx);
        }
        monticulo = new MonticuloMinimo<ValorIndexable<String>>(lista);
        verificaMonticuloMinimo(monticulo);
        MonticuloMinimo<ValorIndexable<String>> otro;
        otro = new MonticuloMinimo<ValorIndexable<String>>();
        for (int i = 0; i < monticulo.getElementos(); i++) {
            Assert.assertFalse(monticulo.equals(otro));
            otro.agrega(monticulo.get(i));
        }
        Assert.assertTrue(monticulo.equals(otro));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#iterator}.
     */
    @Test public void testIterator() {
        monticulo = new MonticuloMinimo<ValorIndexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(i);
            double p = i;
            ValorIndexable<String> idx = new ValorIndexable<String>(s, p);
            monticulo.agrega(idx);
        }
        int i = 0;
        for (ValorIndexable<String> idx : monticulo)
            Assert.assertTrue(idx.getValor() == i++);
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#hasNext} a través
     * del método {@link MonticuloMinimo#iterator}.
     */
    @Test public void testIteradorHasNext() {
        Iterator<ValorIndexable<String>> it = monticulo.iterator();
        for (int i = 0; i < total; i++) {
            Assert.assertTrue(it.hasNext());
            try {
                it.next();
            } catch (NoSuchElementException nsee) {
                Assert.fail();
            }
        }
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#next} a través del
     * método {@link MonticuloMinimo#iterator}.
     */
    @Test public void testIteradorNext() {
        Iterator<ValorIndexable<String>> it = monticulo.iterator();
        while (it.hasNext()) {
            try {
                it.next();
            } catch (NoSuchElementException nsee) {
                Assert.fail();
            }
        }
        try {
            it.next();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#heapSort}.
     */
    @Test public void testHeapSort() {
        Random random = new Random();
        int total = 10 + random.nextInt(90);
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            lista.agrega(random.nextInt() % total);
        Lista<Integer> ordenada = MonticuloMinimo.heapSort(lista);
        Lista<Integer> control = Lista.mergeSort(lista);
        Assert.assertTrue(ordenada.equals(control));
    }
}
