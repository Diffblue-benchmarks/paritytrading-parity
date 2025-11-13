package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class POEClientParserDiffblueTest {
  /**
   * Test {@link POEClientParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When {@code A}.
   *   <li>Then throw {@link POEException}.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when 'A'; then throw POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenA_thenThrowPOEException() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(
        POEException.class,
        () ->
            poeClientParser.message(
                ByteBuffer.wrap(new byte[] {1, 'X', 'A', 'X', 'A', 'X', 'A', 'X'})));
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap {@code AXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then throw {@link POEException}.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test message(ByteBuffer); when wrap 'AXAXAXAX' Bytes is 'UTF-8'; then throw POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenWrapAxaxaxaxBytesIsUtf8_thenThrowPOEException() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(
        POEException.class,
        () -> poeClientParser.message(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap empty array of {@code byte}.
   *   <li>Then throw {@link POEException}.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap empty array of byte; then throw POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenWrapEmptyArrayOfByte_thenThrowPOEException() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeClientParser.message(ByteBuffer.wrap(new byte[] {})));
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap {@code EXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then throw {@link POEException}.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test message(ByteBuffer); when wrap 'EXAXAXAX' Bytes is 'UTF-8'; then throw POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenWrapExaxaxaxBytesIsUtf8_thenThrowPOEException() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(
        POEException.class,
        () -> poeClientParser.message(ByteBuffer.wrap("EXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap {@code RXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then throw {@link POEException}.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test message(ByteBuffer); when wrap 'RXAXAXAX' Bytes is 'UTF-8'; then throw POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenWrapRxaxaxaxBytesIsUtf8_thenThrowPOEException() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(
        POEException.class,
        () -> poeClientParser.message(ByteBuffer.wrap("RXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap {@code XXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then throw {@link POEException}.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test message(ByteBuffer); when wrap 'XXAXAXAX' Bytes is 'UTF-8'; then throw POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenWrapXxaxaxaxBytesIsUtf8_thenThrowPOEException() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(
        POEException.class,
        () -> poeClientParser.message(ByteBuffer.wrap("XXAXAXAX".getBytes("UTF-8"))));
  }
}
