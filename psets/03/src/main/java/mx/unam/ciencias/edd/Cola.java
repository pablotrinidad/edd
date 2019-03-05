package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        if (cabeza == null) {return ""; }
        String rep = "";
        Nodo nodo = cabeza;
        rep += String.valueOf(nodo.elemento) + ",";
        while(nodo.siguiente != null) {
            rep += String.valueOf(nodo.siguiente.elemento) + ",";
            nodo = nodo.siguiente;
        }
        return rep;
    }

    /**
     * Agrega un elemento al final de la cola.
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
            rabo.siguiente = nodo;
            rabo = nodo;
        }
    }
}
