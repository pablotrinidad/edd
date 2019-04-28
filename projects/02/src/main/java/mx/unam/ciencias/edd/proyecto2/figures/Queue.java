package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;
import mx.unam.ciencias.edd.proyecto2.svg.Rectangle;
import mx.unam.ciencias.edd.proyecto2.svg.SVGWrapper;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Queue Figure
 */
public class Queue extends Figure {

    private Cola<Integer> q = new Cola<Integer>();

    private int vPadding = 10, hPadding = 35; // Padding
    private int margin = 15; // Space between boxes

    public Queue(int[] data) {
        this.title = "Cola";
        this.rawData = data;
        for(Integer e: data)
            q.mete(e);
    }

    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 50);

        // Add toString rep
        this.addToStringRep(this.x, this.y + 170, q.toString());

        // Add input data
        this.addRawDataStr(this.x, this.y + 200);


        // Draw queue elements
        int currentX = this.x;
        boolean isHead = true;
        while(!q.esVacia()) {
            int e = q.saca();

            LabeledBox b = new LabeledBox(
                currentX, this.y,
                Integer.toString(e),
                this.vPadding, this.hPadding
            );

            String fill = isHead ? this.yellowAccent : "none";
            fill = q.esVacia() ? this.orangeAccent : fill;
            String textColor = q.esVacia() ? "#ffffff" : "#000000";

            // Style labeled box
            b.box.setProperty("fill", fill);
            b.box.setProperty("stroke", this.darkGray);
            b.box.setProperty("stroke-width", "4px");
            b.box.setProperty("stroke-opacity", "1");
            b.label.setProperty("class", "code");
            b.label.setProperty("fill", textColor);
            svg.addElement(b);

            currentX += b.box.width + this.margin;

            Text arrow = new Text(currentX, this.y + (this.vPadding * 2) + 3, "←");
            arrow.setProperty("stroke", "#000");
            svg.addElement(arrow);

            currentX += 12 + this.margin;

            isHead = false;
        }

        Text cont = new Text(currentX, this.y + (this.vPadding * 2) + 3, "...");
        cont.setProperty("stroke", "#000");
        svg.addElement(cont);

        // Colors explanation
        this.addAnnotation(this.x, this.y + 70, this.yellowAccent, "Cabeza");
        this.addAnnotation(this.x, this.y + 100, this.orangeAccent, "Rabo");

        return svg.toString();
    }
}