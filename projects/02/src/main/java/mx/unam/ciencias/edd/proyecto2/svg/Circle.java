package mx.unam.ciencias.edd.proyecto2.svg;


/**
 * Circle Element
 * 
 * https://developer.mozilla.org/en-US/docs/Web/SVG/Element/circle
 */
public class Circle extends Element {

    public int cx, cy, r;

    // Initialize element with tag and self closing options
    public Circle() { super("circle", true); }

    // Initialize element with position and dimension
    public Circle(int x, int y, int r) {
        super("circle", true);

        this.setProperty("cx", x);
        this.setProperty("cy", y);
        this.setProperty("r", r);
    }

}