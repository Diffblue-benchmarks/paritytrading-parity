package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmr.PMR.OrderAdded;
import com.paritytrading.parity.net.pmr.PMR.OrderCanceled;
import com.paritytrading.parity.net.pmr.PMR.OrderEntered;
import com.paritytrading.parity.net.pmr.PMR.Trade;
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
   *
   * <ul>
   *   <li>Then wrap array of {@code byte} with {@code A} and one position is seventeen.
   * </ul>
   *
   * <p>Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  @DisplayName(
      "Test new PMRParser(PMRListener); then wrap array of byte with 'A' and one position is seventeen")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.<init>(PMRListener)"})
  void testNewPMRParser_thenWrapArrayOfByteWithAAndOnePositionIsSeventeen() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderAdded(Mockito.<OrderAdded>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    ByteBuffer buffer =
        ByteBuffer.wrap(
            new byte[] {
              'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A',
              1, 'A', 1
            });
    actualPmrParser.message(buffer);

    // Assert
    verify(listener).orderAdded(isA(OrderAdded.class));
    assertEquals(17, buffer.position());
  }

  /**
   * Test {@link PMRParser#PMRParser(PMRListener)}.
   *
   * <ul>
   *   <li>Then wrap {@code VXAXAXAX} Bytes is {@code UTF-8} position is five.
   * </ul>
   *
   * <p>Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  @DisplayName(
      "Test new PMRParser(PMRListener); then wrap 'VXAXAXAX' Bytes is 'UTF-8' position is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
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
   * Test {@link PMRParser#message(ByteBuffer)} with an empty buffer.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when buffer is empty, then throws PMRException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenBufferIsEmpty_thenThrowsPMRException() {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    PMRParser parser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[0]);

    // Act and Assert
    assertThrows(PMRException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with a short Version message.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when Version message is too short, then throws PMRException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenVersionMessageIsTooShort_thenThrowsPMRException() {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    PMRParser parser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'V', 0});

    // Act and Assert
    assertThrows(PMRException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with a full OrderEntered message.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when full OrderEntered message, then listener called and position is fifty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenFullOrderEnteredMessage_thenListenerCalledAndPositionIsFifty() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderEntered(Mockito.<OrderEntered>any());
    PMRParser parser = new PMRParser(listener);
    byte[] data = new byte[50];
    data[0] = 'E';
    ByteBuffer buffer = ByteBuffer.wrap(data);

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).orderEntered(isA(OrderEntered.class));
    assertEquals(50, buffer.position());
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with a short OrderEntered message.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when OrderEntered message is too short, then throws PMRException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenOrderEnteredMessageIsTooShort_thenThrowsPMRException() {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    PMRParser parser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'E', 0});

    // Act and Assert
    assertThrows(PMRException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with a short OrderAdded message.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when OrderAdded message is too short, then throws PMRException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenOrderAddedMessageIsTooShort_thenThrowsPMRException() {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    PMRParser parser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'A', 0});

    // Act and Assert
    assertThrows(PMRException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with a full OrderCanceled message.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when full OrderCanceled message, then listener called and position is twenty-five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenFullOrderCanceledMessage_thenListenerCalledAndPositionIsTwentyFive() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderCanceled(Mockito.<OrderCanceled>any());
    PMRParser parser = new PMRParser(listener);
    byte[] data = new byte[25];
    data[0] = 'X';
    ByteBuffer buffer = ByteBuffer.wrap(data);

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).orderCanceled(isA(OrderCanceled.class));
    assertEquals(25, buffer.position());
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with a short OrderCanceled message.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when OrderCanceled message is too short, then throws PMRException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenOrderCanceledMessageIsTooShort_thenThrowsPMRException() {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    PMRParser parser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'X', 0});

    // Act and Assert
    assertThrows(PMRException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with a full Trade message.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when full Trade message, then listener called and position is thirty-seven")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenFullTradeMessage_thenListenerCalledAndPositionIsThirtySeven() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).trade(Mockito.<Trade>any());
    PMRParser parser = new PMRParser(listener);
    byte[] data = new byte[37];
    data[0] = 'T';
    ByteBuffer buffer = ByteBuffer.wrap(data);

    // Act
    parser.message(buffer);

    // Assert
    verify(listener).trade(isA(Trade.class));
    assertEquals(37, buffer.position());
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with a short Trade message.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when Trade message is too short, then throws PMRException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenTradeMessageIsTooShort_thenThrowsPMRException() {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    PMRParser parser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'T', 0});

    // Act and Assert
    assertThrows(PMRException.class, () -> parser.message(buffer));
  }

  /**
   * Test {@link PMRParser#message(ByteBuffer)} with an unknown message type.
   *
   * <p>Method under test: {@link PMRParser#message(ByteBuffer)}
   */
  @Test
  @DisplayName("Test PMRParser message(ByteBuffer); when unknown message type, then throws PMRException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.message(ByteBuffer)"})
  void testPMRParserMessage_whenUnknownMessageType_thenThrowsPMRException() {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    PMRParser parser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap(new byte[] {'Z'});

    // Act and Assert
    assertThrows(PMRException.class, () -> parser.message(buffer));
  }
}
