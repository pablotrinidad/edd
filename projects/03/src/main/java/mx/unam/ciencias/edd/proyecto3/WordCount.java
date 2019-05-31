package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

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

        // Count words
        for(Document doc: documents) {
            doc.countWord();
        }

        // Create output directory
        String dirpath = this.makeDirectory(argsParser.outputDir);
        String assetsPath = this.makeDirectory(argsParser.outputDir + "/assets");

        // Write content inside files
        for(Document doc: documents) {
            System.out.println("Writing report for " + doc.filename);
            doc.assetsFolder = assetsPath;
            this.writeReport(doc, dirpath);
        }
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

    // Make directory
    private String makeDirectory(String dirname) {
        File dir = new File(dirname);
        if(!dir.mkdirs() && !dir.isDirectory()) {
            System.err.println("There was an error creating directory " + dirname);
            System.exit(1);
        }
        return dir.getAbsolutePath();
    }

    // Write report using the document's content
    private void writeReport(Document doc, String dir) {
        // Generate report filename
        String reportFilename = doc.filename + ".html";
        File reportFile = new File(dir, reportFilename);
        String content = doc.getHTMLReport();
        try {
            FileWriter fw = new FileWriter(reportFile);
                fw.write(content);
                fw.close();
        } catch (Exception e) {
            System.err.println(
                "There was an error trying to write to file " + reportFile.getAbsolutePath()
            );
        }
    }
}