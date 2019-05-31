package mx.unam.ciencias.edd.proyecto3;

import java.text.Normalizer;
import java.util.Iterator;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.templates.Template;

/**
 * Document
 * 
 * A document receive a list of string representing each
 * line inside a file and know how to output the words count
 * and the HTML content.
 */
class Document {

    private Lista<String> lines;
    public String filename;
    public Diccionario<String, Integer> words;

    // Templates
    private String baseTemplate = "file_report.html";

    public Document(Lista<String> lines, String filename) {
        this.lines = lines;
        this.filename = filename;
        this.words = new Diccionario<String, Integer>();
    }

    // Count words in file
    public void countWord() {
        for(String line: this.lines) {
            String[] lineWords = this.getLineWords(line);
            for(String word: lineWords) {
                if(this.words.contiene(word)) {
                    int count = this.words.get(word);
                    this.words.agrega(word, count + 1);
                } else {
                    this.words.agrega(word, 1);
                }
            }
        }
    }

    // Generate graphics (bars graph and pie graph)
    public void genGraphics() {}

    // Generate trees (AVL and RBT)
    public void genTrees() {}

    // Return list of sanitized words from a line
    private String[] getLineWords(String line) {
        line = Normalizer.normalize(line, Normalizer.Form.NFKD);
        line = line.replaceAll("[^\\p{IsAlphabetic}\\s]", "");
        line = line.toLowerCase();
        line = line.trim();
        return line.equals("") ? new String[0] : line.split(" ");
    }

    // Build HTML report using pre-computed data
    public String getHTMLReport() {
        Template template = new Template(this.baseTemplate);
        Diccionario<String, String> context = new Diccionario<String, String>();
        context.agrega("file_name", this.filename);
        return template.render(context);
    }
}