package mx.unam.ciencias.edd.proyecto3.svg;


/**
 * Labeled circle
 * 
 * Represent a circle and text grouped together.
 */
public class LabeledCircle extends Element {

    public Circle circle;
    public Text label;

    // Initialize element with tag and self closing options
    public LabeledCircle(int x, int y, String content, int radius) {
        super("g", false);

        this.circle = new Circle(x, y, radius);
        this.label = new Text(x, y + 4, content);

        this.circle.setProperty("stroke-width", "4px");
        this.circle.setProperty("stroke-opacity", "1");

        this.label.setProperty("text-anchor", "middle");
        this.label.setProperty("class", "code");

    }

    // Update content and return representation.
    @Override
    public String toString() {
        this.setContent("\n    " + this.circle.toString() + "\n    " + this.label.toString() + "\n  ");
        return super.toString();
    }

}