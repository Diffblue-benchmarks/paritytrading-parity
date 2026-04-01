package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.util.Instruments;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrdersCommandDiffblueTest {
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
  void testExecute_whenScannerWithFoo_thenThrowIllegalArgumentException() {
    // Arrange
    OrdersCommand ordersCommand = new OrdersCommand();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> ordersCommand.execute(null, new Scanner("foo")));
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

  /**
   * Test {@link OrdersCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with empty string.
   *   <li>Then complete without throwing an exception.
   * </ul>
   *
   * <p>Method under test: {@link OrdersCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with empty string; then complete successfully")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrdersCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithEmptyString_thenCompleteSuccessfully() {
    // Arrange
    OrdersCommand ordersCommand = new OrdersCommand();
    TerminalClient client = mock(TerminalClient.class);
    Instruments instruments = mock(Instruments.class);
    Events events = new Events();

    when(client.getInstruments()).thenReturn(instruments);
    when(client.getEvents()).thenReturn(events);
    when(instruments.getPriceWidth()).thenReturn(10);
    when(instruments.getSizeWidth()).thenReturn(10);

    // Act
    ordersCommand.execute(client, new Scanner(""));

    // Assert
    verify(client).getInstruments();
    verify(client).getEvents();
  }
}
