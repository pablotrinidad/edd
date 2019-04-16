package mx.unam.ciencias.edd.proyecto2.svg;


/**
 * Ellipse Element
 * 
 * https://developer.mozilla.org/en-US/docs/Web/SVG/Element/ellipse
 */
public class Ellipse extends Element {

    public int cx, cy, rx, ry;

    // Initialize element with tag and self closing options
    public Ellipse() { super("ellipse", true); }

}