package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POE.CancelOrder;
import com.paritytrading.parity.net.poe.POE.EnterOrder;
import com.paritytrading.parity.net.poe.POE.OrderAccepted;
import com.paritytrading.parity.net.poe.POE.OrderCanceled;
import com.paritytrading.parity.net.poe.POE.OrderExecuted;
import com.paritytrading.parity.net.poe.POE.OrderRejected;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class POEDiffblueTest {
  /**
   * Test CancelOrder {@link CancelOrder#get(ByteBuffer)}.
   *
   * <p>Method under test: {@link CancelOrder#get(ByteBuffer)}
   */
  @Test
  @DisplayName("Test CancelOrder get(ByteBuffer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelOrder.get(ByteBuffer)"})
  void testCancelOrderGet() throws UnsupportedEncodingException {
    // Arrange
    CancelOrder cancelOrder = new CancelOrder();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAXAXAXAXAXAXAXAXAX".getBytes("UTF-8"));

    // Act
    cancelOrder.get(buffer);

    // Assert
    assertEquals(24, buffer.position());
    assertEquals(4708585257725083992L, cancelOrder.quantity);
    assertFalse(buffer.hasRemaining());
    assertArrayEquals("AXAXAXAXAXAXAXAX".getBytes("UTF-8"), cancelOrder.orderId);
  }

  /**
   * Test CancelOrder {@link CancelOrder#put(ByteBuffer)}.
   *
   * <p>Method under test: {@link CancelOrder#put(ByteBuffer)}
   */
  @Test
  @DisplayName("Test CancelOrder put(ByteBuffer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelOrder.put(ByteBuffer)"})
  void testCancelOrderPut() throws UnsupportedEncodingException {
    // Arrange
    CancelOrder cancelOrder = new CancelOrder();
    cancelOrder.orderId = "AXAXAXAXAXAXAXAX".getBytes("UTF-8");
    cancelOrder.quantity = 4708585257725083992L;
    ByteBuffer buffer = ByteBuffer.allocate(25);

    // Act
    cancelOrder.put(buffer);

    // Assert
    assertEquals(25, buffer.position());
    assertFalse(buffer.hasRemaining());
    buffer.flip();
    assertEquals((byte) 'X', buffer.get());
    byte[] orderId = new byte[16];
    buffer.get(orderId);
    assertArrayEquals("AXAXAXAXAXAXAXAX".getBytes("UTF-8"), orderId);
    assertEquals(4708585257725083992L, buffer.getLong());
  }

  /**
   * Test CancelOrder new {@link CancelOrder} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link CancelOrder}
   */
  @Test
  @DisplayName("Test CancelOrder new CancelOrder (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelOrder.<init>()"})
  void testCancelOrderNewCancelOrder() {
    // Arrange and Act
    CancelOrder actualCancelOrder = new CancelOrder();

    // Assert
    assertEquals(0L, actualCancelOrder.quantity);
    assertArrayEquals(
        new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualCancelOrder.orderId);
  }

  /**
   * Test EnterOrder new {@link EnterOrder} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link EnterOrder}
   */
  @Test
  @DisplayName("Test EnterOrder new EnterOrder (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EnterOrder.<init>()"})
  void testEnterOrderNewEnterOrder() {
    // Arrange and Act
    EnterOrder actualEnterOrder = new EnterOrder();

    // Assert
    assertEquals(0L, actualEnterOrder.instrument);
    assertEquals(0L, actualEnterOrder.price);
    assertEquals(0L, actualEnterOrder.quantity);
    assertEquals((byte) 0, actualEnterOrder.side);
    assertArrayEquals(
        new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualEnterOrder.orderId);
  }

  /**
   * Test OrderAccepted new {@link OrderAccepted} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderAccepted}
   */
  @Test
  @DisplayName("Test OrderAccepted new OrderAccepted (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAccepted.<init>()"})
  void testOrderAcceptedNewOrderAccepted() {
    // Arrange and Act
    OrderAccepted actualOrderAccepted = new OrderAccepted();

    // Assert
    assertEquals(0L, actualOrderAccepted.instrument);
    assertEquals(0L, actualOrderAccepted.orderNumber);
    assertEquals(0L, actualOrderAccepted.price);
    assertEquals(0L, actualOrderAccepted.quantity);
    assertEquals(0L, actualOrderAccepted.timestamp);
    assertEquals((byte) 0, actualOrderAccepted.side);
    assertArrayEquals(
        new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualOrderAccepted.orderId);
  }

  /**
   * Test OrderCanceled new {@link OrderCanceled} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderCanceled}
   */
  @Test
  @DisplayName("Test OrderCanceled new OrderCanceled (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.<init>()"})
  void testOrderCanceledNewOrderCanceled() {
    // Arrange and Act
    OrderCanceled actualOrderCanceled = new OrderCanceled();

    // Assert
    assertEquals(0L, actualOrderCanceled.canceledQuantity);
    assertEquals(0L, actualOrderCanceled.timestamp);
    assertEquals((byte) 0, actualOrderCanceled.reason);
    assertArrayEquals(
        new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualOrderCanceled.orderId);
  }

  /**
   * Test OrderCanceled {@link OrderCanceled#get(ByteBuffer)}.
   *
   * <p>Method under test: {@link OrderCanceled#get(ByteBuffer)}
   */
  @Test
  @DisplayName("Test OrderCanceled get(ByteBuffer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.get(ByteBuffer)"})
  void testOrderCanceledGet() throws UnsupportedEncodingException {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAXAXAXAXAXAXAXAXAXAXAXAXAXA".getBytes("UTF-8"));

    // Act
    orderCanceled.get(buffer);

    // Assert
    assertEquals(33, buffer.position());
    assertEquals(4708585257725083992L, orderCanceled.timestamp);
    assertArrayEquals("AXAXAXAXAXAXAXAX".getBytes("UTF-8"), orderCanceled.orderId);
    assertEquals(4708585257725083992L, orderCanceled.canceledQuantity);
    assertEquals((byte) 'A', orderCanceled.reason);
    assertFalse(buffer.hasRemaining());
  }

  /**
   * Test OrderCanceled {@link OrderCanceled#put(ByteBuffer)}.
   *
   * <p>Method under test: {@link OrderCanceled#put(ByteBuffer)}
   */
  @Test
  @DisplayName("Test OrderCanceled put(ByteBuffer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.put(ByteBuffer)"})
  void testOrderCanceledPut() throws UnsupportedEncodingException {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    orderCanceled.timestamp = 4708585257725083992L;
    orderCanceled.orderId = "AXAXAXAXAXAXAXAX".getBytes("UTF-8");
    orderCanceled.canceledQuantity = 4708585257725083992L;
    orderCanceled.reason = (byte) 'A';
    ByteBuffer buffer = ByteBuffer.allocate(34);

    // Act
    orderCanceled.put(buffer);

    // Assert
    assertEquals(34, buffer.position());
    assertFalse(buffer.hasRemaining());
    assertArrayEquals(
        "XAXAXAXAXAXAXAXAXAXAXAXAXAXAXAXAXA".getBytes("UTF-8"), buffer.array());
  }

  /**
   * Test OrderExecuted new {@link OrderExecuted} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderExecuted}
   */
  @Test
  @DisplayName("Test OrderExecuted new OrderExecuted (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderExecuted.<init>()"})
  void testOrderExecutedNewOrderExecuted() {
    // Arrange and Act
    OrderExecuted actualOrderExecuted = new OrderExecuted();

    // Assert
    assertEquals(0L, actualOrderExecuted.matchNumber);
    assertEquals(0L, actualOrderExecuted.price);
    assertEquals(0L, actualOrderExecuted.quantity);
    assertEquals(0L, actualOrderExecuted.timestamp);
    assertEquals((byte) 0, actualOrderExecuted.liquidityFlag);
    assertArrayEquals(
        new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualOrderExecuted.orderId);
  }

  /**
   * Test OrderRejected new {@link OrderRejected} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderRejected}
   */
  @Test
  @DisplayName("Test OrderRejected new OrderRejected (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderRejected.<init>()"})
  void testOrderRejectedNewOrderRejected() {
    // Arrange and Act
    OrderRejected actualOrderRejected = new OrderRejected();

    // Assert
    assertEquals(0L, actualOrderRejected.timestamp);
    assertEquals((byte) 0, actualOrderRejected.reason);
    assertArrayEquals(
        new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualOrderRejected.orderId);
  }
}
