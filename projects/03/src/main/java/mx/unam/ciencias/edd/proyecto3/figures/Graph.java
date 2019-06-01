package mx.unam.ciencias.edd.proyecto3.figures;


import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.VerticeGrafica;
import mx.unam.ciencias.edd.proyecto3.svg.Line;
import mx.unam.ciencias.edd.proyecto3.svg.Text;
import mx.unam.ciencias.edd.proyecto3.svg.LabeledCircle;
import mx.unam.ciencias.edd.proyecto3.Document.Word;
import mx.unam.ciencias.edd.proyecto3.figures.Figure;


/**
 * Graph Figure
 */
public class Graph extends Figure {

    private class Node {
        public Double x = 0.0, y = 0.0;
        public Double tempX, tempY;

        public String value;
        public int id;

        public Node(String value) {
            this.value = value;
        }

        @Override public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) { return false; }
            @SuppressWarnings("unchecked") Node node = (Node) obj;
            return this.value.equals(node.value);
        }

        @Override public String toString() {
            return this.value;
        }

        public void updateToDisplayCoordinates() {
            this.x = canvasX + (width / 2) + this.x;
            this.y = canvasY + (height / 2) + this.y;
        }

        public void show() {
            LabeledCircle c = new LabeledCircle(this.x.intValue(), this.y.intValue(), this.toString(), 15);
            c.circle.setProperty("fill", "#fff");
            c.circle.setProperty("stroke", darkGray);
            c.label.setProperty("fill", "000");
            svg.addElement(c);
        }

        public void cleanTemps() {
            this.tempX = 0.0;
            this.tempY = 0.0;
        }

    }

    private Grafica<Node> graph = new Grafica<Node>();
    private int width = 1024, height = 800;
    private int canvasX, canvasY;
    
    // Fruchterman-Reingold algorithm
    int iters = 300;
    private double k = 15.0, k_squared, temp;

    public Graph(Word[] data) {
        this.rawData = data;
        this.title = "Gráfica";
        this.y = 0;
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

        if(this.graph.getElementos() == 1) {
            for(Node v: this.graph) {
                v.x = this.x + 50.0;
                v.y = this.y + 50.0;
                v.show();
            }
            return this.svg.toString();
        }

        // Layout nodes in a circle (since random usage is prohibited)
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
        double curWidth = xMax - xMin;
        double curHeight = yMax - yMin;

        double xScale = this.width / curWidth;
        double yScale = this.height / curHeight;
        double scale = 0.9 * Math.min(xScale, yScale);

        for(Node v: this.graph) {
            v.x *= scale; v.y *= scale;
            v.updateToDisplayCoordinates();
        }

        // Print edges
        for(Node v: this.graph) {
            for(Node u: this.graph) {
                if(this.graph.sonVecinos(v, u) && u.id > v.id) {
                    Line l = new Line(v.x.intValue(), v.y.intValue(), u.x.intValue(), u.y.intValue());
                    this.svg.addElement(l);
                }
            }
            v.show();
        }

        return this.svg.toString();
    }

    private void populateGraph(Word[] data) {
        Conjunto<String> added = new Conjunto<String>();
        for(int i = 0; i < data.length; i+=2) {
            Node v = new Node(data[i].id), u = new Node(data[i+1].id);
            if(!added.contiene(v.value)) { this.graph.agrega(v); }
            if(!added.contiene(u.value)) { this.graph.agrega(u); }
            if(!this.graph.sonVecinos(v, u)) {
                this.graph.conecta(v, u);
            }
            added.agrega(v.value);
            added.agrega(u.value);
        }
    }

}