package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.AccionVerticeGrafica;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeGrafica;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Grafica}.
 */
public class TestGrafica {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* La gráfica. */
    private Grafica<Integer> grafica;

    /**
     * Crea una gráfica para cada prueba.
     */
    public TestGrafica() {
        random = new Random();
        total = 2 + random.nextInt(100);
        grafica = new Grafica<Integer>();
    }

    /**
     * Prueba unitaria para {@link Grafica#Grafica}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == 0);
        Assert.assertTrue(grafica.getAristas() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#getElementos}.
     */
    @Test public void testGetElementos() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#getAristas}.
     */
    @Test public void testGetAristas() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int cont = 0;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                cont++;
                Assert.assertTrue(grafica.getAristas() == cont);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#agrega}.
     */
    @Test public void testAgrega() {
        try {
            grafica.agrega(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.contiene(i));
        }
        try {
            grafica.agrega(total/2);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#conecta}.
     */
    @Test public void testConecta() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
        try {
            grafica.conecta(0, 0);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.conecta(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.conecta(0, 1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#desconecta}.
     */
    @Test public void testDesconecta() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int c = (total * (total-1)) / 2;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertTrue(grafica.getAristas() == c--);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                grafica.desconecta(i, j);
                Assert.assertFalse(grafica.sonVecinos(i, j));
            }
        }
        try {
            grafica.desconecta(0, 0);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.desconecta(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#contiene}.
     */
    @Test public void testContiene() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.contiene(i));
        }
        Assert.assertFalse(grafica.contiene(-1));
    }

    /**
     * Prueba unitaria para {@link Grafica#elimina}.
     */
    @Test public void testElimina() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int vertices = total;
        int aristas = (total * (total - 1)) / 2;
        int[] grado = { vertices - 1 };
        for (int i = 0; i < total; i++) {
            grafica.paraCadaVertice(v -> Assert.assertTrue(v.getGrado() ==
                                                           grado[0]));
            Assert.assertTrue(grafica.getElementos() == vertices);
            Assert.assertTrue(grafica.getAristas() == aristas);
            grafica.elimina(i);
            vertices--;
            aristas -= vertices;
            grado[0]--;
        }
        Assert.assertTrue(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == 0);
        Assert.assertTrue(grafica.getAristas() == 0);
        try {
            grafica.elimina(-1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#sonVecinos}.
     */
    @Test public void testSonVecinos() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
        try {
            grafica.sonVecinos(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

   /**
     * Prueba unitaria para {@link Grafica#vertice}.
     */
    @Test public void testVertice() {
        try {
            grafica.vertice(-1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        VerticeGrafica<Integer> vertice = grafica.vertice(0);
        Assert.assertTrue(vertice.get() == 0);
        Assert.assertTrue(vertice.getGrado() == total - 1);
        Assert.assertTrue(vertice.getColor() == Color.NINGUNO);
        grafica.setColor(vertice, Color.ROJO);
        Assert.assertTrue(vertice.getColor() == Color.ROJO);
        int v = 1;
        Lista<Integer> vecinos = new Lista<Integer>();
        for (int i = 1; i < total; i++)
            vecinos.agrega(i);
        for (VerticeGrafica<Integer> vecino : vertice.vecinos()) {
            Assert.assertTrue(vecinos.contiene(vecino.get()));
            vecinos.elimina(vecino.get());
        }
        Assert.assertTrue(vecinos.esVacia());
        Assert.assertTrue(vecinos.getLongitud() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#setColor}.
     */
    @Test public void testSetColor() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            VerticeGrafica<Integer> v = grafica.vertice(i);
            Assert.assertTrue(v.getColor() == Color.NINGUNO);
            grafica.setColor(v, Color.ROJO);
            Assert.assertTrue(v.getColor() == Color.ROJO);
            grafica.setColor(v, Color.NEGRO);
            Assert.assertTrue(v.getColor() == Color.NEGRO);
        }
        /* Queremos ver que falle con vértices que no sean vértices. */
        VerticeGrafica<Integer> vertice = new VerticeGrafica<Integer>() {
                @Override public Integer get() { return 0; }
                @Override public int getGrado() { return 0; }
                @Override public Color getColor() { return Color.NINGUNO; }
                @Override public Iterable<? extends VerticeGrafica<Integer>>
                vecinos() { return new Lista<VerticeGrafica<Integer>>(); }
            };
        try {
            grafica.setColor(vertice, Color.NINGUNO);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#esConexa}.
     */
    @Test public void testEsConexa() {
        if (total < 6)
            total += 6;
        grafica.agrega(0);
        for (int i = 1; i < total; i++) {
            grafica.agrega(i);
            Assert.assertFalse(grafica.esConexa());
            grafica.conecta(i-1, i);
            Assert.assertTrue(grafica.esConexa());
        }
        grafica.limpia();
        for (int i = 0; i < total/2; i++)
            grafica.agrega(i);
        for (int i = 0; i < total/2; i++)
            for (int j = i+1; j < total/2; j++)
                grafica.conecta(i, j);
        Assert.assertTrue(grafica.esConexa());
        for (int i = total/2; i < total; i++)
            grafica.agrega(i);
        for (int i = total/2; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Assert.assertFalse(grafica.esConexa());
        grafica.conecta(total/2-1, total/2);
        Assert.assertTrue(grafica.esConexa());
    }

   /**
     * Prueba unitaria para {@link Grafica#paraCadaVertice}.
     */
    @Test public void testParaCadaVertice() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int[] cont = { 0 };
        grafica.paraCadaVertice(v -> {
                grafica.setColor(v, Color.ROJO);
                cont[0]++;
            });
        Assert.assertTrue(cont[0] == total);
        grafica.paraCadaVertice(v -> Assert.assertTrue(v.getColor() ==
                                                       Color.ROJO));
    }

   /**
     * Prueba unitaria para {@link Grafica#bfs}.
     */
    @Test public void testBfs() {
        for (int i = 0; i < 7; i++)
            grafica.agrega(i);
        grafica.conecta(0, 1);
        grafica.conecta(0, 2);
        grafica.conecta(1, 3);
        grafica.conecta(1, 4);
        grafica.conecta(3, 5);
        grafica.conecta(3, 6);
        int[] c = { 0 };
        int[] a = { 0, 1, 2, 3, 4, 5, 6 };
        grafica.bfs(0, v -> Assert.assertTrue(v.get() == a[c[0]++]));
        grafica = new Grafica<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Lista<Integer> lista = new Lista<Integer>();
        grafica.bfs(0, v -> lista.agrega(v.get()));
        Lista<Integer> vertices = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            vertices.agrega(i);
        Assert.assertTrue(lista.getLongitud() == vertices.getLongitud());
        for (Integer n : lista)
            Assert.assertTrue(vertices.contiene(lista.eliminaPrimero()));
        grafica.paraCadaVertice(v -> Assert.assertTrue(v.getColor() ==
                                                       Color.NINGUNO));
    }

    /**
     * Prueba unitaria para {@link Grafica#dfs}.
     */
    @Test public void testDfs() {
        for (int i = 0; i < 7; i++)
            grafica.agrega(i);
        grafica.conecta(0, 1);
        grafica.conecta(0, 2);
        grafica.conecta(1, 3);
        grafica.conecta(1, 4);
        grafica.conecta(3, 5);
        grafica.conecta(3, 6);
        int[] c = { 0 };
        int[] a = { 0, 2, 1, 4, 3, 6, 5 };
        grafica.dfs(0, v -> Assert.assertTrue(v.get() == a[c[0]++]));
        grafica = new Grafica<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Lista<Integer> lista = new Lista<Integer>();
        grafica.dfs(0, v -> lista.agrega(v.get()));
        Lista<Integer> vertices = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            vertices.agrega(i);
        Assert.assertTrue(lista.getLongitud() == vertices.getLongitud());
        for (Integer n : lista)
            Assert.assertTrue(vertices.contiene(lista.eliminaPrimero()));
        grafica.paraCadaVertice(v -> Assert.assertTrue(v.getColor() ==
                                                       Color.NINGUNO));
    }

    /**
     * Prueba unitaria para {@link Grafica#esVacia}.
     */
    @Test public void testEsVacio() {
        Assert.assertTrue(grafica.esVacia());
        grafica.agrega(0);
        Assert.assertFalse(grafica.esVacia());
    }

    /**
     * Prueba unitaria para {@link Grafica#limpia}.
     */
    @Test public void testLimpia() {
        Assert.assertTrue(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == 0);
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Assert.assertFalse(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == total);
        grafica.limpia();
        Assert.assertTrue(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#toString}.
     */
    @Test public void testToString() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        String s = "{";
        for (int i = 0; i < total; i++)
            s += String.format("%d, ", i);
        s += "}, {";
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                s += String.format("(%d, %d), ", i, j);
        s += "}";
        Assert.assertTrue(grafica.toString().equals(s));
    }

    /**
     * Prueba unitaria para {@link Grafica#equals}.
     */
    @Test public void testEquals() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Grafica<Integer> otra = new Grafica<Integer>();
        for (int i = total - 1; i > -1; i--)
            otra.agrega(i);
        Assert.assertFalse(grafica.equals(otra));
        for (int i = total - 1; i > -1; i--)
            for (int j = 0; j < i; j++)
                otra.conecta(i, j);
        Assert.assertTrue(grafica.equals(otra));
        grafica = new Grafica<Integer>();
        otra = new Grafica<Integer>();
        for (int i = 0; i < 4; i++) {
            grafica.agrega(i);
            otra.agrega(i);
        }
        grafica.conecta(0, 1);
        grafica.conecta(2, 3);
        otra.conecta(0, 2);
        otra.conecta(1, 3);
        Assert.assertFalse(grafica.equals(otra));
    }

    /**
     * Prueba unitaria para {@link Grafica#iterator}.
     */
    @Test public void testIterator() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int c = 0;
        for (Integer i : grafica)
            Assert.assertTrue(i == c++);
    }

    /**
     * Prueba unitaria para la implementación {@link VerticeGrafica#get} a
     * través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeGet() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            Assert.assertTrue(i == vertice.get());
        }
    }

    /**
     * Prueba unitaria para la implementación {@link VerticeGrafica#getGrado}
     * a través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeGetGrado() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            Assert.assertTrue(vertice.getGrado() == total - 1);
        }
    }

    /**
     * Prueba unitaria para la implementación de {@link VerticeGrafica#getColor}
     * a través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeGetColor() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            Assert.assertTrue(vertice.getColor() == Color.NINGUNO);
            grafica.setColor(vertice, Color.ROJO);
            Assert.assertTrue(vertice.getColor() == Color.ROJO);
        }
    }

    /**
     * Prueba unitaria para la implementación de {@link VerticeGrafica#vecinos}
     * a través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeVecinos() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            int c = 0;
            for (VerticeGrafica<Integer> vecino : vertice.vecinos()) {
                int e = vecino.get();
                Assert.assertTrue(e >= 0);
                Assert.assertTrue(e < total);
                Assert.assertFalse(e == i);
                c++;
            }
            Assert.assertTrue(vertice.getGrado() == c);
        }
    }
}
