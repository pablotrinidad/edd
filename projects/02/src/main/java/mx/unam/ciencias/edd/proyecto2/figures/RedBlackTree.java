package mx.unam.ciencias.edd.proyecto2.figures;

import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.VerticeArbolBinario;

import mx.unam.ciencias.edd.proyecto2.figures.BinaryTree;


/**
 * Red-Black Tree Figure
 * 
 * It extends from the Binary Tree figure and use a
 * private local class that extends from ArbolRojinegro
 * to facilitate the access to the tree's properties.
 */
public class RedBlackTree extends BinaryTree {

    // Local Tree
    ArbolRojinegro<Integer> tree = new ArbolRojinegro<Integer>();


    // Initialize tree setup
    public RedBlackTree(int[] data) {
        this.rawData = data;

        for(Integer e: data)
            this.tree.agrega(e);

        this.title = "Árbol rojinegro";
        this.hDistance = 15;
    }


    // Override parent's genSVG method.
    public String genSVG() {
        // Add title to SVG
        this.addFigureTitle(this.x, this.y - 100);
        // Add input data to SVG
        this.addRawDataStr(this.x, this.y - 70);

        if(this.rawData.length == 0) { return this.svg.toString(); }

        // Add tree to SVG
        this.drawTree(this.tree);

        return this.svg.toString();
    }


    // Return fill color based on the actual node's color
    @Override protected String getNodeColor(VerticeArbolBinario<Integer> v) {
        return this.tree.getColor(v) == Color.NEGRO ? this.darkBlue : this.redAccent;
    }


    // Always return white
    @Override protected String getNodeTextColor(VerticeArbolBinario<Integer> v) {
        return "#fff";
    }

}