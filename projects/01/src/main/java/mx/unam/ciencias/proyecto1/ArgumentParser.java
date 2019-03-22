package mx.unam.ciencias;

import mx.unam.ciencias.edd.Lista;

/**
 * Argument parser.
 * 
 * Used to parse the user's input on the lexicographic sort program.
 */
public class ArgumentParser {

    private Lista<String> inputFiles = new Lista<String>();
    private String outputFilePath;

    public static enum ExecutionFlags {
        // Input type
        STDIN, PATH,

        // Ordering
        ASCENDING, DESCENDING,

        // Output source
        STDOUT, FILE
    }

    /**
     * Parse array of command line arguments and return
     * array of length 3 containing the application's
     * execution flags.
     * @return execution flags
     */
    public ExecutionFlags[] parse(String[] args) {
        // Default execution flags
        ExecutionFlags executionFlags[] = {
            ExecutionFlags.STDIN,
            ExecutionFlags.ASCENDING,
            ExecutionFlags.STDOUT
        };

        // Use default execution falgs
        if(args.length == 0) { return executionFlags; }

        for(int i = 0; i < args.length; i++) {
            if(args[i].charAt(0) == '-') { // Argument is a flag
                if(args[i].equals("-r")) {
                    executionFlags[1] = ExecutionFlags.DESCENDING;
                } else if (args[i].equals("-o")) {
                    if ((i++) == args.length) { this.showUsageMenu(); }
                    executionFlags[2] = ExecutionFlags.FILE;
                    this.outputFilePath = args[i];
                } else {
                    this.showUsageMenu();
                }
            } else { // Argument is an input file
                executionFlags[0] = ExecutionFlags.PATH;
                this.inputFiles.agrega(args[i]);
            }
        }

        return executionFlags;

    }

    /**
     * Print uage menu and quit application.
     */
    private void showUsageMenu() {
        System.out.println("Usage: sort");
        System.out.println("\t[file...]\tFile(s) to be sorted where each record (line) is delimited by '\\n'");
        System.out.println("\t[-r]\tIndicate content should be sorted in descending order");
        System.out.println("\t[-o output_file]\tProgram's output will be written in the given path");
        System.out.println("\t[-h]\tShow usage menu");
        System.exit(1);
    }

    /**
     * If input is expected to come from files,
     * this will return the files paths that were stored
     * during arguments parsing.
     * @return input files paths
     */
    public Lista<String> getFilesPaths() {
        return this.inputFiles;
    }

    /**
     * If output should occur on file, return output file
     * path stored during arguments parsing.
     * @return output file path.
     */
    public String getOutputFilePath() {
        return this.outputFilePath;
    }

}