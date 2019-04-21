package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.proyecto2.figures.BinaryTree;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledCircle;
import mx.unam.ciencias.edd.proyecto2.svg.Line;
import mx.unam.ciencias.edd.proyecto2.svg.Rectangle;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Complete Binary Tree Figure
 */
public class CompleteBinaryTree extends BinaryTree {

    private ArbolBinarioCompleto<Integer> tree = new ArbolBinarioCompleto<Integer>();

    public CompleteBinaryTree(int[] data) {
        this.rawData = data;
        this.y = 200;
        this.title = "Árbol binario completo";
        for(Integer e: data)
            tree.agrega(e);
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

        int count = 0;
        int level = this.getLocalHeight(count + 1);
        int mult = -1;
        int connections = 0;
        Cola<Integer> q = new Cola<Integer>(); // Store nodes pending for connection
        for(Integer i: this.tree) {
            count += 1;

            // Compute node position
            level = this.getLocalHeight(count);
            mult = level > this.getLocalHeight(count - 1) ? 1 : mult + 2;
            int div = (int) Math.pow(2, level);
            int x = this.x + (int) (width / div) * mult;
            int y = this.y + (vDistance * (level - 1));

            // Handle connection to parent
            int x1 = q.esVacia() ? -1 : q.mira();
            if(x1 > -1) {
                connections = (connections + 1) % 2;
                int y1 = this.y + (vDistance * (level - 2)) + r;
                Line l = new Line(x1, y1, x, y);
                this.svg.addElement(l);
                if (connections == 0) { q.saca(); }
            }
            q.mete(x);

            // Add node at new position
            String fill = count == 1 ? this.yellowAccent : "#fff";
            this.addNode(x, y, i, r, fill);
        }

        return svg.toString();
    }

}