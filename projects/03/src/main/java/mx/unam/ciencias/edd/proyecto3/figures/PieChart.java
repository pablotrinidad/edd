package mx.unam.ciencias.edd.proyecto3.figures;

import mx.unam.ciencias.edd.proyecto3.Document.Word;
import mx.unam.ciencias.edd.proyecto3.svg.Circle;
import mx.unam.ciencias.edd.proyecto3.svg.Path;

/**
 * Pie chart SVG graphic
 */
public class PieChart extends Figure {

    public static String[] colors = {
        "#dd5e98",
        "#75b9be",
        "#ee7674",
        "#546a76",
        "#d0d6b5",
        "#aa6da3",
        "#1a936f",
        "#f18701",
        "#7678ed",
        "#2e86ab",
        "#7bd389"
    };

    // Initialize tree setup
    public PieChart(Word[] data) {
        this.rawData = data;
        this.x = 0;
        this.y = 0;
    }

    // Override parent's genSVG method.
    public String genSVG() {
        double x = 150;
        double y = 150;
        int r = 150;
        double startingArc = 0, finalArc = 0;

        double sum = 0;
        for(Word w: this.rawData) {
            sum += w.count;
        }

        Circle c = new Circle(150, 150, 150);
        c.setProperty("fill", "#7bd389");
        svg.addElement(c);


        int color = 0;
        for(Word w: this.rawData) {
            double offset = w.count * 360;
            startingArc = finalArc;
            finalArc += offset;
            Path slice = this.genSlice(startingArc, finalArc, x, y, r, color++);
            svg.addElement(slice);
        }

        return svg.toString();
    }

    private Path genSlice(double ai, double af, double x, double y, double r, int color) {
        double[] a1 = this.getLocation(x, y, r, ai);
        double[] a2 = this.getLocation(x, y, r, af);
        String d = (
            "M " + x + ", " + y + " " + 
            "L " + a1[0] + " " + a1[1] + " " +
            "A " + r + " " + r + " " +
            " 0 0 1 " + a2[0] + " " + a2[1] + " z"
        );
        Path p = new Path(d);
        p.setProperty("fill", colors[color]);
        return p;
    }

    private double[] getLocation(double x, double y, double r, double angle) {
        double[] a1 = new double[2];
        angle = Math.toRadians(angle);
        a1[0] = x + r * Math.cos(angle);
        a1[1] = y + r * Math.sin(angle);
        return a1;
    }

}