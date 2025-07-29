package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class SessionDiffblueTest {
  /**
   * Test {@link Session#Session(SocketChannel, OrderBooks)}.
   *
   * <ul>
   *   <li>When {@code null}.
   *   <li>Then return Transport Channel is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link Session#Session(SocketChannel, OrderBooks)}
   */
  @Test
  @DisplayName(
      "Test new Session(SocketChannel, OrderBooks); when 'null'; then return Transport Channel is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Session.<init>(SocketChannel, OrderBooks)"})
  void testNewSession_whenNull_thenReturnTransportChannelIsNull() {
    // Arrange and Act
    Session actualSession = new Session(null, null);

    // Assert
    assertNull(actualSession.getTransport().getChannel());
    assertEquals(0L, actualSession.getUsername());
    assertFalse(actualSession.isTerminated());
  }
}
