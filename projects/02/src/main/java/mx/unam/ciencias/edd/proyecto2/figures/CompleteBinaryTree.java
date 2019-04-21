package mx.unam.ciencias.edd.proyecto2.figures;

import mx.unam.ciencias.edd.ArbolBinarioCompleto;

import mx.unam.ciencias.edd.proyecto2.figures.BinaryTree;


/**
 * Complete Binary Tree Figure
 */
public class CompleteBinaryTree extends BinaryTree {

    // Local tree
    ArbolBinarioCompleto<Integer> tree = new ArbolBinarioCompleto<Integer>();


    // Initialize tree setup
    public CompleteBinaryTree(int[] data) {
        this.rawData = data;

        for(Integer e: data)
            tree.agrega(e);

        this.title = "Árbol binario completo";
        this.hDistance = 30;
    }


    // Override parent's genSVG method.
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