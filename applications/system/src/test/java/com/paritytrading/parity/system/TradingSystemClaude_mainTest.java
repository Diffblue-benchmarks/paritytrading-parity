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
package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Permission;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for TradingSystem.main(String[]) method. The main method validates command-line
 * arguments, loads configuration, and starts the trading system. Since the system enters an
 * infinite event loop and calls System.exit(), we use a SecurityManager to intercept exit calls
 * and primarily test argument validation and error handling paths.
 */
public class TradingSystemClaude_mainTest {

  private ByteArrayOutputStream errContent;
  private PrintStream originalErr;
  private File tempConfigFile;
  private SecurityManager originalSecurityManager;

  /** Custom exception to signal System.exit() was called. */
  private static class SystemExitException extends SecurityException {
    public final int status;

    public SystemExitException(int status) {
      super("System.exit(" + status + ")");
      this.status = status;
    }
  }

  /** SecurityManager that prevents System.exit() and throws SystemExitException instead. */
  private static class NoExitSecurityManager extends SecurityManager {
    @Override
    public void checkExit(int status) {
      throw new SystemExitException(status);
    }

    @Override
    public void checkPermission(Permission perm) {
      // Allow everything else
    }
  }

  @BeforeEach
  public void setUp() {
    errContent = new ByteArrayOutputStream();
    originalErr = System.err;
    System.setErr(new PrintStream(errContent));

    originalSecurityManager = System.getSecurityManager();
    System.setSecurityManager(new NoExitSecurityManager());
  }

  @AfterEach
  public void tearDown() {
    System.setSecurityManager(originalSecurityManager);
    System.setErr(originalErr);
    if (tempConfigFile != null && tempConfigFile.exists()) {
      tempConfigFile.delete();
    }
  }

  /**
   * Test main method with no arguments. The main method expects exactly one argument (the
   * configuration file path). When called with no arguments, it calls usage() which prints an
   * error message and exits with status 1.
   */
  @Test
  public void testMainWithNoArguments() {
    try {
      TradingSystem.main(new String[] {});
      fail("Expected SystemExitException");
    } catch (SystemExitException e) {
      // Expected - the usage() method calls System.exit(1)
      String errorOutput = errContent.toString();
      assertTrue(
          errorOutput.contains("parity-system") || errorOutput.contains("Usage"),
          "Error message should mention usage");
    } catch (IOException e) {
      fail("Should not throw IOException for argument validation");
    }
  }

  /**
   * Test main method with multiple arguments. The main method expects exactly one argument. When
   * called with more than one argument, it calls usage() which prints an error message and exits.
   */
  @Test
  public void testMainWithMultipleArguments() {
    try {
      TradingSystem.main(new String[] {"config1.conf", "config2.conf"});
      fail("Expected SystemExitException");
    } catch (SystemExitException e) {
      // Expected - the usage() method calls System.exit(1)
      String errorOutput = errContent.toString();
      assertTrue(
          errorOutput.contains("parity-system") || errorOutput.contains("Usage"),
          "Error message should mention usage");
    } catch (IOException e) {
      fail("Should not throw IOException for argument validation");
    }
  }

  /**
   * Test main method with empty string argument. The main method accepts one argument but when the
   * file doesn't exist, it should catch FileNotFoundException and call error().
   */
  @Test
  public void testMainWithEmptyStringArgument() {
    try {
      TradingSystem.main(new String[] {""});
      fail("Expected SystemExitException from System.exit");
    } catch (SystemExitException e) {
      // Expected - error() method calls System.exit(1)
      String errorOutput = errContent.toString();
      assertTrue(errorOutput.length() > 0, "Should have error output");
    } catch (IOException e) {
      fail("IOException should be caught internally and call error()");
    }
  }

  /**
   * Test main method with non-existent file. When the configuration file doesn't exist, the
   * config() method throws FileNotFoundException which is caught and passed to error().
   */
  @Test
  public void testMainWithNonExistentFile() {
    try {
      TradingSystem.main(new String[] {"/nonexistent/path/to/config.conf"});
      fail("Expected SystemExitException from System.exit");
    } catch (SystemExitException e) {
      // Expected - error() method calls System.exit(1)
      String errorOutput = errContent.toString();
      assertTrue(errorOutput.length() > 0, "Should have error output");
    } catch (IOException e) {
      fail("IOException should be caught internally and call error()");
    }
  }

  /**
   * Test main method with invalid configuration file. When the configuration file exists but has
   * invalid content, it should throw ConfigException which is caught and passed to error().
   */
  @Test
  public void testMainWithInvalidConfigFile() throws IOException {
    tempConfigFile = File.createTempFile("invalid-config", ".conf");
    try (FileWriter writer = new FileWriter(tempConfigFile)) {
      writer.write("invalid { syntax [ missing bracket");
    }

    try {
      TradingSystem.main(new String[] {tempConfigFile.getAbsolutePath()});
      fail("Expected SystemExitException from System.exit");
    } catch (SystemExitException e) {
      // Expected - error() method calls System.exit(1)
      String errorOutput = errContent.toString();
      assertTrue(errorOutput.length() > 0, "Should have error output");
    } catch (IOException e) {
      fail("IOException should be caught internally and call error()");
    }
  }

  /**
   * Test main method with config file missing required fields. When the configuration file is
   * valid HOCON but missing required fields, it should throw ConfigException.
   */
  @Test
  public void testMainWithIncompleteConfigFile() throws IOException {
    tempConfigFile = File.createTempFile("incomplete-config", ".conf");
    try (FileWriter writer = new FileWriter(tempConfigFile)) {
      writer.write("instruments = []\n");
      // Missing market-data, market-report, and order-entry sections
    }

    try {
      TradingSystem.main(new String[] {tempConfigFile.getAbsolutePath()});
      fail("Expected SystemExitException from System.exit or IOException");
    } catch (SystemExitException e) {
      // Expected - error() method calls System.exit(1)
      String errorOutput = errContent.toString();
      assertTrue(errorOutput.length() > 0, "Should have error output");
    } catch (IOException e) {
      // Also acceptable - the missing config might cause IOException during component creation
    }
  }

  /**
   * Test main method with null argument array. This tests defensive handling though null arrays
   * are uncommon in practice.
   */
  @Test
  public void testMainWithNullArgumentArray() {
    try {
      TradingSystem.main(null);
      fail("Expected NullPointerException or SystemExitException");
    } catch (NullPointerException e) {
      // Expected - accessing null array throws NPE
    } catch (SystemExitException e) {
      // Also acceptable if usage() is called before array access
    } catch (IOException e) {
      fail("Should not throw IOException for null array");
    }
  }

  /**
   * Test main method arguments length check boundary. This verifies the exact condition: args.length
   * != 1.
   */
  @Test
  public void testMainArgumentsLengthBoundary() {
    // Test with 0 arguments
    try {
      TradingSystem.main(new String[0]);
      fail("Expected SystemExitException");
    } catch (SystemExitException e) {
      // Expected
    } catch (IOException e) {
      fail("Should not throw IOException");
    }

    // Test with 2 arguments
    try {
      TradingSystem.main(new String[] {"arg1", "arg2"});
      fail("Expected SystemExitException");
    } catch (SystemExitException e) {
      // Expected
    } catch (IOException e) {
      fail("Should not throw IOException");
    }

    // Test with 3 arguments
    try {
      TradingSystem.main(new String[] {"arg1", "arg2", "arg3"});
      fail("Expected SystemExitException");
    } catch (SystemExitException e) {
      // Expected
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
  }
}
