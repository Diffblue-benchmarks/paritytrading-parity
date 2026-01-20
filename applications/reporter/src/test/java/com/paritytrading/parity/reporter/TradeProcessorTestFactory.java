package com.paritytrading.parity.reporter;

import com.diffblue.cover.annotations.InterestingTestFactory;
import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.pmr.PMR;

/**
 * Factory class for creating test instances of TradeProcessor with pre-populated state.
 */
public class TradeProcessorTestFactory {

    /**
     * Creates a TradeProcessor with a simple no-op listener for general testing.
     * This prevents NullPointerException when constructing DisplayFormat which requires
     * a non-null Instruments object.
     *
     * @return a TradeProcessor instance with a simple listener
     */
    @InterestingTestFactory
    public static TradeProcessor createTradeProcessor() {
        return new TradeProcessor(new TradeListener() {
            @Override
            void trade(Trade event) {
                // No-op listener for testing
            }
        });
    }

    /**
     * Creates a TradeProcessor with pre-populated orders for testing orderCanceled.
     * The factory ensures that an order exists in the internal orders map,
     * preventing NullPointerException at TradeProcessor.java:62.
     *
     * @return a TradeProcessor instance with a pre-populated order
     */
    @InterestingTestFactory
    public static TradeProcessor createTradeProcessorForOrderCanceled() {
        TradeProcessor processor = new TradeProcessor(new TradeListener() {
            @Override
            void trade(Trade event) {
                // No-op listener for testing
            }
        });

        // Pre-populate an order that can be canceled
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000000000L;
        orderEntered.username = ASCII.packLong("USER1");
        orderEntered.orderNumber = 1L;
        orderEntered.side = PMR.BUY;
        orderEntered.instrument = ASCII.packLong("INST1");
        orderEntered.quantity = 100L;
        orderEntered.price = 1000L;

        processor.orderEntered(orderEntered);

        return processor;
    }

    /**
     * Creates a TradeProcessor for testing the orderAdded method.
     * This method is currently a no-op, but this factory provides a proper instance
     * for Cover to test against.
     *
     * @return a TradeProcessor instance with a simple listener
     */
    @InterestingTestFactory
    public static TradeProcessor createTradeProcessorForOrderAdded() {
        return new TradeProcessor(new TradeListener() {
            @Override
            void trade(Trade event) {
                // No-op listener for testing
            }
        });
    }

    /**
     * Creates a TradeProcessor with pre-populated orders for testing trade.
     * The factory ensures that both resting and incoming orders exist in the internal orders map,
     * preventing NullPointerException at TradeProcessor.java:73.
     *
     * @return a TradeProcessor instance with pre-populated resting and incoming orders
     */
    @InterestingTestFactory
    public static TradeProcessor createTradeProcessorForTrade() {
        TradeProcessor processor = new TradeProcessor(new TradeListener() {
            @Override
            void trade(Trade event) {
                // No-op listener for testing
            }
        });

        // Pre-populate resting order (buy side)
        PMR.OrderEntered restingOrder = new PMR.OrderEntered();
        restingOrder.timestamp = 1000000000L;
        restingOrder.username = ASCII.packLong("BUYER");
        restingOrder.orderNumber = 1L;
        restingOrder.side = PMR.BUY;
        restingOrder.instrument = ASCII.packLong("INST1");
        restingOrder.quantity = 100L;
        restingOrder.price = 1000L;

        processor.orderEntered(restingOrder);

        // Pre-populate incoming order (sell side)
        PMR.OrderEntered incomingOrder = new PMR.OrderEntered();
        incomingOrder.timestamp = 1000000001L;
        incomingOrder.username = ASCII.packLong("SELLER");
        incomingOrder.orderNumber = 2L;
        incomingOrder.side = PMR.SELL;
        incomingOrder.instrument = ASCII.packLong("INST1");
        incomingOrder.quantity = 100L;
        incomingOrder.price = 1000L;

        processor.orderEntered(incomingOrder);

        return processor;
    }
}
