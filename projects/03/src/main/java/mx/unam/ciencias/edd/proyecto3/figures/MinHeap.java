package mx.unam.ciencias.edd.proyecto3.figures;


import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.MonticuloMinimo;
import mx.unam.ciencias.edd.ValorIndexable;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.figures.BinaryTree;
import mx.unam.ciencias.edd.proyecto3.svg.Text;


/**
 * Min heap figure
 */
public class MinHeap extends BinaryTree {

    // Local tree
    ArbolBinarioCompleto<Integer> tree = new ArbolBinarioCompleto<Integer>();

    // Initialize tree setup
    public MinHeap(int[] data) {
        this.rawData = data;

        Lista<ValorIndexable<Integer>> l = new Lista<ValorIndexable<Integer>>();
        for(Integer i: data) { l.agrega(new ValorIndexable<Integer>(i, i)); }

        MonticuloMinimo<ValorIndexable<Integer>> heap = new MonticuloMinimo<ValorIndexable<Integer>>(l);
        for(ValorIndexable<Integer> i: heap) { this.tree.agrega(i.getElemento()); }

        this.title = "Montículo mínimo";
        this.hDistance = 5;
        this.vDistance = 50;
    }

    // Override parent's genSVG method.
    public String genSVG() {
        // Addd title
        this.addFigureTitle(this.x, this.y - 100);
        // Add input data
        this.addRawDataStr(this.x, this.y - 70);
        // Annotations
        this.addAnnotation(this.x, this.y - 50, this.yellowAccent, "Raíz");

        if(this.rawData.length == 0) { return svg.toString();}

        // Add tree to SVG
        this.drawTree(this.tree);

        return svg.toString();
    }

}