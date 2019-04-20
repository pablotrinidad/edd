package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.proyecto2.figures.Figure;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;
import mx.unam.ciencias.edd.proyecto2.svg.SVGWrapper;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Array Figure
 */
public class Array implements Figure {

    private int[] data;
    private SVGWrapper svg = new SVGWrapper();

    private String title = "Arreglos";
    private int x = 100, y = 150; // Starting position
    private int vPadding = 10, hPadding = 35; // Padding
    private int margin = 15; // Space between boxes

    public Array(int[] data) {
        this.data = data;
    }

    public String genSVG() {
        // Position title
        Text documentTitle = new Text(this.x, this.y - 50, this.title);
        documentTitle.setProperty("class", "title");
        svg.addElement(documentTitle);

        // Draw boxes
        int currentX = this.x;
        for(Integer e: this.data) {
            LabeledBox b = new LabeledBox(
                currentX,
                this.y,
                Integer.toString(e),
                this.vPadding,
                this.hPadding
            );
            b.box.setProperty("rx", 0); b.box.setProperty("ry", 0);
            b.box.setProperty("fill", "#ffffff");
            b.box.setProperty("stroke", "#424242");
            b.box.setProperty("stroke-width", "4px");
            b.label.setProperty("class", "code");
            svg.addElement(b);

            currentX += b.box.width;
        }

        // toString
        Text strRep = new Text(this.x, this.y + 80, "toString(): " + this.arrayToString(this.data));
        strRep.setProperty("class", "code");
        svg.addElement(strRep);

        return svg.toString();
    }

    private String arrayToString(int[] data) {
        if (data.length == 0) { return "[]"; }
        String rep = "[";
        for(Integer i: data)
            rep += i.toString() + ", ";
        rep = rep.substring(0, rep.length() - 2) + "]";
        return rep;
    }

}