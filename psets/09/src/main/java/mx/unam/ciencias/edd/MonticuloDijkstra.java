package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Iterfaz para montículos usables por el algoritmo de Dijkstra.
 */
public interface MonticuloDijkstra<T extends ComparableIndexable<T>> {

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    public T elimina();

    /**
     * Reordena un elemento en el árbol. Por omisión no hace nada.
     * @param elemento el elemento que hay que reordenar.
     */
    default public void reordena(T elemento) {}

    /**
     * Regresa el <i>i</i>-ésimo elemento del montículo.
     * @param i el índice del elemento que queremos.
     * @return el <i>i</i>-ésimo elemento del montículo.
     * @throws NoSuchElementException si <i>i</i> es inválido.
     */
    public T get(int i);

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    public boolean esVacia();

    /**
     * Regresa el número de elementos en el montículo.
     * @return el número de elementos en el montículo.
     */
    public int getElementos();
}
