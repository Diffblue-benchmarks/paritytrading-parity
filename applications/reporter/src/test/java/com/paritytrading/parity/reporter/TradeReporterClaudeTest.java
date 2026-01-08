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
package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TradeReporterClaudeTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;
  private File tempConfigFile;

  @BeforeEach
  void setUp() throws IOException {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));

    // Create a temporary config file for tests
    tempConfigFile = File.createTempFile("test-config", ".conf");
    tempConfigFile.deleteOnExit();
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
    System.setErr(originalErr);

    if (tempConfigFile != null && tempConfigFile.exists()) {
      tempConfigFile.delete();
    }
  }

  @Test
  void constructorCreatesInstance() {
    // Test that the constructor can be invoked (though the class is designed as a utility)
    assertDoesNotThrow(() -> {
      // Using reflection to test the constructor since it's package-private
      java.lang.reflect.Constructor<?> constructor =
          TradeReporter.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      Object instance = constructor.newInstance();
      assertNotNull(instance);
    });
  }

  @Test
  void mainWithNoArguments() {
    // Test that main exits with usage message when no arguments provided
    SecurityManager originalSecurityManager = System.getSecurityManager();
    try {
      // Set up a security manager to catch System.exit
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
              // Allow everything
            }

            @Override
            public void checkPermission(java.security.Permission perm, Object context) {
              // Allow everything
            }

            @Override
            public void checkExit(int status) {
              throw new SecurityException("System.exit(" + status + ")");
            }
          });

      SecurityException exception =
          assertThrows(SecurityException.class, () -> TradeReporter.main(new String[] {}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String output = outContent.toString() + errContent.toString();
    assertTrue(
        output.contains("parity-reporter") || output.contains("usage"),
        "Expected usage message but got: " + output);
  }

  @Test
  void mainWithThreeArguments() {
    // Test that main exits with usage message when too many arguments provided
    SecurityManager originalSecurityManager = System.getSecurityManager();
    try {
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
              // Allow everything
            }

            @Override
            public void checkPermission(java.security.Permission perm, Object context) {
              // Allow everything
            }

            @Override
            public void checkExit(int status) {
              throw new SecurityException("System.exit(" + status + ")");
            }
          });

      SecurityException exception =
          assertThrows(
              SecurityException.class,
              () -> TradeReporter.main(new String[] {"arg1", "arg2", "arg3"}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String output = outContent.toString() + errContent.toString();
    assertTrue(
        output.contains("parity-reporter") || output.contains("usage"),
        "Expected usage message but got: " + output);
  }

  @Test
  void mainWithInvalidFlag() {
    // Test that main exits with usage message when invalid flag is provided
    SecurityManager originalSecurityManager = System.getSecurityManager();
    try {
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
              // Allow everything
            }

            @Override
            public void checkPermission(java.security.Permission perm, Object context) {
              // Allow everything
            }

            @Override
            public void checkExit(int status) {
              throw new SecurityException("System.exit(" + status + ")");
            }
          });

      SecurityException exception =
          assertThrows(
              SecurityException.class,
              () -> TradeReporter.main(new String[] {"-x", "config.conf"}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String output = outContent.toString() + errContent.toString();
    assertTrue(
        output.contains("parity-reporter") || output.contains("usage"),
        "Expected usage message but got: " + output);
  }

  @Test
  void mainWithNonExistentConfigFile() {
    // Test that main handles FileNotFoundException when config file doesn't exist
    SecurityManager originalSecurityManager = System.getSecurityManager();
    try {
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
              // Allow everything
            }

            @Override
            public void checkPermission(java.security.Permission perm, Object context) {
              // Allow everything
            }

            @Override
            public void checkExit(int status) {
              throw new SecurityException("System.exit(" + status + ")");
            }
          });

      SecurityException exception =
          assertThrows(
              SecurityException.class,
              () -> TradeReporter.main(new String[] {"/nonexistent/file.conf"}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String errorOutput = errContent.toString();
    assertTrue(
        errorOutput.contains("error:") || errorOutput.length() > 0,
        "Expected error message but got: " + errorOutput);
  }

  @Test
  void mainWithInvalidConfigFile() throws IOException {
    // Test that main handles ConfigException when config file is invalid
    try (FileWriter writer = new FileWriter(tempConfigFile)) {
      writer.write("invalid config content {{{");
    }

    SecurityManager originalSecurityManager = System.getSecurityManager();
    try {
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
              // Allow everything
            }

            @Override
            public void checkPermission(java.security.Permission perm, Object context) {
              // Allow everything
            }

            @Override
            public void checkExit(int status) {
              throw new SecurityException("System.exit(" + status + ")");
            }
          });

      SecurityException exception =
          assertThrows(
              SecurityException.class,
              () -> TradeReporter.main(new String[] {tempConfigFile.getAbsolutePath()}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String errorOutput = errContent.toString();
    assertTrue(
        errorOutput.contains("error:") || errorOutput.length() > 0,
        "Expected error message but got: " + errorOutput);
  }

  @Test
  void mainWithTsvFlagAndNonExistentConfigFile() {
    // Test that main handles -t flag with non-existent config file
    SecurityManager originalSecurityManager = System.getSecurityManager();
    try {
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
              // Allow everything
            }

            @Override
            public void checkPermission(java.security.Permission perm, Object context) {
              // Allow everything
            }

            @Override
            public void checkExit(int status) {
              throw new SecurityException("System.exit(" + status + ")");
            }
          });

      SecurityException exception =
          assertThrows(
              SecurityException.class,
              () -> TradeReporter.main(new String[] {"-t", "/nonexistent/file.conf"}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String errorOutput = errContent.toString();
    assertTrue(
        errorOutput.contains("error:") || errorOutput.length() > 0,
        "Expected error message but got: " + errorOutput);
  }

  @Test
  void mainWithMissingInstrumentsInConfig() throws IOException {
    // Test config file missing instruments section
    try (FileWriter writer = new FileWriter(tempConfigFile)) {
      writer.write(
          "trade-report {\n"
              + "  address = 127.0.0.1\n"
              + "  port = 6000\n"
              + "  username = test\n"
              + "  password = test\n"
              + "}\n");
    }

    SecurityManager originalSecurityManager = System.getSecurityManager();
    try {
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
              // Allow everything
            }

            @Override
            public void checkPermission(java.security.Permission perm, Object context) {
              // Allow everything
            }

            @Override
            public void checkExit(int status) {
              throw new SecurityException("System.exit(" + status + ")");
            }
          });

      SecurityException exception =
          assertThrows(
              SecurityException.class,
              () -> TradeReporter.main(new String[] {tempConfigFile.getAbsolutePath()}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String errorOutput = errContent.toString();
    assertTrue(
        errorOutput.length() > 0, "Expected error message but got: " + errorOutput);
  }

  @Test
  void mainWithTsvFlagAndInvalidConfigFile() throws IOException {
    // Test that main handles -t flag with invalid config file
    try (FileWriter writer = new FileWriter(tempConfigFile)) {
      writer.write("invalid config content {{{");
    }

    SecurityManager originalSecurityManager = System.getSecurityManager();
    try {
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
              // Allow everything
            }

            @Override
            public void checkPermission(java.security.Permission perm, Object context) {
              // Allow everything
            }

            @Override
            public void checkExit(int status) {
              throw new SecurityException("System.exit(" + status + ")");
            }
          });

      SecurityException exception =
          assertThrows(
              SecurityException.class,
              () -> TradeReporter.main(new String[] {"-t", tempConfigFile.getAbsolutePath()}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String errorOutput = errContent.toString();
    assertTrue(
        errorOutput.contains("error:") || errorOutput.length() > 0,
        "Expected error message but got: " + errorOutput);
  }
}
