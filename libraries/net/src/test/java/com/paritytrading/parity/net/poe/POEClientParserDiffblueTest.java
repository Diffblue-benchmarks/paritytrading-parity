package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class POEClientParserDiffblueTest {
  /**
   * Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  void testMessage() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeClientParser.message(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  void testMessage2() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(POEException.class,
        () -> poeClientParser.message(ByteBuffer.wrap(new byte[]{1, 'X', 'A', 'X', 'A', 'X', 'A', 'X'})));
  }

  /**
   * Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  void testMessage3() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeClientParser.message(ByteBuffer.wrap("EXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  void testMessage4() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeClientParser.message(ByteBuffer.wrap("RXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  void testMessage5() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeClientParser.message(ByteBuffer.wrap("XXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  void testMessage6() throws IOException {
    // Arrange
    POEClientParser poeClientParser = new POEClientParser(mock(POEClientListener.class));

    // Act and Assert
    assertThrows(POEException.class, () -> poeClientParser.message(ByteBuffer.wrap(new byte[]{})));
  }
}
