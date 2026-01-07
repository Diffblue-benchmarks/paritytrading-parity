package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradesCommandDiffblueTest {
  /**
   * Test {@link TradesCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code Source}.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link TradesCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'Source'; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradesCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithSource_thenThrowIllegalArgumentException() {
    // Arrange
    TradesCommand tradesCommand = new TradesCommand();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> tradesCommand.execute(null, new Scanner("Source")));
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
