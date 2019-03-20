package mx.unam.ciencias.edd;

import mx.unam.ciencias.edd.Arreglos;

/**
 * Proyecto 1: Ordenador lexicográfico
 */
public class LexicographicSort {
    public static void main(String[] args) {
        String stream[] = "Hombres necios que acusáis\n    a la mujer sin razón,\nsin ver que sois la ocasión\n    de lo mismo que culpáis.\n\nSi con ansia sin igual\n    solicitáis su desdén,\n¿por qué queréis que obren bien\n    si las incitáis al mal?\n\nCombatís su resistencia\n    y luego con gravedad\ndecís que fue liviandad\n    lo que hizo la diligencia.\n\nParecer quiere el denuedo\n    de vuestro parecer loco\nal niño que pone el coco\n    y luego le tiene miedo.\n\nQueréis con presunción necia\n    hallar a la que buscáis,\npara pretendida, Tais,\n    y en la posesión, Lucrecia.\n\n¿Qué humor puede ser más raro\n    que el que, falto de consejo,\nél mismo empaña el espejo\n    y siente que no esté claro?\n".split("\n");
        Record[] cool = new Record[stream.length];
        for(int i = 0; i < cool.length; i++) {
            cool[i] = new Record(stream[i]);
        }
        Arreglos.quickSort(cool);
        for (Record r : cool) {
            System.out.println(r);
        }
    }
}