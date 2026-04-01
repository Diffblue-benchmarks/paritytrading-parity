package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmd.PMD.OrderAdded;
import com.paritytrading.parity.net.pmd.PMD.OrderCanceled;
import com.paritytrading.parity.net.pmd.PMD.OrderExecuted;
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
   *   <li>When empty buffer then throws PMDException.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when empty buffer then throws PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenEmptyBuffer_thenThrowsPMDException() {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[0]);

    // Act and Assert
    assertThrows(PMDException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When Version message too short then throws PMDException.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when Version too short then throws PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenVersionTooShort_thenThrowsPMDException() {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'V', 1});

    // Act and Assert
    assertThrows(PMDException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When OrderAdded message then listener orderAdded is called.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when OrderAdded message then listener orderAdded called")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenOrderAdded_thenListenerOrderAddedCalled() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).orderAdded(Mockito.<OrderAdded>any());
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAXAXAXAXAXAXAXAXAXAXAXAXAXAXAXAXAXAX".getBytes("UTF-8"));

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).orderAdded(isA(OrderAdded.class));
    assertEquals(42, buffer.position());
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When OrderAdded message too short then throws PMDException.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when OrderAdded too short then throws PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenOrderAddedTooShort_thenThrowsPMDException() {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'A', 1, 2});

    // Act and Assert
    assertThrows(PMDException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When OrderExecuted message then listener orderExecuted is called.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when OrderExecuted message then listener orderExecuted called")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenOrderExecuted_thenListenerOrderExecutedCalled() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).orderExecuted(Mockito.<OrderExecuted>any());
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("EXAXAXAXAXAXAXAXAXAXAXAXAXAXAX".getBytes("UTF-8"));

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).orderExecuted(isA(OrderExecuted.class));
    assertEquals(29, buffer.position());
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When OrderExecuted message too short then throws PMDException.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when OrderExecuted too short then throws PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenOrderExecutedTooShort_thenThrowsPMDException() {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'E', 1, 2});

    // Act and Assert
    assertThrows(PMDException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When OrderCanceled message then listener orderCanceled is called.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when OrderCanceled message then listener orderCanceled called")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenOrderCanceled_thenListenerOrderCanceledCalled() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).orderCanceled(Mockito.<OrderCanceled>any());
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("XAXAXAXAXAXAXAXAXAXAXAXAX".getBytes("UTF-8"));

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).orderCanceled(isA(OrderCanceled.class));
    assertEquals(25, buffer.position());
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When OrderCanceled message too short then throws PMDException.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when OrderCanceled too short then throws PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenOrderCanceledTooShort_thenThrowsPMDException() {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'X', 1, 2});

    // Act and Assert
    assertThrows(PMDException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMDParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When unknown message type then throws PMDException.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMDParser message(ByteBuffer); when unknown message type then throws PMDException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.message(ByteBuffer)"})
  void testPMDParserMessage_whenUnknownMessageType_thenThrowsPMDException() {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    PMDParser parser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'Z', 1, 2, 3, 4});

    // Act and Assert
    assertThrows(PMDException.class, () -> parser.message(buffer));
  }
}
