package mx.unam.ciencias.edd.proyecto2.svg;

/**
 * SVG Wrapper
 * 
 * As the name implies, is a wrapper around SVG that allow common
 * shapes to be generated given specific parameters. It acts as a
 * whole document where pieces are added to it upon construction.
 */
public class SVGWrapper {

    private String document;

    public SVGWrapper() {
        // Following conventions of https://stackoverflow.com/questions/18467982/are-svg-parameters-such-as-xmlns-and-version-needed/34249810#34249810
        this.document = "<svg xmlns='http://www.w3.org/2000/svg'>\n";
        this.document += "  <style>\n";
        this.document += "    @import url('https://fonts.googleapis.com/css?family=Lato:300,400,700');\n";
        this.document += "    text { font-family: 'Lato', sans-serif; font-size: 14px; font-weight: 300;}\n";
        this.document += "  </style>\n";
    }

    public void addElement(Element e) {
        this.document += "  " + e.toString() + "\n";
    }

    @Override
    public String toString() {
        return this.document + "</svg>";
    }


}