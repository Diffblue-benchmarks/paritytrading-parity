package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class POEServerParserDiffblueTest {
  /**
   * Test {@link POEServerParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap {@code AXAXAXAX} Bytes is {@code UTF-8}.</li>
   *   <li>Then throw {@link POEException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap 'AXAXAXAX' Bytes is 'UTF-8'; then throw POEException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void POEServerParser.message(ByteBuffer)"})
  void testMessage_whenWrapAxaxaxaxBytesIsUtf8_thenThrowPOEException() throws IOException {
    // Arrange
    POEServerParser poeServerParser = new POEServerParser(mock(POEServerListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeServerParser.message(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link POEServerParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap empty array of {@code byte}.</li>
   *   <li>Then throw {@link POEException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap empty array of byte; then throw POEException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void POEServerParser.message(ByteBuffer)"})
  void testMessage_whenWrapEmptyArrayOfByte_thenThrowPOEException() throws IOException {
    // Arrange
    POEServerParser poeServerParser = new POEServerParser(mock(POEServerListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeServerParser.message(ByteBuffer.wrap(new byte[]{})));
  }

  /**
   * Test {@link POEServerParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap {@code EXAXAXAX} Bytes is {@code UTF-8}.</li>
   *   <li>Then throw {@link POEException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap 'EXAXAXAX' Bytes is 'UTF-8'; then throw POEException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void POEServerParser.message(ByteBuffer)"})
  void testMessage_whenWrapExaxaxaxBytesIsUtf8_thenThrowPOEException() throws IOException {
    // Arrange
    POEServerParser poeServerParser = new POEServerParser(mock(POEServerListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeServerParser.message(ByteBuffer.wrap("EXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Test {@link POEServerParser#message(ByteBuffer)}.
   * <ul>
   *   <li>When wrap {@code XXAXAXAX} Bytes is {@code UTF-8}.</li>
   *   <li>Then throw {@link POEException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test message(ByteBuffer); when wrap 'XXAXAXAX' Bytes is 'UTF-8'; then throw POEException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void POEServerParser.message(ByteBuffer)"})
  void testMessage_whenWrapXxaxaxaxBytesIsUtf8_thenThrowPOEException() throws IOException {
    // Arrange
    POEServerParser poeServerParser = new POEServerParser(mock(POEServerListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeServerParser.message(ByteBuffer.wrap("XXAXAXAX".getBytes("UTF-8"))));
  }
}
