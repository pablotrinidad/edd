package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.proyecto2.figures.Figure;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;


/**
 * Array Figure
 */
public class Array extends Figure {

    private int[] data;

    private int vPadding = 10, hPadding = 35; // Padding

    public Array(int[] data) {
        this.rawData = data;
        this.data = data;
        this.title = "Arreglo";
    }

    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 50);

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

        // Add toString rep
        this.addToStringRep(this.x, this.y + 80, this.arrayToString(this.data));

        // Add input data
        this.addRawDataStr(this.x, this.y + 110);

        return svg.toString();
    }

}