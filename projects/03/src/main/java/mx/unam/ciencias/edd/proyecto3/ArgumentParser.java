package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;

/**
 * Argument parser
 * 
 * Provides an API to parse and access the data
 * used to instantiate the WordCount app.
 */
public class ArgumentParser {

    public Lista<String> files = new Lista<String>();
    public String outputDir;

    // Handle incoming data.
    public ArgumentParser(String[] args) {
        if(args.length < 3) {  // flag, output dir and at least one file to parse is required
            System.err.println("Not enough arguments.");
            this.showUsageMenu();
        }

        boolean flagFound = false;

        for(int i = 0; i < args.length; i++){
            if(args[i].charAt(0) == '-') { // Argument is a flag
                if(args[i].equals("-o")) { // Output dir
                    i+= 1;
                    if (i == args.length) {
                        System.err.println("Missing output dir.");
                        this.showUsageMenu();
                    }
                    this.outputDir = args[i];
                    flagFound = true;
                } else { // Invalid flag
                    this.showUsageMenu();
                }
            } else { // Input file
                this.files.agrega(args[i]);
            }
        }

        if(!flagFound || this.outputDir == null) {
            System.err.println("Missing output dir.");
            this.showUsageMenu();
        }
    }

    // Show usage menu and quit application.
    public void showUsageMenu() {
        System.out.println("Usage: proyecto3");
        System.out.println("\tfile..\tFile(s) that will be used to count its words and generate the report.");
        System.out.println("\t-o output_dir\tDirectory to be used to output the report.");
        System.out.println("\t[-h]\tShow usage menu");
        System.exit(1);
    }
}