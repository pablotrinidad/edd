package mx.unam.ciencias.edd.proyecto3.figures;

import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.proyecto3.Document.Word;
import mx.unam.ciencias.edd.proyecto3.figures.BinaryTree;
import mx.unam.ciencias.edd.proyecto3.svg.Text;


/**
 * Red-Black Tree Figure
 * 
 * It extends from the Binary Tree figure and use a
 * private local class that extends from ArbolRojinegro
 * to facilitate the access to the tree's properties.
 */
public class RedBlackTree extends BinaryTree {

    // Local Tree
    ArbolRojinegro<Word> tree = new ArbolRojinegro<Word>();


    // Initialize tree setup
    public RedBlackTree(Word[] data) {
        this.rawData = data;

        for(Word e: data)
            this.tree.agrega(e);

        this.title = "Árbol rojinegro";
        this.hDistance = 0;
        this.vDistance = 20;
    }


    // Override parent's genSVG method.
    public String genSVG() {
        // Add tree to SVG
        this.drawTree(this.tree);

        return this.svg.toString();
    }


    // Return fill color based on the actual node's color
    @Override protected String getNodeColor(VerticeArbolBinario<Word> v) {
        return this.tree.getColor(v) == Color.NEGRO ? this.darkBlue : this.redAccent;
    }


    // Always return white
    @Override protected String getNodeTextColor(VerticeArbolBinario<Word> v) {
        return "#fff";
    }

}