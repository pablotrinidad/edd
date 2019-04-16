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

}