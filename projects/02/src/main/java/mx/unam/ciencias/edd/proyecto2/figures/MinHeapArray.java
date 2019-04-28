package mx.unam.ciencias.edd.proyecto2.figures;


import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.ValorIndexable;
import mx.unam.ciencias.edd.MonticuloMinimo;
import mx.unam.ciencias.edd.MonticuloArreglo;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;
import mx.unam.ciencias.edd.proyecto2.svg.LabeledBox;
import mx.unam.ciencias.edd.proyecto2.svg.Path;


/**
 * MinHeapArray Figure
 */
public class MinHeapArray extends Figure {

    private int[] data;

    private int vPadding = 10, hPadding = 35; // Padding

    public MinHeapArray(int[] data) {
        this.rawData = data;
        this.title = "Montículo Arreglo";
        this.y = 300;

        Lista<ValorIndexable<Integer>> l = new Lista<ValorIndexable<Integer>>();
        for(Integer i: data) { l.agrega(new ValorIndexable<Integer>(i, i)); }
        MonticuloMinimo<ValorIndexable<Integer>> heap = new MonticuloMinimo<ValorIndexable<Integer>>(l);
        MonticuloArreglo<ValorIndexable<Integer>> heapA = new MonticuloArreglo<ValorIndexable<Integer>>(heap);

        this.data = new int[heapA.getElementos()];
        for(int i = 0; i < this.data.length; i++)
            this.data[i] = heapA.get(i).getElemento();
    }

    public String genSVG() {
        // Add title
        this.addFigureTitle(this.x, this.y - 200);
        // Add toString rep
        this.addToStringRep(this.x, this.y - 170, this.arrayToString(this.data));
        // Add input data
        this.addRawDataStr(this.x, this.y - 140);


        // Draw boxes
        int currentX = this.x;
        int points[] = new int[this.data.length];
        int i = 0;
        int boxHeight = 0;
        for(Integer e: this.data) {
            LabeledBox b = new LabeledBox(
                currentX,
                this.y,
                Integer.toString(e),
                this.vPadding,
                this.hPadding
            );
            b.box.setProperty("rx", 0); b.box.setProperty("ry", 0);
            b.box.setProperty("fill", "#ffffff");
            b.box.setProperty("stroke", "#424242");
            b.box.setProperty("stroke-width", "4px");
            b.label.setProperty("class", "code");
            svg.addElement(b);

            points[i] = currentX + (b.box.width / 2);
            boxHeight = b.box.height;

            currentX += b.box.width;
            i += 1;
        }

        int height = -25;
        for(i = 0; i < points.length / 2; i++) {
            // Get left and right chile positions
            int l = 2*i + 1, r = 2*i + 2;
            // Get vertical baseline
            int y = height > 0 ? this.y + boxHeight : this.y; 
            String d = "M " + Integer.toString(points[i]) + "," + Integer.toString(y);

            if(l < points.length) {
                int lMidpoint = points[i] + (points[l] - points[i]) / 2;
                int lYOffset = y + height;
                String ll = d + " Q " + Integer.toString(lMidpoint) + "," + Integer.toString(lYOffset);
                ll += " " + Integer.toString(points[l]) + "," + Integer.toString(y);
                this.svg.addElement(new Path(ll));
            }

            height *= 1.3;

            if(r < points.length) {
                int rMidpoint = points[i] + (points[r] - points[i]) / 2;
                int rYOffset = y + height;
                String rl = d + " Q " + Integer.toString(rMidpoint) + ", " + Integer.toString(rYOffset);
                rl += " " + Integer.toString(points[r]) + "," + Integer.toString(y);
                this.svg.addElement(new Path(rl));
            }

            height *= -1;

        }


        return svg.toString();
    }

}