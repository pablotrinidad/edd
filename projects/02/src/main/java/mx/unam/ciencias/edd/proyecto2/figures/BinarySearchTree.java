package mx.unam.ciencias.edd.proyecto2.figures;

import mx.unam.ciencias.edd.ArbolBinarioOrdenado;

import mx.unam.ciencias.edd.proyecto2.figures.BinaryTree;


/**
 * Binary Search Tree Figure
 */
public class BinarySearchTree extends BinaryTree {

    // Local tree
    ArbolBinarioOrdenado<Integer> tree = new ArbolBinarioOrdenado<Integer>();


    // Initialize tree setup
    public BinarySearchTree(int[] data) {
        this.rawData = data;

        for(Integer i: data)
            this.tree.agrega(i);

        this.title = "Árbol binario ordenado";
        this.hDistance = 15;
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