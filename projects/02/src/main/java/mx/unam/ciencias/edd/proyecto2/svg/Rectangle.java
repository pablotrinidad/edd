package mx.unam.ciencias.edd.proyecto2.svg;


/**
 * Rectangle Element
 * 
 * https://developer.mozilla.org/en-US/docs/Web/SVG/Element/rect
 */
public class Rectangle extends Element {

    public int x, y, rx, ry, width, height;

    // Initialize element with tag and self closing options
    public Rectangle() { super("rect", true); }


    // Initialize with position and dimensions
    public Rectangle(int x, int y, int width, int height) {
        super("rect", true);

        this.setProperty("x", x);
        this.setProperty("y", y);
        this.setProperty("width", width);
        this.setProperty("height", height);
    }

}