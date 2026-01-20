package com.paritytrading.parity.client;

import com.diffblue.cover.annotations.InterestingTestFactory;
import com.paritytrading.parity.util.Instruments;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Test factory for creating TerminalClient instances for unit testing.
 */
public class TerminalClientTestFactory {

    /**
     * Creates a TerminalClient instance for testing with a mock local server.
     * This factory creates a client that connects to localhost to avoid
     * UnresolvedAddressException.
     *
     * @return a TerminalClient instance for testing
     * @throws IOException if an I/O error occurs
     */
    @InterestingTestFactory
    public static TerminalClient createTerminalClient() throws IOException {
        // Create a minimal instance for testing without network connectivity
        // The constructor is package-private, allowing direct instantiation for tests
        Events events = new Events();
        OrderEntry orderEntry = OrderEntryTestFactory.createMockOrderEntry();
        Instruments instruments = InstrumentsTestFactory.createInstruments();

        return new TerminalClient(events, orderEntry, instruments);
    }

    /**
     * Creates an InetSocketAddress for testing TerminalClient.open() method.
     * This factory creates a valid address to avoid NullPointerException.
     *
     * @return a valid InetSocketAddress for testing
     */
    @InterestingTestFactory
    public static InetSocketAddress createInetSocketAddress() {
        return new InetSocketAddress("127.0.0.1", 8080);
    }

    /**
     * Creates a valid username string for testing TerminalClient.open() method.
     * This factory provides a non-null string to avoid NullPointerException.
     *
     * @return a valid username string for testing
     */
    @InterestingTestFactory
    public static String createUsername() {
        return "testuser";
    }

    /**
     * Creates a valid password string for testing TerminalClient.open() method.
     * This factory provides a non-null string to avoid NullPointerException.
     *
     * @return a valid password string for testing
     */
    @InterestingTestFactory
    public static String createPassword() {
        return "testpass";
    }
}
