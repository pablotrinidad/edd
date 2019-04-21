package mx.unam.ciencias.edd.proyecto2.figures;

import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.Pila;
import mx.unam.ciencias.edd.proyecto2.figures.BinaryTree;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;
import mx.unam.ciencias.edd.proyecto2.svg.LabeledCircle;
import mx.unam.ciencias.edd.proyecto2.svg.Line;
import mx.unam.ciencias.edd.proyecto2.svg.Rectangle;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Binary Search Tree Figure
 */
public class BinarySearchTree extends BinaryTree {

    ArbolBinarioOrdenado<Integer> tree = new ArbolBinarioOrdenado<Integer>();


    public BinarySearchTree(int[] data) {
        this.rawData = data;

        for(Integer i: data)
            this.tree.agrega(i);

        this.title = "Árbol binario ordenado";
        this.hDistance = 15;
        this.vDistance = 70;
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