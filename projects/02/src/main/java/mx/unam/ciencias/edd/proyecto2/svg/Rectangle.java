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

}