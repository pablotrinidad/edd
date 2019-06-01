package mx.unam.ciencias.edd.proyecto3.figures;

import mx.unam.ciencias.edd.Arreglos;
import mx.unam.ciencias.edd.proyecto3.Document.Word;
import mx.unam.ciencias.edd.proyecto3.svg.Circle;
import mx.unam.ciencias.edd.proyecto3.svg.Line;
import mx.unam.ciencias.edd.proyecto3.svg.Path;
import mx.unam.ciencias.edd.proyecto3.svg.Rectangle;

/**
 * Pie chart SVG graphic
 */
public class BarChart extends Figure {

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

    int elements = 11;

    // Initialize tree setup
    public BarChart(Word[] data) {
        this.rawData = data;
        this.x = 0;
        this.y = 0;
    }

    // Override parent's genSVG method.
    public String genSVG() {
        int width = 440;
        int height = 300;
        int offset = 30;

        Line yAxis = new Line(this.x + offset, this.y, this.x + offset, this.y + height + 20);
        yAxis.setProperty("stroke", this.darkGray);
        yAxis.setProperty("stroke-width", "2px");
        yAxis.setProperty("stroke-dasharray", "4");
        yAxis.setProperty("stroke-opacity", "1");
        svg.addElement(yAxis);

        Line xAxis = new Line(this.x, this.y + height, this.x + offset + width + 20, this.y + height);
        xAxis.setProperty("stroke", this.darkGray);
        xAxis.setProperty("stroke-width", "2px");
        xAxis.setProperty("stroke-dasharray", "4");
        xAxis.setProperty("stroke-opacity", "1");
        svg.addElement(xAxis);

        int x = 0;
        for(int i = 0; i < 11; i++) {
            Word w = this.rawData[i];
            Double y = new Double(height * w.count);
            Rectangle r = new Rectangle(x + offset, height - y.intValue(), 40, y.intValue());
            r.setProperty("fill", colors[i]);
            svg.addElement(r);
            x += 40;
        }

        return svg.toString();
    }


}