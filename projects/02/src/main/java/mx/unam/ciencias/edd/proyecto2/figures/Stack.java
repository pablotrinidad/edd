package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.Pila;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;
import mx.unam.ciencias.edd.proyecto2.svg.Rectangle;
import mx.unam.ciencias.edd.proyecto2.svg.SVGWrapper;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Stack Figure
 */
public class Stack extends Figure {

    private Pila<Integer> s = new Pila<Integer>();

    private int vPadding = 15, hPadding = 60; // Padding

    public Stack(int[] data) {
        this.rawData = data;
        this.title = "Pila";
        for(Integer e: data)
            s.mete(e);
    }

    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 50);

        // Add toString rep
        this.addToStringRep(this.x + 150, this.y + 70, s.toString());

        // Add input data
        this.addRawDataStr(this.x + 150, this.y + 100);


        // Draw queue elements
        int currentY = this.y;
        boolean isTail = true;
        while(!s.esVacia()) {
            int e = s.saca();

            LabeledBox b = new LabeledBox(
                this.x, currentY,
                Integer.toString(e),
                this.vPadding, this.hPadding
            );

            String fill = isTail ? this.yellowAccent : "none";

            // Style labeled box
            b.box.setProperty("rx", 0); b.box.setProperty("ry", 0);
            b.box.setProperty("fill", fill);
            b.box.setProperty("stroke", this.darkGray);
            b.box.setProperty("stroke-width", "4px");
            b.label.setProperty("class", "code");
            b.label.setProperty("fill", "#000000");
            svg.addElement(b);

            currentY += b.box.height;

            isTail = false;
        }

        // Colors explanation
        this.addAnnotation(this.x + 150, this.y + 10, this.yellowAccent, "Rabo");

        return svg.toString();
    }
}