package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class HelpCommandDiffblueTest {
  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>Then not {@link Scanner#Scanner(String)} with createUsername hasNext.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); then not Scanner(String) with createUsername hasNext")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_thenNotScannerWithCreateUsernameHasNext() throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    Scanner arguments = new Scanner(TerminalClientTestFactory.createUsername());

    // Act
    helpCommand.execute(client, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code buy}.
   *   <li>Then not {@link Scanner#Scanner(String)} with {@code buy} hasNext.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'buy'; then not Scanner(String) with 'buy' hasNext")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithBuy_thenNotScannerWithBuyHasNext() throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    Scanner arguments = new Scanner("buy");

    // Act
    helpCommand.execute(client, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code buytrades}.
   *   <li>Then not {@link Scanner#Scanner(String)} with {@code buytrades} hasNext.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'buytrades'; then not Scanner(String) with 'buytrades' hasNext")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithBuytrades_thenNotScannerWithBuytradesHasNext()
      throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    Scanner arguments = new Scanner("buytrades");

    // Act
    helpCommand.execute(client, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code cancel}.
   *   <li>Then not {@link Scanner#Scanner(String)} with {@code cancel} hasNext.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'cancel'; then not Scanner(String) with 'cancel' hasNext")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithCancel_thenNotScannerWithCancelHasNext() throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    Scanner arguments = new Scanner("cancel");

    // Act
    helpCommand.execute(client, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with empty string.
   *   <li>Then not {@link Scanner#Scanner(String)} with empty string hasNext.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with empty string; then not Scanner(String) with empty string hasNext")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithEmptyString_thenNotScannerWithEmptyStringHasNext()
      throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    Scanner arguments = new Scanner("");

    // Act
    helpCommand.execute(client, arguments);

    // Assert that nothing has changed
    assertFalse(arguments.hasNext());
  }

  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code help}.
   *   <li>Then not {@link Scanner#Scanner(String)} with {@code help} hasNext.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'help'; then not Scanner(String) with 'help' hasNext")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithHelp_thenNotScannerWithHelpHasNext() throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    Scanner arguments = new Scanner("help");

    // Act
    helpCommand.execute(client, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code orders}.
   *   <li>Then not {@link Scanner#Scanner(String)} with {@code orders} hasNext.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'orders'; then not Scanner(String) with 'orders' hasNext")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithOrders_thenNotScannerWithOrdersHasNext() throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    Scanner arguments = new Scanner("orders");

    // Act
    helpCommand.execute(client, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code s %s}.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 's %s'; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithSS_thenThrowIllegalArgumentException() throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> helpCommand.execute(client, new Scanner("s  %s\n")));
  }

  /**
   * Test {@link HelpCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code testuser}.
   *   <li>Then not {@link Scanner#Scanner(String)} with {@code testuser} hasNext.
   * </ul>
   *
   * <p>Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'testuser'; then not Scanner(String) with 'testuser' hasNext")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HelpCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithTestuser_thenNotScannerWithTestuserHasNext() throws IOException {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    Scanner arguments = new Scanner("testuser");

    // Act
    helpCommand.execute(client, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link HelpCommand}
   *   <li>{@link HelpCommand#getDescription()}
   *   <li>{@link HelpCommand#getName()}
   *   <li>{@link HelpCommand#getUsage()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void HelpCommand.<init>()",
    "String HelpCommand.getDescription()",
    "String HelpCommand.getName()",
    "String HelpCommand.getUsage()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    HelpCommand actualHelpCommand = new HelpCommand();
    String actualDescription = actualHelpCommand.getDescription();
    String actualName = actualHelpCommand.getName();

    // Assert
    assertEquals("Display the help", actualDescription);
    assertEquals("help [command]", actualHelpCommand.getUsage());
    assertEquals("help", actualName);
  }
}
