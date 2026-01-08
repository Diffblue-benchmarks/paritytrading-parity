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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TradeReporterClaude_mainTest {
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
  void testMainWithZeroArguments_line40() {
    // This test covers line 40: usage(USAGE) when args.length != 1 && args.length != 2
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
          assertThrows(SecurityException.class, () -> TradeReporter.main(new String[] {}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String output = outContent.toString() + errContent.toString();
    assertTrue(
        output.contains("parity-reporter"),
        "Expected usage message containing 'parity-reporter' but got: " + output);
  }

  @Test
  void testMainWithInvalidFlag_line46() {
    // This test covers line 46: usage(USAGE) when args[0] is not "-t"
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
        output.contains("parity-reporter"),
        "Expected usage message containing 'parity-reporter' but got: " + output);
  }

  @Test
  void testMainWithNonExistentConfigFile_lines54and55() throws IOException {
    // This test covers lines 54-55: error(e) for FileNotFoundException
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
              () -> TradeReporter.main(new String[] {"/nonexistent/config/file.conf"}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String errorOutput = errContent.toString();
    assertTrue(
        errorOutput.contains("error:"), "Expected error message but got: " + errorOutput);
  }

  @Test
  void testMainWithInvalidConfigContent_lines54and55() throws IOException {
    // This test covers lines 54-55: error(e) for ConfigException
    try (FileWriter writer = new FileWriter(tempConfigFile)) {
      writer.write("invalid { { { config");
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
        errorOutput.contains("error:"), "Expected error message but got: " + errorOutput);
  }

  @Test
  void testMainWithValidConfigCausingIOException_lines56and57() throws IOException {
    // This test covers lines 56-57: fatal(e) for IOException during main execution
    // Create a valid config that will pass parsing but fail during network connection
    try (FileWriter writer = new FileWriter(tempConfigFile)) {
      writer.write(
          "instruments {\n"
              + "  price-integer-digits = 4\n"
              + "  size-integer-digits = 7\n"
              + "  AAPL {\n"
              + "    price-fraction-digits = 2\n"
              + "    size-fraction-digits = 0\n"
              + "  }\n"
              + "}\n"
              + "trade-report {\n"
              + "  address = \"0.0.0.0\"\n"
              + "  port = 1\n"
              + "  username = \"test\"\n"
              + "  password = \"test\"\n"
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
        errorOutput.contains("fatal:"), "Expected fatal error message but got: " + errorOutput);
  }

  @Test
  void testMainWithTsvFlagAndNonExistentConfig_lines54and55() throws IOException {
    // This test covers lines 54-55 with the -t flag
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
              () -> TradeReporter.main(new String[] {"-t", "/nonexistent/config/file.conf"}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String errorOutput = errContent.toString();
    assertTrue(
        errorOutput.contains("error:"), "Expected error message but got: " + errorOutput);
  }

  @Test
  void testMainWithTsvFlagAndValidConfigCausingIOException_lines56and57() throws IOException {
    // This test covers lines 56-57 with the -t flag
    try (FileWriter writer = new FileWriter(tempConfigFile)) {
      writer.write(
          "instruments {\n"
              + "  price-integer-digits = 4\n"
              + "  size-integer-digits = 7\n"
              + "  AAPL {\n"
              + "    price-fraction-digits = 2\n"
              + "    size-fraction-digits = 0\n"
              + "  }\n"
              + "}\n"
              + "trade-report {\n"
              + "  address = \"0.0.0.0\"\n"
              + "  port = 1\n"
              + "  username = \"test\"\n"
              + "  password = \"test\"\n"
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
              () -> TradeReporter.main(new String[] {"-t", tempConfigFile.getAbsolutePath()}));

      assertTrue(exception.getMessage().contains("System.exit"));
    } finally {
      System.setSecurityManager(originalSecurityManager);
    }

    String errorOutput = errContent.toString();
    assertTrue(
        errorOutput.contains("fatal:"), "Expected fatal error message but got: " + errorOutput);
  }

  @Test
  void testMainWithThreeArguments_line40() {
    // This test covers line 40 with too many arguments
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
        output.contains("parity-reporter"),
        "Expected usage message containing 'parity-reporter' but got: " + output);
  }
}
