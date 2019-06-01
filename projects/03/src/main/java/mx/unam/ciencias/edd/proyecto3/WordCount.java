package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.Document.Word;
import mx.unam.ciencias.edd.proyecto3.templates.Template;

// Words count main application
public class WordCount {

    private ArgumentParser argsParser;
    private String baseTemplate = "index.html";
    private String docSummaryTemplate = "components/word_listing.html";
    private String wordCountTemplate = "components/small_word_tag.html";

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

        this.writeIndex(documents, dirpath);
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
        File file = new File(dir, reportFilename);
        String content = doc.getHTMLReport();
        doc.reportFile = file;
        this.writeContent(content, file);
    }

    // Write report using the document's content
    private void writeIndex(Document[] docs, String dir) {
        // Generate report filename
        Template template = new Template(this.baseTemplate);
        Diccionario<String, String> context = new Diccionario<String, String>();

        // Files summary
        String filesSummary = "";
        for(Document doc: docs) {
            Template docT = new Template(this.docSummaryTemplate);
            int limit = doc.wordsArray.length > 5 ? 5 : doc.wordsArray.length;
            Diccionario<String, String> dLocal = new Diccionario<String, String>();
            String localWordsSummary = "";
            for(int i = 0; i < limit; i++) {
                Word w = doc.wordsArray[i];
                Template wordT = new Template(this.wordCountTemplate);
                Diccionario<String, String> wLocal = new Diccionario<String, String>();
                wLocal.agrega("word", w.word);
                wLocal.agrega("count", Integer.toString((int) w.count));
                localWordsSummary += wordT.render(wLocal);
            }
            dLocal.agrega("file_name", doc.filename);
            dLocal.agrega("file_ref", doc.reportFile.getPath());
            dLocal.agrega("total_words", Integer.toString(doc.totalWords));
            dLocal.agrega("top_5_words", localWordsSummary);
            filesSummary += docT.render(dLocal);
        }
        context.agrega("files_summary", filesSummary);

        File file = new File(dir, "index.html");
        this.writeContent(template.render(context), file);
    }

    // Write given content int file.
    private void writeContent(String content, File file) {
        try {
            FileWriter fw = new FileWriter(file);
                fw.write(content);
                fw.close();
        } catch (Exception e) {
            System.err.println(
                "There was an error trying to write to file " + file.getAbsolutePath()
            );
        }
    }
}