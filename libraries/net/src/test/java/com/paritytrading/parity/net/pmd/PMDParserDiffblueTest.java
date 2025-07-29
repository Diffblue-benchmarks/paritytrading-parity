package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmd.PMD.Version;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PMDParserDiffblueTest {
  /**
   * Test {@link PMDParser#PMDParser(PMDListener)}.
   *
   * <ul>
   *   <li>Then wrap {@code VXAXAXAX} Bytes is {@code UTF-8} position is five.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#PMDParser(PMDListener)}
   */
  @Test
  @DisplayName(
      "Test new PMDParser(PMDListener); then wrap 'VXAXAXAX' Bytes is 'UTF-8' position is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.<init>(PMDListener)"})
  void testNewPMDParser_thenWrapVxaxaxaxBytesIsUtf8PositionIsFive() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).version(Mockito.<Version>any());

    // Act
    PMDParser actualPmdParser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));
    actualPmdParser.message(buffer);

    // Assert
    verify(listener).version(isA(Version.class));
    assertEquals(5, buffer.position());
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>Given {@link PMDListener} {@link PMDListener#version(Version)} throw {@link
   *       PMDException#PMDException(String)} with message is {@code An error occurred}.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test message(ByteBuffer); given PMDListener version(Version) throw PMDException(String) with message is 'An error occurred'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testMessage_givenPMDListenerVersionThrowPMDExceptionWithMessageIsAnErrorOccurred()
      throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doThrow(new PMDException("An error occurred")).when(listener).version(Mockito.<Version>any());
    PMDParser pmdParser = new PMDParser(listener);

    // Act and Assert
    assertThrows(
        PMDException.class, () -> pmdParser.message(ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"))));
    verify(listener).version(isA(Version.class));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then wrap {@code VXAXAXAX} Bytes is {@code UTF-8} position is five.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); then wrap 'VXAXAXAX' Bytes is 'UTF-8' position is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testMessage_thenWrapVxaxaxaxBytesIsUtf8PositionIsFive() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).version(Mockito.<Version>any());
    PMDParser pmdParser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));

    // Act
    pmdParser.message(buffer);

    // Assert
    verify(listener).version(isA(Version.class));
    assertEquals(5, buffer.position());
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When {@code A}.
   *   <li>Then throw {@link PMDException}.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when 'A'; then throw PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testMessage_whenA_thenThrowPMDException() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(
        PMDException.class,
        () ->
            pmdParser.message(ByteBuffer.wrap(new byte[] {1, 'X', 'A', 'X', 'A', 'X', 'A', 'X'})));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap {@code AXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then throw {@link PMDException}.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test message(ByteBuffer); when wrap 'AXAXAXAX' Bytes is 'UTF-8'; then throw PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testMessage_whenWrapAxaxaxaxBytesIsUtf8_thenThrowPMDException() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(
        PMDException.class, () -> pmdParser.message(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap empty array of {@code byte}.
   *   <li>Then throw {@link PMDException}.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap empty array of byte; then throw PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testMessage_whenWrapEmptyArrayOfByte_thenThrowPMDException() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(PMDException.class, () -> pmdParser.message(ByteBuffer.wrap(new byte[] {})));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap {@code EXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then throw {@link PMDException}.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test message(ByteBuffer); when wrap 'EXAXAXAX' Bytes is 'UTF-8'; then throw PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testMessage_whenWrapExaxaxaxBytesIsUtf8_thenThrowPMDException() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(
        PMDException.class, () -> pmdParser.message(ByteBuffer.wrap("EXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap {@code XXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then throw {@link PMDException}.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test message(ByteBuffer); when wrap 'XXAXAXAX' Bytes is 'UTF-8'; then throw PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testMessage_whenWrapXxaxaxaxBytesIsUtf8_thenThrowPMDException() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(
        PMDException.class, () -> pmdParser.message(ByteBuffer.wrap("XXAXAXAX".getBytes("UTF-8"))));
  }
}
