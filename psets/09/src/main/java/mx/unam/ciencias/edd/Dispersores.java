package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        byte[] k;
        if(llave.length % 4 != 0) {
            int l = 4 - (llave.length % 4);
            k = new byte[llave.length + l];
            for(int i = 0; i < k.length; i++) {
                k[i] = i >= llave.length ? (byte) 0 : llave[i];
            }

        } else { k = llave;}

        int r = 0;

        for(int i = 0; i < k.length / 4; i ++) {
            int m = i * 4;
            int n = (
                (k[m] & 0xFF) << 24 |
                (k[m+1] & 0xFF) << 16 |
                (k[m+2] & 0xFF) << 8 |
                (k[m+3] & 0xFF)
            );
            r ^= n;
        }
        return r;

    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        return 1;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        return 1;
    }

}
