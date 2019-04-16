package mx.unam.ciencias.edd.proyecto2.svg;


//
public class Group {

    public boolean isClosed;
    public String content;

    public Group() {
        this.content = "<g>\n";
        this.isClosed = false;
    }

    public void close() {
        this.content += "<\\g>";
        this.isClosed = true;
    }
}