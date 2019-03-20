package mx.unam.ciencias.edd;

import mx.unam.ciencias.edd.Lista;

/**
 * Argument parser.
 * 
 * Used to parse the user's input
 * on the lexicographic sort program.
 */
public class ArgumentParser {

    private Lista<String> files = new Lista<String>();
    private String outputFilePath;

    public static enum ExecutionFlags {
        // Input type
        STDIN, PATH,

        // Ordering
        ASCENDING, DESCENDING,

        // Output
        STDOUT, FILE
    }

    public ExecutionFlags[] parse(String[] args) {
        ExecutionFlags executionFlags[] = {
            ExecutionFlags.STDIN,
            ExecutionFlags.ASCENDING,
            ExecutionFlags.STDOUT
        };

        // Use default execution mode
        if(args.length == 0) { return executionFlags; }

        // Reformat args to facilitate manipulation
        String argsString = String.join(" ", args).replaceAll(" -", " -+");
        args = argsString.split(" -");

        // Parse arguments
        for(String arg: args) {
            if(arg.startsWith("+")) {  // It's a flag
                String flag = arg.substring(1, 2);
                switch (flag) {
                    case "r":
                        executionFlags[1] = ExecutionFlags.DESCENDING;
                        break;
                    case "o":
                        String outputParts[] = arg.split(" ");
                        if (outputParts.length != 2) { this.showUsageMenu(); }
                        executionFlags[2] = ExecutionFlags.FILE;
                        outputFilePath = outputParts[1];
                        break;
                    default: // Invalid flag
                        this.showUsageMenu();
                        break;
                }
            } else {  // It's a file path
                for(String path: arg.split(" ")) { files.agrega(path); }
                executionFlags[0] = ExecutionFlags.PATH;
            }
        }
        return executionFlags;
    }

    private void showUsageMenu() {
        System.out.println("Usage: sort");
        System.out.println("\t[file...]\tFile(s) to be sorted where each record (line) is delimited by '\\n'");
        System.out.println("\t[-r]\tIndicate content should be sorted in descending order");
        System.out.println("\t[-o output_file]\tProgram's output will be written in the given path");
        System.exit(1);
    }

    public Lista<String> getFilesPath() {
        return this.files;
    }

    public String getOutputFilePath() {
        return this.outputFilePath;
    }

}