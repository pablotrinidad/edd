package mx.unam.ciencias.proyecto1.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import mx.unam.ciencias.ArgumentParser;

/**
 * ArgumentParser unit tests.
 */
public class TestArgumentParser {

    /** Set each unit test's expiration timeout to 5 seconds */
    @Rule public Timeout expTimeout = Timeout.seconds(5);

    // Argument parser
    ArgumentParser args;

    public TestArgumentParser() {
        ArgumentParser args = new ArgumentParser();
    }

    /**
     * If command line arguments are not present,
     * then the default execution flags should be used.
     */
    @Test public void testDefaultExecutionFlags() {

    }

}
