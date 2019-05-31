package mx.unam.ciencias.edd.proyecto3.figures;

import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.VerticeArbolBinario;

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
    ArbolRojinegro<Integer> tree = new ArbolRojinegro<Integer>();


    // Initialize tree setup
    public RedBlackTree(int[] data) {
        this.rawData = data;

        for(Integer e: data)
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
    @Override protected String getNodeColor(VerticeArbolBinario<Integer> v) {
        return this.tree.getColor(v) == Color.NEGRO ? this.darkBlue : this.redAccent;
    }


    // Always return white
    @Override protected String getNodeTextColor(VerticeArbolBinario<Integer> v) {
        return "#fff";
    }

    // protected void addNodeMetadata(VerticeArbolBinario<Integer> v, int x, int y, int r) {
    //     if(!v.hayPadre()) {
    //         x += 20;
    //     } else {
    //         if(v.padre().hayIzquierdo()) {
    //             x = v.padre().izquierdo().equals(v) ? x - 60 : x +20;
    //         } else { x += 20; }
    //     }
    //     String data = "{" + v.toString().split(" ", 2)[1] + "}";
    //     Text metadata = new Text(x, y - 10, data);
    //     metadata.setProperty("class", "code");
    //     metadata.setProperty("style", "font-weight: 300; font-size: 12px;");
    //     this.svg.addElement(metadata);
    // }

}