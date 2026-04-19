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

import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

class SessionFIXListenerTest {

    private FIXMessageListener messageListener;
    private FIXConnectionStatusListener statusListener;
    private FIXConnection mockFix;
    private SoupBinTCPClient mockOrderEntry;
    private Instruments instruments;
    private Instrument instrument;

    @BeforeEach
    void setUp() throws IOException {
        mockOrderEntry = mock(SoupBinTCPClient.class);

        OrderEntryFactory mockFactory = mock(OrderEntryFactory.class);
        when(mockFactory.create(any(), any())).thenReturn(mockOrderEntry);

        instruments = mock(Instruments.class);
        instrument = mock(Instrument.class);
        when(instrument.getSizeFactor()).thenReturn(1.0);
        when(instrument.getPriceFactor()).thenReturn(1.0);
        when(instrument.getSizeFractionDigits()).thenReturn(0);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instruments.get(anyLong())).thenReturn(instrument);
        when(instruments.get(any(String.class))).thenReturn(instrument);

        SocketChannel mockChannel = mock(SocketChannel.class);
        FIXConfig mockConfig = mock(FIXConfig.class);

        try (MockedConstruction<FIXConnection> mocked = mockConstruction(FIXConnection.class,
                (mock, context) -> {
                    mockFix = mock;
                    messageListener = (FIXMessageListener) context.arguments().get(2);
                    statusListener = (FIXConnectionStatusListener) context.arguments().get(3);
                    when(mock.getCurrentTimestamp()).thenReturn("20230101-00:00:00");
                    doAnswer(inv -> {
                        FIXMessage msg = inv.getArgument(0);
                        msg.reset();
                        return null;
                    }).when(mock).prepare(any(FIXMessage.class), anyChar());
                })) {
            new Session(mockFactory, mockChannel, mockConfig, instruments);
        }
    }

    private FIXValue mockStringValue(String value) {
        FIXValue v = mock(FIXValue.class);
        when(v.asString()).thenReturn(value);
        return v;
    }

    private FIXValue mockCharValue(char value) {
        FIXValue v = mock(FIXValue.class);
        when(v.asChar()).thenReturn(value);
        return v;
    }

    private FIXValue mockFloatValue(double value) {
        FIXValue v = mock(FIXValue.class);
        when(v.asFloat()).thenReturn(value);
        return v;
    }

    private FIXValue mockInvalidFloatValue() {
        FIXValue v = mock(FIXValue.class);
        when(v.asFloat()).thenThrow(new FIXValueFormatException("bad format"));
        return v;
    }

    private FIXMessage buildMessage(char msgTypeChar, Object... tagValuePairs) {
        FIXMessage message = mock(FIXMessage.class);

        FIXValue msgType = mock(FIXValue.class);
        when(msgType.length()).thenReturn(1);
        when(msgType.asChar()).thenReturn(msgTypeChar);
        when(message.getMsgType()).thenReturn(msgType);
        when(message.getMsgSeqNum()).thenReturn(1L);

        int fieldCount = tagValuePairs.length / 2;
        when(message.getFieldCount()).thenReturn(fieldCount);

        for (int i = 0; i < fieldCount; i++) {
            int tag = (Integer) tagValuePairs[i * 2];
            FIXValue value = (FIXValue) tagValuePairs[i * 2 + 1];
            when(message.tagAt(i)).thenReturn(tag);
            when(message.valueAt(i)).thenReturn(value);
        }

        return message;
    }

    private FIXMessage buildInvalidMsgTypeMessage() {
        FIXMessage message = mock(FIXMessage.class);
        FIXValue msgType = mock(FIXValue.class);
        when(msgType.length()).thenReturn(2);
        when(message.getMsgType()).thenReturn(msgType);
        when(message.getMsgSeqNum()).thenReturn(1L);
        return message;
    }

    private void submitValidBuyOrder(String clOrdId) throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue(clOrdId),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0),
                Price, mockFloatValue(10.0));

        messageListener.message(message);
    }

    // === message() tests ===

    @Test
    void messageWithInvalidMsgTypeLength() throws IOException {
        FIXMessage message = buildInvalidMsgTypeMessage();

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.InvalidMsgType),
                eq("Invalid MsgType(35)"));
    }

    @Test
    void messageWithUnknownMsgType() throws IOException {
        FIXMessage message = buildMessage('Z');

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.InvalidMsgType),
                eq("Invalid MsgType(35)"));
    }

    // === newOrderSingle() tests ===

    @Test
    void newOrderSingleMissingClOrdID() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle);

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("ClOrdID(11) missing"));
    }

    @Test
    void newOrderSingleMissingSide() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"));

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("Side(54) missing"));
    }

    @Test
    void newOrderSingleMissingSymbol() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy));

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("Symbol(55) missing"));
    }

    @Test
    void newOrderSingleMissingOrderQty() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"));

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("OrderQty(38) missing"));
    }

    @Test
    void newOrderSingleMissingPrice() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0));

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("Price(44) missing"));
    }

    @Test
    void newOrderSingleInvalidSide() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue('X'),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0),
                Price, mockFloatValue(10.0));

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.ValueIsIncorrect),
                eq("Unknown value in Side(54)"));
    }

    @Test
    void newOrderSingleInvalidOrderQtyFormat() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockInvalidFloatValue(),
                Price, mockFloatValue(10.0));

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.IncorrectDataFormatForValue),
                eq("Expected 'float' in OrderQty(38)"));
    }

    @Test
    void newOrderSingleNegativeOrderQty() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(-10.0),
                Price, mockFloatValue(10.0));

        messageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void newOrderSingleUnknownInstrument() throws IOException {
        when(instruments.get(anyLong())).thenReturn(null);

        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0),
                Price, mockFloatValue(10.0));

        messageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void newOrderSingleInvalidPriceFormat() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0),
                Price, mockInvalidFloatValue());

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.IncorrectDataFormatForValue),
                eq("Expected 'float' in Price(44)"));
    }

    @Test
    void newOrderSingleNegativePrice() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0),
                Price, mockFloatValue(-5.0));

        messageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void newOrderSingleDuplicateClOrdID() throws IOException {
        submitValidBuyOrder("order1");
        clearInvocations(mockFix, mockOrderEntry);

        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0),
                Price, mockFloatValue(10.0));

        messageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void newOrderSingleValidBuyOrder() throws IOException {
        submitValidBuyOrder("order1");

        verify(mockOrderEntry).send(any(ByteBuffer.class));
    }

    @Test
    void newOrderSingleValidSellOrder() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Side, mockCharValue(SideValues.Sell),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0),
                Price, mockFloatValue(10.0));

        messageListener.message(message);

        verify(mockOrderEntry).send(any(ByteBuffer.class));
    }

    @Test
    void newOrderSingleWithAccount() throws IOException {
        FIXMessage message = buildMessage(NewOrderSingle,
                ClOrdID, mockStringValue("order1"),
                Account, mockStringValue("ACC1"),
                Side, mockCharValue(SideValues.Buy),
                Symbol, mockStringValue("FOO"),
                OrderQty, mockFloatValue(100.0),
                Price, mockFloatValue(10.0));

        messageListener.message(message);

        verify(mockOrderEntry).send(any(ByteBuffer.class));
    }

    // === orderCancel() tests ===

    @Test
    void orderCancelMissingOrigClOrdID() throws IOException {
        FIXMessage message = buildMessage(OrderCancelRequest);

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("OrigClOrdID(41) missing"));
    }

    @Test
    void orderCancelMissingClOrdID() throws IOException {
        FIXMessage message = buildMessage(OrderCancelRequest,
                OrigClOrdID, mockStringValue("order1"));

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("ClOrdID(11) missing"));
    }

    @Test
    void orderCancelReplaceRequestMissingOrderQty() throws IOException {
        FIXMessage message = buildMessage(OrderCancelReplaceRequest,
                OrigClOrdID, mockStringValue("order1"),
                ClOrdID, mockStringValue("cancel1"));

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("OrderQty(38) missing"));
    }

    @Test
    void orderCancelUnknownOrder() throws IOException {
        FIXMessage message = buildMessage(OrderCancelRequest,
                OrigClOrdID, mockStringValue("nonexistent"),
                ClOrdID, mockStringValue("cancel1"));

        messageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(OrderCancelReject));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCancelOrderInPendingStatus() throws IOException {
        submitValidBuyOrder("order1");
        clearInvocations(mockFix, mockOrderEntry);

        FIXMessage cancelMsg1 = buildMessage(OrderCancelRequest,
                OrigClOrdID, mockStringValue("order1"),
                ClOrdID, mockStringValue("cancel1"));
        messageListener.message(cancelMsg1);
        clearInvocations(mockFix, mockOrderEntry);

        FIXMessage cancelMsg2 = buildMessage(OrderCancelRequest,
                OrigClOrdID, mockStringValue("order1"),
                ClOrdID, mockStringValue("cancel2"));
        messageListener.message(cancelMsg2);

        verify(mockFix).prepare(any(FIXMessage.class), eq(OrderCancelReject));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCancelDuplicateClOrdID() throws IOException {
        submitValidBuyOrder("order1");
        clearInvocations(mockFix, mockOrderEntry);

        FIXMessage message = buildMessage(OrderCancelRequest,
                OrigClOrdID, mockStringValue("order1"),
                ClOrdID, mockStringValue("order1"));

        messageListener.message(message);

        verify(mockFix).prepare(any(FIXMessage.class), eq(OrderCancelReject));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCancelValidCancel() throws IOException {
        submitValidBuyOrder("order1");
        clearInvocations(mockFix, mockOrderEntry);

        FIXMessage message = buildMessage(OrderCancelRequest,
                OrigClOrdID, mockStringValue("order1"),
                ClOrdID, mockStringValue("cancel1"));

        messageListener.message(message);

        verify(mockOrderEntry).send(any(ByteBuffer.class));
        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCancelValidReplace() throws IOException {
        submitValidBuyOrder("order1");
        clearInvocations(mockFix, mockOrderEntry);

        FIXMessage message = buildMessage(OrderCancelReplaceRequest,
                OrigClOrdID, mockStringValue("order1"),
                ClOrdID, mockStringValue("replace1"),
                OrderQty, mockFloatValue(50.0));

        messageListener.message(message);

        verify(mockOrderEntry).send(any(ByteBuffer.class));
        verify(mockFix).prepare(any(FIXMessage.class), eq(ExecutionReport));
        verify(mockFix).send(any(FIXMessage.class));
    }

    @Test
    void orderCancelInvalidOrderQtyFormat() throws IOException {
        submitValidBuyOrder("order1");
        clearInvocations(mockFix, mockOrderEntry);

        FIXMessage message = buildMessage(OrderCancelReplaceRequest,
                OrigClOrdID, mockStringValue("order1"),
                ClOrdID, mockStringValue("replace1"),
                OrderQty, mockInvalidFloatValue());

        messageListener.message(message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.IncorrectDataFormatForValue),
                eq("Expected 'float' in OrderQty(38)"));
    }

    // === close() test ===

    @Test
    void closeClosesOrderEntry() throws IOException {
        statusListener.close(mockFix, "connection closed");

        verify(mockOrderEntry).close();
    }

    // === heartbeatTimeout() test ===

    @Test
    void heartbeatTimeoutDoesNotThrow() {
        assertDoesNotThrow(() -> statusListener.heartbeatTimeout(mockFix));
    }

    // === logon() tests ===

    @Test
    void logonMissingUsername() throws IOException {
        FIXMessage message = mock(FIXMessage.class);
        when(message.valueOf(Username)).thenReturn(null);
        when(message.getMsgSeqNum()).thenReturn(1L);

        statusListener.logon(mockFix, message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("Username(553) missing"));
    }

    @Test
    void logonMissingPassword() throws IOException {
        FIXValue usernameValue = mockStringValue("user");

        FIXMessage message = mock(FIXMessage.class);
        when(message.valueOf(Username)).thenReturn(usernameValue);
        when(message.valueOf(Password)).thenReturn(null);
        when(message.getMsgSeqNum()).thenReturn(1L);

        statusListener.logon(mockFix, message);

        verify(mockFix).sendReject(eq(1L), eq((long) SessionRejectReasonValues.RequiredTagMissing),
                eq("Password(554) missing"));
    }

    @Test
    void logonValid() throws IOException {
        FIXValue usernameValue = mockStringValue("user");
        FIXValue passwordValue = mockStringValue("pass");

        FIXMessage message = mock(FIXMessage.class);
        when(message.valueOf(Username)).thenReturn(usernameValue);
        when(message.valueOf(Password)).thenReturn(passwordValue);
        when(message.getMsgSeqNum()).thenReturn(1L);

        statusListener.logon(mockFix, message);

        verify(mockFix).updateCompID(message);
        verify(mockOrderEntry).login(any(SoupBinTCP.LoginRequest.class));
    }

    // === logout() test ===

    @Test
    void logoutSendsLogout() throws IOException {
        FIXMessage message = mock(FIXMessage.class);

        statusListener.logout(mockFix, message);

        verify(mockFix).sendLogout();
        verify(mockOrderEntry).logout();
    }

    // === reject() test ===

    @Test
    void rejectDoesNotThrow() {
        FIXMessage message = mock(FIXMessage.class);
        assertDoesNotThrow(() -> statusListener.reject(mockFix, message));
    }

    // === sequenceReset() test ===

    @Test
    void sequenceResetDoesNotThrow() {
        assertDoesNotThrow(() -> statusListener.sequenceReset(mockFix));
    }

    // === tooLowMsgSeqNum() test ===

    @Test
    void tooLowMsgSeqNumDoesNotThrow() {
        assertDoesNotThrow(() -> statusListener.tooLowMsgSeqNum(mockFix, 1L, 5L));
    }
}
