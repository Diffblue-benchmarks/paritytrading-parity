package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.match.OrderBookListener;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {
  /**
   * Test {@link Order#Order(byte[], long, Session, OrderBook)}.
   *
   * <ul>
   *   <li>Then return OrderNumber is one.
   * </ul>
   *
   * <p>Method under test: {@link Order#Order(byte[], long, Session, OrderBook)}
   */
  @Test
  @DisplayName("Test new Order(byte[], long, Session, OrderBook); then return OrderNumber is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Order.<init>(byte[], long, Session, OrderBook)"})
  void testNewOrder_thenReturnOrderNumberIsOne() throws UnsupportedEncodingException {
    // Arrange
    byte[] orderId = "AXAXAXAX".getBytes("UTF-8");
    Session session = new Session(null, null);

    OrderBook book = new OrderBook(mock(OrderBookListener.class));

    // Act
    Order actualOrder = new Order(orderId, 1L, session, book);

    // Assert
    assertEquals(1L, actualOrder.getOrderNumber());
    assertSame(book, actualOrder.getBook());
    assertSame(session, actualOrder.getSession());
    byte[] expectedOrderId = "AXAXAXAX".getBytes("UTF-8");
    assertArrayEquals(expectedOrderId, actualOrder.getOrderId());
  }
}
