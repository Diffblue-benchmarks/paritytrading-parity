package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrdersCommandDiffblueTest {
  /**
   * Test {@link OrdersCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link ByteArrayInputStream#ByteArrayInputStream(byte[])} with {@code AXAXAXAX}
   *       Bytes is {@code UTF-8}.
   * </ul>
   *
   * <p>Method under test: {@link OrdersCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when ByteArrayInputStream(byte[]) with 'AXAXAXAX' Bytes is 'UTF-8'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrdersCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenByteArrayInputStreamWithAxaxaxaxBytesIsUtf8() throws IOException {
    // Arrange
    OrdersCommand ordersCommand = new OrdersCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    BufferedInputStream bufferedInputStream =
        new BufferedInputStream(new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")), 1);

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> ordersCommand.execute(client, new Scanner(bufferedInputStream)));
  }

  /**
   * Test {@link OrdersCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with createUsername.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link OrdersCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with createUsername; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrdersCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithCreateUsername_thenThrowIllegalArgumentException()
      throws IOException {
    // Arrange
    OrdersCommand ordersCommand = new OrdersCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () ->
            ordersCommand.execute(client, new Scanner(TerminalClientTestFactory.createUsername())));
  }

  /**
   * Test {@link OrdersCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with empty string.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link OrdersCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with empty string; then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrdersCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithEmptyString_thenDoesNotThrow() throws IOException {
    // Arrange
    OrdersCommand ordersCommand = new OrdersCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertDoesNotThrow(() -> ordersCommand.execute(client, new Scanner("")));
  }

  /**
   * Test {@link OrdersCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code foo}.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link OrdersCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'foo'; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrdersCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithFoo_thenThrowIllegalArgumentException() throws IOException {
    // Arrange
    OrdersCommand ordersCommand = new OrdersCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> ordersCommand.execute(client, new Scanner("foo")));
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link OrdersCommand}
   *   <li>{@link OrdersCommand#getDescription()}
   *   <li>{@link OrdersCommand#getName()}
   *   <li>{@link OrdersCommand#getUsage()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OrdersCommand.<init>()",
    "String OrdersCommand.getDescription()",
    "String OrdersCommand.getName()",
    "String OrdersCommand.getUsage()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    OrdersCommand actualOrdersCommand = new OrdersCommand();
    String actualDescription = actualOrdersCommand.getDescription();
    String actualName = actualOrdersCommand.getName();

    // Assert
    assertEquals("Display open orders", actualDescription);
    assertEquals("orders", actualName);
    assertEquals("orders", actualOrdersCommand.getUsage());
  }
}
