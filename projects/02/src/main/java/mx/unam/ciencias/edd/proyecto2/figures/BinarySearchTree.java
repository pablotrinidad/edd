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

    private ArbolBinarioOrdenado<Integer> tree = new ArbolBinarioOrdenado<Integer>();

    public BinarySearchTree(int[] data) {
        this.rawData = data;
        this.y = 200;
        this.title = "Árbol binario ordenado";
        for(Integer i: data)
            this.tree.agrega(i);
        this.hDistance = 15;
        this.vDistance = 100;
    }

    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 100);
        // Add input data
        this.addRawDataStr(this.x, this.y - 70);
        // Annotations
        this.addAnnotation(this.x, this.y - 50, this.yellowAccent, "Raíz");
        if(this.rawData.length == 0) { return svg.toString();}

        // Compute node radius
        int r = this.computeRadius(this.rawData);

        // Compute tree dimensions
        int width = this.width(this.tree.altura() + 1, r);

        // Level multipliers
        // int mults[] = new int[this.tree.altura()];
        // for(int i = 0; i < mutls.length; i++) { mults[i] = 1; }

        this.tree.limpia();
        this.tree.agrega(this.rawData[0]);

        this.addNode(this.x + (int) (width / 2), this.y, this.rawData[0], r, this.yellowAccent);


        String fill = "#ffffff";
        for(int i = 1; i < this.rawData.length; i++) {

            // Compute node position
            int level = this.getLevel(this.tree.raiz(), this.rawData[i]);
            int margin = this.getMargin(this.tree.raiz(), this.rawData[i], 1);
            int y = this.y + (this.vDistance * (level));
            int x = this.x + (int) (width / Math.pow(2, level + 1)) * margin;

            
            // Handle connection to parent
            int lastI = this.getParentV(this.tree.raiz(), this.rawData[i]);
            int x1Margin = (int) lastI < this.rawData[i] ? (margin - 1) / 2 : (margin + 1) / 2;
            int x1 = this.x + (int) (width / Math.pow(2, level)) * x1Margin;
            int y1 = this.y + (this.vDistance * (level - 1)) + r;
            Line l = new Line(x1, y1, x, y);
            this.tree.agrega(this.rawData[i]);
            this.svg.addElement(l);
            this.addNode(x, y, this.rawData[i], r, "#fff");
        }

        return svg.toString();
    }

    // Return the level of the node
    private int getLevel(VerticeArbolBinario<Integer> v, int a) {
        if(a <= v.get()) {
            return v.hayIzquierdo() ? 1 + this.getLevel(v.izquierdo(), a) : 1;
        } else {
            return v.hayDerecho() ? 1 + this.getLevel(v.derecho(), a) : 1;
        }
    }

    // Return how far to the right the node should be (relative to level)
    private int getMargin(VerticeArbolBinario<Integer> v, int a, int prev){
        if(a <= v.get()) {
            prev = (prev * 2) - 1;
            return v.hayIzquierdo() ? this.getMargin(v.izquierdo(), a, prev) : prev;
        } else {
            prev = (prev * 2) + 1;
            return v.hayDerecho() ? this.getMargin(v.derecho(), a, prev) : prev;
        }
    }

    // Return the value of the parent
    private int getParentV(VerticeArbolBinario<Integer> v, int a) {
        if(a <= v.get()) {
            return v.hayIzquierdo() ? this.getParentV(v.izquierdo(), a) : v.get();
        } else {
            return v.hayDerecho() ? this.getParentV(v.derecho(), a) : v.get();
        }
    }

}