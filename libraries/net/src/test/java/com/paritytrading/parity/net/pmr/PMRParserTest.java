package com.paritytrading.parity.net.pmr;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PMRParserTest {

    @Test
    public void testConstructor() {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);
        assertNotNull(parser);
    }

    @Test
    public void testMessageWithEmptyBuffer() {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);
        ByteBuffer buffer = ByteBuffer.allocate(0);

        PMRException ex = assertThrows(PMRException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: no message type", ex.getMessage());
    }

    @Test
    public void testVersionMessage() throws IOException {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(2);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(listener).version(Mockito.any(PMR.Version.class));
    }

    @Test
    public void testVersionMessageTooShort() {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte) 'V');
        buffer.putShort((short) 1);
        buffer.flip();

        PMRException ex = assertThrows(PMRException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: V", ex.getMessage());
    }

    @Test
    public void testOrderEnteredMessage() throws IOException {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.put((byte) 'E');
        buffer.putLong(1000);
        buffer.putLong(2000);
        buffer.putLong(3000);
        buffer.put((byte) 'B');
        buffer.putLong(4000);
        buffer.putLong(5000);
        buffer.putLong(6000);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(listener).orderEntered(Mockito.any(PMR.OrderEntered.class));
    }

    @Test
    public void testOrderEnteredMessageTooShort() {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'E');
        buffer.putLong(1000);
        buffer.flip();

        PMRException ex = assertThrows(PMRException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: E", ex.getMessage());
    }

    @Test
    public void testOrderAddedMessage() throws IOException {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(17);
        buffer.put((byte) 'A');
        buffer.putLong(1000);
        buffer.putLong(2000);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(listener).orderAdded(Mockito.any(PMR.OrderAdded.class));
    }

    @Test
    public void testOrderAddedMessageTooShort() {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'A');
        buffer.putInt(1000);
        buffer.flip();

        PMRException ex = assertThrows(PMRException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: A", ex.getMessage());
    }

    @Test
    public void testOrderCanceledMessage() throws IOException {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.putLong(1000);
        buffer.putLong(2000);
        buffer.putLong(3000);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(listener).orderCanceled(Mockito.any(PMR.OrderCanceled.class));
    }

    @Test
    public void testOrderCanceledMessageTooShort() {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'X');
        buffer.putLong(1000);
        buffer.flip();

        PMRException ex = assertThrows(PMRException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: X", ex.getMessage());
    }

    @Test
    public void testTradeMessage() throws IOException {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(37);
        buffer.put((byte) 'T');
        buffer.putLong(1000);
        buffer.putLong(2000);
        buffer.putLong(3000);
        buffer.putLong(4000);
        buffer.putInt(5000);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(listener).trade(Mockito.any(PMR.Trade.class));
    }

    @Test
    public void testTradeMessageTooShort() {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'T');
        buffer.putLong(1000);
        buffer.flip();

        PMRException ex = assertThrows(PMRException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: T", ex.getMessage());
    }

    @Test
    public void testUnknownMessageType() {
        PMRListener listener = Mockito.mock(PMRListener.class);
        PMRParser parser = new PMRParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'Z');
        buffer.putInt(1000);
        buffer.flip();

        PMRException ex = assertThrows(PMRException.class, () -> parser.message(buffer));
        assertEquals("Unknown message type: Z", ex.getMessage());
    }
}
