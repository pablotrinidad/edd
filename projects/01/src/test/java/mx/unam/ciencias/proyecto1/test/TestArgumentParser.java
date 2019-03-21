package mx.unam.ciencias.proyecto1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

import mx.unam.ciencias.ArgumentParser;
import mx.unam.ciencias.edd.Lista;

/**
 * ArgumentParser unit tests.
 */
public class TestArgumentParser {

    /** Set each unit test's expiration timeout to 5 seconds */
    @Rule public Timeout expTimeout = Timeout.seconds(5);

    // Argument parser
    ArgumentParser parser;

    public enum ExecutionVariants {
        REVERSED, // Represents -r flag
        OUTPUTFILE, // Represents -o flag
        INPUTFILE // Input source
    }

    /**
     * Given a list of execution flags, ensure it
     * contains exactly 3 elements, and that each
     * index contain the valid options.
     */
    private void verifyExecutionFlagsList(ArgumentParser.ExecutionFlags flags[]) {
        Assert.assertEquals(3, flags.length);
        Assert.assertTrue(
            flags[0].equals(ArgumentParser.ExecutionFlags.STDIN) ||
            flags[0].equals(ArgumentParser.ExecutionFlags.PATH)
        );
        Assert.assertTrue(
            flags[1].equals(ArgumentParser.ExecutionFlags.ASCENDING) ||
            flags[1].equals(ArgumentParser.ExecutionFlags.DESCENDING)
        );
        Assert.assertTrue(
            flags[2].equals(ArgumentParser.ExecutionFlags.STDOUT) ||
            flags[2].equals(ArgumentParser.ExecutionFlags.FILE)
        );
    }

    /**
     * Return all posible execution variants
     * by generating the powerset of all execution varians O(2^n)
     * @return powerset of all execution variants.
     */
    private Lista<Lista<ExecutionVariants>> getAllExecutionVariants() {
        ExecutionVariants variants[] = {
            ExecutionVariants.REVERSED,
            ExecutionVariants.OUTPUTFILE,
            ExecutionVariants.INPUTFILE
        };

        Lista<Lista<ExecutionVariants>> powerSet = new Lista<Lista<ExecutionVariants>>();

        for(int i = 0; i < (1 << variants.length); i++) {
            Lista<ExecutionVariants> localSet = new Lista<ExecutionVariants>();
            for(int j = 0; j < variants.length; j++) {
                if( (i >> j) % 2 == 1) { localSet.agrega(variants[j]); }
            }
            powerSet.agrega(localSet);
        }
        return powerSet;
    }

    /**
     * Build String array representing command line
     * argument given a linked list of execution variants.
     * @return array of command line arguments.
     */
    private String[] buildArgsFromVariants(Lista<ExecutionVariants> variants) {
        Lista<String> argsList = new Lista<String>();
        for(ExecutionVariants v : variants) {
            switch (v) {
                case REVERSED:
                    argsList.agrega("-r");
                    break;
                case OUTPUTFILE:
                    argsList.agrega("-o");
                    argsList.agrega("im_an_output_path.txt");
                    break;
                case INPUTFILE:
                    argsList.agrega("relative_path_to_file.txt");
                    argsList.agrega("/absolute/path/to/file.txt");
                    argsList.agrega("~/aboslue/path/to/file.txt");
                    argsList.agrega("path-with-dashes-oh-no.txt");
                    break;
            }
        }
        String args[] = new String[argsList.getLongitud()];
        int i = 0;
        for(String s : argsList) { args[i] = s; i++; }
        return args;
    }

    /**
     * If command line arguments are not present,
     * then the default execution flags should be used.
     */
    @Test public void testDefaultExecutionFlags() {
        this.parser = new ArgumentParser();
        String args[] = {};
        ArgumentParser.ExecutionFlags flags[] = this.parser.parse(args);

        verifyExecutionFlagsList(flags);

        Assert.assertEquals(ArgumentParser.ExecutionFlags.STDIN, flags[0]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.ASCENDING, flags[1]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.STDOUT, flags[2]);
    }

    /**
     * Verify arguments are parsed correctly.
     * Tries all possible combinations of command line arguments.
     */
    @Test public void testArgumentsParsing() {
        this.parser = new ArgumentParser();

        Lista<Lista<ExecutionVariants>> allVariants = this.getAllExecutionVariants();

        for(Lista<ExecutionVariants> execution : allVariants) {
            String args[] = this.buildArgsFromVariants(execution);
            ArgumentParser.ExecutionFlags flags[] = this.parser.parse(args);
            verifyExecutionFlagsList(flags);
            for(ExecutionVariants v: execution) {
                switch (v) {
                    case REVERSED:
                        Assert.assertEquals(ArgumentParser.ExecutionFlags.DESCENDING, flags[1]);
                        break;
                    case OUTPUTFILE:
                        Assert.assertEquals(ArgumentParser.ExecutionFlags.FILE, flags[2]);
                        break;
                    case INPUTFILE:
                        Assert.assertEquals(ArgumentParser.ExecutionFlags.PATH, flags[0]);
                        break;
                }
            }
        }
    }

    /**
     * If the input source is files paths, then the arguments
     * parser should return the exact paths that were given.
     */
    @Test public void testFilesInput() {
        this.parser = new ArgumentParser();
        String args[] = {
            "relative_path_to_file.txt",
            "/absolute/path/to/file.txt",
            "~/aboslue/path/to/file.txt",
            "path-with-dashes-oh-no.txt"
        };
        ArgumentParser.ExecutionFlags flags[] = this.parser.parse(args);
        verifyExecutionFlagsList(flags);

        // Input source should be PATH
        Assert.assertEquals(ArgumentParser.ExecutionFlags.PATH, flags[0]);

        // Parser should return files path
        Lista<String> filesPaths = this.parser.getFilesPaths();
        Assert.assertEquals(args.length, filesPaths.getLongitud());
        for(String s : args) {
            Assert.assertTrue(filesPaths.contiene(s));
        }
    }

    /**
     * Same as <code>testFilesInput</code> but with
     * flags between args.
     */
    @Test public void testFilesInputWithDisturbingFlags() {
        this.parser = new ArgumentParser();
        String args1[] = {
            "-r", "-o", "some_output_file.txt",
            "relative_path_to_file.txt",
            "/absolute/path/to/file.txt",
            "~/aboslue/path/to/file.txt",
            "path-with-dashes-oh-no.txt"
        };

        Lista<String> files = new Lista<String>();
        for(int i = 3; i < args1.length; i++) { files.agrega(args1[i]);}

        // Flags before input paths
        ArgumentParser.ExecutionFlags flags[] = this.parser.parse(args1);
        verifyExecutionFlagsList(flags);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.PATH, flags[0]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.DESCENDING, flags[1]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.FILE, flags[2]);
        Assert.assertEquals(files, this.parser.getFilesPaths());

        // Flags between input paths
        String args2[] = {
            "relative_path_to_file.txt",
            "/absolute/path/to/file.txt",
            "-r", "-o", "some_output_file.txt",
            "~/aboslue/path/to/file.txt",
            "path-with-dashes-oh-no.txt"
        };
        this.parser = new ArgumentParser();
        flags = this.parser.parse(args2);
        verifyExecutionFlagsList(flags);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.PATH, flags[0]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.DESCENDING, flags[1]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.FILE, flags[2]);
        Assert.assertEquals(files, this.parser.getFilesPaths());

        // Flags after input paths
        String args3[] = {
            "relative_path_to_file.txt",
            "/absolute/path/to/file.txt",
            "~/aboslue/path/to/file.txt",
            "path-with-dashes-oh-no.txt",
            "-r", "-o", "some_output_file.txt"
        };
        this.parser = new ArgumentParser();
        flags = this.parser.parse(args3);
        verifyExecutionFlagsList(flags);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.PATH, flags[0]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.DESCENDING, flags[1]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.FILE, flags[2]);
        Assert.assertEquals(files, this.parser.getFilesPaths());

        // Flags splitted between input paths
        String args4[] = {
            "relative_path_to_file.txt",
            "/absolute/path/to/file.txt",
            "-r",
            "~/aboslue/path/to/file.txt",
            "-o", "some_output_file.txt",
            "path-with-dashes-oh-no.txt"
        };
        this.parser = new ArgumentParser();
        flags = this.parser.parse(args4);
        verifyExecutionFlagsList(flags);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.PATH, flags[0]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.DESCENDING, flags[1]);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.FILE, flags[2]);
        Assert.assertEquals(files, this.parser.getFilesPaths());
    }

    /**
     * Verify -o works
     */
    @Test public void testInvalidOutputFlaf() {
        this.parser = new ArgumentParser();
        String outputPath = "output-paht-oh-ups.txt";
        String args1[] = {"-o", outputPath};
        ArgumentParser.ExecutionFlags flags[] = this.parser.parse(args1);
        verifyExecutionFlagsList(flags);
        Assert.assertEquals(ArgumentParser.ExecutionFlags.FILE, flags[2]);
        Assert.assertEquals(outputPath, this.parser.getOutputFilePath());
    }
}