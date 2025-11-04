package com.paritytrading.parity.net.pmr;

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

class PMRParserDiffblueTest {
  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage2() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class,
        () -> pmrParser.message(ByteBuffer.wrap(new byte[]{1, 'X', 'A', 'X', 'A', 'X', 'A', 'X'})));
  }

  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage3() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap("EXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage4() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap("TXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage5() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap("XXAXAXAX".getBytes("UTF-8"))));
  }

  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage6() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).version(Mockito.<PMR.Version>any());
    PMRParser pmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));

    // Act
    pmrParser.message(buffer);

    // Assert
    verify(listener).version(isA(PMR.Version.class));
    assertEquals(5, buffer.position());
  }

  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage7() throws IOException {
    // Arrange
    PMRParser pmrParser = new PMRParser(mock(PMRListener.class));

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap(new byte[]{})));
  }

  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage8() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderAdded(Mockito.<PMR.OrderAdded>any());
    PMRParser pmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(
        new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    pmrParser.message(buffer);

    // Assert
    verify(listener).orderAdded(isA(PMR.OrderAdded.class));
    assertEquals(17, buffer.position());
  }

  /**
   * Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  void testMessage9() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doThrow(new PMRException("An error occurred")).when(listener).orderAdded(Mockito.<PMR.OrderAdded>any());
    PMRParser pmrParser = new PMRParser(listener);

    // Act and Assert
    assertThrows(PMRException.class, () -> pmrParser.message(ByteBuffer.wrap(
        new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1})));
    verify(listener).orderAdded(isA(PMR.OrderAdded.class));
  }

  /**
   * Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  void testNewPMRParser() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).version(Mockito.<PMR.Version>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));
    actualPmrParser.message(buffer);

    // Assert
    verify(listener).version(isA(PMR.Version.class));
    assertEquals(5, buffer.position());
  }

  /**
   * Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  void testNewPMRParser2() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderAdded(Mockito.<PMR.OrderAdded>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(
        new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1});
    actualPmrParser.message(buffer);

    // Assert
    verify(listener).orderAdded(isA(PMR.OrderAdded.class));
    assertEquals(17, buffer.position());
  }
}
