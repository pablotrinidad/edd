package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            String rep = this.esRojo() ? "R{" : "N{";
            rep += this.elemento != null ? this.elemento.toString() + "}" : "null}";
            return rep;
        }

        private boolean esRojo() {
            return this.color == Color.ROJO;
        }

        private boolean esNegro() {
            return !this.esRojo();
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return this.color == vertice.color && super.equals(objeto);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (ArbolRojinegro<T>.VerticeRojinegro) vertice;
        return v.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro v = (VerticeRojinegro) this.getUltimoVerticeAgregado();
        v.color = Color.ROJO;
        this.balanceTree(v);
    }

    /**
     * Dado un vértice rojo, aplica el algoritmo de balanceo
     * @param v vértice utilizado en el balanceo
     */
    private void balanceTree(VerticeRojinegro v) {
        // Caso 1
        if(!v.hayPadre()) { v.color = Color.NEGRO; return; }

        VerticeRojinegro p = (VerticeRojinegro) v.padre;
        VerticeRojinegro a;
        VerticeRojinegro t;

        // Caso 2
        if(p.esNegro()) { return; }

        a = (VerticeRojinegro) p.padre;
        t = esHijoIzquierdo(p) ? (VerticeRojinegro) a.derecho : (VerticeRojinegro) a.izquierdo;

        // Caso 3
        if(t != null) {
            if(t.esRojo()) {
                t.color = Color.NEGRO;
                p.color = Color.NEGRO;
                a.color = Color.ROJO;
                balanceTree(a);
                return;
            }
        }

        // Caso 4
        if(esHijoIzquierdo(p) ^ esHijoIzquierdo(v)) {
            if(esHijoIzquierdo(p)) {
                giraIzquierdaPriv(p);
                p = (VerticeRojinegro) a.izquierdo;
                v = (VerticeRojinegro) a.izquierdo;
            } else {
                giraDerechaPriv(p);
                p = (VerticeRojinegro) a.derecho;
                v = (VerticeRojinegro) a.derecho;
            }
        }

        // Caso 5
        p.color = Color.NEGRO;
        a.color = Color.ROJO;
        if(esHijoIzquierdo(v)) {
            giraDerechaPriv(a);
        } else {
            giraIzquierdaPriv(a);
        }
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro z = (VerticeRojinegro) this.busca(elemento);
        if(z == null) { return; }
        this.elementos -= 1;

        System.out.println("$$$$$$$$$$$$$\t" + z + "\t$$$$$$$$$$$");
        System.out.println(this);

        VerticeRojinegro y = z;
        VerticeRojinegro x;
        Color yInitialColor = y.color;

        if(!z.hayIzquierdo()) {
            x = (VerticeRojinegro) z.derecho;
            transplant(z, z.derecho);
        } else if (!z.hayDerecho()) {
            x = (VerticeRojinegro) z.izquierdo;
            transplant(z, z.izquierdo);
        } else {
            y = (VerticeRojinegro) minimumInSubtree((VerticeRojinegro) z.derecho);
            yInitialColor = y.color;
            x = (VerticeRojinegro) y.derecho;
            if (y.padre.equals(z)) {
                x.padre = y;
            } else {
                transplant(y, y.derecho);
                y.derecho = z.derecho;
                y.derecho.padre = y;
            }
            transplant(z, y);
            y.izquierdo = z.izquierdo;
            y.izquierdo.padre = y;
            y.color = z.color;
        }
        if(yInitialColor == Color.NEGRO) {
            rebalanceTree(x);
        }
        System.out.println("AFTER MATH:");
        System.out.println(this);
    }

    /**
     * Algoritmo para rebalancear el árbol después de una eliminación
     * @param x vértice raíz del subárbol desbalanceado
     */
    private void rebalanceTree(VerticeRojinegro x) {
        if (x == null) { return; }
        while(!x.equals(this.raiz) && x.color == Color.NEGRO) {
            VerticeRojinegro w, p, hi, hd;
            if(esHijoIzquierdo(x)) {
                p = (VerticeRojinegro) x.padre;
                w = (VerticeRojinegro) p.derecho;
                if(w.color == Color.ROJO) {
                    w.color = Color.NEGRO;
                    p.color = Color.ROJO;
                    giraIzquierdaPriv(p);
                    w = (VerticeRojinegro) p.derecho;
                }
                hi = (VerticeRojinegro) w.izquierdo;
                hd = (VerticeRojinegro) w.derecho;
                if(hi.color == Color.NEGRO && hd.color == Color.NEGRO) {
                    w.color = Color.ROJO;
                    x = (VerticeRojinegro) x.padre;
                    p = (VerticeRojinegro) x.padre;
                }
                else if (hd.color == Color.NEGRO) {
                    hi.color = Color.NEGRO;
                    w.color = Color.ROJO;
                    giraDerechaPriv(w);
                    w = (VerticeRojinegro) p.derecho;
                    hd = (VerticeRojinegro) w.derecho;
                    w.color = p.color;
                    p.color = Color.NEGRO;
                    hd.color = Color.NEGRO;
                    giraIzquierdaPriv(p);
                    x = (VerticeRojinegro) this.raiz;
                }
            } else {
                p = (VerticeRojinegro) x.padre;
                w = (VerticeRojinegro) p.izquierdo;
                if(w.color == Color.ROJO) {
                    w.color = Color.NEGRO;
                    p.color = Color.ROJO;
                    giraIzquierdaPriv(p);
                    w = (VerticeRojinegro) p.izquierdo;
                }
                hi = (VerticeRojinegro) w.izquierdo;
                hd = (VerticeRojinegro) w.derecho;
                if(hd.color == Color.NEGRO && hi.color == Color.NEGRO) {
                    w.color = Color.ROJO;
                    x = (VerticeRojinegro) x.padre;
                    p = (VerticeRojinegro) x.padre;
                }
                else if (hi.color == Color.NEGRO) {
                    hd.color = Color.NEGRO;
                    w.color = Color.ROJO;
                    giraDerechaPriv(w);
                    w = (VerticeRojinegro) p.izquierdo;
                    hi = (VerticeRojinegro) w.izquierdo;
                    w.color = p.color;
                    p.color = Color.NEGRO;
                    hi.color = Color.NEGRO;
                    giraIzquierdaPriv(p);
                    x = (VerticeRojinegro) this.raiz;
                }
            }
        }
        x.color = Color.NEGRO;
    }

    /**
     * Regresa el elemento mínimo del subárbol con raíz en x
     * @param x Vértice raíz del subárbol
     */
    private VerticeArbolBinario<T> minimumInSubtree(Vertice x) {
        while(x.hayIzquierdo()) {
            x = x.izquierdo;
        }
        return x;
    }

    /**
     * Algoritmo auxiliar para mover subárboles
     * @param u subárbol en vértice u
     * @param v subárbol en vértice v
     */
    private void transplant(Vertice u, Vertice v){
        if(!u.hayPadre()) {
            this.raiz = v;
        } else if (esHijoIzquierdo(u)) {
            u.padre.izquierdo = v;
        } else {
            u.padre.derecho = v;
        }

        if(v != null) {
            v.padre = u.padre;
        }
    }

    /**
     * Algoritmo para eliminar vértices de un árbol
     * @param z vértice por eliminar (asume que existe)
     */
    private void deleteNode(Vertice z) {
        if(!z.hayIzquierdo()) {
            transplant(z, z.derecho);
        } else if (!z.hayDerecho()) {
            transplant(z, z.izquierdo);
        } else {
            Vertice y = (Vertice) minimumInSubtree(z.derecho);
            if(!y.padre.equals(z)) {
                transplant(y, y.derecho);
                y.derecho = z.derecho;
                y.derecho.padre = y;
            }
            transplant(z, y);
            y.izquierdo = z.izquierdo;
            y.izquierdo.padre = y;
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
