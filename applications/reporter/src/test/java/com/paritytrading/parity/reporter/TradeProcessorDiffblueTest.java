package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmr.PMR;
import com.paritytrading.parity.net.pmr.PMR.OrderCanceled;
import com.paritytrading.parity.util.Instruments;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradeProcessorDiffblueTest {
  /**
   * Test {@link TradeProcessor#TradeProcessor(TradeListener)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>Then Listener return {@link DisplayFormat}.
   * </ul>
   *
   * <p>Method under test: {@link TradeProcessor#TradeProcessor(TradeListener)}
   */
  @Test
  @DisplayName(
      "Test new TradeProcessor(TradeListener); given one; then Listener return DisplayFormat")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeProcessor.<init>(TradeListener)"})
  void testNewTradeProcessor_givenOne_thenListenerReturnDisplayFormat() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    DisplayFormat listener = new DisplayFormat(instruments);

    // Act
    TradeProcessor actualTradeProcessor = new TradeProcessor(listener);

    // Assert
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
    TradeListener listener2 = actualTradeProcessor.getListener();
    assertTrue(listener2 instanceof DisplayFormat);
    Trade trade = actualTradeProcessor.getTrade();
    assertNull(trade.buyer);
    assertNull(trade.instrument);
    assertNull(trade.seller);
    assertNull(trade.timestamp);
    assertEquals(0L, trade.buyOrderNumber);
    assertEquals(0L, trade.matchNumber);
    assertEquals(0L, trade.price);
    assertEquals(0L, trade.quantity);
    assertEquals(0L, trade.sellOrderNumber);
    assertTrue(actualTradeProcessor.getOrders().isEmpty());
    assertSame(listener, listener2);
  }

  /**
   * Test {@link TradeProcessor#orderCanceled(OrderCanceled)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>Then createTradeProcessorForTrade Orders size is two.
   * </ul>
   *
   * <p>Method under test: {@link TradeProcessor#orderCanceled(OrderCanceled)}
   */
  @Test
  @DisplayName(
      "Test orderCanceled(OrderCanceled); given one; then createTradeProcessorForTrade Orders size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeProcessor.orderCanceled(OrderCanceled)"})
  void testOrderCanceled_givenOne_thenCreateTradeProcessorForTradeOrdersSizeIsTwo() {
    // Arrange
    TradeProcessor createTradeProcessorForTradeResult =
        TradeProcessorTestFactory.createTradeProcessorForTrade();
    OrderCanceled message = new OrderCanceled();
    message.orderNumber = 1L;

    // Act
    createTradeProcessorForTradeResult.orderCanceled(message);

    // Assert that nothing has changed
    Map orders = createTradeProcessorForTradeResult.getOrders();
    assertEquals(2, orders.size());
    assertTrue(orders.containsKey(1L));
    assertTrue(orders.containsKey(2L));
  }

  /**
   * Test {@link TradeProcessor#orderCanceled(OrderCanceled)}.
   *
   * <ul>
   *   <li>Then createTradeProcessorForTrade Orders size is one.
   * </ul>
   *
   * <p>Method under test: {@link TradeProcessor#orderCanceled(OrderCanceled)}
   */
  @Test
  @DisplayName(
      "Test orderCanceled(OrderCanceled); then createTradeProcessorForTrade Orders size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeProcessor.orderCanceled(OrderCanceled)"})
  void testOrderCanceled_thenCreateTradeProcessorForTradeOrdersSizeIsOne() {
    // Arrange
    TradeProcessor createTradeProcessorForTradeResult =
        TradeProcessorTestFactory.createTradeProcessorForTrade();
    OrderCanceled message = new OrderCanceled();
    message.canceledQuantity = 100L;
    message.orderNumber = 1L;

    // Act
    createTradeProcessorForTradeResult.orderCanceled(message);

    // Assert
    Map orders = createTradeProcessorForTradeResult.getOrders();
    assertEquals(1, orders.size());
    assertTrue(orders.containsKey(2L));
  }

  /**
   * Test {@link TradeProcessor#trade(Trade)}.
   *
   * <ul>
   *   <li>Given two.
   *   <li>Then createTradeProcessorForTrade Trade {@link Trade#seller} is {@code SELLER}.
   * </ul>
   *
   * <p>Method under test: {@link TradeProcessor#trade(PMR.Trade)}
   */
  @Test
  @DisplayName(
      "Test trade(Trade); given two; then createTradeProcessorForTrade Trade seller is 'SELLER'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeProcessor.trade(PMR.Trade)"})
  void testTrade_givenTwo_thenCreateTradeProcessorForTradeTradeSellerIsSeller() {
    // Arrange
    TradeProcessor createTradeProcessorForTradeResult =
        TradeProcessorTestFactory.createTradeProcessorForTrade();
    PMR.Trade message = new PMR.Trade();
    message.incomingOrderNumber = 1L;
    message.restingOrderNumber = 2L;

    // Act
    createTradeProcessorForTradeResult.trade(message);

    // Assert
    Trade trade = createTradeProcessorForTradeResult.getTrade();
    assertEquals("00:00:00.000", trade.timestamp);
    assertEquals("BUYER", trade.buyer);
    assertEquals("INST1", trade.instrument);
    assertEquals("SELLER", trade.seller);
    assertEquals(1000L, trade.price);
    assertEquals(1L, trade.buyOrderNumber);
    assertEquals(2L, trade.sellOrderNumber);
  }

  /**
   * Test {@link TradeProcessor#trade(Trade)}.
   *
   * <ul>
   *   <li>Then createTradeProcessorForTrade Trade {@link Trade#seller} is {@code BUYER}.
   * </ul>
   *
   * <p>Method under test: {@link TradeProcessor#trade(PMR.Trade)}
   */
  @Test
  @DisplayName("Test trade(Trade); then createTradeProcessorForTrade Trade seller is 'BUYER'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeProcessor.trade(PMR.Trade)"})
  void testTrade_thenCreateTradeProcessorForTradeTradeSellerIsBuyer() {
    // Arrange
    TradeProcessor createTradeProcessorForTradeResult =
        TradeProcessorTestFactory.createTradeProcessorForTrade();
    PMR.Trade message = new PMR.Trade();
    message.incomingOrderNumber = 1L;
    message.restingOrderNumber = 1L;

    // Act
    createTradeProcessorForTradeResult.trade(message);

    // Assert
    Trade trade = createTradeProcessorForTradeResult.getTrade();
    assertEquals("00:00:00.000", trade.timestamp);
    assertEquals("BUYER", trade.buyer);
    assertEquals("BUYER", trade.seller);
    assertEquals("INST1", trade.instrument);
    assertEquals(1000L, trade.price);
    assertEquals(1L, trade.buyOrderNumber);
    assertEquals(1L, trade.sellOrderNumber);
  }
}
