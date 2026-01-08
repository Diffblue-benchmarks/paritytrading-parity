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
package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.*;
import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.pmr.PMR;
import com.paritytrading.parity.net.pmr.PMR.OrderAdded;
import com.paritytrading.parity.net.pmr.PMR.OrderCanceled;
import com.paritytrading.parity.net.pmr.PMR.OrderEntered;
import com.paritytrading.parity.net.pmr.PMR.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class TradeProcessorClaudeTest {
  private TestTradeListener listener;

  private TradeProcessor processor;

  // Concrete implementation of TradeListener for testing
  private static class TestTradeListener extends TradeListener {
    private final List<Trade> receivedTrades = new ArrayList<>();

    @Override
    void trade(Trade event) {
      // Store a copy of the trade event
      Trade copy = new Trade();
      copy.timestamp = event.timestamp;
      copy.matchNumber = event.matchNumber;
      copy.instrument = event.instrument;
      copy.quantity = event.quantity;
      copy.price = event.price;
      copy.buyer = event.buyer;
      copy.buyOrderNumber = event.buyOrderNumber;
      copy.seller = event.seller;
      copy.sellOrderNumber = event.sellOrderNumber;
      receivedTrades.add(copy);
    }

    List<Trade> getReceivedTrades() {
      return receivedTrades;
    }

    void clear() {
      receivedTrades.clear();
    }
  }

  @BeforeEach
  void setUp() {
    listener = new TestTradeListener();
    processor = new TradeProcessor(listener);
  }

  @Test
  void constructorCreatesProcessor() {
    assertNotNull(processor);
    assertNotNull(new TradeProcessor(listener));
  }

  @Test
  void constructorWithNullListener() {
    assertDoesNotThrow(() -> new TradeProcessor(null));
  }

  @Test
  void versionWithCorrectVersion() {
    Version version = new Version();
    version.version = PMR.VERSION;

    assertDoesNotThrow(() -> processor.version(version));
  }

  @Test
  void orderEnteredStoresOrder() {
    OrderEntered orderEntered = new OrderEntered();
    orderEntered.timestamp = 1000000000L;
    orderEntered.username = ASCII.packLong("user1");
    orderEntered.orderNumber = 12345L;
    orderEntered.side = PMR.BUY;
    orderEntered.instrument = ASCII.packLong("AAPL");
    orderEntered.quantity = 100L;
    orderEntered.price = 15000L;

    assertDoesNotThrow(() -> processor.orderEntered(orderEntered));
  }

  @Test
  void orderEnteredWithMultipleOrders() {
    OrderEntered order1 = new OrderEntered();
    order1.orderNumber = 1L;
    order1.username = ASCII.packLong("user1");
    order1.side = PMR.BUY;
    order1.instrument = ASCII.packLong("AAPL");
    order1.quantity = 100L;
    order1.price = 15000L;

    OrderEntered order2 = new OrderEntered();
    order2.orderNumber = 2L;
    order2.username = ASCII.packLong("user2");
    order2.side = PMR.SELL;
    order2.instrument = ASCII.packLong("MSFT");
    order2.quantity = 200L;
    order2.price = 20000L;

    assertDoesNotThrow(
        () -> {
          processor.orderEntered(order1);
          processor.orderEntered(order2);
        });
  }

  @Test
  void orderAddedDoesNothing() {
    OrderAdded orderAdded = new OrderAdded();
    orderAdded.timestamp = 1000000000L;
    orderAdded.orderNumber = 12345L;

    assertDoesNotThrow(() -> processor.orderAdded(orderAdded));
  }

  @Test
  void orderCanceledPartialCancel() {
    // First enter an order
    OrderEntered orderEntered = new OrderEntered();
    orderEntered.orderNumber = 100L;
    orderEntered.username = ASCII.packLong("user1");
    orderEntered.side = PMR.BUY;
    orderEntered.instrument = ASCII.packLong("AAPL");
    orderEntered.quantity = 100L;
    orderEntered.price = 15000L;
    processor.orderEntered(orderEntered);

    // Cancel part of the order
    OrderCanceled orderCanceled = new OrderCanceled();
    orderCanceled.orderNumber = 100L;
    orderCanceled.canceledQuantity = 50L;

    assertDoesNotThrow(() -> processor.orderCanceled(orderCanceled));
  }

  @Test
  void orderCanceledFullCancel() {
    // First enter an order
    OrderEntered orderEntered = new OrderEntered();
    orderEntered.orderNumber = 100L;
    orderEntered.username = ASCII.packLong("user1");
    orderEntered.side = PMR.BUY;
    orderEntered.instrument = ASCII.packLong("AAPL");
    orderEntered.quantity = 100L;
    orderEntered.price = 15000L;
    processor.orderEntered(orderEntered);

    // Cancel the entire order
    OrderCanceled orderCanceled = new OrderCanceled();
    orderCanceled.orderNumber = 100L;
    orderCanceled.canceledQuantity = 100L;

    assertDoesNotThrow(() -> processor.orderCanceled(orderCanceled));
  }

  @Test
  void orderCanceledMultipleCancels() {
    // Enter an order with quantity 100
    OrderEntered orderEntered = new OrderEntered();
    orderEntered.orderNumber = 100L;
    orderEntered.username = ASCII.packLong("user1");
    orderEntered.side = PMR.BUY;
    orderEntered.instrument = ASCII.packLong("AAPL");
    orderEntered.quantity = 100L;
    orderEntered.price = 15000L;
    processor.orderEntered(orderEntered);

    // Cancel in multiple steps
    OrderCanceled cancel1 = new OrderCanceled();
    cancel1.orderNumber = 100L;
    cancel1.canceledQuantity = 30L;
    processor.orderCanceled(cancel1);

    OrderCanceled cancel2 = new OrderCanceled();
    cancel2.orderNumber = 100L;
    cancel2.canceledQuantity = 70L;

    assertDoesNotThrow(() -> processor.orderCanceled(cancel2));
  }

  @Test
  void tradeWithBuyRestingSellIncoming() {
    // Enter resting buy order
    OrderEntered restingOrder = new OrderEntered();
    restingOrder.timestamp = 1000000000L;
    restingOrder.orderNumber = 100L;
    restingOrder.username = ASCII.packLong("buyer");
    restingOrder.side = PMR.BUY;
    restingOrder.instrument = ASCII.packLong("AAPL");
    restingOrder.quantity = 100L;
    restingOrder.price = 15000L;
    processor.orderEntered(restingOrder);

    // Enter incoming sell order
    OrderEntered incomingOrder = new OrderEntered();
    incomingOrder.timestamp = 1000000001L;
    incomingOrder.orderNumber = 200L;
    incomingOrder.username = ASCII.packLong("seller");
    incomingOrder.side = PMR.SELL;
    incomingOrder.instrument = ASCII.packLong("AAPL");
    incomingOrder.quantity = 50L;
    incomingOrder.price = 15000L;
    processor.orderEntered(incomingOrder);

    // Execute trade
    PMR.Trade trade = new PMR.Trade();

    // Convert to nanoseconds
    trade.timestamp = 1000000002L * 1_000_000L;
    trade.restingOrderNumber = 100L;
    trade.incomingOrderNumber = 200L;
    trade.quantity = 50L;
    trade.matchNumber = 1L;

    processor.trade(trade);

    // Verify trade was reported
    List<Trade> trades = listener.getReceivedTrades();
    assertEquals(1, trades.size());

    Trade reportedTrade = trades.get(0);
    assertNotNull(reportedTrade.timestamp);
    assertEquals(1L, reportedTrade.matchNumber);
    assertEquals("AAPL", reportedTrade.instrument);
    assertEquals(50L, reportedTrade.quantity);
    assertEquals(15000L, reportedTrade.price);
    assertEquals("buyer", reportedTrade.buyer);
    assertEquals(100L, reportedTrade.buyOrderNumber);
    assertEquals("seller", reportedTrade.seller);
    assertEquals(200L, reportedTrade.sellOrderNumber);
  }

  @Test
  void tradeWithSellRestingBuyIncoming() {
    // Enter resting sell order
    OrderEntered restingOrder = new OrderEntered();
    restingOrder.timestamp = 1000000000L;
    restingOrder.orderNumber = 100L;
    restingOrder.username = ASCII.packLong("seller");
    restingOrder.side = PMR.SELL;
    restingOrder.instrument = ASCII.packLong("MSFT");
    restingOrder.quantity = 200L;
    restingOrder.price = 30000L;
    processor.orderEntered(restingOrder);

    // Enter incoming buy order
    OrderEntered incomingOrder = new OrderEntered();
    incomingOrder.timestamp = 1000000001L;
    incomingOrder.orderNumber = 200L;
    incomingOrder.username = ASCII.packLong("buyer");
    incomingOrder.side = PMR.BUY;
    incomingOrder.instrument = ASCII.packLong("MSFT");
    incomingOrder.quantity = 150L;
    incomingOrder.price = 30000L;
    processor.orderEntered(incomingOrder);

    // Execute trade
    PMR.Trade trade = new PMR.Trade();
    trade.timestamp = 1000000002L * 1_000_000L;
    trade.restingOrderNumber = 100L;
    trade.incomingOrderNumber = 200L;
    trade.quantity = 150L;
    trade.matchNumber = 2L;

    processor.trade(trade);

    // Verify trade was reported
    List<Trade> trades = listener.getReceivedTrades();
    assertEquals(1, trades.size());

    Trade reportedTrade = trades.get(0);
    assertEquals(2L, reportedTrade.matchNumber);
    assertEquals("MSFT", reportedTrade.instrument);
    assertEquals(150L, reportedTrade.quantity);
    assertEquals(30000L, reportedTrade.price);
    assertEquals("buyer", reportedTrade.buyer);
    assertEquals(200L, reportedTrade.buyOrderNumber);
    assertEquals("seller", reportedTrade.seller);
    assertEquals(100L, reportedTrade.sellOrderNumber);
  }

  @Test
  void tradeFullyFillsBothOrders() {
    // Enter resting buy order
    OrderEntered restingOrder = new OrderEntered();
    restingOrder.orderNumber = 100L;
    restingOrder.username = ASCII.packLong("buyer");
    restingOrder.side = PMR.BUY;
    restingOrder.instrument = ASCII.packLong("GOOG");
    restingOrder.quantity = 100L;
    restingOrder.price = 25000L;
    processor.orderEntered(restingOrder);

    // Enter incoming sell order
    OrderEntered incomingOrder = new OrderEntered();
    incomingOrder.orderNumber = 200L;
    incomingOrder.username = ASCII.packLong("seller");
    incomingOrder.side = PMR.SELL;
    incomingOrder.instrument = ASCII.packLong("GOOG");
    incomingOrder.quantity = 100L;
    incomingOrder.price = 25000L;
    processor.orderEntered(incomingOrder);

    // Execute trade that fully fills both orders
    PMR.Trade trade = new PMR.Trade();
    trade.timestamp = 1000000002L * 1_000_000L;
    trade.restingOrderNumber = 100L;
    trade.incomingOrderNumber = 200L;
    trade.quantity = 100L;
    trade.matchNumber = 3L;

    assertDoesNotThrow(() -> processor.trade(trade));

    List<Trade> trades = listener.getReceivedTrades();
    assertEquals(1, trades.size());
  }

  @Test
  void tradePartiallyFillsRestingOrder() {
    // Enter resting buy order
    OrderEntered restingOrder = new OrderEntered();
    restingOrder.orderNumber = 100L;
    restingOrder.username = ASCII.packLong("buyer");
    restingOrder.side = PMR.BUY;
    restingOrder.instrument = ASCII.packLong("TSLA");
    restingOrder.quantity = 100L;
    restingOrder.price = 70000L;
    processor.orderEntered(restingOrder);

    // Enter incoming sell order
    OrderEntered incomingOrder = new OrderEntered();
    incomingOrder.orderNumber = 200L;
    incomingOrder.username = ASCII.packLong("seller");
    incomingOrder.side = PMR.SELL;
    incomingOrder.instrument = ASCII.packLong("TSLA");
    incomingOrder.quantity = 30L;
    incomingOrder.price = 70000L;
    processor.orderEntered(incomingOrder);

    // Execute trade that only partially fills the resting order
    PMR.Trade trade = new PMR.Trade();
    trade.timestamp = 1000000002L * 1_000_000L;
    trade.restingOrderNumber = 100L;
    trade.incomingOrderNumber = 200L;
    trade.quantity = 30L;
    trade.matchNumber = 4L;

    processor.trade(trade);

    List<Trade> trades = listener.getReceivedTrades();
    assertEquals(1, trades.size());
    assertEquals(30L, trades.get(0).quantity);
  }

  @Test
  void tradeMultipleTrades() {
    // Enter resting buy order
    OrderEntered restingOrder = new OrderEntered();
    restingOrder.orderNumber = 100L;
    restingOrder.username = ASCII.packLong("buyer");
    restingOrder.side = PMR.BUY;
    restingOrder.instrument = ASCII.packLong("IBM");
    restingOrder.quantity = 100L;
    restingOrder.price = 14000L;
    processor.orderEntered(restingOrder);

    // Enter first incoming sell order
    OrderEntered incomingOrder1 = new OrderEntered();
    incomingOrder1.orderNumber = 200L;
    incomingOrder1.username = ASCII.packLong("seller1");
    incomingOrder1.side = PMR.SELL;
    incomingOrder1.instrument = ASCII.packLong("IBM");
    incomingOrder1.quantity = 40L;
    incomingOrder1.price = 14000L;
    processor.orderEntered(incomingOrder1);

    // First trade
    PMR.Trade trade1 = new PMR.Trade();
    trade1.timestamp = 1000000002L * 1_000_000L;
    trade1.restingOrderNumber = 100L;
    trade1.incomingOrderNumber = 200L;
    trade1.quantity = 40L;
    trade1.matchNumber = 5L;
    processor.trade(trade1);

    // Enter second incoming sell order
    OrderEntered incomingOrder2 = new OrderEntered();
    incomingOrder2.orderNumber = 300L;
    incomingOrder2.username = ASCII.packLong("seller2");
    incomingOrder2.side = PMR.SELL;
    incomingOrder2.instrument = ASCII.packLong("IBM");
    incomingOrder2.quantity = 60L;
    incomingOrder2.price = 14000L;
    processor.orderEntered(incomingOrder2);

    // Second trade
    PMR.Trade trade2 = new PMR.Trade();
    trade2.timestamp = 1000000003L * 1_000_000L;
    trade2.restingOrderNumber = 100L;
    trade2.incomingOrderNumber = 300L;
    trade2.quantity = 60L;
    trade2.matchNumber = 6L;
    processor.trade(trade2);

    List<Trade> trades = listener.getReceivedTrades();
    assertEquals(2, trades.size());
    assertEquals(40L, trades.get(0).quantity);
    assertEquals("seller1", trades.get(0).seller);
    assertEquals(60L, trades.get(1).quantity);
    assertEquals("seller2", trades.get(1).seller);
  }

  @Test
  void tradeWithPaddedInstrumentName() {
    // Test that instrument names with padding are trimmed correctly
    OrderEntered restingOrder = new OrderEntered();
    restingOrder.orderNumber = 100L;
    restingOrder.username = ASCII.packLong("buyer");
    restingOrder.side = PMR.BUY;

    // Short instrument name
    restingOrder.instrument = ASCII.packLong("FB");
    restingOrder.quantity = 100L;
    restingOrder.price = 18000L;
    processor.orderEntered(restingOrder);

    OrderEntered incomingOrder = new OrderEntered();
    incomingOrder.orderNumber = 200L;
    incomingOrder.username = ASCII.packLong("seller");
    incomingOrder.side = PMR.SELL;
    incomingOrder.instrument = ASCII.packLong("FB");
    incomingOrder.quantity = 50L;
    incomingOrder.price = 18000L;
    processor.orderEntered(incomingOrder);

    PMR.Trade trade = new PMR.Trade();
    trade.timestamp = 1000000002L * 1_000_000L;
    trade.restingOrderNumber = 100L;
    trade.incomingOrderNumber = 200L;
    trade.quantity = 50L;
    trade.matchNumber = 7L;

    processor.trade(trade);

    List<Trade> trades = listener.getReceivedTrades();
    assertEquals(1, trades.size());
    assertEquals("FB", trades.get(0).instrument);
  }

  @Test
  void tradeWithPaddedUsernames() {
    // Test that usernames with padding are trimmed correctly
    OrderEntered restingOrder = new OrderEntered();
    restingOrder.orderNumber = 100L;

    // Short username
    restingOrder.username = ASCII.packLong("bob");
    restingOrder.side = PMR.BUY;
    restingOrder.instrument = ASCII.packLong("AAPL");
    restingOrder.quantity = 100L;
    restingOrder.price = 15000L;
    processor.orderEntered(restingOrder);

    OrderEntered incomingOrder = new OrderEntered();
    incomingOrder.orderNumber = 200L;

    // Short username
    incomingOrder.username = ASCII.packLong("alice");
    incomingOrder.side = PMR.SELL;
    incomingOrder.instrument = ASCII.packLong("AAPL");
    incomingOrder.quantity = 50L;
    incomingOrder.price = 15000L;
    processor.orderEntered(incomingOrder);

    PMR.Trade trade = new PMR.Trade();
    trade.timestamp = 1000000002L * 1_000_000L;
    trade.restingOrderNumber = 100L;
    trade.incomingOrderNumber = 200L;
    trade.quantity = 50L;
    trade.matchNumber = 8L;

    processor.trade(trade);

    List<Trade> trades = listener.getReceivedTrades();
    assertEquals(1, trades.size());
    assertEquals("bob", trades.get(0).buyer);
    assertEquals("alice", trades.get(0).seller);
  }

  @Test
  void tradeUsesRestingOrderPrice() {
    // Test that the trade price comes from the resting order, not the incoming order
    OrderEntered restingOrder = new OrderEntered();
    restingOrder.orderNumber = 100L;
    restingOrder.username = ASCII.packLong("buyer");
    restingOrder.side = PMR.BUY;
    restingOrder.instrument = ASCII.packLong("AAPL");
    restingOrder.quantity = 100L;

    // Resting order price
    restingOrder.price = 15000L;
    processor.orderEntered(restingOrder);

    OrderEntered incomingOrder = new OrderEntered();
    incomingOrder.orderNumber = 200L;
    incomingOrder.username = ASCII.packLong("seller");
    incomingOrder.side = PMR.SELL;
    incomingOrder.instrument = ASCII.packLong("AAPL");
    incomingOrder.quantity = 50L;

    // Different price (should be ignored)
    incomingOrder.price = 14500L;
    processor.orderEntered(incomingOrder);

    PMR.Trade trade = new PMR.Trade();
    trade.timestamp = 1000000002L * 1_000_000L;
    trade.restingOrderNumber = 100L;
    trade.incomingOrderNumber = 200L;
    trade.quantity = 50L;
    trade.matchNumber = 9L;

    processor.trade(trade);

    List<Trade> trades = listener.getReceivedTrades();
    assertEquals(1, trades.size());

    // Should use resting order price
    assertEquals(15000L, trades.get(0).price);
  }
}
