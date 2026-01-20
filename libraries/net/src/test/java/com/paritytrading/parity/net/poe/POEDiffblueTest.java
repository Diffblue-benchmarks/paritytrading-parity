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
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class POEDiffblueTest {
  /**
   * Test CancelOrder {@link CancelOrder#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is twenty-four.
   * </ul>
   *
   * <p>Method under test: {@link CancelOrder#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test CancelOrder get(ByteBuffer); then createByteBufferForOrderRejectedPut position is twenty-four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelOrder.get(ByteBuffer)"})
  void testCancelOrderGet_thenCreateByteBufferForOrderRejectedPutPositionIsTwentyFour() {
    // Arrange
    CancelOrder cancelOrder = new CancelOrder();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    cancelOrder.get(buffer);

    // Assert
    assertEquals(24, buffer.position());
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
   * Test CancelOrder {@link CancelOrder#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is twenty-five.
   * </ul>
   *
   * <p>Method under test: {@link CancelOrder#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test CancelOrder put(ByteBuffer); then createByteBufferForOrderRejectedPut position is twenty-five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CancelOrder.put(ByteBuffer)"})
  void testCancelOrderPut_thenCreateByteBufferForOrderRejectedPutPositionIsTwentyFive() {
    // Arrange
    CancelOrder cancelOrder = new CancelOrder();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    cancelOrder.put(buffer);

    // Assert
    assertEquals(25, buffer.position());
    assertArrayEquals(
        new byte[] {'X', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        buffer.array());
  }

  /**
   * Test EnterOrder {@link EnterOrder#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is forty-one.
   * </ul>
   *
   * <p>Method under test: {@link EnterOrder#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test EnterOrder get(ByteBuffer); then createByteBufferForOrderExecutedPut position is forty-one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EnterOrder.get(ByteBuffer)"})
  void testEnterOrderGet_thenCreateByteBufferForOrderExecutedPutPositionIsFortyOne() {
    // Arrange
    EnterOrder enterOrder = new EnterOrder();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    enterOrder.get(buffer);

    // Assert
    assertEquals(41, buffer.position());
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
   * Test EnterOrder {@link EnterOrder#put(ByteBuffer)}.
   *
   * <p>Method under test: {@link EnterOrder#put(ByteBuffer)}
   */
  @Test
  @DisplayName("Test EnterOrder put(ByteBuffer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EnterOrder.put(ByteBuffer)"})
  void testEnterOrderPut() {
    // Arrange
    EnterOrder enterOrder = new EnterOrder();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    enterOrder.put(buffer);

    // Assert
    assertEquals(POE.MAX_INBOUND_MESSAGE_LENGTH, buffer.position());
    assertArrayEquals(
        new byte[] {
          'E', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        buffer.array());
  }

  /**
   * Test OrderAccepted {@link OrderAccepted#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderAcceptedPut position is fifty-seven.
   * </ul>
   *
   * <p>Method under test: {@link OrderAccepted#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderAccepted get(ByteBuffer); then createByteBufferForOrderAcceptedPut position is fifty-seven")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAccepted.get(ByteBuffer)"})
  void testOrderAcceptedGet_thenCreateByteBufferForOrderAcceptedPutPositionIsFiftySeven() {
    // Arrange
    OrderAccepted orderAccepted = new OrderAccepted();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderAcceptedPut();

    // Act
    orderAccepted.get(buffer);

    // Assert
    assertEquals(57, buffer.position());
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
   * Test OrderAccepted {@link OrderAccepted#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then not createByteBufferForOrderAcceptedPut hasRemaining.
   * </ul>
   *
   * <p>Method under test: {@link OrderAccepted#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderAccepted put(ByteBuffer); then not createByteBufferForOrderAcceptedPut hasRemaining")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAccepted.put(ByteBuffer)"})
  void testOrderAcceptedPut_thenNotCreateByteBufferForOrderAcceptedPutHasRemaining() {
    // Arrange
    OrderAccepted orderAccepted = new OrderAccepted();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderAcceptedPut();

    // Act
    orderAccepted.put(buffer);

    // Assert
    assertFalse(buffer.hasRemaining());
    assertEquals(POE.MAX_OUTBOUND_MESSAGE_LENGTH, buffer.position());
    assertArrayEquals(
        new byte[] {
          'A', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        buffer.array());
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
  void testOrderCanceledGet() {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    orderCanceled.get(buffer);

    // Assert
    assertEquals(33, buffer.position());
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
   * Test OrderCanceled {@link OrderCanceled#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is thirty-four.
   * </ul>
   *
   * <p>Method under test: {@link OrderCanceled#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderCanceled put(ByteBuffer); then createByteBufferForOrderExecutedPut position is thirty-four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.put(ByteBuffer)"})
  void testOrderCanceledPut_thenCreateByteBufferForOrderExecutedPutPositionIsThirtyFour() {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    orderCanceled.put(buffer);

    // Assert
    assertEquals(34, buffer.position());
    assertArrayEquals(
        new byte[] {
          'X', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        buffer.array());
  }

  /**
   * Test OrderExecuted {@link OrderExecuted#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is forty-five.
   * </ul>
   *
   * <p>Method under test: {@link OrderExecuted#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderExecuted get(ByteBuffer); then createByteBufferForOrderExecutedPut position is forty-five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderExecuted.get(ByteBuffer)"})
  void testOrderExecutedGet_thenCreateByteBufferForOrderExecutedPutPositionIsFortyFive() {
    // Arrange
    OrderExecuted orderExecuted = new OrderExecuted();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    orderExecuted.get(buffer);

    // Assert
    assertEquals(45, buffer.position());
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
   * Test OrderExecuted {@link OrderExecuted#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is forty-six.
   * </ul>
   *
   * <p>Method under test: {@link OrderExecuted#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderExecuted put(ByteBuffer); then createByteBufferForOrderExecutedPut position is forty-six")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderExecuted.put(ByteBuffer)"})
  void testOrderExecutedPut_thenCreateByteBufferForOrderExecutedPutPositionIsFortySix() {
    // Arrange
    OrderExecuted orderExecuted = new OrderExecuted();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    orderExecuted.put(buffer);

    // Assert
    assertEquals(46, buffer.position());
    assertFalse(buffer.hasRemaining());
    assertArrayEquals(
        new byte[] {
          'E', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        buffer.array());
  }

  /**
   * Test OrderRejected {@link OrderRejected#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is twenty-five.
   * </ul>
   *
   * <p>Method under test: {@link OrderRejected#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderRejected get(ByteBuffer); then createByteBufferForOrderRejectedPut position is twenty-five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderRejected.get(ByteBuffer)"})
  void testOrderRejectedGet_thenCreateByteBufferForOrderRejectedPutPositionIsTwentyFive() {
    // Arrange
    OrderRejected orderRejected = new OrderRejected();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    orderRejected.get(buffer);

    // Assert
    assertEquals(25, buffer.position());
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

  /**
   * Test OrderRejected {@link OrderRejected#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is twenty-six.
   * </ul>
   *
   * <p>Method under test: {@link OrderRejected#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderRejected put(ByteBuffer); then createByteBufferForOrderRejectedPut position is twenty-six")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderRejected.put(ByteBuffer)"})
  void testOrderRejectedPut_thenCreateByteBufferForOrderRejectedPutPositionIsTwentySix() {
    // Arrange
    OrderRejected orderRejected = new OrderRejected();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    orderRejected.put(buffer);

    // Assert
    assertEquals(26, buffer.position());
    assertFalse(buffer.hasRemaining());
    assertArrayEquals(
        new byte[] {'R', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        buffer.array());
  }
}
