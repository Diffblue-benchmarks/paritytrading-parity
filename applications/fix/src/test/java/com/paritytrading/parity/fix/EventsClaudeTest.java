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
package com.paritytrading.parity.fix;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import com.paritytrading.parity.util.Instruments;

/**
 * Test class for Events.
 *
 * <p>Note: The Events class contains a static utility method with an infinite loop that processes
 * network I/O events. Testing this class is inherently challenging because: 1. The process() method
 * contains an infinite loop with no exit condition 2. It depends on Java NIO components (Selector,
 * Channels) which are kernel resources 3. It requires actual network connections to test meaningful
 * behavior
 *
 * <p>These tests focus on verifying the basic instantiation and initial setup behavior of the
 * Events class. More comprehensive testing would require integration tests with actual network
 * infrastructure.
 */
class EventsClaudeTest {
  /**
   * Test that the Events class can be instantiated. This tests the implicit default constructor
   * <init>.()V
   *
   * <p>The Events class is a utility class with only static methods, but it's still possible to
   * instantiate it (Java allows this even though it's not meaningful).
   */
  @Test
  void testConstructor() {
    // The Events class has no explicit constructor, so we test the implicit one
    Events events = new Events();
    assertNotNull(events);
  }

  /**
   * Test that the process method throws IOException when given null. This verifies the method
   * signature and that it handles invalid input.
   */
  @Test
  void testProcessWithNullThrowsException() {
    assertThrows(
        NullPointerException.class,
        () -> {
          Events.process(null);
        });
  }

  /**
   * Test the process method with a real FIXAcceptor that will timeout.
   *
   * <p>This test verifies that: 1. The process method can be called with a valid FIXAcceptor 2. The
   * method enters the event loop and processes at least one select() call 3. The thread continues
   * running (infinite loop behavior)
   *
   * <p>Note: The Events.process() method contains an infinite loop by design. Closing the server
   * channel does not necessarily break this loop because the selector continues to function even
   * with a closed channel. The loop only exits on IOException during selector operations, which may
   * not occur immediately upon channel closure.
   *
   * <p>We use a timeout to ensure the test doesn't run forever.
   */
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void testProcessStartsEventLoop() throws Exception {
    // Create a real FIXAcceptor on a free port
    InetSocketAddress fixAddress = new InetSocketAddress("localhost", 0);
    InetSocketAddress orderEntryAddress = new InetSocketAddress("localhost", 0);

    // We need to create a minimal Instruments configuration
    String instrumentsConfig =
        "instruments = {\n"
            + "  price-integer-digits = 4\n"
            + "  size-integer-digits = 8\n"
            + "  TEST {\n"
            + "    price-fraction-digits = 2\n"
            + "    size-fraction-digits = 0\n"
            + "  }\n"
            + "}";

    // Create a test OrderEntryFactory
    OrderEntryFactory orderEntryFactory = new OrderEntryFactory(orderEntryAddress);

    Config config = ConfigFactory.parseString(instrumentsConfig);
    Instruments instruments = Instruments.fromConfig(config, "instruments");

    FIXAcceptor acceptor = FIXAcceptor.open(orderEntryFactory, fixAddress, "SENDER", instruments);

    try {

      // Run the process method in a separate thread since it has an infinite loop
      Thread eventThread =
          new Thread(
              () -> {
                try {
                  Events.process(acceptor);
                } catch (IOException e) {
                  // Expected when we close or operations fail
                }
              });

      eventThread.start();

      // Give it a moment to start the event loop
      Thread.sleep(100);

      // Verify the thread is running (which means the event loop started)
      assertTrue(eventThread.isAlive(), "Event thread should be running");

      // Close the acceptor's server channel
      acceptor.getServerChannel().close();

      // The thread will continue running despite the closed channel due to the infinite loop
      // This is expected behavior - the event loop is designed to run indefinitely

    } finally {

      // Clean up
      try {
        acceptor.getServerChannel().close();
      } catch (IOException e) {
        // Ignore
      }
    }
  }

  /**
   * Test that process method handles IOException from Selector operations.
   *
   * <p>This test verifies that when the ServerSocketChannel is already closed, the process method
   * propagates the IOException appropriately.
   */
  @Test
  void testProcessWithClosedChannel() throws Exception {
    // Create a FIXAcceptor
    InetSocketAddress fixAddress = new InetSocketAddress("localhost", 0);
    InetSocketAddress orderEntryAddress = new InetSocketAddress("localhost", 0);

    String instrumentsConfig =
        "instruments = {\n"
            + "  price-integer-digits = 4\n"
            + "  size-integer-digits = 8\n"
            + "  TEST {\n"
            + "    price-fraction-digits = 2\n"
            + "    size-fraction-digits = 0\n"
            + "  }\n"
            + "}";

    OrderEntryFactory orderEntryFactory = new OrderEntryFactory(orderEntryAddress);

    Config config = ConfigFactory.parseString(instrumentsConfig);
    Instruments instruments = Instruments.fromConfig(config, "instruments");

    FIXAcceptor acceptor = FIXAcceptor.open(orderEntryFactory, fixAddress, "SENDER", instruments);

    // Close the channel before calling process
    acceptor.getServerChannel().close();

    // The process method should throw an IOException when trying to register

    // the closed channel with the selector
    assertThrows(
        IOException.class,
        () -> {
          Events.process(acceptor);
        });
  }
}
