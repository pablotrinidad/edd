package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
            cola = new Cola<ArbolBinario<T>.Vertice>();
            if(raiz != null) {
                cola.mete(raiz);
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            if(cola.mira().derecho != null) {
                cola.mete(cola.mira().izquierdo);
                cola.mete(cola.mira().derecho);
            } else if(cola.mira().izquierdo != null) {
                cola.mete(cola.mira().izquierdo);
            }
            return cola.saca().elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null) { throw new IllegalArgumentException(); }
        Vertice v = new Vertice(elemento);
        if(this.raiz == null) {
            this.raiz = v;
            this.elementos = 1;
            return;
        }

        this.elementos += 1;
        // Apply BFS to find the right insertion spot
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(this.raiz);
        while(!cola.esVacia()) {
            Vertice vc = cola.saca();
            if(!vc.hayIzquierdo()) {
                vc.izquierdo = v;
                v.padre = vc.izquierdo;
                break;
            } else { cola.mete(vc.izquierdo); }

            if(!vc.hayDerecho()) {
                vc.derecho = v;
                v.padre = vc.derecho;
                break;
            } else { cola.mete(vc.derecho); }
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v = (ArbolBinario<T>.Vertice) this.busca(elemento);
        if (v == null) { return; }
        this.elementos -= 1;
        if(this.elementos == 0) {
            this.raiz = null;
            return;
        }

        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(this.raiz);
        Vertice vf = this.raiz;
        while(!cola.esVacia()) {
            Vertice vi = cola.saca();
            if(!v.hayIzquierdo() && !v.hayDerecho() && cola.esVacia()) {
                vf = vi;
            } else if(vi.hayIzquierdo()) {
                cola.mete(vi.izquierdo);
            } else if(vi.hayDerecho()) {
                cola.mete(vi.derecho);
            }
        }

        // Intercambia vértices
        Vertice aux = new Vertice(elemento);
        v.elemento = vf.elemento;
        vf.elemento = aux.elemento;
        if(esHijoIzquierdo(vf)) {
            vf.padre.izquierdo = null;
        } else {
            vf.padre.derecho = null;
        }
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        return esVacia() ? -1 : (int) (Math.log(this.elementos) / Math.log(2));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        Cola<VerticeArbolBinario<T>> cola = new Cola<VerticeArbolBinario<T>>();
        if(raiz != null) { cola.mete((VerticeArbolBinario<T>) this.raiz); }
        while(!cola.esVacia()) {
            VerticeArbolBinario<T> v = cola.saca();
            accion.actua(v);
            if(v.hayIzquierdo()) { cola.mete(v.izquierdo()); }
            if(v.hayDerecho()) { cola.mete(v.derecho()); }
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
