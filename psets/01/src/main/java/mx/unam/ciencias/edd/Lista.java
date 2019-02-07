package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            T e = siguiente.elemento;
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return e;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(!hasPrevious()) {
                throw new NoSuchElementException();
            }
            T e = anterior.elemento;
            siguiente = anterior;
            anterior = anterior.anterior;
            return e;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            siguiente = cabeza;
            anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return getElementos();
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) { throw new IllegalArgumentException(); }

        Nodo nodo = new Nodo(elemento);
        longitud += 1;

        if (cabeza == null || rabo == null) {
            cabeza = nodo;
            rabo = nodo;
        } else {
            rabo.siguiente = nodo;
            nodo.anterior = rabo;
            rabo = nodo;
        }
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null) { throw new IllegalArgumentException(); }

        Nodo nodo = new Nodo(elemento);
        longitud += 1;

        if(cabeza == null || rabo == null) {
            cabeza = nodo;
            rabo = nodo;
        } else {
            cabeza.anterior = nodo;
            nodo.siguiente = cabeza;
            cabeza = nodo;
        }
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        else if (i <= 0) {
            agregaInicio(elemento);
        }
        else if (i > longitud -1 ) {
            agregaFinal(elemento);
        }
        else {
            longitud += 1;
            Nodo nodo = new Nodo(elemento);

            Nodo leftNode = getNodo(i - 1);
            Nodo rightNode = leftNode.siguiente;
            leftNode.siguiente = nodo;
            rightNode.anterior = nodo;

            nodo.siguiente = rightNode;
            nodo.anterior = leftNode;
        }
    }

    /**
     * Regresa primer nodo de la lista que  sea igual al elemento.
     * Si no lo encuentra, regresa null.
     * @param e el elemento usado en la búsqueda.
     * @return Nodo de la lista
     */
    private Nodo buscaNodo(T elemento) {
        int i = longitud;
        Nodo nodo = cabeza;
        while(i-- > 0) {
            if (nodo.elemento.equals(elemento)) {
                return nodo;
            }
            nodo = nodo.siguiente;
        }
        return null;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo n = buscaNodo(elemento);
        if (n != null) {
            longitud -= 1;
            if (cabeza == rabo) {
                cabeza = null;
                rabo = null;
            } else if (n == cabeza) {
                n.siguiente.anterior = null;
                cabeza = n.siguiente;
            } else if (n == rabo) {
                n.anterior.siguiente = null;
                rabo = n.anterior;
            } else {
                n.anterior.siguiente = n.siguiente;
                n.siguiente.anterior = n.anterior;
            }
        }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (cabeza == null && rabo == null) {
            throw new NoSuchElementException();
        }

        T e = cabeza.elemento;
        elimina(e);
        return e;

    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (cabeza == null && rabo == null) {
            throw new NoSuchElementException();
        }
        T e = rabo.elemento;
        longitud -= 1;
        if (cabeza == rabo) {
            cabeza = null;
            rabo = null;
        } else {
            rabo.anterior.siguiente = null;
            rabo = rabo.anterior;
        }
        return e;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        Nodo n = buscaNodo(elemento);
        return n != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> lista = new Lista<T>();
        IteradorLista<T> i = iteradorLista();
        while(i.hasNext())
            lista.agregaInicio(i.next());
        return lista;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> lista = new Lista<T>();
        IteradorLista<T> i = iteradorLista();
        while(i.hasNext())
            lista.agrega(i.next());
        return lista;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (cabeza == null && rabo == null) {
            throw new NoSuchElementException();
        }
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (cabeza == null && rabo == null) {
            throw new NoSuchElementException();
        }
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo nodo de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo nodo de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de nodos en la lista.
     */
    private Nodo getNodo(int i) {
        if (i < 0 || i >= longitud) {
            throw new ExcepcionIndiceInvalido();
        }

        Nodo nodo = cabeza;
        while(i-- > 0)
            nodo = nodo.siguiente;
        return nodo;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de nodos en la lista.
     */
    public T get(int i) {
        Nodo nodo = getNodo(i);
        return nodo.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        if (cabeza == null && rabo == null) { return -1; }
        int c = 0;
        Iterator<T> i = iteradorLista();
        while(i.hasNext()) {
            if(i.next().equals(elemento)) {
                return c;
            }
            c++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (longitud == 0) { return "[]"; }
        String rep = "[";
        Iterator<T> i = iterator();
        for(int c = 0; c < longitud - 1; c++)
            rep += i.next() + ", ";
        rep += i.next() + "]";
        return rep;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;

        if(lista.getLongitud() != this.getLongitud()) {
            return false;
        }

        Iterator ia = lista.iteradorLista();
        Iterator ib = this.iteradorLista();
        while(ia.hasNext() && ib.hasNext()) {
            if(!ia.next().equals(ib.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
}
