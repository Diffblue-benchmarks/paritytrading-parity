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

import static com.paritytrading.philadelphia.fix44.FIX44Enumerations.*;
import static com.paritytrading.philadelphia.fix44.FIX44MsgTypes.*;
import static com.paritytrading.philadelphia.fix44.FIX44Tags.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.philadelphia.FIXConfig;
import com.paritytrading.philadelphia.FIXConnection;
import com.paritytrading.philadelphia.FIXConnectionStatusListener;
import com.paritytrading.philadelphia.FIXMessage;
import com.paritytrading.philadelphia.FIXMessageListener;
import com.paritytrading.philadelphia.FIXValue;
import com.paritytrading.philadelphia.FIXValueFormatException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;

class SessionTest {

    private OrderEntryFactory factory;
    private SoupBinTCPClient mockClient;
    private SocketChannel channel;
    private FIXConfig fixConfig;
    private Instruments instruments;
    private Instrument instrument;

    private MockedConstruction<FIXConnection> mockedFIXConnection;
    private Session session;
    private FIXConnection mockFix;
    private FIXMessageListener fixMessageListener;
    private FIXConnectionStatusListener fixStatusListener;
    private POEClientListener poeClientListener;

    @BeforeEach
    void setUp() throws IOException {
        factory = mock(OrderEntryFactory.class);
        mockClient = mock(SoupBinTCPClient.class);
        when(factory.create(any(POEClientListener.class), any(SoupBinTCPClientStatusListener.class)))
                .thenReturn(mockClient);

        channel = mock(SocketChannel.class);
        fixConfig = mock(FIXConfig.class);
        instruments = mock(Instruments.class);
        instrument = mock(Instrument.class);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(0);
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);
        when(instruments.get(anyLong())).thenReturn(instrument);
        when(instruments.get(anyString())).thenReturn(instrument);

        mockedFIXConnection = mockConstruction(FIXConnection.class,
                (mock, context) -> {
                    fixMessageListener = (FIXMessageListener) context.arguments().get(2);
                    fixStatusListener = (FIXConnectionStatusListener) context.arguments().get(3);
                    when(mock.getCurrentTimestamp()).thenReturn("20240101-00:00:00.000");
                    doAnswer(invocation -> {
                        FIXMessage msg = invocation.getArgument(0);
                        msg.reset();
                        return null;
                    }).when(mock).prepare(any(FIXMessage.class), anyChar());
                });

        session = new Session(factory, channel, fixConfig, instruments);
        mockFix = mockedFIXConnection.constructed().get(0);

        ArgumentCaptor<POEClientListener> poeCaptor = ArgumentCaptor.forClass(POEClientListener.class);
        verify(factory).create(poeCaptor.capture(), any(SoupBinTCPClientStatusListener.class));
        poeClientListener = poeCaptor.getValue();
    }

    @AfterEach
    void tearDown() {
        mockedFIXConnection.close();
    }

    @Test
    void constructor() {
        assertNotNull(session);
        assertNotNull(session.getFIX());
        assertNotNull(session.getOrderEntry());
    }

    @Test
    void close() throws IOException {
        session.close();

        verify(mockFix).close();
        verify(mockClient).close();
    }

    @Test
    void getFIXReturnsConnection() {
        assertSame(mockFix, session.getFIX());
    }

    @Test
    void getOrderEntryReturnsClient() {
        assertSame(mockClient, session.getOrderEntry());
    }

    @Test
    void sendNewOrderSingle() throws IOException {
        FIXMessage message = newOrderSingle("order1", null, SideValues.Buy, "AAPL", 100.0, 50.0);

        fixMessageListener.message(message);

        verify(mockClient).send(any(ByteBuffer.class));
    }

    @Test
    void invalidMsgTypeLength() throws IOException {
        FIXMessage message = mock(FIXMessage.class);
        FIXValue msgType = mock(FIXValue.class);
        when(message.getMsgType()).thenReturn(msgType);
        when(msgType.length()).thenReturn(2);
        when(message.getMsgSeqNum()).thenReturn(1L);

        fixMessageListener.message(message);

        verify(mockFix).sendReject(eq(1L), anyLong(), eq("Invalid MsgType(35)"));
    }

    @Test
    void invalidMsgTypeUnknown() throws IOException {
        FIXMessage message = mock(FIXMessage.class);
        FIXValue msgType = mock(FIXValue.class);
        when(message.getMsgType()).thenReturn(msgType);
        when(msgType.length()).thenReturn(1);
        when(msgType.asChar()).thenReturn('Z');
        when(message.getMsgSeqNum()).thenReturn(1L);

        fixMessageListener.message(message);

        verify(mockFix).sendReject(eq(1L), anyLong(), eq("Invalid MsgType(35)"));
    }

    @Test
    void requiredTagMissingClOrdID() throws IOException {
        FIXMessage message = newOrderSingle(null, null, null, null, null, null);

        fixMessageListener.message(message);

        verify(mockFix).sendReject(anyLong(), anyLong(), eq("ClOrdID(11) missing"));
    }

    @Test
    void requiredTagMissingSide() throws IOException {
        FIXMessage message = newOrderSingle("order1", null, null, null, null, null);

        fixMessageListener.message(message);

        verify(mockFix).sendReject(anyLong(), anyLong(), eq("Side(54) missing"));
    }

    @Test
    void valueIsIncorrectSide() throws IOException {
        FIXMessage message = newOrderSingle("order1", null, 'X', "AAPL", 100.0, 50.0);

        fixMessageListener.message(message);

        verify(mockFix).sendReject(anyLong(), anyLong(), eq("Unknown value in Side(54)"));
    }

    @Test
    void incorrectDataFormatForOrderQty() throws IOException {
        FIXMessage message = mock(FIXMessage.class);
        FIXValue msgType = mock(FIXValue.class);
        when(message.getMsgType()).thenReturn(msgType);
        when(msgType.length()).thenReturn(1);
        when(msgType.asChar()).thenReturn(NewOrderSingle);
        when(message.getMsgSeqNum()).thenReturn(1L);

        List<Integer> tags = new ArrayList<>();
        List<FIXValue> values = new ArrayList<>();

        addStringField(tags, values, ClOrdID, "order1");
        addCharField(tags, values, Side, SideValues.Buy);
        addStringField(tags, values, Symbol, "AAPL");

        tags.add(OrderQty);
        FIXValue badQty = mock(FIXValue.class);
        when(badQty.asFloat()).thenThrow(new FIXValueFormatException("bad"));
        values.add(badQty);

        addFloatField(tags, values, Price, 50.0);

        stubFields(message, tags, values);

        fixMessageListener.message(message);

        verify(mockFix).sendReject(anyLong(), anyLong(), eq("Expected 'float' in OrderQty(38)"));
    }

    @Test
    void orderCancelRejectUnknownOrder() throws IOException {
        FIXMessage message = orderCancelRequest("cancel1", "nonexistent");

        fixMessageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(OrderCancelReject));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCancelRejectPendingStatus() throws IOException {
        submitAndAcceptOrder("order1", SideValues.Buy, "AAPL", 100.0, 50.0);

        FIXMessage cancel1 = orderCancelRequest("cancel1", "order1");
        fixMessageListener.message(cancel1);
        clearInvocations(mockFix);

        FIXMessage cancel2 = orderCancelRequest("cancel2", "order1");
        fixMessageListener.message(cancel2);

        verify(mockFix).prepare(any(FIXMessage.class), eq(OrderCancelReject));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCancelRejectFilledWhilePending() throws IOException {
        submitAndAcceptOrder("order1", SideValues.Buy, "AAPL", 100.0, 50.0);

        FIXMessage cancel = orderCancelRequest("cancel1", "order1");
        fixMessageListener.message(cancel);
        clearInvocations(mockFix);

        POE.OrderExecuted executed = new POE.OrderExecuted();
        ASCII.putLongLeft(executed.orderId, 1);
        executed.quantity = 100;
        executed.price = 5000;

        poeClientListener.orderExecuted(executed);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).prepare(any(FIXMessage.class), eq(OrderCancelReject));
        verify(mockFix, times(2)).send(any(FIXMessage.class));
    }

    @Test
    void orderAccepted() throws IOException {
        submitAndAcceptOrder("order1", SideValues.Buy, "AAPL", 100.0, 50.0);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderRejectedByExchange() throws IOException {
        FIXMessage message = newOrderSingle("order1", null, SideValues.Buy, "AAPL", 100.0, 50.0);
        fixMessageListener.message(message);

        POE.OrderRejected rejected = new POE.OrderRejected();
        ASCII.putLongLeft(rejected.orderId, 1);
        rejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        poeClientListener.orderRejected(rejected);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderRejectedNegativeQuantity() throws IOException {
        FIXMessage message = newOrderSingle("order1", null, SideValues.Buy, "AAPL", -100.0, 50.0);

        fixMessageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderRejectedUnknownSymbol() throws IOException {
        when(instruments.get(anyLong())).thenReturn(null);
        when(instruments.get(anyString())).thenReturn(null);

        FIXMessage message = newOrderSingle("order1", null, SideValues.Buy, "AAPL", 100.0, 50.0);

        fixMessageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderExecuted() throws IOException {
        submitAndAcceptOrder("order1", SideValues.Buy, "AAPL", 100.0, 50.0);
        clearInvocations(mockFix);

        POE.OrderExecuted executed = new POE.OrderExecuted();
        ASCII.putLongLeft(executed.orderId, 1);
        executed.quantity = 50;
        executed.price = 5000;

        poeClientListener.orderExecuted(executed);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCancelAcknowledgement() throws IOException {
        submitAndAcceptOrder("order1", SideValues.Buy, "AAPL", 100.0, 50.0);
        clearInvocations(mockFix);

        FIXMessage cancel = orderCancelRequest("cancel1", "order1");
        fixMessageListener.message(cancel);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCanceled() throws IOException {
        submitAndAcceptOrder("order1", SideValues.Buy, "AAPL", 100.0, 50.0);

        FIXMessage cancel = orderCancelRequest("cancel1", "order1");
        fixMessageListener.message(cancel);
        clearInvocations(mockFix);

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        ASCII.putLongLeft(canceled.orderId, 1);
        canceled.canceledQuantity = 100;

        poeClientListener.orderCanceled(canceled);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCanceledPartial() throws IOException {
        submitAndAcceptOrder("order1", SideValues.Buy, "AAPL", 100.0, 50.0);

        FIXMessage cancel = orderCancelReplaceRequest("cancel1", "order1", 50.0);
        fixMessageListener.message(cancel);
        clearInvocations(mockFix);

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        ASCII.putLongLeft(canceled.orderId, 1);
        canceled.canceledQuantity = 50;

        poeClientListener.orderCanceled(canceled);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    private void submitAndAcceptOrder(String clOrdId, char side, String symbol,
            double orderQty, double price) throws IOException {
        FIXMessage message = newOrderSingle(clOrdId, null, side, symbol, orderQty, price);
        fixMessageListener.message(message);

        POE.OrderAccepted accepted = new POE.OrderAccepted();
        ASCII.putLongLeft(accepted.orderId, 1);
        accepted.orderNumber = 12345;
        poeClientListener.orderAccepted(accepted);
    }

    private FIXMessage newOrderSingle(String clOrdId, String account,
            Character side, String symbol, Double orderQty, Double price) {
        FIXMessage message = mock(FIXMessage.class);
        FIXValue msgType = mock(FIXValue.class);
        when(message.getMsgType()).thenReturn(msgType);
        when(msgType.length()).thenReturn(1);
        when(msgType.asChar()).thenReturn(NewOrderSingle);
        when(message.getMsgSeqNum()).thenReturn(1L);

        List<Integer> tags = new ArrayList<>();
        List<FIXValue> values = new ArrayList<>();

        addStringField(tags, values, ClOrdID, clOrdId);
        addStringField(tags, values, Account, account);
        addCharField(tags, values, Side, side);
        addStringField(tags, values, Symbol, symbol);
        addFloatField(tags, values, OrderQty, orderQty);
        addFloatField(tags, values, Price, price);

        stubFields(message, tags, values);
        return message;
    }

    private FIXMessage orderCancelRequest(String clOrdId, String origClOrdId) {
        FIXMessage message = mock(FIXMessage.class);
        FIXValue msgType = mock(FIXValue.class);
        when(message.getMsgType()).thenReturn(msgType);
        when(msgType.length()).thenReturn(1);
        when(msgType.asChar()).thenReturn(OrderCancelRequest);
        when(message.getMsgSeqNum()).thenReturn(1L);

        List<Integer> tags = new ArrayList<>();
        List<FIXValue> values = new ArrayList<>();

        addStringField(tags, values, OrigClOrdID, origClOrdId);
        addStringField(tags, values, ClOrdID, clOrdId);

        stubFields(message, tags, values);
        return message;
    }

    private FIXMessage orderCancelReplaceRequest(String clOrdId, String origClOrdId, double orderQty) {
        FIXMessage message = mock(FIXMessage.class);
        FIXValue msgType = mock(FIXValue.class);
        when(message.getMsgType()).thenReturn(msgType);
        when(msgType.length()).thenReturn(1);
        when(msgType.asChar()).thenReturn(OrderCancelReplaceRequest);
        when(message.getMsgSeqNum()).thenReturn(1L);

        List<Integer> tags = new ArrayList<>();
        List<FIXValue> values = new ArrayList<>();

        addStringField(tags, values, OrigClOrdID, origClOrdId);
        addStringField(tags, values, ClOrdID, clOrdId);
        addFloatField(tags, values, OrderQty, orderQty);

        stubFields(message, tags, values);
        return message;
    }

    private void addStringField(List<Integer> tags, List<FIXValue> values, int tag, String value) {
        if (value == null) return;
        tags.add(tag);
        FIXValue v = mock(FIXValue.class);
        when(v.asString()).thenReturn(value);
        values.add(v);
    }

    private void addCharField(List<Integer> tags, List<FIXValue> values, int tag, Character value) {
        if (value == null) return;
        tags.add(tag);
        FIXValue v = mock(FIXValue.class);
        when(v.asChar()).thenReturn(value);
        values.add(v);
    }

    private void addFloatField(List<Integer> tags, List<FIXValue> values, int tag, Double value) {
        if (value == null) return;
        tags.add(tag);
        FIXValue v = mock(FIXValue.class);
        when(v.asFloat()).thenReturn(value);
        values.add(v);
    }

    private void stubFields(FIXMessage message, List<Integer> tags, List<FIXValue> values) {
        when(message.getFieldCount()).thenReturn(tags.size());
        for (int i = 0; i < tags.size(); i++) {
            when(message.tagAt(i)).thenReturn(tags.get(i));
            when(message.valueAt(i)).thenReturn(values.get(i));
        }
    }
}
