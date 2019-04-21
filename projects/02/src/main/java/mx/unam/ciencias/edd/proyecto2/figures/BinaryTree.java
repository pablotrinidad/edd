package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.proyecto2.figures.Figure;
import mx.unam.ciencias.edd.proyecto2.svg.LabeledCircle;

/**
 * Binary Tree
 * 
 * Provides basic computation to children.
 */
public abstract class BinaryTree extends Figure {

    protected int hDistance = 20;
    protected int vDistance = 100;

    // Return the max value of a non-empty array
    private int maxInArray(int[] data) {
        int max = data[0];
        for(Integer i: data)
            max = i > max ? i : max;
        return max;
    }

    // Compute tree's width based on its height and a given node radius
    protected int width(int h, int r) {
        return h == 1 ? (2 * r) + this.hDistance : (2 * this.width(h - 1, r)) + this.hDistance;
    }

    // Return the height of a node assuming it belongs to a complete binary tree.
    protected int getLocalHeight(int c) {
        return (int) (Math.log(c) / Math.log(2)) + 1;
    }

    // Compute radius based on the number of digits of the max number
    protected int computeRadius(int[] data) {
        int digits = (int) Math.floor(Math.log10(this.maxInArray(data))) + 1;
        int r = digits > 1 ? 8 * digits : 16;
        return r;
    }

    // Add note to tree's SVG
    protected void addNode(int x, int y, int v, int r, String fill) {
        LabeledCircle c = new LabeledCircle(x, y, Integer.toString(v), r);
        c.circle.setProperty("fill", fill);
        c.circle.setProperty("stroke", this.darkGray);
        this.svg.addElement(c);
    }

}