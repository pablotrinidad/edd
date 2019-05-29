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

    // "Mezcla"
    private static int[] merge(int a, int b, int c) {
        int[] r = new int[3];
        a -= b; a -= c; a ^= (c >>> 13);
        b -= c; b -= a; b ^= (a << 8);
        c -= a; c -= b; c ^= (b >>> 13);
        a -= b; a -= c; a ^= (c >>> 12);
        b -= c; b -= a; b ^= (a << 16);
        c -= a; c -= b; c ^= (b >>> 5);
        a -= b; a -= c; a ^= (c >>> 3);
        b -= c; b -= a; b ^= (a << 10);
        c -= a; c -= b; c ^= (b >>> 15);
        r[0] = a; r[1] = b; r[2] = c;
        return r;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a = 0x9e3779b9, b = 0x9e3779b9;
        int c = 0xffffffff;
        int i = 0, available = llave.length, n = llave.length;
        while(available >= 12) {
            a += (
                (llave[i] & 0xFF) |
                ((llave[i + 1] & 0xFF) << 8) |
                ((llave[i + 2] & 0xFF) << 16) |
                ((llave[i + 3] & 0xFF) << 24) 
            );

            b += (
                (llave[i + 4] & 0xFF) |
                ((llave[i + 5] & 0xFF) << 8) |
                ((llave[i + 6] & 0xFF) << 16) |
                ((llave[i + 7] & 0xFF) << 24)
            );

            c += (
                (llave[i + 8] & 0xFF) |
                ((llave[i + 9] & 0xFF) << 8) |
                ((llave[i + 10] & 0xFF) << 16) |
                ((llave[i + 11] & 0xFF) << 24)
            );

            int[] r = merge(a, b, c);
            a = r[0]; b = r[1]; c = r[2];

            i += 12; available -= 12;
        }
        c += n;
        switch (available) {
            case 11: c += ((llave[i + 10] & 0xFF) << 24);
            case 10: c += ((llave[i + 9] & 0xFF) << 16);
            case 9: c += ((llave[i + 8] & 0xFF) << 8);
            case 8: b += ((llave[i + 7] & 0xFF) << 24);
            case 7: b += ((llave[i + 6] & 0xFF) << 16);
            case 6: b += ((llave[i + 5] & 0xFF) << 8);
            case 5: b += (llave[i + 4] & 0xFF);
            case 4: a += ((llave[i + 3] & 0xFF) << 24);
            case 3: a += ((llave[i + 2] & 0xFF) << 16);
            case 2: a += ((llave[i + 1] & 0xFF) << 8);
            case 1: a += (llave[i] & 0xFF);
        }

        int[] r = merge(a, b, c);
        return r[2];
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int h = 5381;
        for(int i = 0; i < llave.length; i++) {
            h *= 33;
            h += llave[i] & 0xFF;
        }
        return h;
    }

}
