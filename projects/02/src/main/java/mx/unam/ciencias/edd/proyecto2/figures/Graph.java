package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.proyecto2.svg.Text;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;


/**
 * Graph Figure
 */
public class Graph extends Figure {

    private class Node {
        public int x, y;
        public double dispX = 0.0, dispY = 0.0;

        public int value;
        public Lista<Node> neighbors;

        public Node(int value) { this.value = value; }

    }

    private Grafica<Integer> graph = new Grafica<Integer>();
    
    public Graph(int[] data) {
        this.rawData = data;
        this.title = "Gráfica";
        this.y = 210;
        this.populateGraph(data);
    }


    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 110);
        // Add input data
        this.addRawDataStr(this.x, this.y - 80);

        Text t = new Text(this.x, this.y - 55, "toString(): " + this.graph.toString());
        t.setProperty("class", "code");
        Text t1 = new Text(this.x, this.y - 30, "Aristas: " + Integer.toString(this.graph.getAristas()));
        t1.setProperty("class", "code");
        Text t2 = new Text(this.x + 110, this.y - 30, "Vertices: " + Integer.toString(this.graph.getElementos()));
        t2.setProperty("class", "code");
        this.svg.addElement(t);
        this.svg.addElement(t1);
        this.svg.addElement(t2);

        return this.svg.toString();
    }

    private void populateGraph(int[] data) {
        for(int i = 0; i < data.length / 2; i++) {
            int v = data[i], u = data[i+1];
            if(!this.graph.contiene(v)) { this.graph.agrega(v); }
            if(v == u) {
                this.graph.elimina(v); // Delete node along with its neighbors
                this.graph.agrega(v); // Add it as a stand-alone node (not connected)
            } else {
                System.out.println("Connecting " + Integer.toString(v) + " + " + Integer.toString(u));
                if(!this.graph.contiene(u)) { this.graph.agrega(u); }
                if(!this.graph.sonVecinos(v, u)) { this.graph.conecta(v, u); }
            }
        }
    }

}