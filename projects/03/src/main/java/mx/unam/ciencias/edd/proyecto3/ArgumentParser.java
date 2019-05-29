package mx.unam.ciencias.add.proyecto3;

/**
 * Argument parser
 * 
 * Provides an API to parse and access the data
 * used to instantiate the WordCount app.
 */
public class ArgumentParser {

    private Lista<String> files = new Lista<String>();
    private String outputDir;

    // Handle incoming data.
    public ArgumentParser(String[] args) {
        if(args.length < 3) {  // flag, output dir and at least one file to parse
            System.err.println("Not enough arguments.");
            this.showUsageMenu();
        }

        boolean flagFound = false;

        if(!flagFound) {
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