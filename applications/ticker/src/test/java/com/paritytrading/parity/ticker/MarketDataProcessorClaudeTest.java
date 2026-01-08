/*
 * Copyright 2014 Parity authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.*;
import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.net.pmd.PMD;
import com.paritytrading.parity.net.pmd.PMD.OrderAdded;
import com.paritytrading.parity.net.pmd.PMD.OrderCanceled;
import com.paritytrading.parity.net.pmd.PMD.OrderExecuted;
import com.paritytrading.parity.net.pmd.PMD.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.util.ArrayList;
import java.util.List;

class MarketDataProcessorClaudeTest {
  private Market market;

  private TestMarketDataListener listener;

  private MarketDataProcessor processor;

  @BeforeEach
  void setUp() {
    listener = new TestMarketDataListener();
    market = new Market(listener);
    processor = new MarketDataProcessor(market, listener);
  }

  @Test
  void testConstructorInitializesProcessor() {
    // Test that the constructor creates a valid processor
    assertNotNull(processor, "Processor should not be null");

    // Verify processor can receive messages without throwing
    Version version = new Version();
    version.version = PMD.VERSION;
    assertDoesNotThrow(
        () -> processor.version(version),
        "Processor should handle version message after construction");
  }

  @Test
  void testVersionWithCorrectVersion() {
    // Test with the correct protocol version
    Version version = new Version();
    version.version = PMD.VERSION;

    // Should not throw when version matches
    assertDoesNotThrow(
        () -> processor.version(version),
        "Should not throw error when version matches PMD.VERSION");
  }

  @Test
  void testOrderAddedWithBuyOrder() {
    // Setup: Open an order book for the instrument
    long instrument = 1000L;
    market.open(instrument);

    // Create an OrderAdded message for a buy order
    OrderAdded orderAdded = new OrderAdded();
    orderAdded.timestamp = 123456789L;
    orderAdded.orderNumber = 1L;
    orderAdded.side = PMD.BUY;
    orderAdded.instrument = instrument;
    orderAdded.quantity = 100L;
    orderAdded.price = 5000L;

    // Process the order
    processor.orderAdded(orderAdded);

    // Verify timestamp was set on the listener
    assertEquals(
        123456789L / 1_000_000, listener.timestampMillis(), "Timestamp should be set on listener");

    // Verify the order was added to the market
    assertNotNull(market.find(1L), "Order should be added to the market");

    // Verify update was called
    assertTrue(listener.updates.size() > 0, "Update should have been called");
  }

  @Test
  void testOrderAddedWithSellOrder() {
    // Setup: Open an order book for the instrument
    long instrument = 2000L;
    market.open(instrument);

    // Create an OrderAdded message for a sell order
    OrderAdded orderAdded = new OrderAdded();
    orderAdded.timestamp = 987654321L;
    orderAdded.orderNumber = 2L;
    orderAdded.side = PMD.SELL;
    orderAdded.instrument = instrument;
    orderAdded.quantity = 200L;
    orderAdded.price = 6000L;

    // Process the order
    processor.orderAdded(orderAdded);

    // Verify timestamp was set on the listener
    assertEquals(
        987654321L / 1_000_000, listener.timestampMillis(), "Timestamp should be set on listener");

    // Verify the order was added to the market
    assertNotNull(market.find(2L), "Order should be added to the market");

    // Verify update was called
    assertTrue(listener.updates.size() > 0, "Update should have been called");
  }

  @Test
  void testOrderAddedToClosedBook() {
    // Create an OrderAdded message for an instrument without an open book
    OrderAdded orderAdded = new OrderAdded();
    orderAdded.timestamp = 111111111L;
    orderAdded.orderNumber = 3L;
    orderAdded.side = PMD.BUY;

    // No book opened for this instrument
    orderAdded.instrument = 9999L;
    orderAdded.quantity = 50L;
    orderAdded.price = 4000L;

    // Process the order
    processor.orderAdded(orderAdded);

    // Verify timestamp was still set on the listener
    assertEquals(
        111111111L / 1_000_000,
        listener.timestampMillis(),
        "Timestamp should be set on listener even if book is closed");

    // Verify the order was NOT added to the market (book doesn't exist)
    assertNull(market.find(3L), "Order should not be added when book is closed");
  }

  @Test
  void testOrderExecuted() {
    // Setup: Open an order book and add an order
    long instrument = 3000L;
    market.open(instrument);
    market.add(instrument, 10L, Side.BUY, 5000L, 100L);

    // Create an OrderExecuted message
    OrderExecuted orderExecuted = new OrderExecuted();
    orderExecuted.timestamp = 222222222L;
    orderExecuted.orderNumber = 10L;
    orderExecuted.quantity = 50L;
    orderExecuted.matchNumber = 1L;

    // Process the execution
    processor.orderExecuted(orderExecuted);

    // Verify timestamp was set on the listener
    assertEquals(
        222222222L / 1_000_000, listener.timestampMillis(), "Timestamp should be set on listener");

    // Verify the order still exists with reduced quantity
    assertNotNull(market.find(10L), "Order should still exist after partial execution");

    // Verify trade was recorded
    assertTrue(listener.trades.size() > 0, "Trade should have been recorded");

    // Verify update was called
    assertTrue(listener.updates.size() > 0, "Update should have been called");
  }

  @Test
  void testOrderExecutedCompletely() {
    // Setup: Open an order book and add an order
    long instrument = 4000L;
    market.open(instrument);
    market.add(instrument, 20L, Side.SELL, 6000L, 100L);

    // Create an OrderExecuted message that executes the entire quantity
    OrderExecuted orderExecuted = new OrderExecuted();
    orderExecuted.timestamp = 333333333L;
    orderExecuted.orderNumber = 20L;

    // Execute entire quantity
    orderExecuted.quantity = 100L;
    orderExecuted.matchNumber = 2L;

    // Process the execution
    processor.orderExecuted(orderExecuted);

    // Verify timestamp was set on the listener
    assertEquals(
        333333333L / 1_000_000, listener.timestampMillis(), "Timestamp should be set on listener");

    // Verify the order no longer exists
    assertNull(market.find(20L), "Order should be removed after full execution");

    // Verify trade was recorded
    assertTrue(listener.trades.size() > 0, "Trade should have been recorded");
  }

  @Test
  void testOrderExecutedForNonExistentOrder() {
    // Create an OrderExecuted message for an order that doesn't exist
    OrderExecuted orderExecuted = new OrderExecuted();
    orderExecuted.timestamp = 444444444L;

    // Non-existent order
    orderExecuted.orderNumber = 999L;
    orderExecuted.quantity = 50L;
    orderExecuted.matchNumber = 3L;

    // Process the execution
    processor.orderExecuted(orderExecuted);

    // Verify timestamp was still set on the listener
    assertEquals(
        444444444L / 1_000_000,
        listener.timestampMillis(),
        "Timestamp should be set on listener even for non-existent order");

    // Verify no trade was recorded (order doesn't exist)
    assertEquals(0, listener.trades.size(), "No trade should be recorded for non-existent order");
  }

  @Test
  void testOrderCanceled() {
    // Setup: Open an order book and add an order
    long instrument = 5000L;
    market.open(instrument);
    market.add(instrument, 30L, Side.BUY, 5500L, 100L);

    // Create an OrderCanceled message
    OrderCanceled orderCanceled = new OrderCanceled();
    orderCanceled.timestamp = 555555555L;
    orderCanceled.orderNumber = 30L;
    orderCanceled.canceledQuantity = 40L;

    // Process the cancellation
    processor.orderCanceled(orderCanceled);

    // Verify timestamp was set on the listener
    assertEquals(
        555555555L / 1_000_000, listener.timestampMillis(), "Timestamp should be set on listener");

    // Verify the order still exists with reduced quantity
    assertNotNull(market.find(30L), "Order should still exist after partial cancellation");

    // Verify update was called
    assertTrue(listener.updates.size() > 0, "Update should have been called");
  }

  @Test
  void testOrderCanceledCompletely() {
    // Setup: Open an order book and add an order
    long instrument = 6000L;
    market.open(instrument);
    market.add(instrument, 40L, Side.SELL, 6500L, 100L);

    // Create an OrderCanceled message that cancels the entire quantity
    OrderCanceled orderCanceled = new OrderCanceled();
    orderCanceled.timestamp = 666666666L;
    orderCanceled.orderNumber = 40L;

    // Cancel entire quantity
    orderCanceled.canceledQuantity = 100L;

    // Process the cancellation
    processor.orderCanceled(orderCanceled);

    // Verify timestamp was set on the listener
    assertEquals(
        666666666L / 1_000_000, listener.timestampMillis(), "Timestamp should be set on listener");

    // Verify the order no longer exists
    assertNull(market.find(40L), "Order should be removed after full cancellation");

    // Verify update was called
    assertTrue(listener.updates.size() > 0, "Update should have been called");
  }

  @Test
  void testOrderCanceledForNonExistentOrder() {
    // Create an OrderCanceled message for an order that doesn't exist
    OrderCanceled orderCanceled = new OrderCanceled();
    orderCanceled.timestamp = 777777777L;

    // Non-existent order
    orderCanceled.orderNumber = 888L;
    orderCanceled.canceledQuantity = 50L;

    // Process the cancellation
    processor.orderCanceled(orderCanceled);

    // Verify timestamp was still set on the listener
    assertEquals(
        777777777L / 1_000_000,
        listener.timestampMillis(),
        "Timestamp should be set on listener even for non-existent order");

    // Verify no update was called (order doesn't exist)
    assertEquals(0, listener.updates.size(), "No update should be called for non-existent order");
  }

  @Test
  void testMultipleOrdersSequence() {
    // Test a realistic sequence of operations
    long instrument = 7000L;
    market.open(instrument);

    // Add a buy order
    OrderAdded buyOrder = new OrderAdded();
    buyOrder.timestamp = 100000000L;
    buyOrder.orderNumber = 50L;
    buyOrder.side = PMD.BUY;
    buyOrder.instrument = instrument;
    buyOrder.quantity = 100L;
    buyOrder.price = 5000L;
    processor.orderAdded(buyOrder);

    // Add a sell order
    OrderAdded sellOrder = new OrderAdded();
    sellOrder.timestamp = 200000000L;
    sellOrder.orderNumber = 51L;
    sellOrder.side = PMD.SELL;
    sellOrder.instrument = instrument;
    sellOrder.quantity = 150L;
    sellOrder.price = 5100L;
    processor.orderAdded(sellOrder);

    // Execute part of the buy order
    OrderExecuted executed = new OrderExecuted();
    executed.timestamp = 300000000L;
    executed.orderNumber = 50L;
    executed.quantity = 30L;
    executed.matchNumber = 1L;
    processor.orderExecuted(executed);

    // Cancel part of the sell order
    OrderCanceled canceled = new OrderCanceled();
    canceled.timestamp = 400000000L;
    canceled.orderNumber = 51L;
    canceled.canceledQuantity = 50L;
    processor.orderCanceled(canceled);

    // Verify both orders still exist
    assertNotNull(market.find(50L), "Buy order should still exist");
    assertNotNull(market.find(51L), "Sell order should still exist");

    // Verify final timestamp
    assertEquals(
        400000000L / 1_000_000,
        listener.timestampMillis(),
        "Final timestamp should be from last operation");
  }

  /**
   * Concrete implementation of MarketDataListener for testing. Tracks all updates and trades for
   * verification.
   */
  private static class TestMarketDataListener extends MarketDataListener {
    List<UpdateEvent> updates = new ArrayList<>();

    List<TradeEvent> trades = new ArrayList<>();

    @Override
    public void update(OrderBook book, boolean bbo) {
      updates.add(new UpdateEvent(book, bbo));
    }

    @Override
    public void trade(OrderBook book, Side side, long price, long size) {
      trades.add(new TradeEvent(book, side, price, size));
    }
  }

  private static class UpdateEvent {
    final OrderBook book;

    final boolean bbo;

    UpdateEvent(OrderBook book, boolean bbo) {
      this.book = book;
      this.bbo = bbo;
    }
  }

  private static class TradeEvent {
    final OrderBook book;

    final Side side;

    final long price;

    final long size;

    TradeEvent(OrderBook book, Side side, long price, long size) {
      this.book = book;
      this.side = side;
      this.price = price;
      this.size = size;
    }
  }
}
