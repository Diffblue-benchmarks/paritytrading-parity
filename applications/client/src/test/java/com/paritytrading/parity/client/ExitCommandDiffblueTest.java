package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ExitCommandDiffblueTest {
  /**
   * Test {@link ExitCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link ByteArrayInputStream#ByteArrayInputStream(byte[])} with array of {@code byte}
   *       with {@code A} and one.
   * </ul>
   *
   * <p>Method under test: {@link ExitCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when ByteArrayInputStream(byte[]) with array of byte with 'A' and one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExitCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenByteArrayInputStreamWithArrayOfByteWithAAndOne() throws IOException {
    // Arrange
    ExitCommand exitCommand = new ExitCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();
    ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(new byte[] {'A', 1, 'A', 1, -1, 1, 'A', 1});

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> exitCommand.execute(client, new Scanner(byteArrayInputStream)));
  }

  /**
   * Test {@link ExitCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link ByteArrayInputStream#ByteArrayInputStream(byte[])} with array of {@code byte}
   *       with {@code A} and one.
   * </ul>
   *
   * <p>Method under test: {@link ExitCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when ByteArrayInputStream(byte[]) with array of byte with 'A' and one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExitCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenByteArrayInputStreamWithArrayOfByteWithAAndOne2() {
    // Arrange
    ExitCommand exitCommand = new ExitCommand();
    ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(new byte[] {'A', 1, -1, 1, 'A', 1, 'A', 1});

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> exitCommand.execute(null, new Scanner(byteArrayInputStream)));
  }

  /**
   * Test {@link ExitCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link ByteArrayInputStream#ByteArrayInputStream(byte[])} with array of {@code byte}
   *       with minus twenty-four and one.
   * </ul>
   *
   * <p>Method under test: {@link ExitCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when ByteArrayInputStream(byte[]) with array of byte with minus twenty-four and one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExitCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenByteArrayInputStreamWithArrayOfByteWithMinusTwentyFourAndOne() {
    // Arrange
    ExitCommand exitCommand = new ExitCommand();
    ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(new byte[] {-24, 1, -1, 1, 'A', 1, 'A', 1});

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> exitCommand.execute(null, new Scanner(byteArrayInputStream)));
  }

  /**
   * Test {@link ExitCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with createUsername.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link ExitCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with createUsername; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExitCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithCreateUsername_thenThrowIllegalArgumentException()
      throws IOException {
    // Arrange
    ExitCommand exitCommand = new ExitCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> exitCommand.execute(client, new Scanner(TerminalClientTestFactory.createUsername())));
  }

  /**
   * Test {@link ExitCommand#execute(TerminalClient, Scanner)}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with empty string.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link ExitCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner); when Scanner(String) with empty string; then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExitCommand.execute(TerminalClient, Scanner)"})
  void testExecute_whenScannerWithEmptyString_thenDoesNotThrow() throws IOException {
    // Arrange
    ExitCommand exitCommand = new ExitCommand();
    TerminalClient client = TerminalClientTestFactory.createTerminalClient();

    // Act and Assert
    assertDoesNotThrow(() -> exitCommand.execute(client, new Scanner("")));
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
