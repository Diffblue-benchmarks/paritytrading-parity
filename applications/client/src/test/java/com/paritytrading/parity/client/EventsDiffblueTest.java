package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POE;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class EventsDiffblueTest {

    /**
     * Test {@link Events#orderAccepted(POE.OrderAccepted)}.
     *
     * <p>Method under test: {@link Events#orderAccepted(POE.OrderAccepted)}
     */
    @Test
    @DisplayName("Test orderAccepted(POE.OrderAccepted); then accept visits OrderAccepted event")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void Events.orderAccepted(POE.OrderAccepted)"})
    void testOrderAccepted_thenAcceptVisitsOrderAcceptedEvent() {
        // Arrange
        Events events = new Events();
        POE.OrderAccepted message = new POE.OrderAccepted();
        EventVisitor visitor = mock(EventVisitor.class);

        // Act
        events.orderAccepted(message);
        events.accept(visitor);

        // Assert
        verify(visitor).visit((Event.OrderAccepted) org.mockito.ArgumentMatchers.any());
        verifyNoMoreInteractions(visitor);
    }

    /**
     * Test {@link Events#orderRejected(POE.OrderRejected)}.
     *
     * <p>Method under test: {@link Events#orderRejected(POE.OrderRejected)}
     */
    @Test
    @DisplayName("Test orderRejected(POE.OrderRejected); then accept visits OrderRejected event")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void Events.orderRejected(POE.OrderRejected)"})
    void testOrderRejected_thenAcceptVisitsOrderRejectedEvent() {
        // Arrange
        Events events = new Events();
        POE.OrderRejected message = new POE.OrderRejected();
        EventVisitor visitor = mock(EventVisitor.class);

        // Act
        events.orderRejected(message);
        events.accept(visitor);

        // Assert
        verify(visitor).visit((Event.OrderRejected) org.mockito.ArgumentMatchers.any());
        verifyNoMoreInteractions(visitor);
    }

    /**
     * Test {@link Events#orderExecuted(POE.OrderExecuted)}.
     *
     * <p>Method under test: {@link Events#orderExecuted(POE.OrderExecuted)}
     */
    @Test
    @DisplayName("Test orderExecuted(POE.OrderExecuted); then accept visits OrderExecuted event")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void Events.orderExecuted(POE.OrderExecuted)"})
    void testOrderExecuted_thenAcceptVisitsOrderExecutedEvent() {
        // Arrange
        Events events = new Events();
        POE.OrderExecuted message = new POE.OrderExecuted();
        EventVisitor visitor = mock(EventVisitor.class);

        // Act
        events.orderExecuted(message);
        events.accept(visitor);

        // Assert
        verify(visitor).visit((Event.OrderExecuted) org.mockito.ArgumentMatchers.any());
        verifyNoMoreInteractions(visitor);
    }

    /**
     * Test {@link Events#orderCanceled(POE.OrderCanceled)}.
     *
     * <p>Method under test: {@link Events#orderCanceled(POE.OrderCanceled)}
     */
    @Test
    @DisplayName("Test orderCanceled(POE.OrderCanceled); then accept visits OrderCanceled event")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void Events.orderCanceled(POE.OrderCanceled)"})
    void testOrderCanceled_thenAcceptVisitsOrderCanceledEvent() {
        // Arrange
        Events events = new Events();
        POE.OrderCanceled message = new POE.OrderCanceled();
        EventVisitor visitor = mock(EventVisitor.class);

        // Act
        events.orderCanceled(message);
        events.accept(visitor);

        // Assert
        verify(visitor).visit((Event.OrderCanceled) org.mockito.ArgumentMatchers.any());
        verifyNoMoreInteractions(visitor);
    }

    /**
     * Test {@link Events#accept(EventVisitor)} with multiple events.
     *
     * <p>Method under test: {@link Events#accept(EventVisitor)}
     */
    @Test
    @DisplayName("Test accept(EventVisitor); given multiple events added; then visitor visits all events")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void Events.accept(EventVisitor)"})
    void testAccept_givenMultipleEventsAdded_thenVisitorVisitsAllEvents() {
        // Arrange
        Events events = new Events();
        events.orderAccepted(new POE.OrderAccepted());
        events.orderRejected(new POE.OrderRejected());
        EventVisitor visitor = mock(EventVisitor.class);

        // Act
        events.accept(visitor);

        // Assert
        verify(visitor).visit((Event.OrderAccepted) org.mockito.ArgumentMatchers.any());
        verify(visitor).visit((Event.OrderRejected) org.mockito.ArgumentMatchers.any());
        verifyNoMoreInteractions(visitor);
    }

    /**
     * Test {@link Events#accept(EventVisitor)} with empty events list.
     *
     * <p>Method under test: {@link Events#accept(EventVisitor)}
     */
    @Test
    @DisplayName("Test accept(EventVisitor); given no events added; then visitor is not called")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void Events.accept(EventVisitor)"})
    void testAccept_givenNoEventsAdded_thenVisitorIsNotCalled() {
        // Arrange
        Events events = new Events();
        final AtomicBoolean visited = new AtomicBoolean(false);
        EventVisitor visitor = new DefaultEventVisitor() {
            @Override
            public void visit(Event.OrderAccepted event) {
                visited.set(true);
            }
        };

        // Act
        events.accept(visitor);

        // Assert
        assertTrue(!visited.get());
    }
}
