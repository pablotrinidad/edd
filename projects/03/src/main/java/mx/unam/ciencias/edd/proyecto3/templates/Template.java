package mx.unam.ciencias.edd.proyecto3.templates;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;

/**
 * Template
 * 
 * A template can load and inject data into
 * an string with HTML tags and placeholders
 */
public class Template {

    private String filename;
    public String content;

    private String baseLocation = "src/main/java/mx/unam/ciencias/edd/proyecto3/templates/";

    public Template(String filename) {
        this.filename = this.baseLocation + filename;
        this.content = this.loadTemplate(this.filename);
    }

    private String loadTemplate(String filename) {
        Lista<String> lines = new Lista<String>();
        String line;
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(filename));
            BufferedReader reader = new BufferedReader(in);
            while((line = reader.readLine()) != null) { lines.agrega(line); }
        } catch(IOException e) {
            System.out.println("There was an error parsing the template " + filename);
            System.exit(1);
        }
        String returnable = "";
        for(String s: lines) { returnable += s + "\n"; }
        return returnable;
    }

    public String render(Diccionario<String, String> context) {
        Iterator<String> keys = context.iteradorLlaves();
        while(keys.hasNext()) {
            String key = keys.next();
            String replacementKey = "\\{\\{ " + key + " \\}\\}";
            String value = context.get(key);
            System.out.println("key: " + key + ", value: " + context.get(key) + ", replacement key: " + replacementKey);
            this.content = this.content.replaceAll(replacementKey, value);
        }
        return this.content;
    }

}