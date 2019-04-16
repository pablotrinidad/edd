package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;
import mx.unam.ciencias.edd.proyecto2.svg.SVGWrapper;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Linked List Figure
 */
public class LinkedList implements Figure {

    private Lista<Integer> ll = new Lista<Integer>();
    private SVGWrapper svg = new SVGWrapper();

    private int x = 30, y = 30; // Starting position
    private int vPadding = 5, hPadding = 30; // Padding
    private int bRadius = 0; // Box's border radius
    private int margin = 15; // Space between boxes

    public LinkedList(int[] data) {
        for(Integer e: data)
            ll.agrega(e);
    }

    public String genSVG() {
        for(Integer e: this.ll) {
            LabeledBox b = new LabeledBox(
                this.x, this.y,
                Integer.toString(e),
                this.vPadding, this.hPadding,
                this.bRadius
            );
            b.box.setProperty("fill", "none");
            b.box.setProperty("stroke", "#000");
            b.label.setProperty("stroke", "#000");
            svg.addElement(b);

            if(e != ll.getUltimo()) {
                this.x += b.box.width + this.margin;

                Text arrow = new Text(this.x, this.y + (this.vPadding * 3) + 3, "↔");
                arrow.setProperty("stroke", "#000");
                svg.addElement(arrow);

                this.x += 12 + this.margin;
            }

        }
        return svg.toString();
    }

}