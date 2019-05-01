package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolRojinegro}.
 */
public class TestArbolRojinegro {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El árbol. */
    private ArbolRojinegro<Integer> arbol;

    /* Valida el vértice de un árbol rojinegro, y recursivamente
     * revisa sus hijos. */
    private static <T extends Comparable<T>> void
    arbolRojinegroValido(ArbolRojinegro<T> arbol,
                         VerticeArbolBinario<T> v) {
        switch (arbol.getColor(v)) {
        case NEGRO:
            if (v.hayIzquierdo())
                arbolRojinegroValido(arbol, v.izquierdo());
            if (v.hayDerecho())
                arbolRojinegroValido(arbol, v.derecho());
            break;
        case ROJO:
            if (v.hayIzquierdo()) {
                VerticeArbolBinario<T> i = v.izquierdo();
                Assert.assertTrue(arbol.getColor(i) == Color.NEGRO);
                arbolRojinegroValido(arbol, i);
            }
            if (v.hayDerecho()) {
                VerticeArbolBinario<T> d = v.derecho();
                Assert.assertTrue(arbol.getColor(d) == Color.NEGRO);
                arbolRojinegroValido(arbol, d);
            }
            break;
        default:
            Assert.fail();
        }
    }

    /* Valida que los caminos del vértice a sus hojas tengan todos
       el mismo número de vértices negros. */
    private static <T extends Comparable<T>> int
    validaCaminos(ArbolRojinegro<T> arbol,
                  VerticeArbolBinario<T> v) {
        int ni = -1, nd = -1;
        if (v.hayIzquierdo()) {
            VerticeArbolBinario<T> i = v.izquierdo();
            ni = validaCaminos(arbol, i);
        } else {
            ni = 1;
        }
        if (v.hayDerecho()) {
            VerticeArbolBinario<T> d = v.derecho();
            nd = validaCaminos(arbol, d);
        } else {
            nd = 1;
        }
        Assert.assertTrue(ni == nd);
        switch (arbol.getColor(v)) {
        case NEGRO:
            return 1 + ni;
        case ROJO:
            return ni;
        default:
            Assert.fail();
        }
        // Inalcanzable.
        return -1;
    }

    /**
     * Valida un árbol rojinegro. Comprueba que la raíz sea negra, que las hojas
     * sean negras, que un vértice rojo tenga dos hijos negros, y que todo
     * camino de la raíz a sus hojas tiene el mismo número de vértices negros.
     * @param <T> tipo del que puede ser el árbol rojinegro.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
    arbolRojinegroValido(ArbolRojinegro<T> arbol) {
        if (arbol.esVacia())
            return;
        TestArbolBinarioOrdenado.arbolBinarioOrdenadoValido(arbol);
        VerticeArbolBinario<T> v = arbol.raiz();
        Assert.assertTrue(arbol.getColor(v) == Color.NEGRO);
        arbolRojinegroValido(arbol, v);
        validaCaminos(arbol, v);
    }

    /**
     * Crea un árbol rojo-negro para cada prueba.
     */
    public TestArbolRojinegro() {
        random = new Random();
        arbol = new ArbolRojinegro<Integer>();
        total = random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#ArbolRojinegro()}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(arbol.esVacia());
        Assert.assertTrue(arbol.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#ArbolRojinegro(Coleccion)}.
     */
    @Test public void testConstructorColeccion() {
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            lista.agrega(random.nextInt(total));
        arbol = new ArbolRojinegro<Integer>(lista);
        lista = Lista.mergeSort(lista);
        Assert.assertTrue(lista.getLongitud() == arbol.getElementos());
        Iterator<Integer> i = lista.iterator();
        Iterator<Integer> j = arbol.iterator();
        while (i.hasNext() && j.hasNext())
            Assert.assertTrue(i.next().equals(j.next()));
        Assert.assertTrue(!i.hasNext());
        Assert.assertTrue(!j.hasNext());
    }

    /* Prueba determinísticamente el caso 1. */
    private void testAgregaCaso1() {
        arbol.agrega(1);
        String s = arbol.toString();
        String t = "N{1}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 2a (sin hermano). */
    private void testAgregaCaso2A() {
        arbol.agrega(2);
        String s = arbol.toString();
        String t =
            "N{1}\n" +
            "└─»R{2}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 2b (con hermano). */
    private void testAgregaCaso2B() {
        arbol.agrega(0);
        String s = arbol.toString();
        String t =
            "N{1}\n" +
            "├─›R{0}\n" +
            "└─»R{2}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 3. */
    private void testAgregaCaso3() {
        arbol.agrega(4);
        String s = arbol.toString();
        String t =
            "N{1}\n" +
            "├─›N{0}\n" +
            "└─»N{2}\n" +
            "   └─»R{4}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 4. */
    private void testAgregaCaso4() {
        arbol.agrega(3);
        String s = arbol.toString();
        String t =
            "N{1}\n" +
            "├─›N{0}\n" +
            "└─»N{3}\n" +
            "   ├─›R{2}\n" +
            "   └─»R{4}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 5. */
    private void testAgregaCaso5() {
        arbol.agrega(5);
        String s = arbol.toString();
        String t =
            "N{1}\n" +
            "├─›N{0}\n" +
            "└─»R{3}\n" +
            "   ├─›N{2}\n" +
            "   └─»N{4}\n" +
            "      └─»R{5}\n";
        Assert.assertTrue(s.equals(t));
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#agrega}.
     */
    @Test public void testAgrega() {
        testAgregaCaso1();
        testAgregaCaso2A();
        testAgregaCaso2B();
        testAgregaCaso3();
        testAgregaCaso4();
        testAgregaCaso5();
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolRojinegroValido(arbol);
        }
    }

    /* Prueba determinísticamente el caso negro-rojo. */
    private void testEliminaNegroRojo() {
        arbol = new ArbolRojinegro<Integer>();
        arbol.agrega(1);
        arbol.agrega(0);
        arbol.elimina(1);
        arbolRojinegroValido(arbol);
        Assert.assertFalse(arbol.esVacia());
        String s = arbol.toString();
        String t = "N{0}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso rojo-negro. */
    private void testEliminaRojoNegro() {
        arbol = new ArbolRojinegro<Integer>();
        arbol.agrega(1);
        arbol.agrega(0);
        arbol.elimina(0);
        arbolRojinegroValido(arbol);
        Assert.assertFalse(arbol.esVacia());
        String s = arbol.toString();
        String t = "N{1}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 1. */
    private void testEliminaCaso1() {
        arbol = new ArbolRojinegro<Integer>();
        arbol.agrega(0);
        arbol.elimina(0);
        arbolRojinegroValido(arbol);
        Assert.assertTrue(arbol.esVacia());
        String s = arbol.toString();
        String t = "";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 2-4. */
    private void testEliminaCaso24() {
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 0; i < 6; i++)
            arbol.agrega(i + 1);
        arbol.elimina(2);
        arbolRojinegroValido(arbol);
        String s = arbol.toString();
        String t =
            "N{4}\n" +
            "├─›N{1}\n" +
            "│  └─»R{3}\n" +
            "└─»N{5}\n" +
            "   └─»R{6}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 3-1. */
    private void testEliminaCaso31() {
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 0; i < 4; i++)
            arbol.agrega(i + 1);
        arbol.elimina(4);
        arbolRojinegroValido(arbol);
        String s = arbol.toString();
        String t =
            "N{2}\n" +
            "├─›N{1}\n" +
            "└─»N{3}\n";
        Assert.assertTrue(s.equals(t));
        arbol.elimina(1);
        arbolRojinegroValido(arbol);
        s = arbol.toString();
        t = "N{2}\n" +
            "└─»R{3}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 4. */
    private void testEliminaCaso4() {
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 0; i < 6; i++)
            arbol.agrega(i + 1);
        arbol.elimina(3);
        arbolRojinegroValido(arbol);
        String s = arbol.toString();
        String t =
            "N{2}\n" +
            "├─›N{1}\n" +
            "└─»R{5}\n" +
            "   ├─›N{4}\n" +
            "   └─»N{6}\n";
        Assert.assertTrue(s.equals(t));
        arbol.elimina(4);
        arbolRojinegroValido(arbol);
        s = arbol.toString();
        t = "N{2}\n" +
            "├─›N{1}\n" +
            "└─»N{5}\n" +
            "   └─»R{6}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 5a-6. */
    private void testEliminaCaso5a6() {
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 0; i < 5; i++)
            arbol.agrega(i + 1);
        arbol.elimina(5);
        arbolRojinegroValido(arbol);
        String s = arbol.toString();
        String t =
            "N{2}\n" + 
            "├─›N{1}\n" + 
            "└─»N{4}\n" + 
            "   └─›R{3}\n";
        Assert.assertTrue(s.equals(t));
        arbol.elimina(1);
        arbolRojinegroValido(arbol);
        s = arbol.toString();
        t = "N{3}\n" +
            "├─›N{2}\n" +
            "└─»N{4}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 5b-6. */
    private void testEliminaCaso5b6() {
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 5; i > 0; i--)
            arbol.agrega(i);
        arbol.elimina(1);
        arbolRojinegroValido(arbol);
        String s = arbol.toString();
        String t =
            "N{4}\n" +
            "├─›N{2}\n" +
            "│  └─»R{3}\n" +
            "└─»N{5}\n";
        Assert.assertTrue(s.equals(t));
        arbol.elimina(5);
        arbolRojinegroValido(arbol);
        s = arbol.toString();
        t = "N{3}\n" +
            "├─›N{2}\n" +
            "└─»N{4}\n";
        Assert.assertTrue(s.equals(t));
    }

    /* Prueba determinísticamente el caso 6. */
    private void testEliminaCaso6() {
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 0; i < 6; i++)
            arbol.agrega(i + 1);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == 2);
        Assert.assertTrue(arbol.getColor(v) == Color.NEGRO);
        Assert.assertTrue(v.hayIzquierdo());
        Assert.assertTrue(v.hayDerecho());
        v = v.derecho();
        Assert.assertTrue(v.get() == 4);
        Assert.assertTrue(arbol.getColor(v) == Color.ROJO);
        Assert.assertTrue(v.hayIzquierdo());
        Assert.assertTrue(v.hayDerecho());
        arbol.elimina(4);
        arbolRojinegroValido(arbol);
        v = arbol.raiz();
        Assert.assertTrue(v.get() == 2);
        Assert.assertTrue(arbol.getColor(v) == Color.NEGRO);
        Assert.assertTrue(v.hayIzquierdo());
        Assert.assertTrue(v.hayDerecho());
        v = v.derecho();
        Assert.assertTrue(v.get() == 5);
        Assert.assertTrue(arbol.getColor(v) == Color.ROJO);
        Assert.assertTrue(v.hayIzquierdo());
        Assert.assertTrue(v.hayDerecho());
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#elimina}.
     */
    @Test public void testElimina() {
        testEliminaNegroRojo();
        testEliminaRojoNegro();
        testEliminaCaso1();
        testEliminaCaso24();
        testEliminaCaso31();
        testEliminaCaso4();
        testEliminaCaso5a6();
        testEliminaCaso5b6();
        testEliminaCaso6();
        arbol = new ArbolRojinegro<Integer>();
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
            arbol.agrega(a[i]);
        }
        for (int i : a)
            Assert.assertTrue(arbol.busca(i) != null);
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
            arbolRojinegroValido(arbol);
            a[i] = -1;
        }
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#getColor}.
     */
    @Test public void testGetColor() {
        arbol.agrega(1);
        arbol.agrega(0);
        arbol.agrega(2);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == 1);
        Assert.assertTrue(arbol.getColor(v) == Color.NEGRO);
        Assert.assertTrue(v.hayIzquierdo());
        v = v.izquierdo();
        Assert.assertTrue(v.get() == 0);
        Assert.assertTrue(arbol.getColor(v) == Color.ROJO);
        Assert.assertTrue(v.hayPadre());
        v = v.padre();
        Assert.assertTrue(v.hayDerecho());
        v = v.derecho();
        Assert.assertTrue(v.get() == 2);
        Assert.assertTrue(arbol.getColor(v) == Color.ROJO);
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#giraIzquierda}.
     */
    @Test public void testGiraIzquierda() {
        try {
            arbol.giraIzquierda(null);
            Assert.fail();
        } catch (UnsupportedOperationException uoe) {}
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            try {
                arbol.giraIzquierda(v);
                Assert.fail();
            } catch (UnsupportedOperationException uoe) {}
        }
        if (arbol.esVacia())
            return;
        Cola<VerticeArbolBinario<Integer>> cola;
        cola = new Cola<VerticeArbolBinario<Integer>>();
        cola.mete(arbol.raiz());
        while (!cola.esVacia()) {
            VerticeArbolBinario<Integer> v = cola.saca();
            try {
                arbol.giraIzquierda(v);
                Assert.fail();
            } catch (UnsupportedOperationException uoe) {}
            if (v.hayIzquierdo())
                cola.mete(v.izquierdo());
            if (v.hayDerecho())
                cola.mete(v.derecho());
        }
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#giraDerecha}.
     */
    @Test public void testGiraDerecha() {
        try {
            arbol.giraDerecha(null);
            Assert.fail();
        } catch (UnsupportedOperationException uoe) {}
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            try {
                arbol.giraDerecha(v);
                Assert.fail();
            } catch (UnsupportedOperationException uoe) {}
        }
        if (arbol.esVacia())
            return;
        Cola<VerticeArbolBinario<Integer>> cola;
        cola = new Cola<VerticeArbolBinario<Integer>>();
        cola.mete(arbol.raiz());
        while (!cola.esVacia()) {
            VerticeArbolBinario<Integer> v = cola.saca();
            try {
                arbol.giraDerecha(v);
                Assert.fail();
            } catch (UnsupportedOperationException uoe) {}
            if (v.hayIzquierdo())
                cola.mete(v.izquierdo());
            if (v.hayDerecho())
                cola.mete(v.derecho());
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#equals}.
     */
    @Test public void testEquals() {
        arbol = new ArbolRojinegro<Integer>();
        ArbolRojinegro<Integer> arbol2 = new ArbolRojinegro<Integer>();
        Assert.assertTrue(arbol.equals(arbol2));
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertTrue(arbol.equals(arbol2));
        arbol = new ArbolRojinegro<Integer>();
        arbol2 = new ArbolRojinegro<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            if (i != total - 1)
                arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        if (total > 0)
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
            arbolRojinegroValido(arbol);
            Assert.assertTrue(arbol.toString() != null &&
                              !arbol.toString().equals(""));
        }
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 1; i <= 7; i++)
            arbol.agrega(i);
        String cadena =
            "N{2}\n" +
            "├─›N{1}\n" +
            "└─»R{4}\n" +
            "   ├─›N{3}\n" +
            "   └─»N{6}\n" +
            "      ├─›R{5}\n" +
            "      └─»R{7}\n";
        Assert.assertTrue(arbol.toString().equals(cadena));
    }
}
