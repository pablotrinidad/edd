package mx.unam.ciencias.edd.proyecto1;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Record.
 * 
 * A record can contain any printable characters
 * and will implement the comparable class so it
 * can provide a sense of lexicographic order by
 * default via the compareTo method.
 */
public class Record implements Comparable<Record> {

    private String content;
    private String sortableContent;

    /**
     * Initialize Record from string.
     * Store given content as original and also a
     * sortable representation.
     * @param content content representing a <i>line</i>
     */
    public Record(String content) {
        this.content = content;
        this.sortableContent = this.makeSortable(content);
    }

    /**
     * Transform original content into a simpler
     * string that does not contain special characters.
     * @param content origin string.
     * @return sortable string. 
     */
    private String makeSortable(String content) {
        String sortable = Normalizer.normalize(content, Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .replaceAll("[^a-zA-Z0-9]","")
            .toLowerCase();
        return sortable;
    }

    /**
     * @return sortable version of the Record's content.
     */
    public String getSortable() {
        return this.sortableContent;
    }

    /**
     * Apply comparation using the Record's sortable content.
     */
    public int compareTo(Record record) {
        return this.sortableContent.compareTo(record.getSortable());
    }

    /**
     * @return Record's original content
     */
    public String get() {
        return this.content;
    }

    /**
     * @return Record's original content
     */
    public String toString() {
        return this.get();
    }
}