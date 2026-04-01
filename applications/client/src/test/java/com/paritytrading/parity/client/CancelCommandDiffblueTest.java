package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POE;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class CancelCommandDiffblueTest {
  /**
   * Test new {@link CancelCommand} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link CancelCommand}
   */
  @Test
  @DisplayName("Test new CancelCommand (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelCommand.<init>()"})
  void testNewCancelCommand() {
    // Arrange and Act
    CancelCommand actualCancelCommand = new CancelCommand();

    // Assert
    assertEquals("Cancel an order", actualCancelCommand.getDescription());
    assertEquals("cancel <order-id>", actualCancelCommand.getUsage());
    assertEquals("cancel", actualCancelCommand.getName());
  }

  /**
   * Test {@link CancelCommand#execute(TerminalClient, Scanner)} with {@code client}, {@code
   * arguments}.
   *
   * <ul>
   *   <li>Given empty string.
   * </ul>
   *
   * <p>Method under test: {@link CancelCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner) with 'client', 'arguments'; given empty string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelCommand.execute(TerminalClient, Scanner)"})
  void testExecuteWithClientArguments_givenEmptyString() throws IOException {
    // Arrange
    CancelCommand cancelCommand = new CancelCommand();

    Scanner arguments = new Scanner("foo");
    arguments.useDelimiter("");

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> cancelCommand.execute(null, arguments));
  }

  /**
   * Test {@link CancelCommand#execute(TerminalClient, Scanner)} with {@code client}, {@code
   * arguments}.
   *
   * <ul>
   *   <li>Given {@code foo}.
   *   <li>When {@link Scanner#Scanner(String)} with {@code foo} useDelimiter {@code foo}.
   * </ul>
   *
   * <p>Method under test: {@link CancelCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner) with 'client', 'arguments'; given 'foo'; when Scanner(String) with 'foo' useDelimiter 'foo'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelCommand.execute(TerminalClient, Scanner)"})
  void testExecuteWithClientArguments_givenFoo_whenScannerWithFooUseDelimiterFoo()
      throws IOException {
    // Arrange
    CancelCommand cancelCommand = new CancelCommand();

    Scanner arguments = new Scanner("foo");
    arguments.useDelimiter("foo");

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> cancelCommand.execute(null, arguments));
  }

  /**
   * Test {@link CancelCommand#execute(TerminalClient, Scanner)} with {@code client}, {@code
   * arguments}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with single token; then send cancel order.
   * </ul>
   *
   * <p>Method under test: {@link CancelCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner) with 'client', 'arguments'; when Scanner with single token; then send cancel order")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelCommand.execute(TerminalClient, Scanner)"})
  void testExecuteWithClientArguments_whenScannerWithSingleToken_thenSendCancelOrder()
      throws IOException {
    // Arrange
    CancelCommand cancelCommand = new CancelCommand();
    OrderEntry orderEntry = mock(OrderEntry.class);
    TerminalClient client = mock(TerminalClient.class);
    when(client.getOrderEntry()).thenReturn(orderEntry);

    // Act
    cancelCommand.execute(client, new Scanner("ORDER1"));

    // Assert
    verify(orderEntry).send(any(POE.InboundMessage.class));
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link CancelCommand#getDescription()}
   *   <li>{@link CancelCommand#getName()}
   *   <li>{@link CancelCommand#getUsage()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "String CancelCommand.getDescription()",
    "String CancelCommand.getName()",
    "String CancelCommand.getUsage()"
  })
  void testGettersAndSetters() {
    // Arrange
    CancelCommand cancelCommand = new CancelCommand();

    // Act
    String actualDescription = cancelCommand.getDescription();
    String actualName = cancelCommand.getName();

    // Assert
    assertEquals("Cancel an order", actualDescription);
    assertEquals("cancel <order-id>", cancelCommand.getUsage());
    assertEquals("cancel", actualName);
  }
}
