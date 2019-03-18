package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link
 * ArbolBinarioOrdenado}.
 */
public class TestArbolBinarioOrdenado {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El árbol. */
    private ArbolBinarioOrdenado<Integer> arbol;

    /* Método auxiliar recursivo para llenar una cola con los elementos del
     * árbol recorrido en in-order. */
    private static <T extends Comparable<T>> void
    llenaColaEnOrden(VerticeArbolBinario<T> v, Cola<T> cola) {
        if (v.hayIzquierdo())
            llenaColaEnOrden(v.izquierdo(), cola);
        cola.mete(v.get());
        if (v.hayDerecho())
            llenaColaEnOrden(v.derecho(), cola);
    }

    /**
     * Valida un árbol ordenado. Comprueba que para todo nodo A se
     * cumpla que si A tiene como hijo izquierdo a B, entonces B ≤
     * A, y si A tiene como hijo derecho a C, entonces A ≤ C.
     * @param <T> tipo del que puede ser el árbol binario ordenado.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
    arbolBinarioOrdenadoValido(ArbolBinarioOrdenado<T> arbol) {
        if (arbol.esVacia())
            return;
        UtilTestArbolBinario.arbolBinarioValido(arbol);
        Cola<T> cola = new Cola<T>();
        try {
            llenaColaEnOrden(arbol.raiz(), cola);
        } catch (NoSuchElementException sdee) {
            Assert.fail();
        }
        T a = cola.saca();
        while (!cola.esVacia()) {
            T b = cola.saca();
            Assert.assertTrue(a.compareTo(b) <= 0);
            a = b;
        }
    }

    /**
     * Crea un árbol binario para cada prueba.
     */
    public TestArbolBinarioOrdenado() {
        random = new Random();
        arbol = new ArbolBinarioOrdenado<Integer>();
        total = 3 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#ArbolBinarioOrdenado()}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(arbol.esVacia());
        Assert.assertTrue(arbol.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link
     * ArbolBinarioOrdenado#ArbolBinarioOrdenado(Coleccion)}.
     */
    @Test public void testConstructorColeccion() {
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            lista.agrega(random.nextInt(total));
        arbol = new ArbolBinarioOrdenado<Integer>(lista);
        Assert.assertTrue(lista.getLongitud() == arbol.getElementos());
        for (Integer n : lista)
            Assert.assertTrue(arbol.contiene(n));
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#agrega}.
     */
    @Test public void testAgrega() {
        try {
            arbol.agrega(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            VerticeArbolBinario<Integer> v;
            v = arbol.getUltimoVerticeAgregado();
            Assert.assertTrue(v.get() == n);
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolBinarioOrdenadoValido(arbol);
        }
    }

    /* Llena el árbol con elementos no repetidos. */
    private int[] arregloSinRepetidos() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            int r;
            boolean repetido = false;
            do {
                r = random.nextInt(1000);
                repetido = false;
                for (int j = 0; j < i; j++)
                    if (r == a[j])
                        repetido = true;
            } while (repetido);
            a[i] = r;
        }
        return a;
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#elimina}.
     */
    @Test public void testElimina() {
        int[] a = arregloSinRepetidos();
        for (int n : a)
            arbol.agrega(n);
        int n = total;
        while (arbol.getElementos() > 0) {
            Assert.assertTrue(arbol.getElementos() == n);
            int i = random.nextInt(total);
            if (a[i] == -1)
                continue;
            int e = a[i];
            VerticeArbolBinario<Integer> it = arbol.busca(e);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == e);
            arbol.elimina(e);
            it = arbol.busca(e);
            Assert.assertTrue(it == null);
            Assert.assertTrue(arbol.getElementos() == --n);
            arbolBinarioOrdenadoValido(arbol);
            a[i] = -1;
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#altura}.
     */
    @Test public void testAltura() {
        Assert.assertTrue(arbol.altura() == -1);
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioOrdenadoValido(arbol);
            int min = (int)Math.floor(Math.log(i+1)/Math.log(2));
            int max = i;
            Assert.assertTrue(arbol.altura() >= min &&
                              arbol.altura() <= max);
        }
        arbol = new ArbolBinarioOrdenado<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbolBinarioOrdenadoValido(arbol);
            Assert.assertTrue(arbol.altura() == i);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#getElementos}.
     */
    @Test public void testGetElementos() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioOrdenadoValido(arbol);
            Assert.assertTrue(arbol.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#getUltimoVerticeAgregado}.
     */
    @Test public void testGetUltimoVerticeAgregado() {
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(total);
            arbol.agrega(n);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            Assert.assertTrue(v.get().equals(n));
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#contiene}.
     */
    @Test public void testContiene() {
        int[] a = arregloSinRepetidos();
        for (int n : a) {
            Assert.assertFalse(arbol.contiene(n));
            arbol.agrega(n);
            Assert.assertTrue(arbol.contiene(n));
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#busca}.
     */
    @Test public void testBusca() {
        int[] a = arregloSinRepetidos();
        for (int n : a) {
            Assert.assertTrue(arbol.busca(n) == null);
            arbol.agrega(n);
            Assert.assertFalse(arbol.busca(n) == null);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#raiz}.
     */
    @Test public void testRaiz() {
        try {
            arbol.raiz();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int p = Integer.MAX_VALUE;
        for (int i = 0; i < total; i++) {
            int v = random.nextInt(total);
            if (p == Integer.MAX_VALUE)
                p = v;
            arbol.agrega(v);
        }
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == p);
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#esVacia}.
     */
    @Test public void testEsVacio() {
        Assert.assertTrue(arbol.esVacia());
        arbol.agrega(1);
        Assert.assertFalse(arbol.esVacia());
        arbol.elimina(1);
        Assert.assertTrue(arbol.esVacia());
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            Assert.assertFalse(arbol.esVacia());
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#limpia}.
     */
    @Test public void testLimpia() {
        Assert.assertTrue(arbol.esVacia());
        Assert.assertTrue(arbol.getElementos() == 0);
        for (int i = 0; i < total; i++)
            arbol.agrega(i);
        Assert.assertFalse(arbol.esVacia());
        Assert.assertTrue(arbol.getElementos() == total);
        arbol.limpia();
        Assert.assertTrue(arbol.esVacia());
        Assert.assertTrue(arbol.getElementos() == 0);
        try {
            VerticeArbolBinario<Integer> v = arbol.raiz();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#equals}.
     */
    @Test public void testEquals() {
        arbol = new ArbolBinarioOrdenado<Integer>();
        ArbolBinarioOrdenado<Integer> arbol2 = new ArbolBinarioOrdenado<Integer>();
        Assert.assertTrue(arbol.equals(arbol2));
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertTrue(arbol.equals(arbol2));
        arbol = new ArbolBinarioOrdenado<Integer>();
        arbol2 = new ArbolBinarioOrdenado<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            if (i != total - 1)
                arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertFalse(arbol.equals(arbol2));
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#toString}.
     */
    @Test public void testToString() {
        /* Estoy dispuesto a aceptar una mejor prueba. */
        Assert.assertTrue(arbol.toString() != null &&
                          arbol.toString().equals(""));
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioOrdenadoValido(arbol);
            Assert.assertTrue(arbol.toString() != null &&
                              !arbol.toString().equals(""));
        }
        String cadena =
            "-1\n" +
            "├─›-3\n" +
            "│  └─›-5\n" +
            "└─»2\n" +
            "   └─»4\n";
        arbol = new ArbolBinarioOrdenado<Integer>();
        for (int i = 1; i <= 5; i++)
            arbol.agrega(i * (int)Math.pow(-1, i));
        Assert.assertTrue(arbol.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#iterator}.
     */
    @Test public void testIterator() {
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            lista.agrega(n);
        }
        lista = Lista.mergeSort(lista);
        int c = 0;
        Iterator<Integer> i1 = arbol.iterator();
        Iterator<Integer> i2 = lista.iterator();
        while (i1.hasNext() && i2.hasNext())
            Assert.assertTrue(i1.next() == i2.next());
        Assert.assertTrue(!i1.hasNext() && !i2.hasNext());
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#hayPadre}.
     */
    @Test public void testVerticeHayPadre() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayPadre());
        int i = random.nextInt(total);
        arbol.agrega(i);
        v = arbol.getUltimoVerticeAgregado();
        Assert.assertTrue(v.hayPadre());
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#hayIzquierdo}.
     */
    @Test public void testVerticeHayIzquierdo() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayIzquierdo());
        int i = r-1;
        arbol.agrega(i);
        Assert.assertTrue(v.hayIzquierdo());
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#hayDerecho}.
     */
    @Test public void testVerticeHayDerecho() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayDerecho());
        arbol.agrega(r-1);
        arbol.agrega(r+1);
        Assert.assertTrue(v.hayDerecho());
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#padre}.
     */
    @Test public void testVerticePadre() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayPadre());
        try {
            v.padre();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int i = random.nextInt(total);
        arbol.agrega(i);
        v = arbol.getUltimoVerticeAgregado();
        Assert.assertTrue(v.get() == i);
        Assert.assertTrue(v.hayPadre());
        v = v.padre();
        Assert.assertTrue(v.get() == r);
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#izquierdo}.
     */
    @Test public void testVerticeIzquierdo() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayIzquierdo());
        try {
            v.izquierdo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int i = r-1;
        arbol.agrega(i);
        Assert.assertTrue(v.hayIzquierdo());
        v = v.izquierdo();
        Assert.assertTrue(v.get() == i);
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#derecho}.
     */
    @Test public void testVerticeDerecho() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayDerecho());
        try {
            v.derecho();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int i = r-1;
        int d = r+1;
        arbol.agrega(i);
        arbol.agrega(d);
        Assert.assertTrue(v.hayDerecho());
        v = v.derecho();
        Assert.assertTrue(v.get() == d);
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#altura}.
     */
    @Test public void testVerticeAltura() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            VerticeArbolBinario<Integer> r = arbol.raiz();
            Assert.assertTrue(r.altura() == i);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            Assert.assertTrue(v.altura() == 0);
        }
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#profundidad}.
     */
    @Test public void testVerticeProfundidad() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            VerticeArbolBinario<Integer> r = arbol.raiz();
            Assert.assertTrue(r.profundidad() == 0);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            Assert.assertTrue(v.profundidad() == i);
        }
    }

    /**
     * Prueba unitaria para la implementación de {@link
     * VerticeArbolBinario#get}.
     */
    @Test public void testVerticeGet() {
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(total);
            arbol.agrega(n);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            Assert.assertTrue(v.get().equals(n));
        }
    }

    /**
     * Prueba unitaria para la sobrecarga de {@link Object#toString} en la
     * implementación de {@link VerticeArbolBinario}.
     */
    @Test public void testVerticeToString() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.toString().equals(String.valueOf(r)));
    }

    /**
     * Prueba unitaria para la sobrecarga de {@link Object#equals} en la
     * implementación de {@link VerticeArbolBinario}.
     */
    @Test public void testVerticeEquals() {
        int r = random.nextInt(total);
        int i = r-1;
        int d = r+1;
        arbol.agrega(r);
        arbol.agrega(i);
        arbol.agrega(d);
        ArbolBinarioOrdenado<Integer> otro = new ArbolBinarioOrdenado<Integer>();
        otro.agrega(r);
        Assert.assertFalse(arbol.raiz().equals(otro.raiz()));
        otro.agrega(i);
        Assert.assertFalse(arbol.raiz().equals(otro.raiz()));
        otro.agrega(d);
        Assert.assertTrue(arbol.raiz().equals(otro.raiz()));
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#hasNext} a través
     * del método {@link ArbolBinarioOrdenado#iterator}.
     */
    @Test public void testIteradorHasNext() {
        Iterator<Integer> iterador = arbol.iterator();
        Assert.assertFalse(iterador.hasNext());
        arbol.agrega(-1);
        iterador = arbol.iterator();
        Assert.assertTrue(iterador.hasNext());
        for (int i = 0; i < total; i++)
            arbol.agrega(i);
        iterador = arbol.iterator();
        for (int i = 0; i < total; i++)
            iterador.next();
        Assert.assertTrue(iterador.hasNext());
        iterador.next();
        Assert.assertFalse(iterador.hasNext());
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#next} a través del
     * método {@link ArbolBinarioOrdenado#iterator}.
     */
    @Test public void testIteradorNext() {
        Iterator<Integer> iterador = arbol.iterator();
        try {
            iterador.next();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++)
            arbol.agrega(i);
        iterador = arbol.iterator();
        for (int i = 0; i < total; i++)
            Assert.assertTrue(iterador.next().equals(i));
        try {
            iterador.next();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link
     * ArbolBinarioOrdenado#giraDerecha}.
     */
    @Test public void testGiraDerecha() {
        arbol.agrega(1);
        arbol.agrega(0);
        VerticeArbolBinario<Integer> vertice = arbol.raiz();
        Assert.assertTrue(vertice.hayIzquierdo());
        arbol.giraDerecha(vertice);
        vertice = arbol.raiz();
        Assert.assertTrue(vertice.get() == 0);
        Assert.assertTrue(vertice.hayDerecho());
        Assert.assertTrue(vertice.derecho().get() == 1);
        arbol = new ArbolBinarioOrdenado<Integer>();
        arbol.agrega(0);
        arbol.agrega(2);
        arbol.agrega(1);
        vertice = arbol.raiz();
        Assert.assertTrue(vertice.get() == 0);
        Assert.assertTrue(vertice.hayDerecho());
        vertice = vertice.derecho();
        Assert.assertTrue(vertice.get() == 2);
        Assert.assertTrue(vertice.hayIzquierdo());
        arbol.giraDerecha(vertice);
        vertice = arbol.raiz();
        Assert.assertTrue(vertice.get() == 0);
        Assert.assertTrue(vertice.hayDerecho());
        vertice = vertice.derecho();
        Assert.assertTrue(vertice.get() == 1);
        Assert.assertTrue(vertice.hayDerecho());
        vertice = vertice.derecho();
        Assert.assertTrue(vertice.get() == 2);
        arbol = new ArbolBinarioOrdenado<Integer>();
        if (total == 1)
            total++;
        int[] a = arregloSinRepetidos();
        a[total-2] = 2001;
        a[total-1] = 2000;
        for (int n : a)
            arbol.agrega(n);
        vertice = null;
        int Q = -1;
        do {
            Q = a[random.nextInt(total)];
            vertice = arbol.busca(Q);
            Assert.assertTrue(vertice != null);
            Assert.assertTrue(vertice.get() == Q);
        } while (!vertice.hayIzquierdo());
        vertice = vertice.izquierdo();
        int P = vertice.get();
        int A = -1, B = -1, C = -1;
        if (vertice.hayIzquierdo()) {
            vertice = vertice.izquierdo();
            A = vertice.get();
            vertice = vertice.padre();
        }
        if (vertice.hayDerecho()) {
            vertice = vertice.derecho();
            B = vertice.get();
            vertice = vertice.padre();
        }
        vertice = vertice.padre();
        if (vertice.hayDerecho()) {
            vertice = vertice.derecho();
            C = vertice.get();
            vertice = vertice.padre();
        }
        arbol.giraDerecha(vertice);
        Assert.assertTrue(arbol.getElementos() == total);
        arbolBinarioOrdenadoValido(arbol);
        for (int n : a)
            Assert.assertTrue(arbol.busca(n) != null);
        Assert.assertTrue(vertice.get() == Q);
        Assert.assertTrue(vertice.hayPadre());
        vertice = vertice.padre();
        Assert.assertTrue(vertice.get() == P);
        if (A != -1) {
            Assert.assertTrue(vertice.hayIzquierdo());
            vertice = vertice.izquierdo();
            Assert.assertTrue(vertice.get() == A);
            vertice = vertice.padre();
        }
        Assert.assertTrue(vertice.hayDerecho());
        vertice = vertice.derecho();
        Assert.assertTrue(vertice.get() == Q);
        if (B != -1) {
            Assert.assertTrue(vertice.hayIzquierdo());
            vertice = vertice.izquierdo();
            Assert.assertTrue(vertice.get() == B);
            vertice = vertice.padre();
        }
        if (C != -1) {
            Assert.assertTrue(vertice.hayDerecho());
            vertice = vertice.derecho();
            Assert.assertTrue(vertice.get() == C);
            vertice = vertice.padre();
        }
    }

    /**
     * Prueba unitaria para {@link
     * ArbolBinarioOrdenado#giraIzquierda}.
     */
    @Test public void testGiraIzquierda() {
        arbol.agrega(0);
        arbol.agrega(1);
        VerticeArbolBinario<Integer> vertice = arbol.raiz();
        Assert.assertTrue(vertice.hayDerecho());
        arbol.giraIzquierda(vertice);
        vertice = arbol.raiz();
        Assert.assertTrue(vertice.get() == 1);
        Assert.assertTrue(vertice.hayIzquierdo());
        Assert.assertTrue(vertice.izquierdo().get() == 0);
        arbol = new ArbolBinarioOrdenado<Integer>();
        arbol.agrega(2);
        arbol.agrega(0);
        arbol.agrega(1);
        vertice = arbol.raiz();
        Assert.assertTrue(vertice.get() == 2);
        Assert.assertTrue(vertice.hayIzquierdo());
        vertice = vertice.izquierdo();
        Assert.assertTrue(vertice.get() == 0);
        Assert.assertTrue(vertice.hayDerecho());
        arbol.giraIzquierda(vertice);
        vertice = arbol.raiz();
        Assert.assertTrue(vertice.get() == 2);
        Assert.assertTrue(vertice.hayIzquierdo());
        vertice = vertice.izquierdo();
        Assert.assertTrue(vertice.get() == 1);
        Assert.assertTrue(vertice.hayIzquierdo());
        vertice = vertice.izquierdo();
        Assert.assertTrue(vertice.get() == 0);
        arbol = new ArbolBinarioOrdenado<Integer>();
        if (total == 1)
            total++;
        int[] a = arregloSinRepetidos();
        a[total-2] = 2000;
        a[total-1] = 2001;
        for (int n : a)
            arbol.agrega(n);
        vertice = null;
        int P = -1;
        do {
            P = a[random.nextInt(total)];
            vertice = arbol.busca(P);
            Assert.assertTrue(vertice != null);
            Assert.assertTrue(vertice.get() == P);
        } while (!vertice.hayDerecho());
        vertice = vertice.derecho();
        int Q = vertice.get();
        int A = -1, B = -1, C = -1;
        if (vertice.hayIzquierdo()) {
            vertice = vertice.izquierdo();
            B = vertice.get();
            vertice = vertice.padre();
        }
        if (vertice.hayDerecho()) {
            vertice = vertice.derecho();
            C = vertice.get();
            vertice = vertice.padre();
        }
        vertice = vertice.padre();
        if (vertice.hayIzquierdo()) {
            vertice = vertice.izquierdo();
            A = vertice.get();
            vertice = vertice.padre();
        }
        arbol.giraIzquierda(vertice);
        Assert.assertTrue(arbol.getElementos() == total);
        arbolBinarioOrdenadoValido(arbol);
        for (int n : a)
            Assert.assertTrue(arbol.busca(n) != null);
        Assert.assertTrue(vertice.get() == P);
        Assert.assertTrue(vertice.hayPadre());
        vertice = vertice.padre();
        Assert.assertTrue(vertice.get() == Q);
        if (C != -1) {
            Assert.assertTrue(vertice.hayDerecho());
            vertice = vertice.derecho();
            Assert.assertTrue(vertice.get() == C);
            vertice = vertice.padre();
        }
        Assert.assertTrue(vertice.hayIzquierdo());
        vertice = vertice.izquierdo();
        Assert.assertTrue(vertice.get() == P);
        if (A != -1) {
            Assert.assertTrue(vertice.hayIzquierdo());
            vertice = vertice.izquierdo();
            Assert.assertTrue(vertice.get() == A);
            vertice = vertice.padre();
        }
        if (B != -1) {
            Assert.assertTrue(vertice.hayDerecho());
            vertice = vertice.derecho();
            Assert.assertTrue(vertice.get() == B);
            vertice = vertice.padre();
        }
    }

    /* Índice para probar DFS con lambdas. */
    private Integer indice;
    /* Arreglo para probar DFS pre-order. */
    private static final int[] PRE_ORDER  = { -1, -3, -5, 2, 4 };
    /* Arreglo para probar DFS in-order. */
    private static final int[] IN_ORDER   = { -5, -3, -1, 2, 4 };
    /* Arreglo para probar DFS post-order. */
    private static final int[] POST_ORDER = { -5, -3, 4, 2, -1 };

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#dfsPreOrder}.
     */
    @Test synchronized public void testDfsPreOrder() {
        for (int i = 1; i <= 5; i++)
            arbol.agrega(i * (int)Math.pow(-1, i));
        indice = 0;
        arbol.dfsPreOrder(v -> {
                Assert.assertTrue(v.get().equals(PRE_ORDER[indice++]));
            });
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#dfsPostOrder}.
     */
    @Test synchronized public void testDfsPostOrder() {
        for (int i = 1; i <= 5; i++)
            arbol.agrega(i * (int)Math.pow(-1, i));
        indice = 0;
        arbol.dfsPostOrder(v -> {
                Assert.assertTrue(v.get().equals(POST_ORDER[indice++]));
            });
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#dfsInOrder}.
     */
    @Test synchronized public void testDfsInOrder() {
        for (int i = 1; i <= 5; i++)
            arbol.agrega(i * (int)Math.pow(-1, i));
        indice = 0;
        arbol.dfsInOrder(v -> {
                Assert.assertTrue(v.get().equals(IN_ORDER[indice++]));
            });
    }
}
