package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.VerticeGrafica;
import mx.unam.ciencias.edd.proyecto2.svg.Text;
import mx.unam.ciencias.edd.proyecto2.svg.LabeledCircle;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;


/**
 * Graph Figure
 */
public class Graph extends Figure {

    private class Node {
        public double x = 0.0, y = 0.0;
        public double tempX, tempY;

        public int value;
        public int id;

        public Node(int value) {
            this.value = value;
        }

        @Override public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) { return false; }
            @SuppressWarnings("unchecked") Node node = (Node) obj;
            return this.value == node.value;
        }

        @Override public String toString() {
            return Integer.toString(this.value);
        }

        public void show() {
            Double xc = canvasX + (width / 2) + this.x;
            Double yc = canvasY + (height / 2) + this.y;
            LabeledCircle c = new LabeledCircle(xc.intValue(), yc.intValue(), this.toString(), 15);
            c.circle.setProperty("fill", "#fff");
            c.circle.setProperty("stroke", darkGray);
            c.label.setProperty("fill", "000");
            // Text t = new Text(canvasX + 30, canvasY, "(" + this.x.toString() + ", " + this.y.toString() + ")");
            // t.setProperty("class", "code");
            svg.addElement(c);
            // svg.addElement(t);
            // canvasY += 30;
        }

        public void cleanTemps() {
            this.tempX = 0.0;
            this.tempY = 0.0;
        }

    }

    private Grafica<Node> graph = new Grafica<Node>();
    private int width = 1024, height = 1024;
    private int canvasX, canvasY;
    
    // Fruchterman-Reingold algorithm
    int iters = 300;
    private double k = 15.0, k_squared, temp;
    
    public Graph(int[] data) {
        this.rawData = data;
        this.title = "Gráfica";
        this.y = 210;
        this.populateGraph(data);

        int id = 0;
        for(Node n: this.graph) { n.id = id; id +=1; }

        this.canvasX = this.x;
        this.canvasY = this.y;

        // Fruchterman-Reingold algorithm
        this.k_squared = this.k * this.k;
        this.temp = 10 * Math.sqrt(this.graph.getElementos());
    }


    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 110);
        // Add input data
        this.addRawDataStr(this.x, this.y - 80);

        // Add text elements
        Text t = new Text(this.x, this.y - 55, "toString(): " + this.graph.toString());
        t.setProperty("class", "code");
        Text t1 = new Text(this.x, this.y - 30, "Aristas: " + Integer.toString(this.graph.getAristas()));
        t1.setProperty("class", "code");
        Text t2 = new Text(this.x + 110, this.y - 30, "Vertices: " + Integer.toString(this.graph.getElementos()));
        t2.setProperty("class", "code");
        this.svg.addElement(t);
        this.svg.addElement(t1);
        this.svg.addElement(t2);

        // Layout nodes in a circle
        double angle = 2.0 * Math.PI / this.graph.getElementos();
        int count = 0;
        for(Node n: this.graph) {
            n.x = Math.cos(count * angle);
            n.y = Math.sin(count * angle);
            count += 1;
        }

        // Iters
        for(int i = 0; i < this.iters; i++) {
            for(Node v: this.graph)
                v.cleanTemps();

            // Compute repulsion force between nodes
            for(Node v: this.graph) {
                Lista<Node> neighbors = new Lista<Node>();
                for(Node u: this.graph) {
                    if(u.equals(v) || u.id <= v.id) { continue; }
                    if(this.graph.sonVecinos(v, u)) {neighbors.agrega(u);}

                    double deltaX = v.x - u.x;
                    double deltaY = v.y - u.y;
                    double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);

                    // Not worth computing
                    if(distance > 1000.0) { continue; }

                    double repulsion = this.k_squared / distance;
                    v.tempX += deltaX / (distance * repulsion);
                    v.tempY += deltaY / (distance * repulsion);
                    u.tempX -= deltaX / (distance * repulsion);
                    u.tempY -= deltaY / (distance * repulsion);
                }

                // Compute attraction force between edges
                for(Node n: neighbors) {
                    if(n.id > v.id) { continue; }

                    double deltaX = v.x - n.x;
                    double deltaY = v.y - n.y;
                    double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);

                    if(distance == 0.0) { continue; }

                    double attraction = distance * distance / this.k;
                    v.tempX -= deltaX / (distance * attraction);
                    v.tempY -= deltaY / (distance * attraction);
                    n.tempX += deltaX /(distance * attraction);
                    n.tempY += deltaY /(distance * attraction);
                }
            }

            // Max movement capped by current temperature
            for(Node v: this.graph) {
                double norm = Math.sqrt(v.tempX*v.tempX + v.tempY*v.tempY);
                if (norm < 1.0) { continue; }

                double capped_norm = Math.min(norm, this.temp);

                v.x += v.tempX / (norm * capped_norm);
                v.y = v.tempY / (norm * capped_norm);
            }

            // Update system temperature
            this.temp = this.temp > 1.5 ? this.temp * 0.85 : 1.5;
        }

        // for(Node v: this.graph) { v.show();}
        // this.canvasY = this.y;
        // this.canvasX = this.x + 500;

        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;
        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;
        for(Node v: this.graph) {
            xMin = v.x < xMin ? v.x : xMin;
            xMax = v.x > xMax ? v.x : xMax;
            yMin = v.y < yMin ? v.y : yMin;
            yMax = v.y > yMax ? v.y : yMax;
        }

        System.out.println("xMin: " + Double.toString(xMin));
        System.out.println("xMax: " + Double.toString(xMax));
        System.out.println("yMin: " + Double.toString(yMin));
        System.out.println("yMax: " + Double.toString(yMax));
        double curWidth = xMax - xMin;
        double curHeight = yMax - yMin;

        double xScale = this.width / curWidth;
        double yScale = this.height / curHeight;
        double scale = 0.9 * Math.min(xScale, yScale);

        double centerX = xMax + xMin + this.x;
        double centerY = yMax + yMin + this.y;
        System.out.println("centerX: " + Double.toString(centerX));
        System.out.println("centerY: " + Double.toString(centerY));
        double offsetX = centerX / 2.0 * scale;
        double offsetY = centerY / 2.0 * scale;

        System.out.println("offsetX: " + Double.toString(offsetX));
        System.out.println("offsetY: " + Double.toString(offsetY));

        System.out.println("xScale: " + Double.toString(xScale));
        System.out.println("yScale: " + Double.toString(yScale));

        for(Node v: this.graph) {
            v.x = v.x * scale;
            v.y = v.y * scale;
            v.show();
        }

        return this.svg.toString();
    }

    private void populateGraph(int[] data) {
        for(int i = 0; i < data.length; i+=2) {
            Node v = new Node(data[i]), u = new Node(data[i+1]);
            if(!this.graph.contiene(v)) { this.graph.agrega(v); }
            if(v.equals(u)) {
                this.graph.elimina(v); // Delete node along with its neighbors
                this.graph.agrega(v); // Add it as a stand-alone node (not connected)
            } else {
                if(!this.graph.contiene(u)) { this.graph.agrega(u); }
                if(!this.graph.sonVecinos(v, u)) { this.graph.conecta(v, u); }
            }
        }
    }

}