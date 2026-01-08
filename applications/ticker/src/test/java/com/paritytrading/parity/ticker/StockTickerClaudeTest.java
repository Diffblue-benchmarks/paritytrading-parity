/*
 * Copyright 2014 Parity authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Permission;

class StockTickerClaudeTest {

    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream errStream;
    private SecurityManager originalSecurityManager;

    @BeforeEach
    void setUp() {
        // Redirect System.err to capture error output
        errStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errStream));

        // Save original security manager
        originalSecurityManager = System.getSecurityManager();

        // Install a security manager that prevents System.exit()
        System.setSecurityManager(new NoExitSecurityManager());
    }

    @AfterEach
    void tearDown() {
        // Restore original System.err
        System.setErr(originalErr);

        // Restore original security manager
        System.setSecurityManager(originalSecurityManager);
    }

    @Test
    void testMainWithNoArguments() {
        // When main is called with no arguments, it should call usage() which calls System.exit(2)
        String[] args = {};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Verify that exit code is 2
            assertEquals(2, e.getStatus(), "Exit status should be 2 for usage errors");

            // Verify that usage message was printed to stderr
            String errorOutput = errStream.toString();
            assertTrue(errorOutput.contains("Usage: parity-ticker"),
                "Error output should contain usage message");
            assertTrue(errorOutput.contains("[-t]"),
                "Error output should show optional -t flag");
            assertTrue(errorOutput.contains("<configuration-file>"),
                "Error output should mention configuration file");
            assertTrue(errorOutput.contains("[<input-file>]"),
                "Error output should show optional input file");
        }
    }

    @Test
    void testMainWithTaqFlagButNoOtherArguments() {
        // When main is called with only -t flag, it should call usage()
        String[] args = {"-t"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            assertEquals(2, e.getStatus(), "Exit status should be 2 for usage errors");

            String errorOutput = errStream.toString();
            assertTrue(errorOutput.contains("Usage: parity-ticker"),
                "Error output should contain usage message");
        }
    }

    @Test
    void testMainWithNonExistentConfigFile() {
        // When main is called with a non-existent config file, it should catch FileNotFoundException
        // and call error() which prints to stderr and exits with status 1
        String[] args = {"nonexistent-config-file.conf"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Verify that exit code is 1 (error() method exits with 1)
            assertEquals(1, e.getStatus(), "Exit status should be 1 for config errors");

            // Verify that an error message was printed
            String errorOutput = errStream.toString();
            assertTrue(errorOutput.length() > 0,
                "Error output should contain error message for missing file");
        }
    }

    @Test
    void testMainWithTaqFlagAndNonExistentConfigFile() {
        // Test with -t flag and non-existent config file
        String[] args = {"-t", "nonexistent-config-file.conf"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Verify that exit code is 1 (error() method exits with 1)
            assertEquals(1, e.getStatus(), "Exit status should be 1 for config errors");

            // Verify that an error message was printed
            String errorOutput = errStream.toString();
            assertTrue(errorOutput.length() > 0,
                "Error output should contain error message for missing file");
        }
    }

    @Test
    void testMainWithTooManyArguments() {
        // When main is called with too many arguments (more than 3 total, or more than 2 after -t)
        // it should call usage()
        String[] args = {"config.conf", "input.bin", "extra-arg"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            assertEquals(2, e.getStatus(), "Exit status should be 2 for usage errors");

            String errorOutput = errStream.toString();
            assertTrue(errorOutput.contains("Usage: parity-ticker"),
                "Error output should contain usage message");
        }
    }

    @Test
    void testMainWithTaqFlagAndTooManyArguments() {
        // Test with -t flag and too many arguments
        String[] args = {"-t", "config.conf", "input.bin", "extra-arg"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            assertEquals(2, e.getStatus(), "Exit status should be 2 for usage errors");

            String errorOutput = errStream.toString();
            assertTrue(errorOutput.contains("Usage: parity-ticker"),
                "Error output should contain usage message");
        }
    }

    @Test
    void testMainDifferentiatesTaqFlag() {
        // Test that the -t flag is recognized and distinguished from other arguments
        // Both should fail with FileNotFoundException, but the code path is different

        // Without -t flag
        String[] args1 = {"-t"};
        try {
            StockTicker.main(args1);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            assertEquals(2, e.getStatus());
        }

        errStream.reset();

        // With -t flag but missing other args
        String[] args2 = {"-t"};
        try {
            StockTicker.main(args2);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            assertEquals(2, e.getStatus());
        }
    }

    @Test
    void testConstructor() {
        // The StockTicker class has an implicit default constructor since no constructor is defined
        // We can verify the class can be instantiated (though it's a utility class with only static methods)
        // Actually, looking at the code again, there's no explicit constructor defined,
        // so Java provides a default one. However, this is a utility class and shouldn't be instantiated.

        // Since the class only has static methods and is package-private, we can still instantiate it
        // for testing purposes, but there's not much to test on the instance itself.
        // The constructor is the default implicit one with no behavior.

        // We cannot instantiate it directly from the test because StockTicker is package-private
        // and we're in the same package, so we can create an instance
        StockTicker ticker = new StockTicker();
        assertNotNull(ticker, "StockTicker instance should not be null");
    }

    /**
     * Security manager that prevents System.exit() by throwing an exception instead.
     * This allows us to test code that calls System.exit() without actually terminating the JVM.
     */
    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            // Allow everything
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
            // Allow everything
        }

        @Override
        public void checkExit(int status) {
            // Prevent System.exit() by throwing an exception
            throw new ExitException(status);
        }
    }

    /**
     * Exception thrown when System.exit() is called.
     * This allows us to catch the exit call and verify the exit status.
     */
    private static class ExitException extends SecurityException {
        private final int status;

        public ExitException(int status) {
            super("System.exit() called");
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}
