package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PMDParserDiffblueTest {
  /**
   * Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  void testMessage() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(PMDException.class, () -> pmdParser.message(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  void testMessage2() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(PMDException.class,
        () -> pmdParser.message(ByteBuffer.wrap(new byte[]{1, 'X', 'A', 'X', 'A', 'X', 'A', 'X'})));
  }

  /**
   * Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  void testMessage3() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(PMDException.class, () -> pmdParser.message(ByteBuffer.wrap("EXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  void testMessage4() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).version(Mockito.<PMD.Version>any());
    PMDParser pmdParser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));

    // Act
    pmdParser.message(buffer);

    // Assert
    verify(listener).version(isA(PMD.Version.class));
    assertEquals(5, buffer.position());
  }

  /**
   * Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  void testMessage5() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(PMDException.class, () -> pmdParser.message(ByteBuffer.wrap("XXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  void testMessage6() throws IOException {
    // Arrange
    PMDParser pmdParser = new PMDParser(mock(PMDListener.class));

    // Act and Assert
    assertThrows(PMDException.class, () -> pmdParser.message(ByteBuffer.wrap(new byte[]{})));
  }

  /**
   * Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  void testMessage7() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doThrow(new PMDException("An error occurred")).when(listener).version(Mockito.<PMD.Version>any());
    PMDParser pmdParser = new PMDParser(listener);

    // Act and Assert
    assertThrows(PMDException.class, () -> pmdParser.message(ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"))));
    verify(listener).version(isA(PMD.Version.class));
  }

  /**
   * Method under test: {@link PMDParser#PMDParser(PMDListener)}
   */
  @Test
  void testNewPMDParser() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).version(Mockito.<PMD.Version>any());

    // Act
    PMDParser actualPmdParser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));
    actualPmdParser.message(buffer);

    // Assert
    verify(listener).version(isA(PMD.Version.class));
    assertEquals(5, buffer.position());
  }
}
