package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EventDiffblueTest {
  /**
   * Method under test: {@link Event.OrderAccepted#accept(EventVisitor)}
   */
  @Test
  void testOrderAcceptedAccept() {
    // Arrange
    Event.OrderAccepted orderAccepted = new Event.OrderAccepted(new POE.OrderAccepted());
    DefaultEventVisitor visitor = mock(DefaultEventVisitor.class);
    doNothing().when(visitor).visit(Mockito.<Event.OrderAccepted>any());

    // Act
    orderAccepted.accept(visitor);

    // Assert
    verify(visitor).visit(isA(Event.OrderAccepted.class));
  }

  /**
   * Method under test:
   * {@link Event.OrderAccepted#OrderAccepted(POE.OrderAccepted)}
   */
  @Test
  void testOrderAcceptedNewOrderAccepted() {
    // Arrange and Act
    Event.OrderAccepted actualOrderAccepted = new Event.OrderAccepted(new POE.OrderAccepted());
    actualOrderAccepted.accept(new DefaultEventVisitor());

    // Assert that nothing has changed
    assertEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderAccepted.orderId);
    assertEquals(0L, actualOrderAccepted.instrument);
    assertEquals(0L, actualOrderAccepted.orderNumber);
    assertEquals(0L, actualOrderAccepted.price);
    assertEquals(0L, actualOrderAccepted.quantity);
    assertEquals(0L, actualOrderAccepted.timestamp);
    assertEquals((byte) 0, actualOrderAccepted.side);
  }

  /**
   * Method under test: {@link Event.OrderCanceled#accept(EventVisitor)}
   */
  @Test
  void testOrderCanceledAccept() {
    // Arrange
    Event.OrderCanceled orderCanceled = new Event.OrderCanceled(new POE.OrderCanceled());
    DefaultEventVisitor visitor = mock(DefaultEventVisitor.class);
    doNothing().when(visitor).visit(Mockito.<Event.OrderCanceled>any());

    // Act
    orderCanceled.accept(visitor);

    // Assert
    verify(visitor).visit(isA(Event.OrderCanceled.class));
  }

  /**
   * Method under test:
   * {@link Event.OrderCanceled#OrderCanceled(POE.OrderCanceled)}
   */
  @Test
  void testOrderCanceledNewOrderCanceled() {
    // Arrange and Act
    Event.OrderCanceled actualOrderCanceled = new Event.OrderCanceled(new POE.OrderCanceled());
    actualOrderCanceled.accept(new DefaultEventVisitor());

    // Assert that nothing has changed
    assertEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderCanceled.orderId);
    assertEquals(0L, actualOrderCanceled.canceledQuantity);
    assertEquals(0L, actualOrderCanceled.timestamp);
    assertEquals((byte) 0, actualOrderCanceled.reason);
  }

  /**
   * Method under test: {@link Event.OrderExecuted#accept(EventVisitor)}
   */
  @Test
  void testOrderExecutedAccept() {
    // Arrange
    Event.OrderExecuted orderExecuted = new Event.OrderExecuted(new POE.OrderExecuted());
    DefaultEventVisitor visitor = mock(DefaultEventVisitor.class);
    doNothing().when(visitor).visit(Mockito.<Event.OrderExecuted>any());

    // Act
    orderExecuted.accept(visitor);

    // Assert
    verify(visitor).visit(isA(Event.OrderExecuted.class));
  }

  /**
   * Method under test:
   * {@link Event.OrderExecuted#OrderExecuted(POE.OrderExecuted)}
   */
  @Test
  void testOrderExecutedNewOrderExecuted() {
    // Arrange and Act
    Event.OrderExecuted actualOrderExecuted = new Event.OrderExecuted(new POE.OrderExecuted());
    actualOrderExecuted.accept(new DefaultEventVisitor());

    // Assert that nothing has changed
    assertEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderExecuted.orderId);
    assertEquals(0L, actualOrderExecuted.matchNumber);
    assertEquals(0L, actualOrderExecuted.price);
    assertEquals(0L, actualOrderExecuted.quantity);
    assertEquals(0L, actualOrderExecuted.timestamp);
    assertEquals((byte) 0, actualOrderExecuted.liquidityFlag);
  }

  /**
   * Method under test: {@link Event.OrderRejected#accept(EventVisitor)}
   */
  @Test
  void testOrderRejectedAccept() {
    // Arrange
    Event.OrderRejected orderRejected = new Event.OrderRejected(new POE.OrderRejected());
    DefaultEventVisitor visitor = mock(DefaultEventVisitor.class);
    doNothing().when(visitor).visit(Mockito.<Event.OrderRejected>any());

    // Act
    orderRejected.accept(visitor);

    // Assert
    verify(visitor).visit(isA(Event.OrderRejected.class));
  }

  /**
   * Method under test:
   * {@link Event.OrderRejected#OrderRejected(POE.OrderRejected)}
   */
  @Test
  void testOrderRejectedNewOrderRejected() {
    // Arrange and Act
    Event.OrderRejected actualOrderRejected = new Event.OrderRejected(new POE.OrderRejected());
    actualOrderRejected.accept(new DefaultEventVisitor());

    // Assert that nothing has changed
    assertEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderRejected.orderId);
    assertEquals(0L, actualOrderRejected.timestamp);
    assertEquals((byte) 0, actualOrderRejected.reason);
  }
}
