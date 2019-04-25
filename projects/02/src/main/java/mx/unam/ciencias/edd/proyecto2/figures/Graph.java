package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.proyecto2.figures.Figure;


/**
 * Graph Figure
 */
public class Graph extends Figure {


    public Graph(int[] data) {
        this.rawData = data;
        this.title = "Arreglo";
        this.y = 200;
    }


    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 100);
        // Add input data
        this.addRawDataStr(this.x, this.y - 70);

        return svg.toString();
    }

}