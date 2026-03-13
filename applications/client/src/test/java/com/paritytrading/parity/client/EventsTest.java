package com.paritytrading.parity.client;

import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EventsTest {

    @Test
    public void testConstructor() {
        Events events = new Events();
        EventVisitor visitor = Mockito.mock(EventVisitor.class);

        events.accept(visitor);

        Mockito.verifyNoInteractions(visitor);
    }

    @Test
    public void testAcceptWithSingleEvent() {
        Events events = new Events();

        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 123456789L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.side = (byte) 'B';
        message.instrument = 100L;
        message.quantity = 1000L;
        message.price = 5000L;
        message.orderNumber = 42L;

        events.orderAccepted(message);

        EventVisitor visitor = Mockito.mock(EventVisitor.class);
        events.accept(visitor);

        verify(visitor, times(1)).visit(Mockito.any(Event.OrderAccepted.class));
    }

    @Test
    public void testAcceptWithMultipleEvents() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 123456789L;
        acceptedMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        acceptedMessage.side = (byte) 'B';
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 42L;

        POE.OrderRejected rejectedMessage = new POE.OrderRejected();
        rejectedMessage.timestamp = 123456790L;
        rejectedMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        rejectedMessage.reason = (byte) 'I';

        events.orderAccepted(acceptedMessage);
        events.orderRejected(rejectedMessage);

        EventVisitor visitor = Mockito.mock(EventVisitor.class);
        events.accept(visitor);

        verify(visitor, times(1)).visit(Mockito.any(Event.OrderAccepted.class));
        verify(visitor, times(1)).visit(Mockito.any(Event.OrderRejected.class));
    }

    @Test
    public void testOrderAccepted() {
        Events events = new Events();

        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 123456789L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.side = (byte) 'B';
        message.instrument = 100L;
        message.quantity = 1000L;
        message.price = 5000L;
        message.orderNumber = 42L;

        events.orderAccepted(message);

        EventVisitor visitor = Mockito.mock(EventVisitor.class);
        events.accept(visitor);

        verify(visitor).visit(Mockito.any(Event.OrderAccepted.class));
    }

    @Test
    public void testOrderRejected() {
        Events events = new Events();

        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 123456789L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.reason = (byte) 'I';

        events.orderRejected(message);

        EventVisitor visitor = Mockito.mock(EventVisitor.class);
        events.accept(visitor);

        verify(visitor).visit(Mockito.any(Event.OrderRejected.class));
    }

    @Test
    public void testOrderExecuted() {
        Events events = new Events();

        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 123456789L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.quantity = 500L;
        message.price = 5000L;
        message.liquidityFlag = (byte) 'A';
        message.matchNumber = 1L;

        events.orderExecuted(message);

        EventVisitor visitor = Mockito.mock(EventVisitor.class);
        events.accept(visitor);

        verify(visitor).visit(Mockito.any(Event.OrderExecuted.class));
    }

    @Test
    public void testOrderCanceled() {
        Events events = new Events();

        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 123456789L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.canceledQuantity = 300L;
        message.reason = (byte) 'U';

        events.orderCanceled(message);

        EventVisitor visitor = Mockito.mock(EventVisitor.class);
        events.accept(visitor);

        verify(visitor).visit(Mockito.any(Event.OrderCanceled.class));
    }
}
