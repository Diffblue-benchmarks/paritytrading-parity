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

class OrdersDiffblueTest {
  /**
   * Test {@link Orders#collect(Events)}.
   *
   * <ul>
   *   <li>When {@link Events} (default constructor).
   *   <li>Then return Empty.
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when Events (default constructor); then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenEvents_thenReturnEmpty() {
    // Arrange and Act
    List<Order> actualCollectResult = Orders.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }

  /**
   * Test {@link Orders#getOrders()}.
   *
   * <p>Method under test: {@link Orders#getOrders()}
   */
  @Test
  @DisplayName("Test getOrders()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Map Orders.getOrders()"})
  void testGetOrders() {
    // Arrange, Act and Assert
    assertTrue(OrdersTestFactory.createOrders().getOrders().isEmpty());
  }

  /**
   * Test {@link Orders#visit(OrderAccepted)} with {@code OrderAccepted}.
   *
   * <ul>
   *   <li>Then createOrders Orders size is one.
   * </ul>
   *
   * <p>Method under test: {@link Orders#visit(OrderAccepted)}
   */
  @Test
  @DisplayName(
      "Test visit(OrderAccepted) with 'OrderAccepted'; then createOrders Orders size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Orders.visit(OrderAccepted)"})
  void testVisitWithOrderAccepted_thenCreateOrdersOrdersSizeIsOne() {
    // Arrange
    Orders createOrdersResult = OrdersTestFactory.createOrders();

    // Act
    createOrdersResult.visit(new OrderAccepted(new POE.OrderAccepted()));

    // Assert
    Map orders = createOrdersResult.getOrders();
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
