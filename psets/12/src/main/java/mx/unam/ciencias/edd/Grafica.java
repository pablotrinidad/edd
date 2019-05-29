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
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.vecinos = new Diccionario<T, Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return this.indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            return customCompare(this.distancia, vertice.distancia);
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return this.vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return this.vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return this.vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Diccionario<T, Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return this.vertices.getElementos();
    }
    
        
    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return this.aristas;
    }

    /**
     * Comparación personalizada de doubles.
     * 
     * Realiza la comparación de dos valores doubles
     * según la especificación del libro.
     * @param a
     * @param b
     * @return
     */
    private int customCompare(double a, double b) {
        if(a != -1 && (b == -1 || a < b)) { return -1; }
        if( b!= -1 && (a == -1 || a > b)) { return 1; }
        return 0;
    }

    /**
     * Suma personalizada de doubles.
     * 
     * Realiza la suma de dos valores doubles
     * según la especificación del libro.
     * @param a
     * @param b
     * @return
     */
    private double customAdd(double a, double b) {
        return (a==-1 || b==-1) ? -1 : a + b;
    }

    /**
     * Algoritmo auxiliar para buscar vértice
     * @param e, elemento que se quiere encontrar.
     * @return Vertice encontrado.
     */
    private Vertice getV(T elemento) {
        if (this.vertices.contiene(elemento))
            return this.vertices.get(elemento);
        return null;
    }

    /**
     * Algoritmo auxiliar para buscar vecino de v
     * @param v, vertice con vecino u
     * @param u, vertice con vecino v
     * @return Vecino encontrado
     */
    private Vecino getN(Vertice v, Vertice u) {
        if(v.vecinos.contiene(u.get()))
            return v.vecinos.get(u.get());
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
        this.vertices.agrega(elemento, v);
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
        this.conecta(a, b, 1.0);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        if(peso <= 0) { throw new IllegalArgumentException(); }
        Vertice v = this.getV(a);
        Vertice u = this.getV(b);
        if (v == null || u == null) { throw new NoSuchElementException(); }
        if (v.equals(u)) { throw new IllegalArgumentException(); }
        if (this.sonVecinos(a, b)) { throw new IllegalArgumentException(); }

        this.aristas += 1;
        u.vecinos.agrega(a, new Vecino(v, peso));
        v.vecinos.agrega(b, new Vecino(u, peso));
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
        u.vecinos.elimina(this.getN(u, v).get());
        v.vecinos.elimina(this.getN(v, u).get());
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

        for (Vecino u: v.vecinos) {
            this.desconecta(v.elemento, u.vecino.elemento);
        }
        this.vertices.elimina(v.get());
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
        if(this.getN(v, u) == null) { return false; }
        if(this.getN(u, v) == null) { return false; }
        return true;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        Vertice v = this.getV(a);
        Vertice u = this.getV(b);
        if (v == null || u == null) { throw new NoSuchElementException(); }
        if (!this.sonVecinos(a, b)) { throw new IllegalArgumentException(); }
        return this.getN(v, u).peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        Vertice v = this.getV(a);
        Vertice u = this.getV(b);
        if (v == null || u == null) { throw new NoSuchElementException(); }
        if (!this.sonVecinos(a, b) || peso <= 0) { throw new IllegalArgumentException(); }
        this.getN(v, u).peso = peso;
        this.getN(u, v).peso = peso;
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
        if (vertice == null ||
            (vertice.getClass() != Vertice.class &&
             vertice.getClass() != Vecino.class)) {
            throw new IllegalArgumentException("Vértice inválido");
        }
        if (vertice.getClass() == Vertice.class) {
            Vertice v = (Vertice) vertice;
            v.color = color;
        }
        if (vertice.getClass() == Vecino.class) {
            Vecino v = (Vecino) vertice;
            v.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if(this.getElementos() == 0) { return true; }
        Iterator<T> it = this.vertices.iteradorLlaves(); T k = it.next();
        Vertice v = this.vertices.get(k);
        Cola<Vertice> queue = new Cola<Vertice>();
        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));
        queue.mete(v); v.color = Color.NEGRO;

        while(!queue.esVacia()) {
            Vertice aux = queue.saca();
            for (Vecino n: aux.vecinos) {
                if(n.vecino.color == Color.NINGUNO) {
                    queue.mete(n.vecino);
                    n.vecino.color = Color.NEGRO;
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
            for (Vecino n: aux.vecinos) {
                if(n.vecino.color == Color.NINGUNO) {
                    queue.mete(n.vecino);
                    n.vecino.color = Color.NEGRO;
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
            for (Vecino n: aux.vecinos) {
                if(n.vecino.color == Color.NINGUNO) {
                    stack.mete(n.vecino);
                    n.vecino.color = Color.NEGRO;
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
            s += String.format(v.elemento.toString() + ", ");
        }
        s += "}, {";

        this.paraCadaVertice((u) -> this.setColor(u, Color.NINGUNO));

        for(Vertice v: this.vertices) {
            v.color = Color.NEGRO;
            for(Vecino u: v.vecinos) {
                if(u.vecino.color != Color.NEGRO) {
                    s += String.format("(" + v.get().toString() + ", " + u.get().toString() + "), ");
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
            Vertice u = g.vertices.get(v.get());
            if(u == null) { return false; }
            for(Vecino n: v.vecinos) {
                if(!u.vecinos.contiene(n.get())) { return false; }
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        Vertice s = this.getV(origen), t = this.getV(destino);
        if (s == null || t == null) { throw new NoSuchElementException(); }
        Lista<VerticeGrafica<T>> l = new Lista<VerticeGrafica<T>>();

        if(origen.equals(destino)) { l.agrega(s); return l; }

        for(Vertice v: this.vertices) { v.distancia = -1; }
        s.distancia = 0;

        Cola<Vertice> q = new Cola<Vertice>(); q.mete(s);

        while(!q.esVacia()) {
            Vertice u = q.saca();
            for(Vecino v: u.vecinos) {
                if(v.vecino.distancia == -1) {
                    v.vecino.distancia = u.distancia + 1;
                    q.mete(v.vecino);
                }
            }
        }

        if(t.distancia == -1) { return l; }

        Vertice u = t;
        l.agrega(t);

        while(!u.elemento.equals(s.elemento)) {
            for(Vecino v: u.vecinos) {
                if(u.distancia == v.vecino.distancia + 1) {
                    l.agrega(v.vecino);
                    u = v.vecino;
                }
            }
        }

        return l.reversa();
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        Vertice s = this.getV(origen), t = this.getV(destino);
        if (s == null || t == null) { throw new NoSuchElementException(); }
        Lista<VerticeGrafica<T>> l = new Lista<VerticeGrafica<T>>();

        if(origen.equals(destino)) { l.agrega(s); return l; }

        for(Vertice v: this.vertices) { v.distancia = -1; }
        s.distancia = 0;

        MonticuloMinimo<Vertice> heap = new MonticuloMinimo<Vertice>(this.vertices, this.vertices.getElementos());

        while(!heap.esVacia()) {
            Vertice u = heap.elimina();
            for(Vecino v: u.vecinos) {
                if(this.customCompare(v.vecino.distancia, this.customAdd(u.distancia, v.peso)) > 0) {
                    v.vecino.distancia = this.customAdd(u.distancia, v.peso);
                    heap.reordena(v.vecino);
                }
            }

        }

        if(t.distancia == -1) { return l; }

        Vertice u = t;
        l.agrega(t);

        while(!u.elemento.equals(s.elemento)) {
            for(Vecino v: u.vecinos) {
                if(u.distancia == this.customAdd(v.vecino.distancia, 1)) {
                    l.agrega(v.vecino);
                    u = v.vecino;
                }
            }
        }

        return l.reversa();
    }
}
