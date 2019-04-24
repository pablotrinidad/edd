package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice < elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if(!isValidIndex(indice)) { throw new NoSuchElementException(); }
            return arbol[indice++];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            this.indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return this.indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return this.compareTo(adaptador);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100); /* 100 es arbitrario. */
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        this.arbol = this.nuevoArreglo(n);
        int i = 0;
        this.elementos = n;
        for(T e: iterable) {
            this.arbol[i] = e;
            e.setIndice(i++);
        }
        for(i = (n - 1) / 2; i >= 0; i--) {
            this.heapifyDown(this.arbol[i]);
        }
    }

    /**
     * Return if given index is valid
     * @param i index
     */
    private boolean isValidIndex(int i) {
        return !(i < 0 || i >= this.elementos);
    }

    /**
     * Heapify-down algorithm
     * @param e element being heapified down
     */
    private void heapifyDown(T e) {
        if (e == null) { return; }
        int l = e.getIndice() * 2 + 1;
        int r = e.getIndice() * 2 + 2;

        // Invalid indices
        if(!this.isValidIndex(l) && !this.isValidIndex(r)) { return; }

        int min = r;
        if(this.isValidIndex(l)) { // Since it's a complete binary tree
            if(this.isValidIndex(r)) {
                if(this.arbol[l].compareTo(this.arbol[r]) < 0) { min = l; }
            } else { min = l; }
        }

        if(this.arbol[min].compareTo(e) < 0) {
            this.swapElements(e, arbol[min]);
            this.heapifyDown(e);
        }
    }

    /**
     * Heapify-up algorithm
     * @param e element being heapified up
     */
    private void heapifyUp(T e) {
        int parent = e.getIndice() - 1;
        parent = parent == -1 ? -1 : parent / 2;
        if(!this.isValidIndex(parent) || this.arbol[parent].compareTo(e) < 0) {
            return;
        }
        this.swapElements(arbol[parent], e);
        this.heapifyUp(e);
    }

    /**
     * Swap two given elements holding their indices
     * @param e1
     * @param e2
     */
    private void swapElements(T e1, T e2) {
        int aux = e1.getIndice();
        this.arbol[e1.getIndice()] = e2;
        this.arbol[e2.getIndice()] = e1;
        e2.setIndice(e1.getIndice());
        e1.setIndice(aux);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        // Grow array
        if(this.elementos == this.arbol.length) {
            T[] newArray = this.nuevoArreglo(this.elementos * 2);
            for(int i = 0; i < this.elementos; i++) 
                newArray[i] = this.arbol[i];
            this.arbol = newArray;
        }
        this.arbol[this.elementos] = elemento;
        this.elementos += 1;
        this.heapifyUp(elemento);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(this.esVacia()) { throw new IllegalStateException(); }
        T e = this.arbol[0];

        this.swapElements(this.arbol[0], this.arbol[this.elementos - 1]);
        this.heapifyDown(this.arbol[0]);

        return e;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int i = elemento.getIndice();
        if(!this.isValidIndex(i)) { return; }
        this.swapElements(this.arbol[i], this.arbol[this.elementos - 1]);
        this.elementos -= 1;
        this.reordena(this.arbol[i]);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        int i = elemento.getIndice();
        if(!this.isValidIndex(i)) { return false; }
        return this.arbol[i].compareTo(elemento) == 0;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacia() {
        return this.elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        for(int i = 0; i < this.elementos; i++)
            this.arbol[i] = null;
        this.elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if(elemento == null) { return; }
        int parent = elemento.getIndice() - 1;
        parent = parent == -1 ? -1 : parent / 2;
        if(!this.isValidIndex(parent) || this.arbol[parent].compareTo(elemento) <= 0) {
            this.heapifyDown(elemento);
        } else {
            this.heapifyUp(elemento);
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return this.elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(!this.isValidIndex(i)) { throw new NoSuchElementException(); }
        return this.arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String rep = "";
        for(T e: this)
            rep += e.toString() + ", ";
        return rep;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if(this.getElementos() != monticulo.getElementos()) { return false;}
        for(int i = 0; i < this.getElementos(); i++) {
            if(!this.get(i).equals(monticulo.get(i))) { return false; }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>> Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> l1 = new Lista<Adaptador<T>>();
        for(T e: coleccion)
            l1.agrega(new Adaptador<T>(e));

        Lista<T> l2 = new Lista<T>();

        MonticuloMinimo<Adaptador<T>> heap = new MonticuloMinimo<Adaptador<T>>(l1);

        while(!heap.esVacia()) {
            Adaptador<T> min = heap.elimina();
            l2.agrega(min.elemento);
        }

        return l2;
    }
}
