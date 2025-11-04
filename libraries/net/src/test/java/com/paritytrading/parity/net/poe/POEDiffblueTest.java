package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class POEDiffblueTest {
  /**
   * Method under test: {@link POE.CancelOrder#get(ByteBuffer)}
   */
  @Test
  void testCancelOrderGet() throws UnsupportedEncodingException {
    // Arrange
    POE.CancelOrder cancelOrder = new POE.CancelOrder();
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
   * Method under test: default or parameterless constructor of
   * {@link POE.CancelOrder}
   */
  @Test
  void testCancelOrderNewCancelOrder() {
    // Arrange and Act
    POE.CancelOrder actualCancelOrder = new POE.CancelOrder();

    // Assert
    assertEquals(0L, actualCancelOrder.quantity);
    assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualCancelOrder.orderId);
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link POE.EnterOrder}
   */
  @Test
  void testEnterOrderNewEnterOrder() {
    // Arrange and Act
    POE.EnterOrder actualEnterOrder = new POE.EnterOrder();

    // Assert
    assertEquals(0L, actualEnterOrder.instrument);
    assertEquals(0L, actualEnterOrder.price);
    assertEquals(0L, actualEnterOrder.quantity);
    assertEquals((byte) 0, actualEnterOrder.side);
    assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualEnterOrder.orderId);
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link POE.OrderAccepted}
   */
  @Test
  void testOrderAcceptedNewOrderAccepted() {
    // Arrange and Act
    POE.OrderAccepted actualOrderAccepted = new POE.OrderAccepted();

    // Assert
    assertEquals(0L, actualOrderAccepted.instrument);
    assertEquals(0L, actualOrderAccepted.orderNumber);
    assertEquals(0L, actualOrderAccepted.price);
    assertEquals(0L, actualOrderAccepted.quantity);
    assertEquals(0L, actualOrderAccepted.timestamp);
    assertEquals((byte) 0, actualOrderAccepted.side);
    assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualOrderAccepted.orderId);
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link POE.OrderCanceled}
   */
  @Test
  void testOrderCanceledNewOrderCanceled() {
    // Arrange and Act
    POE.OrderCanceled actualOrderCanceled = new POE.OrderCanceled();

    // Assert
    assertEquals(0L, actualOrderCanceled.canceledQuantity);
    assertEquals(0L, actualOrderCanceled.timestamp);
    assertEquals((byte) 0, actualOrderCanceled.reason);
    assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualOrderCanceled.orderId);
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link POE.OrderExecuted}
   */
  @Test
  void testOrderExecutedNewOrderExecuted() {
    // Arrange and Act
    POE.OrderExecuted actualOrderExecuted = new POE.OrderExecuted();

    // Assert
    assertEquals(0L, actualOrderExecuted.matchNumber);
    assertEquals(0L, actualOrderExecuted.price);
    assertEquals(0L, actualOrderExecuted.quantity);
    assertEquals(0L, actualOrderExecuted.timestamp);
    assertEquals((byte) 0, actualOrderExecuted.liquidityFlag);
    assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualOrderExecuted.orderId);
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link POE.OrderRejected}
   */
  @Test
  void testOrderRejectedNewOrderRejected() {
    // Arrange and Act
    POE.OrderRejected actualOrderRejected = new POE.OrderRejected();

    // Assert
    assertEquals(0L, actualOrderRejected.timestamp);
    assertEquals((byte) 0, actualOrderRejected.reason);
    assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, actualOrderRejected.orderId);
  }
}
