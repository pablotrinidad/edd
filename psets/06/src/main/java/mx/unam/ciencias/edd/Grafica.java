package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            this.iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return this.iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return this.iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.vecinos = new Lista<Vertice>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return this.vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return this.aristas;
    }

    /**
     * Algoritmo auxiliar para buscar vértice
     * @param e, elemento que se quiere encontrar.
     * @return Vertice encontrado.
     */
    private Vertice getV(T elemento) {
        for (Vertice v: this.vertices) {
            if (v.elemento.equals(elemento)) { return v; }
        }
        return null;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) { throw new IllegalArgumentException(); }
        if (this.contiene(elemento)) { throw new IllegalArgumentException(); }
        Vertice v = new Vertice(elemento);
        this.vertices.agrega(v);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        Vertice v = this.getV(a);
        Vertice u = this.getV(b);
        if (v == null || u == null) { throw new NoSuchElementException(); }
        if (v.equals(u)) { throw new IllegalArgumentException(); }
        if (this.sonVecinos(a, b)) { throw new IllegalArgumentException(); }

        this.aristas += 1;
        u.vecinos.agrega(v);
        v.vecinos.agrega(u);
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice v = this.getV(a);
        Vertice u = this.getV(b);
        if (v == null || u == null) { throw new NoSuchElementException(); }
        if (!this.sonVecinos(a, b)) { throw new IllegalArgumentException(); }

        this.aristas -= 1;
        u.vecinos.elimina(v);
        v.vecinos.elimina(u);
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return this.getV(elemento) != null;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice v = this.getV(elemento);
        if (v == null) { throw new NoSuchElementException(); }

        this.vertices.elimina(v);
        for (Vertice u: v.vecinos) {
            this.aristas -= 1;
            u.vecinos.elimina(v);
        }
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice v = this.getV(a);
        Vertice u = this.getV(b);
        if (v == null || u == null) { throw new NoSuchElementException(); }
        if(vInList(v, u.vecinos) == null) { return false; }
        if(vInList(u, v.vecinos) == null) { return false; }
        return true;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Vertice v = this.getV(elemento);
        if (v == null) { throw new NoSuchElementException(); }
        return v;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || vertice.getClass() != Vertice.class)
            throw new IllegalArgumentException("Vértice inválido");
        Vertice v = (Vertice)vertice;
        v.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if(this.getElementos() == 0) { return true; }
        Vertice v = this.vertices.getPrimero();
        Cola<Vertice> queue = new Cola<Vertice>();
        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));
        queue.mete(v); v.color = Color.NEGRO;

        while(!queue.esVacia()) {
            Vertice aux = queue.saca();
            for (Vertice n: aux.vecinos) {
                if(n.color == Color.NINGUNO) {
                    queue.mete(n);
                    n.color = Color.NEGRO;
                }
            }
        }
        for(Vertice w: this.vertices) {
            if(w.color.equals(Color.NINGUNO)) { return false;}
        }
        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));
        return true;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for (Vertice v: this.vertices) { accion.actua(v); }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = this.getV(elemento);
        if (v == null) { throw new NoSuchElementException(); }
        Cola<Vertice> queue = new Cola<Vertice>();

        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));
        queue.mete(v); v.color = Color.NEGRO;

        while(!queue.esVacia()) {
            Vertice aux = queue.saca();
            accion.actua(aux);
            for (Vertice n: aux.vecinos) {
                if(n.color == Color.NINGUNO) {
                    queue.mete(n);
                    n.color = Color.NEGRO;
                }
            }
        }

        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = this.getV(elemento);
        if (v == null) { throw new NoSuchElementException(); }
        Pila<Vertice> stack = new Pila<Vertice>();

        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));
        stack.mete(v); v.color = Color.NEGRO;

        Vertice aux;
        while(!stack.esVacia()) {
            aux = stack.saca();
            accion.actua(aux);
            for (Vertice n: aux.vecinos) {
                if(n.color == Color.NINGUNO) {
                    stack.mete(n);
                    n.color = Color.NEGRO;
                }
            }
        }

        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return this.vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        this.vertices.limpia();
        this.aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String s = "{";
        for(Vertice v: this.vertices) {
            s += String.format("%d, ", v.elemento);
        }
        s += "}, {";

        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));

        for(Vertice v: this.vertices) {
            v.color = Color.NEGRO;
            for(Vertice u: v.vecinos) {
                if(u.color != Color.NEGRO) {
                    s += String.format("(%d, %d), ", v.elemento, u.elemento);
                }
            }
        }
        s += "}";

        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));

        return s;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la gráfica es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> g = (Grafica<T>)objeto;
        if (g.getAristas() != this.getAristas()) { return false; }
        if (g.getElementos() != this.getElementos()) { return false; }

        for(Vertice v: this.vertices) {
            Vertice u = this.vInList(v, g.vertices);
            if(u == null) { return false; }
            for(Vertice n: v.vecinos) {
                if(this.vInList(n, u.vecinos) == null) { return false;}
            }
        }
        return true;
    }

    /**
     * Método auxiliar que regresa un booleano indicando
     * si v está en la lista de vértices provista
     * @param v Vertice a buscar
     * @param l Lista
     */
    private Vertice vInList(Vertice v, Lista<Vertice> l) {
        for (Vertice u: l) {
            if (v.elemento.equals(u.elemento)) { return u; }
        }
        return null;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
