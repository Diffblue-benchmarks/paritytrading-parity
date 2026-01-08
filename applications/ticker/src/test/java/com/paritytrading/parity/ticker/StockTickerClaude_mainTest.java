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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Permission;

class StockTickerClaude_mainTest {

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
    void testMainWithEmptyArgs() {
        // Test line 43: usage() is called when args.length < 1
        String[] args = {};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Verify that exit code is 2 (usage error)
            assertEquals(2, e.getStatus(), "Exit status should be 2 for usage errors");

            // Verify that usage message was printed
            String errorOutput = errStream.toString();
            assertTrue(errorOutput.contains("Usage: parity-ticker"),
                "Error output should contain usage message");
        }
    }

    @Test
    void testMainWithNonExistentConfigFileTriggersError() {
        // Test line 50: error(e) is called when FileNotFoundException is caught
        String[] args = {"nonexistent-config-file-12345.conf"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Verify that exit code is 1 (error() exits with 1)
            assertEquals(1, e.getStatus(), "Exit status should be 1 for config errors");

            // Verify that an error message was printed
            String errorOutput = errStream.toString();
            assertTrue(errorOutput.length() > 0,
                "Error output should contain error message for missing file");
        }
    }

    @Test
    void testMainWithInvalidConfigFileTriggersConfigException() throws IOException {
        // Test line 50: error(e) is called when ConfigException is caught
        // Create a temporary invalid config file
        File tempConfig = File.createTempFile("invalid-config", ".conf");
        tempConfig.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempConfig)) {
            // Write invalid HOCON that will cause ConfigException
            writer.write("{\n");
            writer.write("  this is not valid config syntax!!!\n");
            writer.write("  { { { broken\n");
        }

        String[] args = {tempConfig.getAbsolutePath()};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Verify that exit code is 1 (error() exits with 1)
            assertEquals(1, e.getStatus(), "Exit status should be 1 for config errors");

            // Verify that an error message was printed
            String errorOutput = errStream.toString();
            assertTrue(errorOutput.length() > 0,
                "Error output should contain error message for invalid config");
        } finally {
            tempConfig.delete();
        }
    }

    @Test
    void testMainWithTaqFlagAndNonExistentConfig() {
        // Test line 50 with -t flag: error(e) is called
        String[] args = {"-t", "nonexistent-config-file-67890.conf"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            assertEquals(1, e.getStatus(), "Exit status should be 1 for config errors");

            String errorOutput = errStream.toString();
            assertTrue(errorOutput.length() > 0,
                "Error output should contain error message for missing file");
        }
    }

    @Test
    void testMainWithValidConfigButMissingInstrumentsTriggersConfigException() throws IOException {
        // Test line 50: error(e) with ConfigException when config is missing required fields
        File tempConfig = File.createTempFile("missing-instruments", ".conf");
        tempConfig.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempConfig)) {
            // Write a valid HOCON config but without the required "instruments" section
            writer.write("some-other-config = \"value\"\n");
            writer.write("market-data {\n");
            writer.write("  address = \"127.0.0.1\"\n");
            writer.write("  port = 5000\n");
            writer.write("  username = \"test\"\n");
            writer.write("  password = \"test\"\n");
            writer.write("}\n");
        }

        String[] args = {tempConfig.getAbsolutePath()};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Should exit with status 1 (error) or 3 (fatal)
            assertTrue(e.getStatus() == 1 || e.getStatus() == 3,
                "Exit status should be 1 for config errors or 3 for fatal errors");

            String errorOutput = errStream.toString();
            assertTrue(errorOutput.length() > 0,
                "Error output should contain error message");
        } finally {
            tempConfig.delete();
        }
    }

    @Test
    void testMainWithConfigAndInputFile() throws IOException {
        // Test the case where we have 2 arguments (config + input file)
        // This will trigger the read() path which will process the file
        File tempConfig = File.createTempFile("test-config", ".conf");
        File tempInput = File.createTempFile("test-input", ".bin");
        tempConfig.deleteOnExit();
        tempInput.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempConfig)) {
            // Write a minimal valid config
            writer.write("instruments {\n");
            writer.write("  price-integer-digits = 4\n");
            writer.write("  size-integer-digits = 8\n");
            writer.write("  FOO {\n");
            writer.write("    price-fraction-digits = 2\n");
            writer.write("    size-fraction-digits = 0\n");
            writer.write("  }\n");
            writer.write("}\n");
        }

        // Create an empty file (will be read successfully but produce no output)
        tempInput.createNewFile();

        String[] args = {tempConfig.getAbsolutePath(), tempInput.getAbsolutePath()};

        // The method will read the empty file successfully and return normally (no exit)
        // This tests the successful path through lines 48, 49 (no exception)
        StockTicker.main(args);

        // If we get here, the method completed successfully
        // The display format header should have been printed
        String errorOutput = errStream.toString();
        // Depending on format, there may or may not be output

        tempConfig.delete();
        tempInput.delete();
    }

    @Test
    void testMainWithTaqFlagConfigAndInputFile() throws IOException {
        // Test the case with -t flag and 2 additional arguments
        // This tests the TAQ format path with read()
        File tempConfig = File.createTempFile("test-config-taq", ".conf");
        File tempInput = File.createTempFile("test-input-taq", ".bin");
        tempConfig.deleteOnExit();
        tempInput.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempConfig)) {
            // Write a minimal valid config
            writer.write("instruments {\n");
            writer.write("  price-integer-digits = 4\n");
            writer.write("  size-integer-digits = 8\n");
            writer.write("  BAR {\n");
            writer.write("    price-fraction-digits = 2\n");
            writer.write("    size-fraction-digits = 0\n");
            writer.write("  }\n");
            writer.write("}\n");
        }

        // Create an empty file (will be read successfully)
        tempInput.createNewFile();

        String[] args = {"-t", tempConfig.getAbsolutePath(), tempInput.getAbsolutePath()};

        // The method will read the empty file successfully and return normally
        // This tests the successful path through lines 48, 49 with TAQ format
        StockTicker.main(args);

        // If we get here, the method completed successfully
        // The TAQ format header should have been printed
        String errorOutput = errStream.toString();
        // Depending on format, there may or may not be output

        tempConfig.delete();
        tempInput.delete();
    }

    @Test
    void testMainWithTooManyArgumentsCallsUsage() {
        // Test that usage() is called when too many arguments are provided
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
    void testMainNormalFlowWithValidFlag() {
        // Test that the boolean taq flag is set correctly
        // Even with valid syntax, it will fail due to missing config
        String[] args = {"-t", "some-config.conf"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Will fail with file not found
            assertEquals(1, e.getStatus(), "Exit status should be 1");
        }
    }

    @Test
    void testMainNormalFlowWithoutFlag() {
        // Test without -t flag
        String[] args = {"some-config.conf"};

        try {
            StockTicker.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Will fail with file not found
            assertEquals(1, e.getStatus(), "Exit status should be 1");
        }
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
