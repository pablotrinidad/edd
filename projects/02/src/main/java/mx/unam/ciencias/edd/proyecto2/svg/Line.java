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

}