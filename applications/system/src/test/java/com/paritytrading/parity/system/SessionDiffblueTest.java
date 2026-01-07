package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionDiffblueTest {
  @InjectMocks private Session session;

  /**
   * Test {@link Session#Session(SocketChannel, OrderBooks)}.
   *
   * <p>Method under test: {@link Session#Session(SocketChannel, OrderBooks)}
   */
  @Test
  @DisplayName("Test new Session(SocketChannel, OrderBooks)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Session.<init>(SocketChannel, OrderBooks)"})
  void testNewSession() {
    // Arrange and Act
    Session actualSession = new Session(null, null);

    // Assert
    assertNull(actualSession.getTransport().getChannel());
    assertEquals(0L, actualSession.getUsername());
    assertFalse(actualSession.isTerminated());
  }

  /**
   * Test {@link Session#track(Order)}.
   *
   * <ul>
   *   <li>Given {@code AXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then calls {@link Order#getOrderId()}.
   * </ul>
   *
   * <p>Method under test: {@link Session#track(Order)}
   */
  @Test
  @DisplayName("Test track(Order); given 'AXAXAXAX' Bytes is 'UTF-8'; then calls getOrderId()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Session.track(Order)"})
  void testTrack_givenAxaxaxaxBytesIsUtf8_thenCallsGetOrderId()
      throws UnsupportedEncodingException {
    // Arrange
    Order order = mock(Order.class);
    when(order.getOrderId()).thenReturn("AXAXAXAX".getBytes("UTF-8"));

    // Act
    session.track(order);

    // Assert
    verify(order).getOrderId();
  }

  /**
   * Test {@link Session#release(Order)}.
   *
   * <ul>
   *   <li>Given {@code AXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then calls {@link Order#getOrderId()}.
   * </ul>
   *
   * <p>Method under test: {@link Session#release(Order)}
   */
  @Test
  @DisplayName("Test release(Order); given 'AXAXAXAX' Bytes is 'UTF-8'; then calls getOrderId()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Session.release(Order)"})
  void testRelease_givenAxaxaxaxBytesIsUtf8_thenCallsGetOrderId()
      throws UnsupportedEncodingException {
    // Arrange
    Order order = mock(Order.class);
    when(order.getOrderId()).thenReturn("AXAXAXAX".getBytes("UTF-8"));

    // Act
    session.release(order);

    // Assert
    verify(order).getOrderId();
  }
}
