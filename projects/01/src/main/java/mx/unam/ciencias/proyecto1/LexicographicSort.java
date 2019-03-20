package mx.unam.ciencias.edd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mx.unam.ciencias.edd.Lista;

/**
 * Proyecto 1: Ordenador lexicográfico
 */
public class LexicographicSort {

    private Lista<String> rawContent = new Lista<String>();
    private Lista<Record> content = new Lista<Record>();

    public static void main(String[] args) {
        ArgumentParser argsParser = new ArgumentParser();
        LexicographicSort app = new LexicographicSort();
        app.start(argsParser.parse(args));
    }

    public void start(ArgumentParser.ExecutionFlags[] options) {
        if(options.length != 3) { throw new IllegalArgumentException(); }

        // Obtain input
        switch (options[0]) {
            case STDIN:
                rawContent = this.readFromSTDIN();
                break;
            case PATH:
                System.out.println("Expecting input from path");
                break;
        }

        // Build list of records
        for(String rawLine: rawContent) {
            Record record = new Record(rawLine);
            if (options[1] == ArgumentParser.ExecutionFlags.DESCENDING) {
                record.setReversedOrder();
            }
            content.agrega(record);
        }
        content = Lista.mergeSort(content);

        // Ouput method
        switch (options[2]) {
            case STDOUT:
                for(Record r: content) { System.out.println(r); }
                break;
            case FILE:
                System.out.println("Will output on file");
                break;
        }
    }

    private Lista<String> readFromSTDIN() {
        Lista<String> rawContent = new Lista<String>();
        String line;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while((line = in.readLine()) != null) {
                rawContent.agrega(line);
            }
        } catch (IOException e) {
            System.out.println("There was an error parsing the input:\n\t" + e.getMessage());
        }
        return rawContent;
    }
}