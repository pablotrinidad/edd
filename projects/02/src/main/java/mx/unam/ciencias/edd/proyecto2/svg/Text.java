package mx.unam.ciencias.edd.proyecto2.svg;


/**
 * Text Element
 * 
 * https://developer.mozilla.org/en-US/docs/Web/SVG/Element/text
 */
public class Text extends Element {

    public int x, y, lengthAdjust, textLength;

    // Initialize element with tag and self closing options
    public Text() { super("text", false); }

}