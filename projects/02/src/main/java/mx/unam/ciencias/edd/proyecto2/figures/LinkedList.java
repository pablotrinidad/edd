package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;

import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;
import mx.unam.ciencias.edd.proyecto2.svg.Rectangle;
import mx.unam.ciencias.edd.proyecto2.svg.SVGWrapper;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


/**
 * Linked List Figure
 */
public class LinkedList extends Figure {

    private Lista<Integer> ll = new Lista<Integer>();

    private int vPadding = 10, hPadding = 35; // Padding
    private int margin = 15; // Space between boxes

    public LinkedList(int[] data) {
        this.rawData = data;
        this.title = "Lista doblemente ligada";
        for(Integer e: data)
            ll.agrega(e);
    }

    public String genSVG() {
        // Position title
        this.addFigureTitle(this.x, this.y - 50);

        // Draw linked list elements
        int currentX = this.x;
        for(Integer e: this.ll) {
            LabeledBox b = new LabeledBox(
                currentX, this.y,
                Integer.toString(e),
                this.vPadding, this.hPadding
            );

            String fill = e.equals(ll.getPrimero()) ? this.yellowAccent : "none";
            fill = e.equals(ll.getUltimo()) ? this.orangeAccent : fill;

            String textColor = e.equals(ll.getUltimo()) ? "#ffffff" : "#000000";

            b.box.setProperty("fill", fill);
            b.box.setProperty("stroke", this.darkGray);
            b.box.setProperty("stroke-width", "4px");
            b.box.setProperty("stroke-opacity", "1");

            b.label.setProperty("class", "code");
            b.label.setProperty("fill", textColor);

            svg.addElement(b);

            if(e != ll.getUltimo()) {
                currentX += b.box.width + this.margin;

                Text arrow = new Text(currentX, this.y + (this.vPadding * 2) + 3, "↔");
                arrow.setProperty("stroke", "#000");
                svg.addElement(arrow);

                currentX += 12 + this.margin;
            }
        }

        // Colors explanation
        Rectangle head = new Rectangle(this.x, this.y + 70, 20, 20);
        head.setProperty("stroke", this.darkGray);
        head.setProperty("stroke-width", "2px");
        head.setProperty("rx", "4px"); head.setProperty("ry", "4px");
        head.setProperty("fill", this.yellowAccent);
        svg.addElement(head);
        Text headText = new Text(this.x + 30, this.y + 84, "Cabeza");
        svg.addElement(headText);

        Rectangle tail = new Rectangle(this.x, this.y + 100, 20, 20);
        tail.setProperty("stroke", this.darkGray);
        tail.setProperty("stroke-width", "2px");
        tail.setProperty("rx", "4px"); tail.setProperty("ry", "4px");
        tail.setProperty("fill", this.orangeAccent);
        svg.addElement(tail);
        Text tailText = new Text(this.x + 30, this.y + 114, "Cola");
        svg.addElement(tailText);

        // toString
        this.addToStringRep(this.x, this.y + 170, ll.toString());

        // Add input data
        this.addRawDataStr(this.x, this.y + 200);

        return svg.toString();
    }

}