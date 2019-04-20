package mx.unam.ciencias.edd.proyecto2.svg;


/**
 * Line Element
 * 
 * https://developer.mozilla.org/en-US/docs/Web/SVG/Element/line
 */
public class Line extends Element {

    public int x1, x2, y1, y2;

    // Initialize element with tag and self closing options
    public Line() { super("line", true); }

    // Initialize line at given position
    public Line(int x1, int y1, int x2, int y2) {
        super("line", true);

        this.setProperty("x1", x1);
        this.setProperty("x2", x2);
        this.setProperty("y1", y1);
        this.setProperty("y2", y2);
        this.setProperty("stroke", "#000000");
        this.setProperty("stroke-width", "2px");
    }

}