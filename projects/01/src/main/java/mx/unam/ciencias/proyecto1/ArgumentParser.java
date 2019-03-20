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
        String argsString = String.join(" ", args);

        // Check for -r flag
        int l = argsString.length();
        argsString = argsString.replaceAll(" -r ", " ");
        if (argsString.length() < l) { executionFlags[1] = ExecutionFlags.DESCENDING; }

        // Check for -o output_path flag
        int outPos = argsString.indexOf(" -o ");
        if(outPos >= 0) {
            String outRaw = argsString.substring(outPos+4, argsString.length());
            if(outRaw.length() > 0) {
                outputFilePath = outRaw.split(" ")[0];
            }
            argsString = (
                argsString.substring(0, outPos) +
                argsString.substring(outPos + 4 + outputFilePath.length(), argsString.length())
            );
            executionFlags[2] = ExecutionFlags.FILE;
        }

        System.out.println(argsString);
        args = argsString.split(" ");

        // Parse arguments
        for(String inputPath: args) {
            files.agrega(inputPath);
            executionFlags[0] = ExecutionFlags.PATH;
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

    public Lista<String> getFilesPaths() {
        return this.files;
    }

    public String getOutputFilePath() {
        return this.outputFilePath;
    }

}