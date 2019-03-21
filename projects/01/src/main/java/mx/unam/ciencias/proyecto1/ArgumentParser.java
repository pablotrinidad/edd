package mx.unam.ciencias;

import mx.unam.ciencias.edd.Lista;

/**
 * Argument parser.
 * 
 * Used to parse the user's input on the lexicographic sort program.
 */
public class ArgumentParser {

    private Lista<String> files = new Lista<String>();
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
        // Default options
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
        argsString = argsString.replaceAll("-r", "");
        if (argsString.length() < l) { executionFlags[1] = ExecutionFlags.DESCENDING; }

        // Check for -h/--help flag
        l = argsString.length();
        argsString = argsString.replaceAll("-h", "");
        argsString = argsString.replaceAll("--help", "");
        if (argsString.length() < l) { this.showUsageMenu(); }

        // Check for -o output_path flag
        int outPos = argsString.indexOf("-o ");
        if(outPos >= 0) {
            String outRaw = argsString.substring(outPos+3, argsString.length());
            if(outRaw.length() > 0) {
                outputFilePath = outRaw.split(" ")[0];
            } else {
                showUsageMenu();
            }
            argsString = (
                argsString.substring(0, outPos) +
                argsString.substring(outPos + 3 + outputFilePath.length(), argsString.length())
            );
            executionFlags[2] = ExecutionFlags.FILE;
        }

        // At this point all identifiable flags where stripped out the string
        // only input files should be here
        args = argsString.trim().replaceAll(" +", " ").split(" "); 

        // Add input files
        for(String inputPath: args) {
            files.agrega(inputPath);
            executionFlags[0] = ExecutionFlags.PATH;
        }
        return executionFlags;
    }

    /**
     * Print user menu and quit application.
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
     * this will return the files paths.
     * @return input files paths
     */
    public Lista<String> getFilesPaths() {
        return this.files;
    }

    /**
     * If output should occur on file, return output file path
     * @return output file path.
     */
    public String getOutputFilePath() {
        return this.outputFilePath;
    }

}