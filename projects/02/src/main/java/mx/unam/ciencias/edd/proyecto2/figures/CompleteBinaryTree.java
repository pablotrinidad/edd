package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledCircle;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Complete Binary Tree Figure
 */
public class CompleteBinaryTree extends Figure {

    private ArbolBinarioCompleto<Integer> tree = new ArbolBinarioCompleto<Integer>();

    private int hDistance = 20;
    private int vDistance = 100;

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

        int digits = (int) Math.floor(Math.log10(this.maxInArray(this.rawData))) + 1;
        int r = 8 * digits; // Node radius
        int h = this.tree.altura(); // Tree height
        int width = this.width(h + 1, r);

        int count = 0; int level = this.getLocalHeight(count + 1);
        int mult = -1;
        for(Integer i: this.tree) {
            count += 1;
            level = this.getLocalHeight(count);
            mult = level > this.getLocalHeight(count - 1) ? 1 : mult + 2;
            int div = (int) Math.pow(2, level);
            int x = this.x + (int) (width / div) * mult;
            int y = this.y + (vDistance * (level - 1));

            LabeledCircle c = new LabeledCircle(x, y, Integer.toString(i), r);
            this.svg.addElement(c);

            Text t = new Text(
                x + 20,
                y,
                "level: " + Integer.toString(level) + " | new level: " + Integer.toString(level) + " | mult: " + Integer.toString(mult)
            );
            this.svg.addElement(t);
        }

        return svg.toString();
    }

    private int maxInArray(int[] data) {
        int max = data[0];
        for(Integer i: data)
            max = i > max ? i : max;
        return max;
    }

    private int width(int h, int r) {
        return h == 1 ? (2 * r) + this.hDistance : (2 * this.width(h - 1, r)) + this.hDistance;
    }

    private int getLocalHeight(int c) {
        return (int) (Math.log(c) / Math.log(2)) + 1;
    }

}