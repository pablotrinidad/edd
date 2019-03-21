package mx.unam.ciencias;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.proyecto1.Record;


/**
 * Lexicographic Sort.
 * 
 * A command line application that receives
 * input either from the standard input or
 * a set of files and return it's records
 * sorted lexicographically.
 */
public class LexicographicSort {

    // Content retrieved from input
    private Lista<String> rawContent = new Lista<String>();

    // Content stored as Records
    private Lista<Record> content = new Lista<Record>();

    // Command line arguments parser used throughout the application
    private ArgumentParser argsParser;

    /**
     * Application's entry point.
     * 
     * Create application instance with the given
     * command line arguments.
     * 
     * @param args command line argument
     */
    public static void main(String[] args) {
        LexicographicSort app = new LexicographicSort();
        app.sort(args);
    }

    /**
     * Lexicographic sort.
     * 
     * Parse the command line arguments, read the
     * input content, and output the sorted content.
     * 
     * @param args command line arguments
     */
    public void sort(String[] args) {
        // Obtain excecution flags
        this.argsParser = new ArgumentParser();
        ArgumentParser.ExecutionFlags options[] = this.argsParser.parse(args);

        // Read input
        this.rawContent = this.readInput(options[0]);
        this.content = this.buildRecords(this.rawContent, options[1]);

        // Sort content
        this.content = Lista.mergeSort(this.content);

        // Ouput sorted content
        this.outputContent(this.content, options[2]);
    }

    /**
     * Read input content from the indicated source
     * and store its lines in a linked list.
     * 
     * @param inType input type, either standard input (STDIN) or
     * a series of file paths (PATH).
     * @return Lista<String> containing the content' raw records.
     */
    private Lista<String> readInput(ArgumentParser.ExecutionFlags inType) {
        // Initialize content linked list.
        Lista<String> content = new Lista<String>();
        switch (inType) {
            case STDIN: // Input is provided through the standard input
                content = this.populateListFromStream(
                    content,
                    new InputStreamReader(System.in)
                );
                break;
            case PATH: // Input is provided through a series of files
                for(String path: this.argsParser.getFilesPaths()) {
                    try {
                        InputStreamReader in = new InputStreamReader(new FileInputStream(path));
                        content = this.populateListFromStream(content, in);
                    } catch(IOException e) {
                        System.out.println("There was an error reading file\n\t" + e.getMessage());
                        System.exit(1);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return content;
    }

    /**
     * Given a linked list and an input stream, populate
     * given list with each of the input's lines.
     * 
     * @param content linked list of raw content.
     * @param in input stream reader.
     * @return Lista<String> containing the input's lines.
     */
    private Lista<String> populateListFromStream(Lista<String> content, InputStreamReader in) {
        String line;
        try {
            BufferedReader reader = new BufferedReader(in);
            while((line = reader.readLine()) != null) {
                content.agrega(line);
            }
        } catch (IOException e) {
            System.out.println("There was an error parsing the input:\n\t" + e.getMessage());
            System.exit(1);
        }
        return content;
    }

    /**
     * Build a Records linked list from a raw content linked list.
     * 
     * @param rawContent linked list of raw content.
     * @param order if DESCENDING, Records comparisons will be inverted.
     * @return Lista<Record> containing parsed Record instances.
     */
    private Lista<Record> buildRecords(Lista<String> rawContent, ArgumentParser.ExecutionFlags order) {
        Lista<Record> content = new Lista<Record>();
        for (String line : rawContent) {
            Record record = new Record(line);
            if(order.equals(ArgumentParser.ExecutionFlags.DESCENDING)) {
                record.setReversedOrder();
            }
            content.agrega(record);
        }
        return content;
    }

    /**
     * Output sorted content on the given output source.
     * 
     * @param content sorted Records linked list.
     * @param outSrc outout source, either standard output (STDOUT) or
     * file (FILE).
     */
    private void outputContent(Lista<Record> content, ArgumentParser.ExecutionFlags outSrc) {
        // Change output stream to file if flag was present
        if(outSrc.equals(ArgumentParser.ExecutionFlags.FILE)) {
            String outputFile = this.argsParser.getOutputFilePath();
            try {
                System.setOut(new PrintStream(new File(outputFile)));
            } catch (Exception e) {
                System.out.println("There was a problem trying to write to file\n\t" + e.getMessage());
                System.exit(1);
            }
        }

        // Ourtput content
        for(Record r: content) {
            System.out.println(r);
        }

        // Reset output type
        System.setOut(System.out);
    }
}