package mx.unam.ciencias.edd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;


/**
 * Proyecto 1: Ordenador lexicográfico
 */
public class LexicographicSort {

    private Lista<String> rawContent = new Lista<String>();
    private Lista<Record> content = new Lista<Record>();
    private ArgumentParser argsParser;

    public static void main(String[] args) {
        LexicographicSort app = new LexicographicSort();
        app.start(args);
    }

    public void start(String[] args) {
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

    private Lista<String> readInput(ArgumentParser.ExecutionFlags inType) {
        Lista<String> content = new Lista<String>();
        switch (inType) {
            case STDIN:
                content = this.populateListFromStream(
                    content,
                    new InputStreamReader(System.in)
                );
                break;
            case PATH:
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

    private void outputContent(Lista<Record> content, ArgumentParser.ExecutionFlags outType) {
        if(outType.equals(ArgumentParser.ExecutionFlags.FILE)) {
            String outputFile = this.argsParser.getOutputFilePath();
            try {
                System.setOut(new PrintStream(new File(outputFile)));
            } catch (Exception e) {
                System.out.println("There was a problem trying to write to file\n\t" + e.getMessage());
                System.exit(1);
            }
        }
        for(Record r: content) {
            System.out.println(r);
        }

        // Reset output type
        System.setOut(System.out);
    }
}