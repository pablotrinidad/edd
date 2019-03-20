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
            return !this.esNegro();
        }

        private boolean esNegro() {
            return this.elemento == null || this.color == Color.NEGRO;
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
     * Elimina un elemento del árbol. El método eliminaå el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro v = (VerticeRojinegro) this.busca(elemento);
        if(v == null) { return; }
        this.elementos -= 1;

        if(v.hayIzquierdo() && v.hayDerecho()) {
            v = (VerticeRojinegro) intercambiaEliminable(v);
        }

        VerticeRojinegro h;
        boolean hIsSentinel = false;
        if(!v.hayIzquierdo() && !v.hayDerecho()) {
            h = createSentinel(v, "left");
            hIsSentinel = true;
        } else {
            h = v.hayIzquierdo() ? (VerticeRojinegro) v.izquierdo : (VerticeRojinegro) v.derecho;
        }

        eliminaVertice(v);

        if(h.esRojo() && v.esNegro()) { 
            h.color = Color.NEGRO;
            return;
        }
        
        if(v.esNegro() && h.esNegro()) {
            rebalanceTree(h);
        }

        if(hIsSentinel) {
            eliminaVertice(h);
        }
    }

    /**
     * Rebalance árbol después de haber eliminado un elemento.
     * @param v un vértice NEGRO distringo de null.
     */
    private void rebalanceTree(VerticeRojinegro v) {
        if(v == null) { throw new IllegalArgumentException(); }

        // Caso 1: v no tiene padre
        if (!v.hayPadre()) { return; }

        VerticeRojinegro p = (VerticeRojinegro) v.padre;
        VerticeRojinegro h = esHijoIzquierdo(v) ? (VerticeRojinegro) p.derecho : (VerticeRojinegro) p.izquierdo;

        // Caso 2
        if(h.esRojo() && p.esNegro()) {
            p.color = Color.ROJO;
            h.color = Color.NEGRO;
            
            // Giramos sobre p en dirección de v
            if(esHijoIzquierdo(v)) {
                giraIzquierdaPriv(p);
            } else {
                giraDerechaPriv(p);
            }

            // Actualizamos h para que vuelva a ser hermano de v
            h = esHijoIzquierdo(v) ? (VerticeRojinegro) p.derecho : (VerticeRojinegro) p.izquierdo;
        }

        // Hijos del hermano
        VerticeRojinegro hi = (VerticeRojinegro) h.izquierdo;
        VerticeRojinegro hd = (VerticeRojinegro) h.derecho;

        boolean hiEsNegro = hi != null ? hi.esNegro() : true;
        boolean hdEsNegro = hd != null ? hd.esNegro() : true;

        // Caso 3
        if(p.esNegro() && h.esNegro() && hiEsNegro && hdEsNegro) {
            h.color = Color.ROJO;
            rebalanceTree(p);
            return;
        }

        // Caso 4
        if(h.esNegro() && hiEsNegro && hdEsNegro && p.esRojo()) {
            h.color = Color.ROJO;
            p.color = Color.NEGRO;
            return;
        }

        // Caso 5
        if((esHijoIzquierdo(v) && !hiEsNegro && hdEsNegro) || (!esHijoIzquierdo(v) && hiEsNegro && !hdEsNegro)) {
            h.color = Color.ROJO;
            // Coloreamos al hijo rojo de h de negro
            if(!hiEsNegro && hi != null) { hi.color = Color.NEGRO; }
            if(!hdEsNegro && hd != null) { hd.color = Color.NEGRO; }
            
            // Giramos sobre h en la dirección contraria a v
            if(esHijoIzquierdo(v)) {
                giraDerechaPriv(h);
            } else {
                giraIzquierdaPriv(h);
            }

            // Actualizamos h para que vuelva a ser hermano de v
            h = esHijoIzquierdo(v) ? (VerticeRojinegro) p.derecho : (VerticeRojinegro) p.izquierdo;
            hi = (VerticeRojinegro) h.izquierdo;
            hd = (VerticeRojinegro) h.derecho;
            hiEsNegro = hi != null ? hi.esNegro() : true;
            hdEsNegro = hd != null ? hd.esNegro() : true;
        }

        // Caso 6
        if((esHijoIzquierdo(v)) && !hdEsNegro || (!esHijoIzquierdo(v) && !hiEsNegro)) {
            h.color = p.color;
            p.color = Color.NEGRO;
            if(esHijoIzquierdo(v)) {
                if(hd != null) { hd.color = Color.NEGRO; }
                giraIzquierdaPriv(p);
            } else {
                if(hi != null) { hi.color = Color.NEGRO; }
                giraDerechaPriv(p);
            }
        }
    }

    /**
     * Crea vértice fantasma
     * @param v vértice padre
     * @param direccion valor "left" o "right";
     */
    private VerticeRojinegro createSentinel(Vertice v, String d) {
        VerticeRojinegro u = (VerticeRojinegro) this.nuevoVertice(null);
        u.color = Color.NEGRO;
        if (v != null) {
            u.padre = v;
            switch (d) {
                case "left":
                    v.izquierdo = u;
                    break;
                case "right":
                    v.derecho = u;
                    break;
            }
        }
        return u;
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
