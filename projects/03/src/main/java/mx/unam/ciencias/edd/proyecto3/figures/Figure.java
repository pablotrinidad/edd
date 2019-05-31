package mx.unam.ciencias.edd.proyecto3.figures;

import mx.unam.ciencias.edd.proyecto3.svg.SVGWrapper;
import mx.unam.ciencias.edd.proyecto3.Document.Word;
import mx.unam.ciencias.edd.proyecto3.svg.Rectangle;
import mx.unam.ciencias.edd.proyecto3.svg.Text;


public abstract class Figure {

    protected SVGWrapper svg = new SVGWrapper();

    protected Word[] rawData;

    protected String title;

    protected int x = 0, y = 0; // Starting position

    public String yellowAccent = "#fed65c";
    public String orangeAccent = "#ff7244";
    public String darkGray = "#424242";
    public String redAccent = "#d36060";
    public String darkBlue = "#011627";

    // Return SVG representation of the figure
    public String genSVG() { return ""; };

    // Add annotation to SVG
    protected void addAnnotation(int x, int y, String color, String title) {
        Rectangle annotation = new Rectangle(x, y, 20, 20);
        annotation.setProperty("stroke", this.darkGray);
        annotation.setProperty("stroke-width", "2px");
        annotation.setProperty("rx", "4px");
        annotation.setProperty("ry", "4px");
        annotation.setProperty("fill", color);
        svg.addElement(annotation);

        Text annotationText = new Text(x + 30, y  + 14, title);
        svg.addElement(annotationText);
    }

}