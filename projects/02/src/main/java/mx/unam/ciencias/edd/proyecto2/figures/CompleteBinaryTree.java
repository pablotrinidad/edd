package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledCircle;
import mx.unam.ciencias.edd.proyecto2.svg.SVGWrapper;


/**
 * Linked List Figure
 */
public class CompleteBinaryTree implements Figure {

    private ArbolBinarioCompleto<Integer> tree = new ArbolBinarioCompleto<Integer>();;
    private SVGWrapper svg = new SVGWrapper();

    public CompleteBinaryTree(int[] data) {
        System.out.println(data);
    }

    public String genSVG() {
        return "";
    }

}