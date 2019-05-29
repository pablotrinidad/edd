package mx.unam.ciencias.edd;

/**
 * Clase para fabricar dispersores.
 */
public class FabricaDispersores {

    /* Constructor privado para evitar instanciación. */
    private FabricaDispersores() {}

    /**
     * Regresa una instancia de {@link Dispersor} para cadenas.
     * @param algoritmo el algoritmo de dispersor que se desea.
     * @return una instancia de {@link Dispersor} para cadenas.
     * @throws IllegalArgumentException si recibe un identificador no
     *         reconocido.
     */
    public static Dispersor<String>
    dispersorCadena(AlgoritmoDispersor algoritmo) {
        switch (algoritmo) {
        case XOR_STRING:
            return c -> Dispersores.dispersaXOR(c.getBytes());
        case BJ_STRING:
            return c -> Dispersores.dispersaBJ(c.getBytes());
        case DJB_STRING:
            return c -> Dispersores.dispersaDJB(c.getBytes());
        default: throw new IllegalArgumentException("Algoritmo inválido");
        }
    }
}
