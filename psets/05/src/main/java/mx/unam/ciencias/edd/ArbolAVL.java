package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
            this.altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return this.altura;
        }

        private int balance() {
            return getBalance(this);
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            return this.elemento + " " + this.altura + "/" + this.balance();
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL v = (VerticeAVL)objeto;
            return this.altura == v.altura && super.equals(v);
        }
    }

    /* Convierte el vértice a VerticeAVL */
    private VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeAVL v = (VerticeAVL) this.getUltimoVerticeAgregado();
        this.balanceo(v);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL v = verticeAVL(super.busca(elemento));
        if(v == null) { return; }

        this.elementos -= 1;

        // Si tiene a lo más 1 hijo
        if((v.hayIzquierdo() ^ v.hayDerecho() || (!v.hayIzquierdo() && !v.hayDerecho()))) {
            this.eliminaVertice(v);
            this.balanceo((VerticeAVL) v.padre);
        }
        // Si ambos hijos existen
        else {
            Vertice u = intercambiaEliminable(v);
            this.eliminaVertice(u);
            this.balanceo((VerticeAVL) u.padre);
        }

    }

    private int getBalance(VerticeAVL v) {
        return this.getAltura((VerticeAVL) v.izquierdo) - this.getAltura((VerticeAVL) v.derecho);
    }

    private int getAltura(VerticeAVL v) {
        if (v == null) { return -1; }
        v.altura = Math.max(
            this.getAltura((VerticeAVL) v.izquierdo),
            this.getAltura((VerticeAVL) v.derecho)
        ) + 1;
        return v.altura;
    }

    /**
     * Método auxiliar para realizar balanceo de vértices AVL
     */
    private void balanceo(VerticeAVL v) {
        if (v == null) { return; }
        getAltura(v);
        if(getBalance(v) == -2) {
            if(getBalance((VerticeAVL) v.derecho) == 1) {
                VerticeAVL d = (VerticeAVL) v.derecho;
                this.giraDerechaPriv(d);
                this.getAltura(d);
                this.getAltura((VerticeAVL) d.padre);
            }
            this.giraIzquierdaPriv(v);
            this.getAltura(v);
        } else if (getBalance(v) == 2) {
            if(getBalance((VerticeAVL) v.izquierdo) == -1)  {
                VerticeAVL i = (VerticeAVL) v.izquierdo;
                this.giraIzquierdaPriv(i);
                this.getAltura(i);
                this.getAltura((VerticeAVL) i.padre);
            }
            this.giraDerechaPriv(v);
            this.getAltura(v);
        }
        this.balanceo((VerticeAVL) v.padre);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
