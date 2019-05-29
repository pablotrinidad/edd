package mx.unam.ciencias.edd;

/**
 * Enumeración para los distintos algoritmos disponibles para dispersores.
 */
public enum AlgoritmoDispersor {
    /** Algoritmo de XOR para cadenas. */
    XOR_STRING,
    /** Algoritmo de Bob Jenkins para cadenas. */
    BJ_STRING,
    /** Algoritmo de Daniel J. Bernstein para cadenas. */
    DJB_STRING;
}
