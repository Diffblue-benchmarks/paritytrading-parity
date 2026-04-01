package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POE.CancelOrder;
import com.paritytrading.parity.net.poe.POE.EnterOrder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class POEServerParserDiffblueTest {
  /**
   * Test {@link POEServerParser#POEServerParser(POEServerListener)}.
   *
   * <ul>
   *   <li>Then wrap 'EXAXAXAX' Bytes is 'UTF-8' and verify enterOrder is called.
   * </ul>
   *
   * <p>Method under test: {@link POEServerParser#POEServerParser(POEServerListener)}
   */
  @Test
  @DisplayName(
      "Test new POEServerParser(POEServerListener); then wrap EnterOrder bytes and listener.enterOrder is called")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEServerParser.<init>(POEServerListener)"})
  void testNewPOEServerParser_thenEnterOrderListenerCalled() throws IOException {
    // Arrange
    POEServerListener listener = mock(POEServerListener.class);
    doNothing().when(listener).enterOrder(Mockito.<EnterOrder>any());

    // Act
    POEServerParser parser = new POEServerParser(listener);
    // 'E' + 16 bytes orderId + 1 byte side + 8 bytes instrument + 8 bytes quantity + 8 bytes price = 42
    byte[] data = new byte[42];
    data[0] = 'E';
    for (int i = 1; i < 42; i++) {
      data[i] = 'A';
    }
    ByteBuffer buffer = ByteBuffer.wrap(data);
    parser.message(buffer);

    // Assert
    verify(listener).enterOrder(isA(EnterOrder.class));
    assertEquals(42, buffer.position());
  }

  /**
   * Test {@link POEServerParser#POEServerParser(POEServerListener)}.
   *
   * <ul>
   *   <li>Then wrap CancelOrder bytes and listener.cancelOrder is called.
   * </ul>
   *
   * <p>Method under test: {@link POEServerParser#POEServerParser(POEServerListener)}
   */
  @Test
  @DisplayName(
      "Test new POEServerParser(POEServerListener); then wrap CancelOrder bytes and listener.cancelOrder is called")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEServerParser.<init>(POEServerListener)"})
  void testNewPOEServerParser_thenCancelOrderListenerCalled() throws IOException {
    // Arrange
    POEServerListener listener = mock(POEServerListener.class);
    doNothing().when(listener).cancelOrder(Mockito.<CancelOrder>any());

    // Act
    POEServerParser parser = new POEServerParser(listener);
    // 'X' + 16 bytes orderId + 8 bytes quantity = 25
    byte[] data = new byte[25];
    data[0] = 'X';
    for (int i = 1; i < 25; i++) {
      data[i] = 'A';
    }
    ByteBuffer buffer = ByteBuffer.wrap(data);
    parser.message(buffer);

    // Assert
    verify(listener).cancelOrder(isA(CancelOrder.class));
    assertEquals(25, buffer.position());
  }

  /**
   * Test {@link POEServerParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When empty buffer, then throws POEException with no message type.
   * </ul>
   *
   * <p>Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test POEServerParser message(ByteBuffer); when empty buffer then throws POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEServerParser.message(ByteBuffer)"})
  void testMessage_whenEmptyBuffer_thenThrowsPOEException() throws IOException {
    // Arrange
    POEServerListener listener = mock(POEServerListener.class);
    POEServerParser parser = new POEServerParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[0]);

    // Act and Assert
    assertThrows(POEException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link POEServerParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When unknown message type, then throws POEException.
   * </ul>
   *
   * <p>Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test POEServerParser message(ByteBuffer); when unknown message type then throws POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEServerParser.message(ByteBuffer)"})
  void testMessage_whenUnknownMessageType_thenThrowsPOEException() throws IOException {
    // Arrange
    POEServerListener listener = mock(POEServerListener.class);
    POEServerParser parser = new POEServerParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] { 'Z' });

    // Act and Assert
    assertThrows(POEException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link POEServerParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When EnterOrder message too short, then throws POEException (malformed).
   * </ul>
   *
   * <p>Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test POEServerParser message(ByteBuffer); when EnterOrder message too short then throws POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEServerParser.message(ByteBuffer)"})
  void testMessage_whenEnterOrderTooShort_thenThrowsPOEException() throws IOException {
    // Arrange
    POEServerListener listener = mock(POEServerListener.class);
    POEServerParser parser = new POEServerParser(listener);
    // Only 5 bytes, less than MESSAGE_LENGTH_ENTER_ORDER=42
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] { 'E', 'A', 'A', 'A', 'A' });

    // Act and Assert
    assertThrows(POEException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link POEServerParser#message(ByteBuffer)}.
   *
   * <ul>
   *   <li>When CancelOrder message too short, then throws POEException (malformed).
   * </ul>
   *
   * <p>Method under test: {@link POEServerParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test POEServerParser message(ByteBuffer); when CancelOrder message too short then throws POEException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void POEServerParser.message(ByteBuffer)"})
  void testMessage_whenCancelOrderTooShort_thenThrowsPOEException() throws IOException {
    // Arrange
    POEServerListener listener = mock(POEServerListener.class);
    POEServerParser parser = new POEServerParser(listener);
    // Only 5 bytes, less than MESSAGE_LENGTH_CANCEL_ORDER=25
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] { 'X', 'A', 'A', 'A', 'A' });

    // Act and Assert
    assertThrows(POEException.class, () -> parser.message(buffer));
  }
}
