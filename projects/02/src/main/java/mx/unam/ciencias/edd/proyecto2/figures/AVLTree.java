package mx.unam.ciencias.edd.proyecto2.figures;

import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.VerticeArbolBinario;

import mx.unam.ciencias.edd.proyecto2.figures.BinaryTree;

import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Binary Search Tree Figure
 */
public class AVLTree extends BinaryTree {

    // Local tree
    ArbolAVL<Integer> tree = new ArbolAVL<Integer>();


    // Initialize tree setup
    public AVLTree(int[] data) {
        this.rawData = data;

        for(Integer i: data)
            this.tree.agrega(i);

        this.title = "Árbol AVL";
        this.hDistance = 10;
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

    /**
     * Add node metadata.
     * 
     * Add balance and height as super-scripts.
     */
    protected void addNodeMetadata(VerticeArbolBinario<Integer> v, int x, int y, int r) {
        if(!v.hayPadre()) {
            x += 20;
        } else {
            if(v.padre().hayIzquierdo()) {
                x = v.padre().izquierdo().equals(v) ? x - 60 : x +20;
            } else { x += 20; }
        }
        String data = "{" + v.toString().split(" ", 2)[1] + "}";
        Text metadata = new Text(x, y - 10, data);
        metadata.setProperty("class", "code");
        metadata.setProperty("style", "font-weight: 300; font-size: 12px;");
        this.svg.addElement(metadata);
    }

}