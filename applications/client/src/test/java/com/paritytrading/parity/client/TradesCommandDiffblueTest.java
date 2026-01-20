package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradesCommandDiffblueTest {
  /**
   * Test {@link TradesCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>Given createUsername.
   * </ul>
   *
   * <p>Method under test: {@link TradesCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName("Test execute(TerminalClient, Scanner); given createUsername")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradesCommand.execute(TerminalClient, Scanner)"})
  void testExecute_givenCreateUsername() throws IOException {
    // Arrange
    TradesCommand tradesCommand = new TradesCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    Scanner arguments = new Scanner("");
    arguments.useDelimiter(TerminalClientTestFactory.createUsername());

    // Act and Assert
    assertDoesNotThrow(() -> tradesCommand.execute(client, arguments));
  }

  /**
   * Test {@link TradesCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>Given {@code TimestampTimestamp}.
   * </ul>
   *
   * <p>Method under test: {@link TradesCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName("Test execute(TerminalClient, Scanner); given 'TimestampTimestamp'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradesCommand.execute(TerminalClient, Scanner)"})
  void testExecute_givenTimestampTimestamp() throws IOException {
    // Arrange
    TradesCommand tradesCommand = new TradesCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    Scanner arguments = new Scanner("");
    arguments.useDelimiter("TimestampTimestamp");

    // Act and Assert
    assertDoesNotThrow(() -> tradesCommand.execute(client, arguments));
  }

  /**
   * Test {@link TradesCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with createUsername.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link TradesCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with createUsername; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradesCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithCreateUsername_thenThrowIllegalArgumentException()
      throws IOException {
    // Arrange
    TradesCommand tradesCommand = new TradesCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () ->
            tradesCommand.execute(client, new Scanner(TerminalClientTestFactory.createUsername())));
  }

  /**
   * Test {@link TradesCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with empty string.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link TradesCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with empty string; then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradesCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithEmptyString_thenDoesNotThrow() throws IOException {
    // Arrange
    TradesCommand tradesCommand = new TradesCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertDoesNotThrow(() -> tradesCommand.execute(client, new Scanner("")));
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link TradesCommand}
   *   <li>{@link TradesCommand#getDescription()}
   *   <li>{@link TradesCommand#getName()}
   *   <li>{@link TradesCommand#getUsage()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TradesCommand.<init>()",
    "String TradesCommand.getDescription()",
    "String TradesCommand.getName()",
    "String TradesCommand.getUsage()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    TradesCommand actualTradesCommand = new TradesCommand();
    String actualDescription = actualTradesCommand.getDescription();
    String actualName = actualTradesCommand.getName();

    // Assert
    assertEquals("Display occurred trades", actualDescription);
    assertEquals("trades", actualName);
    assertEquals("trades", actualTradesCommand.getUsage());
  }
}
