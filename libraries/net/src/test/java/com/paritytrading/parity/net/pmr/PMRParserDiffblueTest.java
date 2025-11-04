package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmr.PMR.OrderAdded;
import com.paritytrading.parity.net.pmr.PMR.Version;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PMRParserDiffblueTest {
  /**
   * Test {@link PMRParser#PMRParser(PMRListener)}.
   * <ul>
   *   <li>Then wrap array of {@code byte} with {@code A} and one position is seventeen.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  @DisplayName("Test new PMRParser(PMRListener); then wrap array of byte with 'A' and one position is seventeen")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.<init>(PMRListener)"})
  void testNewPMRParser_thenWrapArrayOfByteWithAAndOnePositionIsSeventeen() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderAdded(Mockito.<OrderAdded>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(
        new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1});
    actualPmrParser.message(buffer);

    // Assert
    verify(listener).orderAdded(isA(OrderAdded.class));
    assertEquals(17, buffer.position());
  }

  /**
   * Test {@link PMRParser#PMRParser(PMRListener)}.
   * <ul>
   *   <li>Then wrap {@code VXAXAXAX} Bytes is {@code UTF-8} position is five.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  @DisplayName("Test new PMRParser(PMRListener); then wrap 'VXAXAXAX' Bytes is 'UTF-8' position is five")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.<init>(PMRListener)"})
  void testNewPMRParser_thenWrapVxaxaxaxBytesIsUtf8PositionIsFive() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).version(Mockito.<Version>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));
    actualPmrParser.message(buffer);

    // Assert
    verify(listener).version(isA(Version.class));
    assertEquals(5, buffer.position());
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doThrow(new PMRException("An error occurred")).when(listener).orderAdded(Mockito.<OrderAdded>any());
    PMRParser pmrParser = new PMRParser(listener);

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap(
        new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1})));
    verify(listener).orderAdded(isA(OrderAdded.class));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <ul>
   *   <li>Then wrap array of {@code byte} with {@code A} and one position is seventeen.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); then wrap array of byte with 'A' and one position is seventeen")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage_thenWrapArrayOfByteWithAAndOnePositionIsSeventeen() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderAdded(Mockito.<OrderAdded>any());
    PMRParser pmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(
        new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    pmrParser.message(buffer);

    // Assert
    verify(listener).orderAdded(isA(OrderAdded.class));
    assertEquals(17, buffer.position());
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <ul>
   *   <li>Then wrap {@code VXAXAXAX} Bytes is {@code UTF-8} position is five.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); then wrap 'VXAXAXAX' Bytes is 'UTF-8' position is five")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage_thenWrapVxaxaxaxBytesIsUtf8PositionIsFive() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).version(Mockito.<Version>any());
    PMRParser pmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));

    // Act
    pmrParser.message(buffer);

    // Assert
    verify(listener).version(isA(Version.class));
    assertEquals(5, buffer.position());
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap {@code AXAXAXAX} Bytes is {@code UTF-8}.</li>
   *   <li>Then throw {@link PMRException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap 'AXAXAXAX' Bytes is 'UTF-8'; then throw PMRException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage_whenWrapAxaxaxaxBytesIsUtf8_thenThrowPMRException() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap empty array of {@code byte}.</li>
   *   <li>Then throw {@link PMRException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap empty array of byte; then throw PMRException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage_whenWrapEmptyArrayOfByte_thenThrowPMRException() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap(new byte[]{})));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap {@code EXAXAXAX} Bytes is {@code UTF-8}.</li>
   *   <li>Then throw {@link PMRException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap 'EXAXAXAX' Bytes is 'UTF-8'; then throw PMRException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage_whenWrapExaxaxaxBytesIsUtf8_thenThrowPMRException() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap("EXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap {@code TXAXAXAX} Bytes is {@code UTF-8}.</li>
   *   <li>Then throw {@link PMRException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap 'TXAXAXAX' Bytes is 'UTF-8'; then throw PMRException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage_whenWrapTxaxaxaxBytesIsUtf8_thenThrowPMRException() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap("TXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap {@code XXAXAXAX} Bytes is {@code UTF-8}.</li>
   *   <li>Then throw {@link PMRException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap 'XXAXAXAX' Bytes is 'UTF-8'; then throw PMRException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage_whenWrapXxaxaxaxBytesIsUtf8_thenThrowPMRException() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap("XXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When {@code X}.</li>
   *   <li>Then throw {@link PMRException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when 'X'; then throw PMRException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testMessage_whenX_thenThrowPMRException() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class,
        () -> pmrParser.message(ByteBuffer.wrap(new byte[]{1, 'X', 'A', 'X', 'A', 'X', 'A', 'X'})));
  }
}
