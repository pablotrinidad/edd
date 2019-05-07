package mx.unam.ciencias.edd;

import java.util.Random;

/**
 * Práctica 9: Funciones de dispersión.
 */
public class Practica9 {

    public static void main(String[] args) {

        Random random = new Random();

        String[] letras = {
            "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z"
        };

        int n = 10 + random.nextInt(40);

        String mensaje = "";
        for (int i = 0; i < n; i++)
            mensaje += letras[random.nextInt(letras.length)];

        System.out.println("Mensaje: " + mensaje);
        Dispersor<String> xor =
            FabricaDispersores.dispersorCadena(AlgoritmoDispersor.XOR_STRING);
        System.out.printf("XOR : 0x%08x\n", xor.dispersa(mensaje));
        Dispersor<String> bj =
            FabricaDispersores.dispersorCadena(AlgoritmoDispersor.BJ_STRING);
        System.out.printf("BJ  : 0x%08x\n", bj.dispersa(mensaje));
        Dispersor<String> djb =
            FabricaDispersores.dispersorCadena(AlgoritmoDispersor.DJB_STRING);
        System.out.printf("DJB: 0x%08x\n", djb.dispersa(mensaje));
    }
}
