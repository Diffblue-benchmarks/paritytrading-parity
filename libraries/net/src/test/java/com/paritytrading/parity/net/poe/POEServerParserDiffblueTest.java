package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class POEServerParserDiffblueTest {
  /**
   * Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  void testMessage() throws IOException {
    // Arrange
    POEServerParser poeServerParser = new POEServerParser(mock(POEServerListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeServerParser.message(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  void testMessage2() throws IOException {
    // Arrange
    POEServerParser poeServerParser = new POEServerParser(mock(POEServerListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeServerParser.message(ByteBuffer.wrap("EXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  void testMessage3() throws IOException {
    // Arrange
    POEServerParser poeServerParser = new POEServerParser(mock(POEServerListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeServerParser.message(ByteBuffer.wrap("XXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  void testMessage4() throws IOException {
    // Arrange
    POEServerParser poeServerParser = new POEServerParser(mock(POEServerListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeServerParser.message(ByteBuffer.wrap(new byte[]{})));
  }
}
