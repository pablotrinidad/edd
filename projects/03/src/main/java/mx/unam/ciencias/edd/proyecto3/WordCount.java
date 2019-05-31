package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import mx.unam.ciencias.edd.Lista;

// Words count main application
public class WordCount {

    private ArgumentParser argsParser;

    // Main method
    public static void main(String[] args) {
        WordCount app = new WordCount();
        app.run(args);
    }

    public void run(String[] args) {
        this.argsParser = new ArgumentParser(args);

        Document[] documents = this.createDocs(argsParser.files);
        for(Document doc: documents) {
            doc.countWord();
            doc.genGraphics();
            doc.genTrees();
        }
        // Read content from files
        //      Create document for each file
        // Write files
        // Write content inside files
    }

    // Return document instances with content loaded
    private Document[] createDocs(Lista<String> files) {
        Document[] docs = new Document[files.getElementos()];
        int i = 0;
        for(String file: files) {
            Lista<String> lines = new Lista<String>();
            File f = new File(file);
            try {
                BufferedReader in =
                    new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(file)));
                String line = null;
                while((line = in.readLine()) != null) { lines.agrega(line); }
                in.close();
            } catch (IOException ioe) {
                System.err.println("There was a problem reading file " + file);
                System.exit(1);
            }
            docs[i++] = new Document(lines, f.getName());
        }
        return docs;
    }

}