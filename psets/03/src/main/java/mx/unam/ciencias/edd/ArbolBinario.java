package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
            this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <tt>true</tt> si el vértice tiene padre,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayPadre() {
            return this.padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <tt>true</tt> si el vértice tiene izquierdo,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return this.izquierdo != null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <tt>true</tt> si el vértice tiene derecho,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return this.derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            if(this.padre == null) {
                throw new NoSuchElementException();
            }
            return this.padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            if(this.izquierdo == null) {
                throw new NoSuchElementException();
            }
            return this.izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            if(this.derecho == null) {
                throw new NoSuchElementException();
            }
            return this.derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            if(hayIzquierdo() && hayDerecho()) {
                return 1 + Math.max(this.izquierdo.altura(), this.derecho.altura());
            } else if(hayIzquierdo()) {
                return 1 + this.izquierdo.altura();
            } else if(hayDerecho()) {
                return 1 + this.derecho.altura();
            } else {
                return 0;
            }
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            return 1;
            // return hayPadre() ? 1 + this.padre.profundidad() : 0;
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return this.elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            return equals(this, vertice);
        }

        /**
         * Método auxiliar para comparar dos vértices
         * @param v1 vértice 1
         * @param v2 vértice 2
         * @return <code>boolean</code> indicando si los vértices son iguales
         */
        private boolean equals(Vertice v1, Vertice v2) {
            if(v1 == null && v2 == null) { return true; }
            if((v1 == null && v2 != null) || (v1 != null && v2 == null)) { return false; }
            if(!v1.elemento.equals(v2.elemento)) { return false; }
            return equals(v1.izquierdo, v2.izquierdo) && equals(v1.derecho, v2.derecho);
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            return this.elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        for(T e:coleccion) {
            this.agrega(e);
        }
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        int r = esVacia() ? -1 : raiz.altura();
        return r;
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return this.elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return this.contiene(this.raiz, elemento);
    }

    /**
     * Indica si el elemento recibido es el elemento del vértice.
     * @param vertice que se desea usar para la comparación .
     * @param elemento buscado.
     * @return <code>true</code> si es encontrado, <code>false</code> si no.
     */
    private boolean contiene(Vertice vertice, T elemento) {
        if(vertice == null) {
            return false;
        }
        if(vertice.get().equals(elemento)){
            return true;
        }
        return this.contiene(vertice.izquierdo, elemento) || this.contiene(vertice.derecho, elemento);
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <tt>null</tt>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <tt>null</tt> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        /* Busca recursivamente. */
        return this.busca(raiz, elemento);
    }

    /**
     * Dado un vértice y un elemento, verifica si son el mismo, si no, sigue la recursión
     * a sus hijos.
     * @param vertice que se desea usar para la comparación .
     * @param elemento buscado.
     * @return el vértice si es encontrado, null si no.
     */
    private VerticeArbolBinario<T> busca(Vertice vertice, T elemento) {
        if (vertice == null) {
            return null;
        }
        if (vertice.elemento.equals(elemento)) {
            return vertice;
        }
        VerticeArbolBinario<T> iz = this.busca(vertice.izquierdo, elemento);
        return (iz != null) ? iz : this.busca(vertice.derecho, elemento);
}

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        if (this.raiz == null) { throw new NoSuchElementException(); }
        return this.raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return this.raiz == null;
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        this.raiz = null;
        this.elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        if(this.esVacia() || arbol.esVacia()) {
            return false;
        }
        return this.raiz.equals(arbol.raiz);
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        if (this.raiz == null) { return ""; }
        int a = altura() + 1;
        boolean[] opt = new boolean[a];
        // false para dibujar epacio y true para dibujar barra vertical
        for(int i = 0; i < a; i++)
            opt[i] = false;
        return toString(this.raiz, 0, opt);
    }

    /**
     * Algoritmo recursivo que pinta el árbol
     * @param v vertice
     * @param l longitud
     * @param opt opciones booleanas
     * @return representación en string del árbol.
     */
    private String toString(Vertice v, int l, boolean[] opt) {
        String s = v + "\n";
        opt[l] = true;
        if(v.hayIzquierdo() && v.hayDerecho()) {
            s += dibujaEspacios(l, opt);
            s += "├─›";
            s += toString(v.izquierdo, l+1, opt);
            s += dibujaEspacios(l, opt);
            s += "└─»";
            opt[l] = false;
            s += toString(v.derecho, l+1, opt);
        } else if(v.hayIzquierdo()) {
            s += dibujaEspacios(l, opt);
            s += "└─›";
            opt[l] = false;
            s += toString(v.izquierdo, l+1, opt);
        } else if(v.hayDerecho()) {
            s += dibujaEspacios(l, opt);
            s += "└─»";
            opt[l] = false;
            s += toString(v.derecho, l+1, opt);
        }
        return s;
    }

    /**
     * Algoritmo auxiliar para dibujar epacios
     */
    private String dibujaEspacios(int l, boolean[] opt) {
        String s = "";
        for(int i = 0; i < l; i++) {
            s += opt[i] ? "│  " : "   ";
        }
        return s;
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }

    /**
     * Auxiliar que nos indica si el vértice recibido es el hijo
     * izquierdo de su padre o no
     * @param vertice a evaluar
     * @return boolean indicando si es hijo izquierdo
     * @throws IllegalArgumentException si el vértice no tiene padre.
     */
    protected boolean esHijoIzquierdo(Vertice vertice) {
        if(vertice.padre == null) { throw new IllegalArgumentException(); }
        if(vertice.padre.hayIzquierdo()) {
            return vertice.padre.izquierdo.equals(vertice);
        }
        return false;
    }
}
