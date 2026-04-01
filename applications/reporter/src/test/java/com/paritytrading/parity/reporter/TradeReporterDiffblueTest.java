package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradeReporterDiffblueTest {

  /**
   * Test {@link TradeReporter#main(String[])}.
   *
   * <ul>
   *   <li>Given no arguments.
   *   <li>Then calls usage and exits.
   * </ul>
   *
   * <p>Method under test: {@link TradeReporter#main(String[])}
   */
  @Test
  @Disabled("Calls System.exit() via usage(); cannot intercept JVM exit in JDK 17+")
  @DisplayName("Test main(String[]); given no args; then calls usage()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeReporter.main(String[])"})
  void testMain_givenNoArgs_thenCallsUsage() {
    // Arrange and Act
    TradeReporter.main(new String[]{});
  }

  /**
   * Test {@link TradeReporter#main(String[])}.
   *
   * <ul>
   *   <li>Given three arguments.
   *   <li>Then calls usage and exits.
   * </ul>
   *
   * <p>Method under test: {@link TradeReporter#main(String[])}
   */
  @Test
  @Disabled("Calls System.exit() via usage(); cannot intercept JVM exit in JDK 17+")
  @DisplayName("Test main(String[]); given three args; then calls usage()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeReporter.main(String[])"})
  void testMain_givenThreeArgs_thenCallsUsage() {
    // Arrange and Act
    TradeReporter.main(new String[]{"a", "b", "c"});
  }

  /**
   * Test {@link TradeReporter#main(String[])}.
   *
   * <ul>
   *   <li>Given two args where first arg is not "-t".
   *   <li>Then calls usage and exits.
   * </ul>
   *
   * <p>Method under test: {@link TradeReporter#main(String[])}
   */
  @Test
  @Disabled("Calls System.exit() via usage(); cannot intercept JVM exit in JDK 17+")
  @DisplayName("Test main(String[]); given two args with invalid flag; then calls usage()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeReporter.main(String[])"})
  void testMain_givenTwoArgsWithInvalidFlag_thenCallsUsage() {
    // Arrange and Act
    TradeReporter.main(new String[]{"invalid", "file.conf"});
  }

  /**
   * Test {@link TradeReporter#main(String[])}.
   *
   * <ul>
   *   <li>Given a non-existent config file path.
   *   <li>Then calls error() and exits via FileNotFoundException handling.
   * </ul>
   *
   * <p>Method under test: {@link TradeReporter#main(String[])}
   */
  @Test
  @Disabled("Calls System.exit() via error(); cannot intercept JVM exit in JDK 17+")
  @DisplayName("Test main(String[]); given non-existent config file; then calls error()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeReporter.main(String[])"})
  void testMain_givenNonExistentConfigFile_thenCallsError() {
    // Arrange and Act
    TradeReporter.main(new String[]{"non-existent-config.conf"});
  }

  /**
   * Test {@link TradeReporter#main(Config, boolean)}.
   *
   * <ul>
   *   <li>Given SoupBinTCP config (no multicast-interface).
   *   <li>Then throws IOException when connecting fails.
   * </ul>
   *
   * <p>Method under test: {@link TradeReporter#main(Config, boolean)}
   */
  @Test
  @DisplayName("Test main(Config, boolean); given SoupBinTCP config; then throws IOException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeReporter.main(Config, boolean)"})
  void testMainConfig_givenSoupBinTcpConfig_thenThrowsIOException() throws Exception {
    // Arrange
    Config config = ConfigFactory.parseString(
        "instruments {\n"
        + "  price-integer-digits = 4\n"
        + "  size-integer-digits = 4\n"
        + "}\n"
        + "trade-report {\n"
        + "  address = 127.0.0.1\n"
        + "  port = 19999\n"
        + "  username = testuser\n"
        + "  password = testpass\n"
        + "}\n");
    Method method = TradeReporter.class.getDeclaredMethod("main", Config.class, boolean.class);
    method.setAccessible(true);

    // Act and Assert
    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, config, false);
    });
    assert ex.getCause() instanceof IOException;
  }

  /**
   * Test {@link TradeReporter#main(Config, boolean)}.
   *
   * <ul>
   *   <li>Given SoupBinTCP config with tsv flag enabled.
   *   <li>Then throws IOException when connecting fails.
   * </ul>
   *
   * <p>Method under test: {@link TradeReporter#main(Config, boolean)}
   */
  @Test
  @DisplayName("Test main(Config, boolean); given SoupBinTCP config and tsv true; then throws IOException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeReporter.main(Config, boolean)"})
  void testMainConfig_givenSoupBinTcpConfigAndTsvTrue_thenThrowsIOException() throws Exception {
    // Arrange
    Config config = ConfigFactory.parseString(
        "instruments {\n"
        + "  price-integer-digits = 4\n"
        + "  size-integer-digits = 4\n"
        + "}\n"
        + "trade-report {\n"
        + "  address = 127.0.0.1\n"
        + "  port = 19998\n"
        + "  username = testuser\n"
        + "  password = testpass\n"
        + "}\n");
    Method method = TradeReporter.class.getDeclaredMethod("main", Config.class, boolean.class);
    method.setAccessible(true);

    // Act and Assert
    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, config, true);
    });
    assert ex.getCause() instanceof IOException;
  }
}
