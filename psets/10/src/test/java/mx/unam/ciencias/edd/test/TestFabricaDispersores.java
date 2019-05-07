package mx.unam.ciencias.edd.test;

import java.util.Random;
import mx.unam.ciencias.edd.AlgoritmoDispersor;
import mx.unam.ciencias.edd.FabricaDispersores;
import mx.unam.ciencias.edd.Dispersor;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link FabricaDispersores}.
 */
public class TestFabricaDispersores {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Los mensajes. */
    private static final String[] MENSAJES = {
        "alegóricamente",
        "benévolamente",
        "característicamente",
        "democráticamente",
        "efímeramente",
        "fotografiándoselo",
        "graduándoselo",
        "humillación",
        "imprimiéndoselo",
        "júntamelo",
        "kilómetro",
        "lanzándoselo",
        "manifestáselo",
        "numéricamente",
        "objétamelo",
        "pacíficamente",
        "químicamente",
        "reproduciéndoselo",
        "sintonizándomelo",
        "transmitiéndoselo",
        "últimamente",
        "viérteselos",
        "wendolyn",
        "xochimilca",
        "yucatán",
        "zurciéndoselo"
    };

    /* Los resultados para XOR. */
    private static final int[] DISPERSORES_XOR = {
        0xafca1f6b, 0xde1b64c1, 0xcfb5716f, 0x7b1ba3db, 0x0066dfb8,
        0x60dec702, 0x1ab1b36f, 0x6ddabf64, 0xda0b11b1, 0x72cdd70b,
        0xaa6b09b7, 0xafb36572, 0xa4ca6e78, 0xd20f61ce, 0xa37a64ae,
        0xc80f6fce, 0x1479ceb8, 0x7cc2df1a, 0xdfcb0c06, 0x66dcd211,
        0xcfb9797c, 0x6171c9a9, 0x1809170a, 0x72630a04, 0x0db6c20f,
        0x1bb6a868
    };

    /* Los resultados para Bob Jenkins. */
    private static final int[] DISPERSORES_BJ = {
        0x04f28df5, 0x32a22039, 0xdbecee50, 0xa79cd229, 0x55ad66a4,
        0x64e50282, 0xe8d33211, 0xf7d3da1c, 0x1af75efe, 0xa3086dc4,
        0x961c0862, 0x26712ab0, 0x1bff733d, 0xf2be20cd, 0x37b4a028,
        0xab720be1, 0xc963505e, 0xc02ff5b8, 0x6db33023, 0x49a36961,
        0xeebbc9fb, 0x2c38e0d8, 0x83d0f164, 0x12e95da8, 0xada5efef,
        0x3e845628
    };

    /* Los resultados para Daniel J. Bernstein. */
    private static final int[] DISPERSORES_DJB = {
        0x571b2fcc, 0xda4fe731, 0x4bd442a7, 0xd85106fd, 0x8fc9af1e,
        0x17b9927e, 0x0de542d0, 0xbd41e721, 0xc53036fc, 0x6de2043c,
        0xa4fe2dc2, 0x053d1f92, 0x7c1e4cf3, 0x5606f159, 0x63352e0e,
        0x49edf935, 0x54de5e0e, 0x650d2492, 0xda9b8595, 0xb926d880,
        0x9bf54cf2, 0xa0b0e641, 0xb4eef7d5, 0x3c926ce6, 0x433b9a5d,
        0x4e22fdf2
    };

    /* El dispersor. */
    private Dispersor<String> dispersor;

    /**
     * Prueba unitaria para {@link FabricaDispersores#dispersorCadena} con la
     * huella digital XOR.
     */
    @Test public void testDispersorCadenaXOR() {
        Dispersor<String> xor =
            FabricaDispersores.dispersorCadena(AlgoritmoDispersor.XOR_STRING);
        int i = 0;
        for (String mensaje : MENSAJES)
            Assert.assertTrue(xor.dispersa(mensaje) == DISPERSORES_XOR[i++]);
    }

    /**
     * Prueba unitaria para {@link FabricaDispersores#dispersorCadena} con la
     * huella digital de Bob Jenkins.
     */
    @Test public void testDispersorCadenaBJ() {
        Dispersor<String> bj =
            FabricaDispersores.dispersorCadena(AlgoritmoDispersor.BJ_STRING);
        int i = 0;
        for (String mensaje : MENSAJES)
            Assert.assertTrue(bj.dispersa(mensaje) == DISPERSORES_BJ[i++]);
    }

    /**
     * Prueba unitaria para {@link FabricaDispersores#dispersorCadena} con la
     * huella digital de Daniel J. Bernstein.
     */
    @Test public void testDispersorCadenaDJB() {
        Dispersor<String> djb =
            FabricaDispersores.dispersorCadena(AlgoritmoDispersor.DJB_STRING);
        int i = 0;
        for (String mensaje : MENSAJES)
            Assert.assertTrue(djb.dispersa(mensaje) == DISPERSORES_DJB[i++]);
    }
}
