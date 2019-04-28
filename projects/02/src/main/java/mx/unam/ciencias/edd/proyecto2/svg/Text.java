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


    // Initialize with content
    public Text(int x, int y, String text) {
        super("text", false);

        this.setProperty("x", x);
        this.setProperty("y", y);
        this.setText(text);
    }


    // Return text content
    public String getText() { return this.insideContent; }


    // Set text content
    public void setText(String s) { this.setContent(s); }

}