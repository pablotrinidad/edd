package mx.unam.ciencias.edd.proyecto2.svg;


/**
 * Labeled box
 * 
 * Represent a rectangle and text grouped together.
 */
public class LabeledBox extends Element {

    public Rectangle box;
    public Text label;

    // Initialize element with tag and self closing options
    public LabeledBox(int x, int y, String content, int VPadding, int HPadding) {
        super("g", false);

        int textWidth = content.length() * 9;
        int textHeight = 20; // Hardcoded

        // Box dimensions
        int width = textWidth + (2*HPadding);
        int height = textHeight + (2 * VPadding);
        int bRadius = Math.round(height/2) - Math.round(height/5);

        // Create new box
        this.box = new Rectangle(x, y, width, height);
        this.box.setProperty("rx", bRadius);
        this.box.setProperty("ry", bRadius);

        // Create new label
        this.label = new Text(x + HPadding, y + VPadding + textHeight, content);
        this.label.setProperty("textLength", textWidth);
        this.label.setProperty("dy", -6);

    }

    // Update content and return representation.
    @Override
    public String toString() {
        this.setContent("\n    " + this.box.toString() + "\n    " + this.label.toString() + "\n  ");
        return super.toString();
    }

}