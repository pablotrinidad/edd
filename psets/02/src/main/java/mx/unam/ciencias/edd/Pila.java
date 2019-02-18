package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        if (cabeza == null) {return ""; }
        String rep = "";
        Nodo nodo = cabeza;
        rep += String.valueOf(nodo.elemento) + "\n";
        while(nodo.siguiente != null) {
            rep += String.valueOf(nodo.siguiente.elemento) + "\n";
            nodo = nodo.siguiente;
        }
        return rep;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if (elemento == null) { throw new IllegalArgumentException(); }

        Nodo nodo = new Nodo(elemento);

        if (rabo == null) {
            cabeza = nodo;
            rabo = nodo;
        } else {
            nodo.siguiente = cabeza;
            cabeza = nodo;
        }
    }
}
