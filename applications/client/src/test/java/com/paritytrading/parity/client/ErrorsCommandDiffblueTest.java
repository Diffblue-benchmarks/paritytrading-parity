package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ErrorsCommandDiffblueTest {
  /**
   * Test {@link ErrorsCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code foo}.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link ErrorsCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with 'foo'; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ErrorsCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithFoo_thenThrowIllegalArgumentException() {
    // Arrange
    ErrorsCommand errorsCommand = new ErrorsCommand();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> errorsCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link ErrorsCommand}
   *   <li>{@link ErrorsCommand#getDescription()}
   *   <li>{@link ErrorsCommand#getName()}
   *   <li>{@link ErrorsCommand#getUsage()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ErrorsCommand.<init>()",
    "String ErrorsCommand.getDescription()",
    "String ErrorsCommand.getName()",
    "String ErrorsCommand.getUsage()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    ErrorsCommand actualErrorsCommand = new ErrorsCommand();
    String actualDescription = actualErrorsCommand.getDescription();
    String actualName = actualErrorsCommand.getName();

    // Assert
    assertEquals("Display occurred errors", actualDescription);
    assertEquals("errors", actualName);
    assertEquals("errors", actualErrorsCommand.getUsage());
  }

  /**
   * Test {@link ErrorsCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with empty string.
   *   <li>Then print errors header without exception.
   * </ul>
   *
   * <p>Method under test: {@link ErrorsCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with empty string; then print errors header")
  @Tag("ContributionFromDiffblue")
  @MethodsUnderTest({"void ErrorsCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithEmptyString_thenPrintErrorsHeader() {
    // Arrange
    ErrorsCommand errorsCommand = new ErrorsCommand();
    TerminalClient mockClient = mock(TerminalClient.class);
    when(mockClient.getEvents()).thenReturn(new Events());

    // Act and Assert
    assertDoesNotThrow(() -> errorsCommand.execute(mockClient, new Scanner("")));
  }
}
