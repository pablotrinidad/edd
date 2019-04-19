package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import mx.unam.ciencias.edd.Lista;

/**
 * Argument Parser
 * 
 * Provides and API to parse and access the data used tu instantiate the
 * DSDrawer class. Details are provided in the methods.
 */
public class ArgumentParser {

    private int[] data;
    private DataStructures ds;
    private String outputFile;
    private String inputFile;
    private boolean isGraph = false;

    /**
     * Parse command line arguments.
     * 
     * The program is expecting to receive a data structure (with its input)
     * which can be read from a text file or from the standard input.
     * In addition, the user can specify where to output the resulting SVG:
     * either on the standard output or inside a file, in that case an output
     * file path should be provided. Any additional information is treated
     * as incorrect usage which prints the usage menu.
     */
    public ArgumentParser(String[] args) {
        if(args.length > 0) {
            for(int i = 0; i < args.length; i++){
                if(args[i].charAt(0) == '-') { // Argument is a flag
                    if(args[i].equals("-o")) { // Output file
                        i+= 1;
                        if (i == args.length) { this.showUsageMenu(); }
                        this.outputFile = args[i];
                    } else { // Invalid flag
                        this.showUsageMenu();
                    }
                } else { // Input file
                    if(this.inputFile != null) { this.showUsageMenu(); }
                    this.inputFile = args[i];
                }
            }
        }
    }

    /**
     * Return the selected data structure.
     */
    public DataStructures getDataStructure() {
        if (this.ds == null) { this.readInput(); }
        return this.ds;
    }

    /**
     * Return DS data.
     */
    public int[] getData() {
        if (this.data == null) { this.readInput(); }
        return this.data;
    }

    /**
     * Read the data structure information.
     */
    private void readInput() {
        Lista<String> content;
        if(this.inputFile == null) { // Read from STDIN
            InputStreamReader in = new InputStreamReader(System.in);
            content = readFromStream(in);
            this.parseRawInput(content);
        } else { // Read from file
            try {
                InputStreamReader in = new InputStreamReader(new FileInputStream(this.inputFile));
                content = readFromStream(in);
                this.parseRawInput(content);
            } catch(IOException e) {
                System.out.println("There was an error reading the file\n\t" + e.getMessage());
                System.exit(1);
            }
        }
    }

    /**
     * Read and pre-clean content from input stream.
     * @param in Input stream containing the content
     * @return Lista<String> with pre-cleaned content.
     */
    private Lista<String> readFromStream(InputStreamReader in) {
        Lista<String> content = new Lista<String>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(in);
            while((line = reader.readLine()) != null) {
                line = line.trim().replaceAll("\\#(\\ |).*", "");
                if(line.length() > 0) { content.agrega(line); }
            }
        } catch(IOException e) {
            System.out.println("There was an error parsing the input.\n\t" + e.getMessage());
            System.exit(1);
        }
        return content;
    }

    /**
     * Parse raw input.
     * 
     * Receives a linked list containing the raw input and populate the ds and
     * data values.
     */
    private void parseRawInput(Lista<String> content) {
        this.identifyDS(content.getPrimero());
        content.eliminaPrimero();

        Lista<Integer> llData = new Lista<Integer>();;
        for(String s: content)
            for(String e: s.split("(?!(-?\\d)).")) {
                if(e.length() > 0) { llData.agrega(Integer.parseInt(e)); }
            }

        if(llData.getLongitud() % 2 != 0 && this.isGraph) {
            System.out.println("Graphs must have an even number of integers since they represent edges.");
            System.exit(1);
        }

        this.data = new int[llData.getLongitud()];
        int index = 0;
        for(int e: llData) {
            this.data[index] = e;
            index += 1;
        }
    }

    /**
     * Identify data structure and update <code>ds</code> parameter.
     * @param s String used to match data structure.
     */
    private void identifyDS(String s) {
        switch (s) {
            case "ArbolAVL":
                this.ds = DataStructures.AVLTree;
                break;
            case "ArbolBinarioCompleto":
                this.ds = DataStructures.CompleteBinaryTree;
                break;
            case "ArbolBinarioOrdenado":
                this.ds = DataStructures.BinarySearchTree;
                break;
            case "ArbolRojinegro":
                this.ds = DataStructures.RBTree;
                break;
            case "Arreglo":
                this.ds = DataStructures.Array;
                break;
            case "Cola":
                this.ds = DataStructures.Queue;
                break;
            case "Grafica":
                this.isGraph = true;
                this.ds = DataStructures.Graph;
                break;
            case "Lista":
                this.ds = DataStructures.LinkedList;
                break;
            case "Pila":
                this.ds = DataStructures.Stack;
                break;
            default:
                System.out.println("Invalid data structure name received (" + s + "), available options are:");
                System.out.println("\tArbolAVL\n\tArbolBinarioCompleto\n\tArbolBinarioOrdenado\n\tArbolRojinegro\n\tCola\n\tGrafica\n\tLista\n\tPila");
                System.exit(1);
                break;
        }
    }

    /**
     * Show usage menu and quit application.
     */
    public void showUsageMenu() {
        System.out.println("Usage: dsdrawer");
        System.out.println("\t[file]\tData structure file: Contains the desired DS and its data, read specs to see format.");
        System.out.println("\t[-o output_file]\tProgram's output will be written in the given path");
        System.out.println("\t[-h]\tShow usage menu");
        System.exit(1);
    }

}