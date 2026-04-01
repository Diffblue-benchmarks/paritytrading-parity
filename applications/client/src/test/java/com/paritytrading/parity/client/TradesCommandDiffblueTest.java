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

class TradesCommandDiffblueTest {
  /**
   * Test {@link TradesCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code foo}.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link TradesCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'foo'; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradesCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithFoo_thenThrowIllegalArgumentException() {
    // Arrange
    TradesCommand tradesCommand = new TradesCommand();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> tradesCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Test {@link TradesCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with empty string.
   *   <li>Then invoke {@link TerminalClient#getInstruments()} and {@link TerminalClient#getEvents()}.
   * </ul>
   *
   * <p>Method under test: {@link TradesCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with empty string; then invoke getInstruments and getEvents")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradesCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerEmpty_thenInvokeGetInstrumentsAndGetEvents() {
    // Arrange
    TradesCommand tradesCommand = new TradesCommand();
    TerminalClient client = mock(TerminalClient.class);
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(10);
    when(instruments.getSizeWidth()).thenReturn(10);
    when(client.getInstruments()).thenReturn(instruments);
    when(client.getEvents()).thenReturn(new Events());

    // Act
    tradesCommand.execute(client, new Scanner(""));

    // Assert
    verify(client).getInstruments();
    verify(client).getEvents();
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
