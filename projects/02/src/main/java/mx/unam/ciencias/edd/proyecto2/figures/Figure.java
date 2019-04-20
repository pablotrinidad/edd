package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.proyecto2.svg.Element;
import mx.unam.ciencias.edd.proyecto2.svg.SVGWrapper;
import mx.unam.ciencias.edd.proyecto2.svg.Text;


public abstract class Figure {

    protected SVGWrapper svg = new SVGWrapper();

    protected int[] rawData;

    protected String title;

    protected int x = 100, y = 150; // Starting position

    protected String yellowAccent = "#fed65c";
    protected String orangeAccent = "#ff7244";
    protected String darkGray = "#424242";

    // Return SVG representation of the figure
    public String genSVG() { return ""; };

    protected String arrayToString(int[] data) {
        if(data.length == 0) { return "[]"; }

        String rep = "[";
        for(int i = 0; i < data.length - 1; i++) {
            rep += Integer.toString(data[i]) + ", ";
        }
        rep += Integer.toString(data[data.length - 1]) + "]";
        return rep;
    }

    // Add collection's title to SVG
    protected void addFigureTitle(int x, int y) {
        Text documentTitle = new Text(x, y, this.title);
        documentTitle.setProperty("class", "title");
        svg.addElement(documentTitle);
    }

    // Add collection's string representation to SVG
    protected void addToStringRep(int x, int y, String s) {
        Text strRep = new Text(x, y, "toString(): " + s);
        strRep.setProperty("class", "code");
        this.svg.addElement(strRep);
    }

    // Add raw data to SVG
    protected void addRawDataStr(int x, int y) {
        Text rawDataRep = new Text(x, y, "Entrada recibida: " + this.arrayToString(this.rawData));
        rawDataRep.setProperty("class", "code");
        svg.addElement(rawDataRep);
    }

}