package mx.unam.ciencias.edd.proyecto2;


import mx.unam.ciencias.edd.proyecto2.figures.Array;

import java.io.File;
import java.io.PrintStream;

import mx.unam.ciencias.edd.proyecto2.figures.AVLTree;
import mx.unam.ciencias.edd.proyecto2.figures.BinarySearchTree;
import mx.unam.ciencias.edd.proyecto2.figures.CompleteBinaryTree;
import mx.unam.ciencias.edd.proyecto2.figures.Figure;
import mx.unam.ciencias.edd.proyecto2.figures.LinkedList;
import mx.unam.ciencias.edd.proyecto2.figures.Queue;
import mx.unam.ciencias.edd.proyecto2.figures.RedBlackTree;
import mx.unam.ciencias.edd.proyecto2.figures.Stack;


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

        String content = this.drawDataStructure(
            argsParser.getDataStructure(),
            argsParser.getData()
        );

        this.writeFigure(content, argsParser.getOutputFile());
    }

    private String drawDataStructure(DataStructures DS, int[] data) {
        Figure figure;
        switch (DS) {
            case LinkedList:
                figure = new LinkedList(data);
                break;
            case Array:
                figure = new Array(data);
                break;
            case Queue:
                figure = new Queue(data);
                break;
            case Stack:
                figure = new Stack(data);
                break;
            case CompleteBinaryTree:
                figure = new CompleteBinaryTree(data);
                break;
            case BinarySearchTree:
                figure = new BinarySearchTree(data);
                break;
            case RBTree:
                figure = new RedBlackTree(data);
                break;
            case AVLTree:
                figure = new AVLTree(data);
                break;
            default:
                // This case is impossible since it caught by the argument parser
                // But allow us to declare figure before the switch case.
                throw new IllegalArgumentException("Invalid data structure, use -h to show usage menu");
        }
        return figure.genSVG();
    }

    private void writeFigure(String content, String outputFile) {
        if(outputFile != null) {
            try {
                System.setOut(new PrintStream(new File(outputFile)));
            } catch (Exception e) {
                System.out.println("There was a problem trying to write to file\n\t" + e.getMessage());
                System.exit(1);
            }
        }

        // Output content
        System.out.println(content);

        // Reset output type
        System.setOut(System.out);
    }
}