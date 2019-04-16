package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.proyecto2.svg.Text;
import mx.unam.ciencias.edd.proyecto2.svg.Rectangle;
import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;
import mx.unam.ciencias.edd.proyecto2.svg.SVGWrapper;

/**
 * Data Structure Drawer.
 * 
 * A small command line application that output the graphic representation
 * of a defined set of data structures as SVG files. It only relies on the
 * data structures' implementations available on mx.unam.ciencias.edd.
 */
public class DSDrawer {

    // Provides an API to parse and access the data used to instantiate the program.
    private ArgumentParser argsParser;

    /**
     * Application's entry point.
     * 
     * Create an application instance with the given command line arguments.
     */
    public static void main(String[] args) {
        DSDrawer app = new DSDrawer();
        app.draw(args);
    }


    /**
     * Parse the command line arguments and call the
     * drawDataStructure method with the parsed data.
     */
    public void draw(String[] args) {
        this.argsParser = new ArgumentParser(args);

        this.drawDataStructure(
            argsParser.getDataStructure(),
            argsParser.getData()
        );
    }

    private void drawDataStructure(DataStructures DS, int[] data) {
        LabeledBox box = new LabeledBox(10, 10, "52", 10, 40, 2);
        box.box.setProperty("fill", "#fff");
        box.box.setProperty("stroke", "#000");
        box.label.setProperty("fill", "#000");
        SVGWrapper svg = new SVGWrapper();
        svg.addElement(box);
        System.out.println(svg);
    }
}