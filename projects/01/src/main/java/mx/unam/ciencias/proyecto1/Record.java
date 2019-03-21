package mx.unam.ciencias.edd;

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
    private boolean descendingOrder = false;

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
        String sortable = new String(content)
            // Replace ñ/Ñ with the respective n
            .replaceAll("[ñÑ]", "n")
            // // Replace accents with simple vowels
            .replaceAll("[ÀÁÂÃÄÅáàâäãå]", "a")
            .replaceAll("[ÉÈÊËéèêë]", "e")
            .replaceAll("[ÍÌÎÏíìîï]", "i")
            .replaceAll("[ÓÒÔÖÕóòôöõ]", "o")
            .replaceAll("[ÚÙÛÜúùûü]", "u")
            // Remove anything that is not a character or a digit
            .replaceAll("[^a-zA-Z0-9]","")
            // Convert to lowercase
            .toLowerCase();
        return sortable;
    }

    /**
     * Set private variable <code>descendingOrder</code>
     * to <tt>true</tt> which is later used by
     * the <code>compareTo</code> method
     * in order to perform descending sorting.
     */
    public void setReversedOrder() {
        descendingOrder = true;
    }

    /**
     * Set private variable <code>descendingOrder</code>
     * to <tt>false</tt> which is later used by
     * the <code>compareTo</code> method
     * in order to perform ascending sorting.
     */
    public void unsetReversedOrder() {
        descendingOrder = false;
    }

    /**
     * @return sortable version of the Record's content.
     */
    public String getSortable() {
        return this.sortableContent;
    }

    /**
     * Apply comparation using the Record's sortable
     * content and using the <code>descendingOrder</code>
     * variable to return the adecuate value.
     */
    public int compareTo(Record record) {
        if(descendingOrder) {
            return -1 * this.sortableContent.compareTo(record.getSortable());
        }
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