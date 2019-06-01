package mx.unam.ciencias.edd.proyecto3.figures;

import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.proyecto3.Document.Word;
import mx.unam.ciencias.edd.proyecto3.figures.BinaryTree;

import mx.unam.ciencias.edd.proyecto3.svg.Text;


/**
 * Binary Search Tree Figure
 */
public class AVLTree extends BinaryTree {

    // Local tree
    ArbolAVL<Word> tree = new ArbolAVL<Word>();


    // Initialize tree setup
    public AVLTree(Word[] data) {
        this.rawData = data;

        for(Word i: data)
            this.tree.agrega(i);

        this.title = "Árbol AVL";
        this.hDistance = 30;
        this.vDistance = 30;
    }


    // Override parent's genSVG method.
    public String genSVG() {
        // Add tree to SVG
        this.drawTree(this.tree);

        return svg.toString();
    }

    /**
     * Add node metadata.
     * 
     * Add balance and height as super-scripts.
     */
    protected void addNodeMetadata(VerticeArbolBinario<Word> v, int x, int y, int r) {
        if(!v.hayPadre()) {
            x += 20;
        } else {
            if(v.padre().hayIzquierdo()) {
                x = v.padre().izquierdo().equals(v) ? x - 60 : x +20;
            } else { x += 20;}
        }
        String data = "{" + v.toString().split(" ", 2)[1] + "}";
        Text metadata = new Text(x, y - 10, data);
        metadata.setProperty("class", "code");
        metadata.setProperty("style", "font-weight: 300; font-size: 12px;");
        this.svg.addElement(metadata);
    }

}