package mx.unam.ciencias.edd.proyecto3;

import java.text.Normalizer;
import java.util.Iterator;

import mx.unam.ciencias.edd.Arreglos;
import mx.unam.ciencias.edd.Conjunto;
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
    public int totalWords = 0;
    public int totalUniqueWords = 0;
    private Word[] wordsArray;
    private Word[] distributionArray;  // Most common (top 10) words distributed

    // Templates
    private String baseTemplate = "file_report.html";
    private String wordTagTemplate = "components/word_tag.html";

    public Document(Lista<String> lines, String filename) {
        this.lines = lines;
        this.filename = filename;
        this.words = new Diccionario<String, Integer>();
    }

    private class Word implements Comparable<Word> {
        public String word;
        public float count;

        public Word(String word, float count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public int compareTo(Word word) {
            if (word.count == this.count) { return 0; }
            return word.count < this.count ? -1 : 1;  // Flips order from ascending to descending
        }
    }

    // Count words in file
    public void countWord() {
        for(String line: this.lines) {
            String[] lineWords = this.getLineWords(line);
            for(String word: lineWords) {
                this.totalWords += 1;
                if(this.words.contiene(word)) {
                    int count = this.words.get(word);
                    this.words.agrega(word, count + 1);
                } else {
                    this.totalUniqueWords += 1;
                    this.words.agrega(word, 1);
                }
            }
        }
        this.computeWordsArray();
        this.computeDistributionArray();
    }
    
    // Build words array
    private void computeWordsArray() {
        this.wordsArray = new Word[this.words.getElementos()];
        int i = 0;
        Iterator<String> keys = this.words.iteradorLlaves();
        while(keys.hasNext()) {
            String key = keys.next(); int value = this.words.get(key);
            this.wordsArray[i++] = new Word(key, value);
        }
        Arreglos.quickSort(this.wordsArray);
    }

    // Build distribution array
    public void computeDistributionArray() {
        int i = 0;
        Word[] dist = new Word[11];
        for(i; i < 10; i++) {
            Word w = this.wordsArray[i];
            float p = w.count / this.totalUniqueWords;
            dist[i] = new Word(w.word, p);
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

        // File name
        context.agrega("file_name", this.filename);
        context.agrega("total_words", Integer.toString(this.totalWords));
        context.agrega("total_unique_words", Integer.toString(this.totalUniqueWords));

        // Words count
        String wordsContent = "";
        for(Word w: this.wordsArray) {
            Template wordT = new Template(this.wordTagTemplate);
            Diccionario<String, String> localDict = new Diccionario<String, String>();
            localDict.agrega("word", w.word);
            localDict.agrega("count", Integer.toString((int) w.count));
            wordsContent += wordT.render(localDict);
        }
        context.agrega("word_count", wordsContent);

        return template.render(context);
    }
}