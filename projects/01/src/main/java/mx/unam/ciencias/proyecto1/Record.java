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

    public Record(String content) {
        this.content = content;
        this.sortableContent = this.makeSortable(content);
    }

    private String makeSortable(String content) {
        // System.out.println(content.toLowerCase());
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
            .replaceAll("[^a-zA-Z0-9]","").toLowerCase();
        return sortable;
    }

    public void setReversedOrder() {
        descendingOrder = true;
    }

    public void unsetReversedOrder() {
        descendingOrder = false;
    }

    public String getSortable() {
        return this.sortableContent;
    }

    public int compareTo(Record record) {
        if(descendingOrder) {
            return -1 * this.sortableContent.compareTo(record.getSortable());
        }
        return this.sortableContent.compareTo(record.getSortable());
    }

    public String toString() {
        return this.content;
    }
}