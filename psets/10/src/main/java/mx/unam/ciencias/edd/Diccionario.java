package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.management.RuntimeErrorException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            for(int i = 0; i < entradas.length; i++) {
                if(entradas[i] != null && entradas[i].getLongitud() > 0) {
                    this.iterador = entradas[i].iterator();
                    this.indice = i;
                    break;
                }
            }
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            if(this.iterador == null) { return false; }
            return this.iterador.hasNext();
            // return this.iterador != null;
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
            if(this.iterador == null) { throw new NoSuchElementException(); }
            Entrada e = this.iterador.next();
            if(!this.iterador.hasNext()) {
                this.iterador = null;
                for(int i = this.indice + 1; i < entradas.length; i++) {
                    if(entradas[i] != null) {
                        this.iterador = entradas[i].iterator();
                        this.indice = i;
                        break;
                    }
                }
            }
            return e;
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            return this.siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            return this.siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        this.dispersor = dispersor;
        capacidad = capacidad < MINIMA_CAPACIDAD ? MINIMA_CAPACIDAD : capacidad;
        int c = 1;
        while (c < capacidad * 2) { c *= 2; }
        this.entradas = this.nuevoArreglo(c);
        this.elementos = 0;
    }

    // Return masked key
    private int hashHey(K llave) {
        int mask = this.entradas.length - 1;
        return this.dispersor.dispersa(llave) & mask;
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if(llave == null || valor == null) { throw new IllegalArgumentException(); }

        int i = this.hashHey(llave);
        Entrada e = new Entrada(llave, valor);
        if(this.entradas[i] == null) {
            this.entradas[i] = new Lista<Entrada>();
        } else {
            for(Entrada a: entradas[i])
            if(a.llave.equals(llave)) { a.valor = valor; return; }
        }
        
        entradas[i].agrega(e);
        this.elementos += 1;
        // Resize dictionary if max load is reached
        if(this.carga() >= MAXIMA_CARGA) {
            Lista<Entrada>[] old = this.entradas;
            this.entradas = this.nuevoArreglo((this.entradas.length) * 2);
            for(Lista<Entrada> l_: old) {
                if(l_ != null) {
                    for(Entrada a: l_) {
                        int k = this.hashHey(a.llave);
                        if(this.entradas[k] == null) { this.entradas[k] = new Lista<Entrada>(); }
                        this.entradas[k].agrega(a);
                    }
                }
            }
        }
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if(llave == null) { throw new IllegalArgumentException(); }

        int i = this.hashHey(llave);

        if(this.entradas[i] == null) { throw new NoSuchElementException(); }

        for(Entrada e: this.entradas[i]) {
            if(e.llave.equals(llave)) { return e.valor; }
        }

        throw new NoSuchElementException();
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
        if(llave == null) { return false; }
        int i = this.hashHey(llave);
        if(this.entradas[i] == null) { return false; }
        for(Entrada e: this.entradas[i])
            if(e.llave.equals(llave)) { return true; }
        return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        if(llave == null) { throw new IllegalArgumentException(); }
        int i = this.hashHey(llave);
        if(this.entradas[i] == null) { throw new NoSuchElementException(); }
        for(Entrada e: this.entradas[i]) {
            if(e.llave.equals(llave)) {
                this.entradas[i].elimina(e);
                this.elementos -= 1;
            }
        }
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int c = 0;
        for(Lista<Entrada> l: this.entradas)
            c += l != null ? l.getLongitud() - 1 : 0;
        return c;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        if(this.elementos == 0) { return 0; }
        int max = Integer.MIN_VALUE;
        for(Lista<Entrada> l: this.entradas) {
            if(l != null)
                max = l.getLongitud() - 1 > max ? l.getLongitud() - 1 : max;
        }
        return max;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return ((double) this.elementos)/this.entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return this.elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        return this.elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        this.entradas = this.nuevoArreglo(this.entradas.length);
        this.elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        String rep = "{ ";
        Iterador it = new Iterador();
        while(it.hasNext()) {
            Entrada e = it.siguiente();
            rep += "'" + e.llave.toString() + "': ";
            rep += "'" + e.valor.toString() + "', ";
        }
        rep += "}";
        return rep;
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        if(this.getElementos() != d.getElementos()) { return false; }
        Iterator<K> it = this.iteradorLlaves();
        while (it.hasNext()) {
            K k = it.next();
            if(!d.contiene(k)) { return false; }
            if(!d.get(k).equals(this.get(k))) { return false; }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
