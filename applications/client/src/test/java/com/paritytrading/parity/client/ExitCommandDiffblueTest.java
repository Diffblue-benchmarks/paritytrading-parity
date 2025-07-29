package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ExitCommandDiffblueTest {
  /**
   * Test {@link ExitCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code foo}.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link ExitCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'foo'; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExitCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithFoo_thenThrowIllegalArgumentException() {
    // Arrange
    ExitCommand exitCommand = new ExitCommand();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> exitCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link ExitCommand}
   *   <li>{@link ExitCommand#getDescription()}
   *   <li>{@link ExitCommand#getName()}
   *   <li>{@link ExitCommand#getUsage()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ExitCommand.<init>()",
    "String ExitCommand.getDescription()",
    "String ExitCommand.getName()",
    "String ExitCommand.getUsage()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    ExitCommand actualExitCommand = new ExitCommand();
    String actualDescription = actualExitCommand.getDescription();
    String actualName = actualExitCommand.getName();

    // Assert
    assertEquals("Exit the client", actualDescription);
    assertEquals("exit", actualName);
    assertEquals("exit", actualExitCommand.getUsage());
  }
}
