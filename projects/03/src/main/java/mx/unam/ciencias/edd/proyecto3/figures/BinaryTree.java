package mx.unam.ciencias.edd.proyecto3.figures;


import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.VerticeArbolBinario;

import mx.unam.ciencias.edd.proyecto3.figures.Figure;

import mx.unam.ciencias.edd.proyecto3.svg.LabeledCircle;
import mx.unam.ciencias.edd.proyecto3.svg.Line;

/**
 * Binary Tree
 * 
 * Provides basic computations and styles customization to
 * its children.
 */
public abstract class BinaryTree extends Figure {

    // Distance between adjacent nodes
    protected int hDistance;
    // Distance between levels
    protected int vDistance = 70;
    // Starting Y position
    protected int y = 20;
    protected int x = 20;


    /**
     * Draw tree.
     * 
     * Perform initial computations and call auxiliary
     * methods
     * @param t Tree
     */
    protected void drawTree(ArbolBinario<Integer> tree) {
        // Compute node radius
        int r = this.computeRadius(this.rawData);
        // Compute tree dimensions
        int width = this.width(tree.altura() + 1, r);

        this.addNodes(
            tree.raiz(),
            this.x + (int) (width / 2),
            this.y,
            r,
            width / 2
        );
    }


    /**
     * Add nodes recursively to SVG
     * 
     * @param v Starting node
     * @param x Node's x position
     * @param y Node's y position
     * @param r Node's radius
     * @param localW Local width is defined as the distance available to the left
     *               and right side of the given node
     */
    protected void addNodes(VerticeArbolBinario<Integer> v, int x, int y, int r, int localW) {
        // Compute margin
        int margin = localW / 2;

        // Compute new Y position
        int y1 = y + this.vDistance + r;

        // Draw left subtree
        if(v.hayIzquierdo()) {
            Line ll = new Line(x, y, x - margin, y1);
            this.svg.addElement(ll);
            this.addNodes(v.izquierdo(), x - margin, y1, r, margin);
        }

        // Draw right subtree
        if(v.hayDerecho()) {
            Line lr = new Line(x, y, x + margin, y1);
            this.svg.addElement(lr);
            this.addNodes(v.derecho(), x + margin, y1, r, margin);
        }

        // Add given node to SVG
        this.addNode(v, x, y, r);
    }


    /**
     * Add node to SVG
     * 
     * @param v Node object
     * @param x X position
     * @param y R position
     * @param r Node's radius
     */
    protected void addNode(VerticeArbolBinario<Integer> v, int x, int y, int r) {
        LabeledCircle c = new LabeledCircle(x, y, Integer.toString(v.get()), r);
        c.circle.setProperty("fill", this.getNodeColor(v));
        c.circle.setProperty("stroke", this.darkGray);
        c.label.setProperty("fill", this.getNodeTextColor(v));
        this.svg.addElement(c);
        this.addNodeMetadata(v, x, y, r);
    }

    /**
     * Return the node's fill color based on its type
     * and hierarchy in the tree
     * 
     * @param v Node object
     * @return String with hexadecimal color
     */
    protected String getNodeColor(VerticeArbolBinario<Integer> v) {
        if(!v.hayPadre()) { return this.yellowAccent; }
        return "#fff";
    }


    /**
     * Return the node's text color based on its type
     * and hierarchy in the tree
     * 
     * @param v Node object
     * @return String with hexadecimal color
     */
    protected String getNodeTextColor(VerticeArbolBinario<Integer> v) {
        return "#000";
    }


    /**
     * Add node metadata.
     * 
     * This methods add text at the node's top left or right
     * side. By default does nothing and subclasses may or
     * may not override this
     */
    protected void addNodeMetadata(VerticeArbolBinario<Integer> v, int x, int y, int r) {}


    /**
     * Return the radius of the nodes based
     * on the maximum value of the given dataset.
     *
     * @param data
     * @return
     */
    protected int computeRadius(int[] data) {
        int digits = (int) Math.floor(Math.log10(this.maxInArray(data))) + 1;
        int r = digits > 1 ? 8 * digits : 16;
        return r;
    }


    /**
     * Return the maximum value of a non empty array
     * 
     * @param data Array of integers
     * @return maximum value of given array
     */
    private int maxInArray(int[] data) {
        int max = data[0];
        for(Integer i: data)
            max = i > max ? i : max;
        return max;
    }


    /**
     * Return the tree's width.
     * 
     * Width is computed based on the minimum distance
     * that must exist between to adjacent nodes from
     * bottom to top.
     * @param h Height of the current node
     * @param r Radius of the node
     * @return tree's width
     */
    protected int width(int h, int r) {
        if(h == 1) {
            return (2 * r) + this.hDistance;
        }
        return (2 * this.width(h - 1, r)) + this.hDistance;
    }

}