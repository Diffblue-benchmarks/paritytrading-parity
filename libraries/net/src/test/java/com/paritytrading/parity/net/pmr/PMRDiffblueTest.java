package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmr.PMR.OrderAdded;
import com.paritytrading.parity.net.pmr.PMR.OrderCanceled;
import com.paritytrading.parity.net.pmr.PMR.OrderEntered;
import com.paritytrading.parity.net.pmr.PMR.Trade;
import com.paritytrading.parity.net.pmr.PMR.Version;
import com.paritytrading.parity.net.poe.POETestFactory;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class PMRDiffblueTest {
  /**
   * Test OrderAdded {@link OrderAdded#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is {@link Short#SIZE}.
   * </ul>
   *
   * <p>Method under test: {@link OrderAdded#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderAdded get(ByteBuffer); then createByteBufferForOrderRejectedPut position is SIZE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAdded.get(ByteBuffer)"})
  void testOrderAddedGet_thenCreateByteBufferForOrderRejectedPutPositionIsSize() {
    // Arrange
    OrderAdded orderAdded = new OrderAdded();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    orderAdded.get(buffer);

    // Assert
    assertEquals(Short.SIZE, buffer.position());
  }

  /**
   * Test OrderAdded new {@link OrderAdded} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderAdded}
   */
  @Test
  @DisplayName("Test OrderAdded new OrderAdded (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAdded.<init>()"})
  void testOrderAddedNewOrderAdded() {
    // Arrange and Act
    OrderAdded actualOrderAdded = new OrderAdded();

    // Assert
    assertEquals(0L, actualOrderAdded.orderNumber);
    assertEquals(0L, actualOrderAdded.timestamp);
  }

  /**
   * Test OrderAdded {@link OrderAdded#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is seventeen.
   * </ul>
   *
   * <p>Method under test: {@link OrderAdded#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderAdded put(ByteBuffer); then createByteBufferForOrderRejectedPut position is seventeen")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAdded.put(ByteBuffer)"})
  void testOrderAddedPut_thenCreateByteBufferForOrderRejectedPutPositionIsSeventeen() {
    // Arrange
    OrderAdded orderAdded = new OrderAdded();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    orderAdded.put(buffer);

    // Assert
    assertEquals(17, buffer.position());
    assertArrayEquals(
        new byte[] {'A', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        buffer.array());
  }

  /**
   * Test OrderCanceled {@link OrderCanceled#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is twenty-four.
   * </ul>
   *
   * <p>Method under test: {@link OrderCanceled#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderCanceled get(ByteBuffer); then createByteBufferForOrderRejectedPut position is twenty-four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.get(ByteBuffer)"})
  void testOrderCanceledGet_thenCreateByteBufferForOrderRejectedPutPositionIsTwentyFour() {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    orderCanceled.get(buffer);

    // Assert
    assertEquals(24, buffer.position());
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
    assertEquals(0L, actualOrderCanceled.orderNumber);
    assertEquals(0L, actualOrderCanceled.timestamp);
  }

  /**
   * Test OrderCanceled {@link OrderCanceled#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is twenty-five.
   * </ul>
   *
   * <p>Method under test: {@link OrderCanceled#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderCanceled put(ByteBuffer); then createByteBufferForOrderRejectedPut position is twenty-five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.put(ByteBuffer)"})
  void testOrderCanceledPut_thenCreateByteBufferForOrderRejectedPutPositionIsTwentyFive() {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    orderCanceled.put(buffer);

    // Assert
    assertEquals(25, buffer.position());
    assertArrayEquals(
        new byte[] {'X', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        buffer.array());
  }

  /**
   * Test OrderEntered {@link OrderEntered#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderAcceptedPut position is forty-nine.
   * </ul>
   *
   * <p>Method under test: {@link OrderEntered#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderEntered get(ByteBuffer); then createByteBufferForOrderAcceptedPut position is forty-nine")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderEntered.get(ByteBuffer)"})
  void testOrderEnteredGet_thenCreateByteBufferForOrderAcceptedPutPositionIsFortyNine() {
    // Arrange
    OrderEntered orderEntered = new OrderEntered();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderAcceptedPut();

    // Act
    orderEntered.get(buffer);

    // Assert
    assertEquals(49, buffer.position());
  }

  /**
   * Test OrderEntered new {@link OrderEntered} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderEntered}
   */
  @Test
  @DisplayName("Test OrderEntered new OrderEntered (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderEntered.<init>()"})
  void testOrderEnteredNewOrderEntered() {
    // Arrange and Act
    OrderEntered actualOrderEntered = new OrderEntered();

    // Assert
    assertEquals(0L, actualOrderEntered.instrument);
    assertEquals(0L, actualOrderEntered.orderNumber);
    assertEquals(0L, actualOrderEntered.price);
    assertEquals(0L, actualOrderEntered.quantity);
    assertEquals(0L, actualOrderEntered.timestamp);
    assertEquals(0L, actualOrderEntered.username);
    assertEquals((byte) 0, actualOrderEntered.side);
  }

  /**
   * Test OrderEntered {@link OrderEntered#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderAcceptedPut position is fifty.
   * </ul>
   *
   * <p>Method under test: {@link OrderEntered#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderEntered put(ByteBuffer); then createByteBufferForOrderAcceptedPut position is fifty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderEntered.put(ByteBuffer)"})
  void testOrderEnteredPut_thenCreateByteBufferForOrderAcceptedPutPositionIsFifty() {
    // Arrange
    OrderEntered orderEntered = new OrderEntered();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderAcceptedPut();

    // Act
    orderEntered.put(buffer);

    // Assert
    assertEquals(50, buffer.position());
    assertArrayEquals(
        new byte[] {
          'E', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        buffer.array());
  }

  /**
   * Test Trade {@link Trade#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is thirty-six.
   * </ul>
   *
   * <p>Method under test: {@link Trade#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test Trade get(ByteBuffer); then createByteBufferForOrderExecutedPut position is thirty-six")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Trade.get(ByteBuffer)"})
  void testTradeGet_thenCreateByteBufferForOrderExecutedPutPositionIsThirtySix() {
    // Arrange
    Trade trade = new Trade();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    trade.get(buffer);

    // Assert
    assertEquals(36, buffer.position());
  }

  /**
   * Test Trade new {@link Trade} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link Trade}
   */
  @Test
  @DisplayName("Test Trade new Trade (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Trade.<init>()"})
  void testTradeNewTrade() {
    // Arrange and Act
    Trade actualTrade = new Trade();

    // Assert
    assertEquals(0L, actualTrade.incomingOrderNumber);
    assertEquals(0L, actualTrade.matchNumber);
    assertEquals(0L, actualTrade.quantity);
    assertEquals(0L, actualTrade.restingOrderNumber);
    assertEquals(0L, actualTrade.timestamp);
  }

  /**
   * Test Trade {@link Trade#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is thirty-seven.
   * </ul>
   *
   * <p>Method under test: {@link Trade#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test Trade put(ByteBuffer); then createByteBufferForOrderExecutedPut position is thirty-seven")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Trade.put(ByteBuffer)"})
  void testTradePut_thenCreateByteBufferForOrderExecutedPutPositionIsThirtySeven() {
    // Arrange
    Trade trade = new Trade();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    trade.put(buffer);

    // Assert
    assertEquals(37, buffer.position());
    assertArrayEquals(
        new byte[] {
          'T', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        buffer.array());
  }

  /**
   * Test Version {@link Version#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is four.
   * </ul>
   *
   * <p>Method under test: {@link Version#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test Version get(ByteBuffer); then createByteBufferForOrderRejectedPut position is four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Version.get(ByteBuffer)"})
  void testVersionGet_thenCreateByteBufferForOrderRejectedPutPositionIsFour() {
    // Arrange
    Version version = new Version();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    version.get(buffer);

    // Assert
    assertEquals(4, buffer.position());
  }

  /**
   * Test Version new {@link Version} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link Version}
   */
  @Test
  @DisplayName("Test Version new Version (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Version.<init>()"})
  void testVersionNewVersion() {
    // Arrange, Act and Assert
    assertEquals(0L, new Version().version);
  }

  /**
   * Test Version {@link Version#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is five.
   * </ul>
   *
   * <p>Method under test: {@link Version#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test Version put(ByteBuffer); then createByteBufferForOrderRejectedPut position is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Version.put(ByteBuffer)"})
  void testVersionPut_thenCreateByteBufferForOrderRejectedPutPositionIsFive() {
    // Arrange
    Version version = new Version();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    version.put(buffer);

    // Assert
    assertEquals(5, buffer.position());
    assertArrayEquals(
        new byte[] {'V', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        buffer.array());
  }
}
