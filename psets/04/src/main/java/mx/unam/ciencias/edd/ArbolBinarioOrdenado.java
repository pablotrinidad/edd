package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
            pila = new Pila<ArbolBinario<T>.Vertice>();
            if (esVacia()) { return; }
            Vertice v = raiz;
            while(v != null) {
                pila.mete(v);
                v = v.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice v = pila.saca();
            Vertice vi;
            if(v.hayDerecho()) {
                vi = v.derecho;
                while(vi != null) {
                    pila.mete(vi);
                    vi = vi.izquierdo;
                }
            }
            return v.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) { throw new IllegalArgumentException(); }
        Vertice v = this.nuevoVertice(elemento);
        this.elementos += 1;
        this.ultimoAgregado = v;

        if(this.esVacia()) {
            this.raiz = v;
        } else {
            this.agrega(this.raiz, v);
        }
    }

    /**
     * Método auxiliar recursivo que recibe un vértice actual
     * distinto de <code>null</code> y el nuevo vértice. Agrega
     * el nuevo elemento en orden.
     * @param vc vértice usado en la comparación.
     * @param v vértice por agregar
     */
    private void agrega(Vertice vc, Vertice v) {
        if(v.elemento.compareTo(vc.elemento) <= 0) {
            if(!vc.hayIzquierdo()) {
                vc.izquierdo = v;
                v.padre = vc;
            } else {
                this.agrega(vc.izquierdo, v);
            }
        } else {
            if(!vc.hayDerecho()) {
                vc.derecho = v;
                v.padre = vc;
            } else {
                this.agrega(vc.derecho, v);
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v = (ArbolBinario<T>.Vertice) this.busca(elemento);
        if(v == null) { return; }
        this.elementos -= 1;

        // Si tiene a lo más 1 hijo
        if((v.hayIzquierdo() ^ v.hayDerecho() || (!v.hayIzquierdo() && !v.hayDerecho()))) {
            eliminaVertice(v);
        }
        // Si ambos hijos existen
        else {
            Vertice u = intercambiaEliminable(v);
            eliminaVertice(u);
        }

    }

    /**
     * Regresa vértice máximo en árbol.
     * @param vertice inicial del árbol
     * @return vertice máximo.
     */
    protected Vertice maxInSubtree(Vertice vertice) {
        while(vertice.hayDerecho()) {
            vertice = vertice.derecho;
        }
        return vertice;
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice u = maxInSubtree(vertice.izquierdo);
        Vertice aux = this.nuevoVertice(vertice.elemento);
        vertice.elemento = u.elemento;
        u.elemento = aux.elemento;
        return u;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        if(vertice.hayIzquierdo() && vertice.hayDerecho()) {
            throw new IllegalArgumentException();
        }

        Vertice p = vertice.padre;
        Vertice u = vertice.hayIzquierdo() ? vertice.izquierdo : vertice.derecho;

        if(u != null) { u.padre = p; }
        if(p == null) {
            this.raiz = u;
            return;
        }
        if(esHijoIzquierdo(vertice)) {
            p.izquierdo = u;
        } else {
            p.derecho = u;
        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(this.raiz, elemento);
    }

    /**
     * Método auxiliar que realiza la búsqueda de manera recursiva
     * @param vertice usado en la comparación
     * @param elemento buscado
     * @return vertice si existe, null si no.
     */
    private VerticeArbolBinario<T> busca(Vertice vertice, T elemento) {
        if(vertice == null) { return null; }
        if(vertice.elemento.equals(elemento)) { return vertice; }

        if(elemento.compareTo(vertice.elemento) <= 0) {
            return busca(vertice.izquierdo, elemento);
        } else {
            return busca(vertice.derecho, elemento);
        }
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        giraDerechaPriv(vertice);
    }

    protected void giraDerechaPriv(VerticeArbolBinario<T> vertice) {
        Vertice q = (ArbolBinario<T>.Vertice) vertice;
        if (!q.hayIzquierdo()) { throw new IllegalArgumentException(); }
        Vertice p = q.izquierdo;

        if(p.hayDerecho()) {
            q.izquierdo = p.derecho;
            p.derecho.padre = q;
        } else {
            q.izquierdo = null;
        }
        p.derecho = q;

        if(q.hayPadre()) {
            if(esHijoIzquierdo(q)) {
                q.padre.izquierdo = p;
            } else {
                q.padre.derecho = p;
            }
            p.padre = q.padre;
        } else {
            p.padre = null;
            this.raiz = p;
        }
        q.padre = p;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        giraIzquierdaPriv(vertice);
    }

    protected void giraIzquierdaPriv(VerticeArbolBinario<T> vertice) {
        Vertice p = (ArbolBinario<T>.Vertice) vertice;
        if (!p.hayDerecho()) { throw new IllegalArgumentException(); }
        Vertice q = p.derecho;

        if(q.hayIzquierdo()) {
            p.derecho = q.izquierdo;
            q.izquierdo.padre = p;
        } else {
            p.derecho = null;
        }
        q.izquierdo = p;

        if(p.hayPadre()) {
            if(esHijoIzquierdo(p)) {
                p.padre.izquierdo = q;
            } else {
                p.padre.derecho = q;
            }
            q.padre = p.padre;
        } else {
            q.padre = null;
            this.raiz = q;
        }
        p.padre = q;
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsWithOrder(accion, this.raiz, "pre");
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsWithOrder(accion, this.raiz, "in");
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsWithOrder(accion, this.raiz, "post");
    }

    private void dfsWithOrder(AccionVerticeArbolBinario<T> accion, Vertice v, String order) {
        if (v == null) { return; }
        switch (order) {
            case "pre":
                accion.actua(v);
                dfsWithOrder(accion, v.izquierdo, order);
                dfsWithOrder(accion, v.derecho, order);
                break;
            case "in":
                dfsWithOrder(accion, v.izquierdo, order);
                accion.actua(v);
                dfsWithOrder(accion, v.derecho, order);
                break;
            case "post":
                dfsWithOrder(accion, v.izquierdo, order);
                dfsWithOrder(accion, v.derecho, order);
                accion.actua(v);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
