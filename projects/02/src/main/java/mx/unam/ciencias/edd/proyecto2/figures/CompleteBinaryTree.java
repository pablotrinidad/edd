package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.proyecto2.figures.BinaryTree;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledCircle;
import mx.unam.ciencias.edd.proyecto2.svg.Line;
import mx.unam.ciencias.edd.proyecto2.svg.Rectangle;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Complete Binary Tree Figure
 */
public class CompleteBinaryTree extends BinaryTree {

    ArbolBinarioCompleto<Integer> tree = new ArbolBinarioCompleto<Integer>();


    public CompleteBinaryTree(int[] data) {
        this.rawData = data;

        for(Integer e: data)
            tree.agrega(e);

        this.title = "Árbol binario completo";
        this.vDistance = 70;
        this.hDistance = 30;
    }

    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 100);
        // Add input data
        this.addRawDataStr(this.x, this.y - 70);
        // Annotations
        this.addAnnotation(this.x, this.y - 50, this.yellowAccent, "Raíz");

        if(this.rawData.length == 0) { return svg.toString();}

        // Add tree to SVG
        this.drawTree(this.tree);

        return svg.toString();
    }

}