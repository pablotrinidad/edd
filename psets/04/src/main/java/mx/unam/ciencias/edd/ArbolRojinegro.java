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
        VerticeRojinegro v = (VerticeRojinegro) this.busca(elemento);
        if(v == null) { return; }
        this.elementos -= 1;

        System.out.println("\tv = " + v);
        System.out.println(this);
        boolean disconectedAlready = false;
        // Si ambos hijos son distintos de null
        if(v.hayDerecho() && v.hayIzquierdo()) {
            VerticeRojinegro aux = (VerticeRojinegro) maxInSubtree(v.izquierdo);
            v.elemento = aux.elemento;
            v = aux;
        }
        VerticeRojinegro h;
        boolean esFantasma = false;
        // Si el nuevo vértice eliminable no tiene hijos
        if(!v.hayDerecho() && !v.hayIzquierdo()) {
            h = (VerticeRojinegro) nuevoVertice(null);
            v.izquierdo = h;
            h.padre = v;
            esFantasma = true;
            System.out.println("Creando fantasma");
        } else {
            h = v.hayIzquierdo() ? (VerticeRojinegro) v.izquierdo : (VerticeRojinegro) v.derecho; 
        }

        System.out.println("Después de obtener v con h único");
        System.out.println("\tv = " + v);
        System.out.println("\th = " + h);
        System.out.println(this);
        eliminaVertice(v);
        System.out.println("Después de desconectar v y dejar a h");
        System.out.println("\tv = " + v);
        System.out.println("\th = " + h);
        System.out.println(this);

        if(h.color != v.color) {
            h.color = Color.NEGRO;
            System.out.println("Aplicando rebalanceo sobre h = " + h);
            rebalanceTree(h);
        } else {
            h.color = Color.NEGRO;
        }

        // Si h es fantasma, lo eliminamos
        if(esFantasma) {
            eliminaVertice(h);
        }
        System.out.println("Resuldato final");
        System.out.println(this);
        System.out.println("########################");
    }

    /**
     * Dado un vértice negro, aplica el algoritmo de rebalanceo
     * @param v vértice utilizado en el rebalanceo
     */
    private void rebalanceTree(VerticeRojinegro v) {
        // Caso 1
        if(!v.hayPadre()) { 
            this.raiz = v;
            return;
        }

        VerticeRojinegro p = (VerticeRojinegro) v.padre;

        // Hermano de v
        VerticeRojinegro h;
        h = esHijoIzquierdo(v) ? (VerticeRojinegro) p.derecho : (VerticeRojinegro) p.izquierdo;
        System.out.println("v = " + v);
        System.out.println("p = " + p);
        System.out.println("h = " + h);

        // Caso 2
        if (h.esRojo()) {
            p.color = Color.ROJO;
            h.color = Color.NEGRO;
            if(esHijoIzquierdo(v)) {
                giraIzquierdaPriv(p);
            } else {
                giraDerechaPriv(p);
            }

            // Actualizamos h para que vuelva a ser hermano de v
            if(esHijoIzquierdo(v)) {
                v.padre.derecho = h;
            } else {
                v.padre.izquierdo = h;
            }
            h.padre = v.padre;
        }

        VerticeRojinegro hi = (VerticeRojinegro) v.izquierdo;
        VerticeRojinegro hd = (VerticeRojinegro) v.derecho;

        // Caso 3
        if(p.esNegro() && h.esNegro()) {
            boolean case3 = true;
            if(hi != null) {
                case3 = case3 && hi.esNegro();
            }
            if(hd != null) {
                case3 = case3 && hd.esNegro();
            }
            if(case3) {
                h.color = Color.ROJO;
                rebalanceTree(p);
                return;
            }
        }

        // Caso 4
        if(h.esNegro() && p.esRojo()) {
            boolean case4 = true;
            if(hi != null) {
                case4 = case4 && hi.esNegro();
            }
            if(hd != null) {
                case4 = case4 && hd.esNegro();
            }
            if(case4) {
                h.color = Color.ROJO;
                p.color = Color.NEGRO;
                return;
            }
        }

        // Caso 5
        boolean case5a = true;
        if(hi != null) { // Si hi es rojo
            case5a = case5a && hi.esRojo();
        } else { case5a = false; }
        if(hd != null) { // Si hd es negro
            case5a = case5a && hd.esNegro();
        }
        boolean case5b = true;
        if(hd != null) { // Si hd es rojo
            case5b = case5b && hd.esRojo();
        } else { case5b = false; }
        if(hi != null) { // Si hi es negro
            case5b = case5b && hi.esNegro();
        }
        if((esHijoIzquierdo(v) && case5a) || (!esHijoIzquierdo(v) && case5b)) {
            h.color = Color.ROJO;
            hi.color = Color.NEGRO;
            hd.color = Color.NEGRO;
            if(esHijoIzquierdo(v)) {
                giraDerechaPriv(h);
            } else {
                giraIzquierdaPriv(h);
            }
            // Actualizamos h para que vuelva a ser hermano de v
            if(esHijoIzquierdo(v)) {
                v.padre.derecho = h;
            } else {
                v.padre.izquierdo = h;
            }
            h.padre = v.padre;
        }

        // Caso 6
        boolean case6a = hd != null ? hd.esRojo() : false;
        boolean case6b = hi != null ? hi.esRojo() : false;
        if((esHijoIzquierdo(v)) && case6a || (!esHijoIzquierdo(v) && case6b)) {
            h.color = p.color;
            p.color = Color.NEGRO;
            if(esHijoIzquierdo(v)) {
                giraIzquierdaPriv(p);
            } else {
                giraDerechaPriv(p);
            }
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
