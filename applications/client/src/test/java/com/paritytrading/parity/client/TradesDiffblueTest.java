package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.client.Event.OrderAccepted;
import com.paritytrading.parity.net.poe.POE;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradesDiffblueTest {
  /**
   * Test {@link Trades#collect(Events)}.
   *
   * <ul>
   *   <li>When {@link Events} (default constructor).
   *   <li>Then return Empty.
   * </ul>
   *
   * <p>Method under test: {@link Trades#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when Events (default constructor); then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Trades.collect(Events)"})
  void testCollect_whenEvents_thenReturnEmpty() {
    // Arrange and Act
    List<Trade> actualCollectResult = Trades.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link Trades#getOrders()}
   *   <li>{@link Trades#getTrades()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Map Trades.getOrders()", "Map Trades.getTrades()"})
  void testGettersAndSetters() {
    // Arrange
    Trades createTradesResult = TradesTestFactory.createTrades();

    // Act
    Map actualOrders = createTradesResult.getOrders();
    Map actualTrades = createTradesResult.getTrades();

    // Assert
    assertTrue(actualOrders.isEmpty());
    assertTrue(actualTrades.isEmpty());
  }

  /**
   * Test {@link Trades#visit(OrderAccepted)} with {@code OrderAccepted}.
   *
   * <ul>
   *   <li>Then createTrades Orders size is one.
   * </ul>
   *
   * <p>Method under test: {@link Trades#visit(OrderAccepted)}
   */
  @Test
  @DisplayName(
      "Test visit(OrderAccepted) with 'OrderAccepted'; then createTrades Orders size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Trades.visit(OrderAccepted)"})
  void testVisitWithOrderAccepted_thenCreateTradesOrdersSizeIsOne() {
    // Arrange
    Trades createTradesResult = TradesTestFactory.createTrades();

    // Act
    createTradesResult.visit(new OrderAccepted(new POE.OrderAccepted()));

    // Assert
    Map orders = createTradesResult.getOrders();
    assertEquals(1, orders.size());
    Object getResult =
        orders.get(
            "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000");
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        ((Order) getResult).getOrderId());
    assertEquals(0L, ((Order) getResult).getInstrument());
    assertEquals(0L, ((Order) getResult).getQuantity());
    assertEquals(0L, ((Order) getResult).getTimestamp());
    assertEquals((byte) 0, ((Order) getResult).getSide());
  }
}
