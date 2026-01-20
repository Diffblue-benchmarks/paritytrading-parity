package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPServer;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class SessionDiffblueTest {
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
    Session actualSession = new Session(null, TestFactories.createOrderBooks());

    // Assert
    assertNull(actualSession.getTransport().getChannel());
    assertEquals(0L, actualSession.getUsername());
    assertFalse(actualSession.isTerminated());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link Session#getTransport()}
   *   <li>{@link Session#getUsername()}
   *   <li>{@link Session#isTerminated()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "SoupBinTCPServer Session.getTransport()",
    "long Session.getUsername()",
    "void Session.heartbeatTimeout(SoupBinTCPServer)",
    "boolean Session.isTerminated()",
    "void Session.logoutRequest(SoupBinTCPServer)"
  })
  void testGettersAndSetters() {
    // Arrange
    Session session = new Session(null, TestFactories.createOrderBooks());

    // Act
    SoupBinTCPServer actualTransport = session.getTransport();
    long actualUsername = session.getUsername();

    // Assert
    assertNull(actualTransport.getChannel());
    assertEquals(0L, actualUsername);
    assertFalse(session.isTerminated());
  }

  /**
   * Test {@link Session#track(Order)}.
   *
   * <ul>
   *   <li>Given createSession.
   *   <li>Then calls {@link Order#getOrderId()}.
   * </ul>
   *
   * <p>Method under test: {@link Session#track(Order)}
   */
  @Test
  @DisplayName("Test track(Order); given createSession; then calls getOrderId()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Session.track(Order)"})
  void testTrack_givenCreateSession_thenCallsGetOrderId() throws UnsupportedEncodingException {
    // Arrange
    Session createSessionResult = TestFactories.createSession();

    Order order = mock(Order.class);
    when(order.getOrderId()).thenReturn("AXAXAXAX".getBytes("UTF-8"));

    // Act
    createSessionResult.track(order);

    // Assert
    verify(order).getOrderId();
  }

  /**
   * Test {@link Session#release(Order)}.
   *
   * <ul>
   *   <li>Given createSession.
   *   <li>Then calls {@link Order#getOrderId()}.
   * </ul>
   *
   * <p>Method under test: {@link Session#release(Order)}
   */
  @Test
  @DisplayName("Test release(Order); given createSession; then calls getOrderId()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Session.release(Order)"})
  void testRelease_givenCreateSession_thenCallsGetOrderId() throws UnsupportedEncodingException {
    // Arrange
    Session createSessionResult = TestFactories.createSession();

    Order order = mock(Order.class);
    when(order.getOrderId()).thenReturn("AXAXAXAX".getBytes("UTF-8"));

    // Act
    createSessionResult.release(order);

    // Assert
    verify(order).getOrderId();
  }
}
