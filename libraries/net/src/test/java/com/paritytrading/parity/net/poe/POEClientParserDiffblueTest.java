package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POE.OrderAccepted;
import com.paritytrading.parity.net.poe.POE.OrderCanceled;
import com.paritytrading.parity.net.poe.POE.OrderExecuted;
import com.paritytrading.parity.net.poe.POE.OrderRejected;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class POEClientParserDiffblueTest {

  /**
   * Test {@link POEClientParser#POEClientParser(POEClientListener)}.
   *
   * <ul>
   *   <li>Then wrap array of byte with 'A' and 57 zeros position is 58.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#POEClientParser(POEClientListener)}
   */
  @Test
  @DisplayName(
      "Test new POEClientParser(POEClientListener); then wrap ORDER_ACCEPTED buffer position is 58")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.<init>(POEClientListener)"})
  void testNewPOEClientParser_thenWrapOrderAcceptedBufferPositionIs58() throws IOException {
    // Arrange
    POEClientListener listener = mock(POEClientListener.class);
    doNothing().when(listener).orderAccepted(Mockito.<OrderAccepted>any());

    // Act
    POEClientParser parser = new POEClientParser(listener);
    byte[] bytes = new byte[58];
    bytes[0] = 'A';
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    parser.message(buffer);

    // Assert
    verify(listener).orderAccepted(isA(OrderAccepted.class));
    assertEquals(58, buffer.position());
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)} with ORDER_REJECTED.
   *
   * <ul>
   *   <li>Then wrap array of byte with 'R' and 25 bytes position is 26.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test POEClientParser message(ByteBuffer); then ORDER_REJECTED buffer position is 26")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenOrderRejected_thenBufferPositionIs26() throws IOException {
    // Arrange
    POEClientListener listener = mock(POEClientListener.class);
    doNothing().when(listener).orderRejected(Mockito.<OrderRejected>any());
    POEClientParser parser = new POEClientParser(listener);
    byte[] bytes = new byte[26];
    bytes[0] = 'R';
    ByteBuffer buffer = ByteBuffer.wrap(bytes);

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).orderRejected(isA(OrderRejected.class));
    assertEquals(26, buffer.position());
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)} with ORDER_EXECUTED.
   *
   * <ul>
   *   <li>Then wrap array of byte with 'E' and 45 bytes position is 46.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test POEClientParser message(ByteBuffer); then ORDER_EXECUTED buffer position is 46")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenOrderExecuted_thenBufferPositionIs46() throws IOException {
    // Arrange
    POEClientListener listener = mock(POEClientListener.class);
    doNothing().when(listener).orderExecuted(Mockito.<OrderExecuted>any());
    POEClientParser parser = new POEClientParser(listener);
    byte[] bytes = new byte[46];
    bytes[0] = 'E';
    ByteBuffer buffer = ByteBuffer.wrap(bytes);

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).orderExecuted(isA(OrderExecuted.class));
    assertEquals(46, buffer.position());
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)} with ORDER_CANCELED.
   *
   * <ul>
   *   <li>Then wrap array of byte with 'X' and 33 bytes position is 34.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test POEClientParser message(ByteBuffer); then ORDER_CANCELED buffer position is 34")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenOrderCanceled_thenBufferPositionIs34() throws IOException {
    // Arrange
    POEClientListener listener = mock(POEClientListener.class);
    doNothing().when(listener).orderCanceled(Mockito.<OrderCanceled>any());
    POEClientParser parser = new POEClientParser(listener);
    byte[] bytes = new byte[34];
    bytes[0] = 'X';
    ByteBuffer buffer = ByteBuffer.wrap(bytes);

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).orderCanceled(isA(OrderCanceled.class));
    assertEquals(34, buffer.position());
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)} with unknown message type.
   *
   * <ul>
   *   <li>Then throws {@link POEException}.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test POEClientParser message(ByteBuffer); when unknown type then throws POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenUnknownType_thenThrowsPOEException() {
    // Arrange
    POEClientListener listener = mock(POEClientListener.class);
    POEClientParser parser = new POEClientParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'Z', 1, 2, 3});

    // Act / Assert
    assertThrows(POEException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)} with empty buffer.
   *
   * <ul>
   *   <li>Then throws {@link POEException} for no message type.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test POEClientParser message(ByteBuffer); when empty buffer then throws POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEClientParser.message(ByteBuffer)"})
  void testMessage_whenEmptyBuffer_thenThrowsPOEException() {
    // Arrange
    POEClientListener listener = mock(POEClientListener.class);
    POEClientParser parser = new POEClientParser(listener);
    ByteBuffer buffer = ByteBuffer.allocate(0);

    // Act / Assert
    assertThrows(POEException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link POEClientParser#message(ByteBuffer)} with malformed ORDER_ACCEPTED (too short).
   *
   * <ul>
   *   <li>Then throws {@link POEException} via malformedMessage.
   * </ul>
   *
   * <p>Method under test: {@link POEClientParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test POEClientParser message(ByteBuffer); when malformed ORDER_ACCEPTED then throws POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void POEClientParser.message(ByteBuffer)",
    "void POEClientParser.malformedMessage(byte)"
  })
  void testMessage_whenMalformedOrderAccepted_thenThrowsPOEException() {
    // Arrange
    POEClientListener listener = mock(POEClientListener.class);
    POEClientParser parser = new POEClientParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'A', 1, 2});

    // Act / Assert
    assertThrows(POEException.class, () -> parser.message(buffer));
  }
}
