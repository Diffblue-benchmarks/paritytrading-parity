package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
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
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link CancelCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner) with 'client', 'arguments'; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelCommand.execute(TerminalClient, Scanner)"})
  void testExecuteWithClientArguments_thenThrowIllegalArgumentException() throws IOException {
    // Arrange
    CancelCommand cancelCommand = new CancelCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> cancelCommand.execute(client, new Scanner("")));
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
