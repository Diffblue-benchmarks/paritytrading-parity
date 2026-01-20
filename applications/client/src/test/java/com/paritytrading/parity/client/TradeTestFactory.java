package com.paritytrading.parity.client;

import com.diffblue.cover.annotations.InterestingTestFactory;
import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;

/**
 * Test factory for creating Trade instances for unit testing.
 */
public class TradeTestFactory {

    /**
     * Creates a Trade instance for testing.
     * This factory creates a Trade with sample data to avoid
     * sandboxing policy violations that might occur during
     * normal instantiation.
     *
     * @return a Trade instance for testing
     */
    @InterestingTestFactory
    public static Trade createTrade() {
        // Create a POE.OrderAccepted message with sample data
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L; // Fixed timestamp to avoid System.currentTimeMillis()
        ASCII.putLeft(acceptedMessage.orderId, "TEST-ORDER-1");
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = ASCII.packLong("TEST");
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 10000L;
        acceptedMessage.orderNumber = 1L;

        // Create an Event.OrderAccepted from the POE message
        Event.OrderAccepted orderAcceptedEvent = new Event.OrderAccepted(acceptedMessage);

        // Create an Order from the event
        Order order = new Order(orderAcceptedEvent);

        // Create a POE.OrderExecuted message with sample data
        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 1000000000L; // Fixed timestamp to avoid System.currentTimeMillis()
        ASCII.putLeft(executedMessage.orderId, "TEST-ORDER-1");
        executedMessage.quantity = 500L;
        executedMessage.price = 10000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        // Create an Event.OrderExecuted from the POE message
        Event.OrderExecuted orderExecutedEvent = new Event.OrderExecuted(executedMessage);

        // Create and return the Trade
        return new Trade(order, orderExecutedEvent);
    }

    /**
     * Creates a Trade instance with specific parameters for testing.
     * This factory allows more control over the trade properties.
     *
     * @param orderId the order ID
     * @param side the order side (BUY or SELL)
     * @param instrument the instrument identifier
     * @param quantity the trade quantity
     * @param price the trade price
     * @return a Trade instance for testing
     */
    @InterestingTestFactory
    public static Trade createTradeWithParameters(String orderId, byte side,
            long instrument, long quantity, long price) {
        // Create a POE.OrderAccepted message with provided data
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L; // Fixed timestamp to avoid System.currentTimeMillis()
        ASCII.putLeft(acceptedMessage.orderId, orderId);
        acceptedMessage.side = side;
        acceptedMessage.instrument = instrument;
        acceptedMessage.quantity = quantity;
        acceptedMessage.price = price;
        acceptedMessage.orderNumber = 1L;

        // Create an Event.OrderAccepted from the POE message
        Event.OrderAccepted orderAcceptedEvent = new Event.OrderAccepted(acceptedMessage);

        // Create an Order from the event
        Order order = new Order(orderAcceptedEvent);

        // Create a POE.OrderExecuted message with provided data
        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 1000000000L; // Fixed timestamp to avoid System.currentTimeMillis()
        ASCII.putLeft(executedMessage.orderId, orderId);
        executedMessage.quantity = quantity;
        executedMessage.price = price;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        // Create an Event.OrderExecuted from the POE message
        Event.OrderExecuted orderExecutedEvent = new Event.OrderExecuted(executedMessage);

        // Create and return the Trade
        return new Trade(order, orderExecutedEvent);
    }

    /**
     * Creates a Trade instance with all parameters including timestamp for testing.
     * This factory allows complete control over all trade properties.
     *
     * @param orderId the order ID
     * @param side the order side (BUY or SELL)
     * @param instrument the instrument identifier
     * @param quantity the trade quantity
     * @param price the trade price
     * @param timestamp the timestamp in nanoseconds
     * @return a Trade instance for testing
     */
    @InterestingTestFactory
    public static Trade createTradeWithTimestamp(String orderId, byte side,
            long instrument, long quantity, long price, long timestamp) {
        // Create a POE.OrderAccepted message with provided data
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = timestamp;
        ASCII.putLeft(acceptedMessage.orderId, orderId);
        acceptedMessage.side = side;
        acceptedMessage.instrument = instrument;
        acceptedMessage.quantity = quantity;
        acceptedMessage.price = price;
        acceptedMessage.orderNumber = 1L;

        // Create an Event.OrderAccepted from the POE message
        Event.OrderAccepted orderAcceptedEvent = new Event.OrderAccepted(acceptedMessage);

        // Create an Order from the event
        Order order = new Order(orderAcceptedEvent);

        // Create a POE.OrderExecuted message with provided data
        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = timestamp;
        ASCII.putLeft(executedMessage.orderId, orderId);
        executedMessage.quantity = quantity;
        executedMessage.price = price;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        // Create an Event.OrderExecuted from the POE message
        Event.OrderExecuted orderExecutedEvent = new Event.OrderExecuted(executedMessage);

        // Create and return the Trade
        return new Trade(order, orderExecutedEvent);
    }
}
