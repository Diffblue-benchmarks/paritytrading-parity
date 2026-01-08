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
package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class POEClaude_constructorTest {

    @Test
    void testEnterOrderConstructorCreatesInstance() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertNotNull(enterOrder);
    }

    @Test
    void testEnterOrderConstructorInitializesOrderId() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertNotNull(enterOrder.orderId);
        assertEquals(16, enterOrder.orderId.length);
    }

    @Test
    void testEnterOrderConstructorOrderIdIsEmptyArray() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        byte[] expectedEmptyArray = new byte[16];
        assertArrayEquals(expectedEmptyArray, enterOrder.orderId);
    }

    @Test
    void testEnterOrderConstructorAllBytesAreZero() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        for (int i = 0; i < enterOrder.orderId.length; i++) {
            assertEquals(0, enterOrder.orderId[i]);
        }
    }

    @Test
    void testEnterOrderConstructorSideIsZero() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertEquals(0, enterOrder.side);
    }

    @Test
    void testEnterOrderConstructorInstrumentIsZero() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertEquals(0L, enterOrder.instrument);
    }

    @Test
    void testEnterOrderConstructorQuantityIsZero() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertEquals(0L, enterOrder.quantity);
    }

    @Test
    void testEnterOrderConstructorPriceIsZero() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertEquals(0L, enterOrder.price);
    }

    @Test
    void testEnterOrderConstructorMultipleInstancesHaveDifferentArrays() {
        POE.EnterOrder enterOrder1 = new POE.EnterOrder();
        POE.EnterOrder enterOrder2 = new POE.EnterOrder();

        assertNotSame(enterOrder1.orderId, enterOrder2.orderId);
    }

    @Test
    void testEnterOrderConstructorArrayCanBeModified() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        enterOrder.orderId[0] = (byte) 1;
        enterOrder.orderId[15] = (byte) 255;

        assertEquals((byte) 1, enterOrder.orderId[0]);
        assertEquals((byte) 255, enterOrder.orderId[15]);
    }

    @Test
    void testEnterOrderConstructorCreatesValidOrderIdLength() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertEquals(POE.ORDER_ID_LENGTH, enterOrder.orderId.length);
    }

    @Test
    void testEnterOrderConstructorOrderIdIsNotNull() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertNotNull(enterOrder.orderId);
    }

    @Test
    void testEnterOrderConstructorFieldsAreAccessible() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        enterOrder.side = POE.BUY;
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        assertEquals(POE.BUY, enterOrder.side);
        assertEquals(100L, enterOrder.instrument);
        assertEquals(200L, enterOrder.quantity);
        assertEquals(300L, enterOrder.price);
    }

    @Test
    void testEnterOrderConstructorCanCreateMultipleInstances() {
        POE.EnterOrder enterOrder1 = new POE.EnterOrder();
        POE.EnterOrder enterOrder2 = new POE.EnterOrder();
        POE.EnterOrder enterOrder3 = new POE.EnterOrder();

        assertNotNull(enterOrder1);
        assertNotNull(enterOrder2);
        assertNotNull(enterOrder3);
        assertNotSame(enterOrder1, enterOrder2);
        assertNotSame(enterOrder2, enterOrder3);
        assertNotSame(enterOrder1, enterOrder3);
    }

    @Test
    void testEnterOrderConstructorOrderIdArrayIsIndependent() {
        POE.EnterOrder enterOrder1 = new POE.EnterOrder();
        POE.EnterOrder enterOrder2 = new POE.EnterOrder();

        enterOrder1.orderId[0] = (byte) 42;

        assertEquals((byte) 42, enterOrder1.orderId[0]);
        assertEquals((byte) 0, enterOrder2.orderId[0]);
    }

    @Test
    void testEnterOrderConstructorUsesOrderIdLengthConstant() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertEquals(POE.ORDER_ID_LENGTH, enterOrder.orderId.length);
        assertEquals(16, POE.ORDER_ID_LENGTH);
    }

    @Test
    void testEnterOrderConstructorInitializesAllFieldsToDefaults() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertNotNull(enterOrder.orderId);
        assertEquals(16, enterOrder.orderId.length);
        assertEquals(0, enterOrder.side);
        assertEquals(0L, enterOrder.instrument);
        assertEquals(0L, enterOrder.quantity);
        assertEquals(0L, enterOrder.price);
    }

    @Test
    void testEnterOrderConstructorOrderIdArrayNotShared() {
        POE.EnterOrder enterOrder1 = new POE.EnterOrder();
        byte[] array1 = enterOrder1.orderId;

        POE.EnterOrder enterOrder2 = new POE.EnterOrder();
        byte[] array2 = enterOrder2.orderId;

        assertNotSame(array1, array2);
    }

    @Test
    void testEnterOrderConstructorRepeatedCalls() {
        for (int i = 0; i < 10; i++) {
            POE.EnterOrder enterOrder = new POE.EnterOrder();
            assertNotNull(enterOrder);
            assertNotNull(enterOrder.orderId);
            assertEquals(16, enterOrder.orderId.length);
        }
    }

    @Test
    void testEnterOrderConstructorInstanceOfInboundMessage() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertInstanceOf(POE.InboundMessage.class, enterOrder);
    }

    // ========== OrderAccepted Constructor Tests ==========

    @Test
    void testOrderAcceptedConstructorCreatesInstance() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertNotNull(orderAccepted);
    }

    @Test
    void testOrderAcceptedConstructorInitializesOrderId() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertNotNull(orderAccepted.orderId);
        assertEquals(16, orderAccepted.orderId.length);
    }

    @Test
    void testOrderAcceptedConstructorOrderIdIsEmptyArray() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        byte[] expectedEmptyArray = new byte[16];
        assertArrayEquals(expectedEmptyArray, orderAccepted.orderId);
    }

    @Test
    void testOrderAcceptedConstructorAllBytesAreZero() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        for (int i = 0; i < orderAccepted.orderId.length; i++) {
            assertEquals(0, orderAccepted.orderId[i]);
        }
    }

    @Test
    void testOrderAcceptedConstructorTimestampIsZero() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(0L, orderAccepted.timestamp);
    }

    @Test
    void testOrderAcceptedConstructorSideIsZero() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(0, orderAccepted.side);
    }

    @Test
    void testOrderAcceptedConstructorInstrumentIsZero() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(0L, orderAccepted.instrument);
    }

    @Test
    void testOrderAcceptedConstructorQuantityIsZero() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(0L, orderAccepted.quantity);
    }

    @Test
    void testOrderAcceptedConstructorPriceIsZero() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(0L, orderAccepted.price);
    }

    @Test
    void testOrderAcceptedConstructorOrderNumberIsZero() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(0L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorMultipleInstancesHaveDifferentArrays() {
        POE.OrderAccepted orderAccepted1 = new POE.OrderAccepted();
        POE.OrderAccepted orderAccepted2 = new POE.OrderAccepted();

        assertNotSame(orderAccepted1.orderId, orderAccepted2.orderId);
    }

    @Test
    void testOrderAcceptedConstructorArrayCanBeModified() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        orderAccepted.orderId[0] = (byte) 1;
        orderAccepted.orderId[15] = (byte) 255;

        assertEquals((byte) 1, orderAccepted.orderId[0]);
        assertEquals((byte) 255, orderAccepted.orderId[15]);
    }

    @Test
    void testOrderAcceptedConstructorCreatesValidOrderIdLength() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(POE.ORDER_ID_LENGTH, orderAccepted.orderId.length);
    }

    @Test
    void testOrderAcceptedConstructorOrderIdIsNotNull() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertNotNull(orderAccepted.orderId);
    }

    @Test
    void testOrderAcceptedConstructorFieldsAreAccessible() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        orderAccepted.timestamp = 123456L;
        orderAccepted.side = POE.BUY;
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        assertEquals(123456L, orderAccepted.timestamp);
        assertEquals(POE.BUY, orderAccepted.side);
        assertEquals(100L, orderAccepted.instrument);
        assertEquals(200L, orderAccepted.quantity);
        assertEquals(300L, orderAccepted.price);
        assertEquals(400L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorCanCreateMultipleInstances() {
        POE.OrderAccepted orderAccepted1 = new POE.OrderAccepted();
        POE.OrderAccepted orderAccepted2 = new POE.OrderAccepted();
        POE.OrderAccepted orderAccepted3 = new POE.OrderAccepted();

        assertNotNull(orderAccepted1);
        assertNotNull(orderAccepted2);
        assertNotNull(orderAccepted3);
        assertNotSame(orderAccepted1, orderAccepted2);
        assertNotSame(orderAccepted2, orderAccepted3);
        assertNotSame(orderAccepted1, orderAccepted3);
    }

    @Test
    void testOrderAcceptedConstructorOrderIdArrayIsIndependent() {
        POE.OrderAccepted orderAccepted1 = new POE.OrderAccepted();
        POE.OrderAccepted orderAccepted2 = new POE.OrderAccepted();

        orderAccepted1.orderId[0] = (byte) 42;

        assertEquals((byte) 42, orderAccepted1.orderId[0]);
        assertEquals((byte) 0, orderAccepted2.orderId[0]);
    }

    @Test
    void testOrderAcceptedConstructorUsesOrderIdLengthConstant() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(POE.ORDER_ID_LENGTH, orderAccepted.orderId.length);
        assertEquals(16, POE.ORDER_ID_LENGTH);
    }

    @Test
    void testOrderAcceptedConstructorInitializesAllFieldsToDefaults() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertNotNull(orderAccepted.orderId);
        assertEquals(16, orderAccepted.orderId.length);
        assertEquals(0L, orderAccepted.timestamp);
        assertEquals(0, orderAccepted.side);
        assertEquals(0L, orderAccepted.instrument);
        assertEquals(0L, orderAccepted.quantity);
        assertEquals(0L, orderAccepted.price);
        assertEquals(0L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorOrderIdArrayNotShared() {
        POE.OrderAccepted orderAccepted1 = new POE.OrderAccepted();
        byte[] array1 = orderAccepted1.orderId;

        POE.OrderAccepted orderAccepted2 = new POE.OrderAccepted();
        byte[] array2 = orderAccepted2.orderId;

        assertNotSame(array1, array2);
    }

    @Test
    void testOrderAcceptedConstructorRepeatedCalls() {
        for (int i = 0; i < 10; i++) {
            POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
            assertNotNull(orderAccepted);
            assertNotNull(orderAccepted.orderId);
            assertEquals(16, orderAccepted.orderId.length);
        }
    }

    @Test
    void testOrderAcceptedConstructorInstanceOfOutboundMessage() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertInstanceOf(POE.OutboundMessage.class, orderAccepted);
    }

    @Test
    void testOrderAcceptedConstructorInstanceOfMessage() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertInstanceOf(POE.Message.class, orderAccepted);
    }

    // ========== OrderCanceled Constructor Tests ==========

    @Test
    void testOrderCanceledConstructorCreatesInstance() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertNotNull(orderCanceled);
    }

    @Test
    void testOrderCanceledConstructorInitializesOrderId() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertNotNull(orderCanceled.orderId);
        assertEquals(16, orderCanceled.orderId.length);
    }

    @Test
    void testOrderCanceledConstructorOrderIdIsEmptyArray() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        byte[] expectedEmptyArray = new byte[16];
        assertArrayEquals(expectedEmptyArray, orderCanceled.orderId);
    }

    @Test
    void testOrderCanceledConstructorAllBytesAreZero() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        for (int i = 0; i < orderCanceled.orderId.length; i++) {
            assertEquals(0, orderCanceled.orderId[i]);
        }
    }

    @Test
    void testOrderCanceledConstructorTimestampIsZero() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertEquals(0L, orderCanceled.timestamp);
    }

    @Test
    void testOrderCanceledConstructorCanceledQuantityIsZero() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertEquals(0L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledConstructorReasonIsZero() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertEquals(0, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledConstructorMultipleInstancesHaveDifferentArrays() {
        POE.OrderCanceled orderCanceled1 = new POE.OrderCanceled();
        POE.OrderCanceled orderCanceled2 = new POE.OrderCanceled();

        assertNotSame(orderCanceled1.orderId, orderCanceled2.orderId);
    }

    @Test
    void testOrderCanceledConstructorArrayCanBeModified() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        orderCanceled.orderId[0] = (byte) 1;
        orderCanceled.orderId[15] = (byte) 255;

        assertEquals((byte) 1, orderCanceled.orderId[0]);
        assertEquals((byte) 255, orderCanceled.orderId[15]);
    }

    @Test
    void testOrderCanceledConstructorCreatesValidOrderIdLength() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertEquals(POE.ORDER_ID_LENGTH, orderCanceled.orderId.length);
    }

    @Test
    void testOrderCanceledConstructorOrderIdIsNotNull() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertNotNull(orderCanceled.orderId);
    }

    @Test
    void testOrderCanceledConstructorFieldsAreAccessible() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        orderCanceled.timestamp = 123456L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        assertEquals(123456L, orderCanceled.timestamp);
        assertEquals(100L, orderCanceled.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledConstructorCanCreateMultipleInstances() {
        POE.OrderCanceled orderCanceled1 = new POE.OrderCanceled();
        POE.OrderCanceled orderCanceled2 = new POE.OrderCanceled();
        POE.OrderCanceled orderCanceled3 = new POE.OrderCanceled();

        assertNotNull(orderCanceled1);
        assertNotNull(orderCanceled2);
        assertNotNull(orderCanceled3);
        assertNotSame(orderCanceled1, orderCanceled2);
        assertNotSame(orderCanceled2, orderCanceled3);
        assertNotSame(orderCanceled1, orderCanceled3);
    }

    @Test
    void testOrderCanceledConstructorOrderIdArrayIsIndependent() {
        POE.OrderCanceled orderCanceled1 = new POE.OrderCanceled();
        POE.OrderCanceled orderCanceled2 = new POE.OrderCanceled();

        orderCanceled1.orderId[0] = (byte) 42;

        assertEquals((byte) 42, orderCanceled1.orderId[0]);
        assertEquals((byte) 0, orderCanceled2.orderId[0]);
    }

    @Test
    void testOrderCanceledConstructorUsesOrderIdLengthConstant() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertEquals(POE.ORDER_ID_LENGTH, orderCanceled.orderId.length);
        assertEquals(16, POE.ORDER_ID_LENGTH);
    }

    @Test
    void testOrderCanceledConstructorInitializesAllFieldsToDefaults() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertNotNull(orderCanceled.orderId);
        assertEquals(16, orderCanceled.orderId.length);
        assertEquals(0L, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.canceledQuantity);
        assertEquals(0, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledConstructorOrderIdArrayNotShared() {
        POE.OrderCanceled orderCanceled1 = new POE.OrderCanceled();
        byte[] array1 = orderCanceled1.orderId;

        POE.OrderCanceled orderCanceled2 = new POE.OrderCanceled();
        byte[] array2 = orderCanceled2.orderId;

        assertNotSame(array1, array2);
    }

    @Test
    void testOrderCanceledConstructorRepeatedCalls() {
        for (int i = 0; i < 10; i++) {
            POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
            assertNotNull(orderCanceled);
            assertNotNull(orderCanceled.orderId);
            assertEquals(16, orderCanceled.orderId.length);
        }
    }

    @Test
    void testOrderCanceledConstructorInstanceOfOutboundMessage() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertInstanceOf(POE.OutboundMessage.class, orderCanceled);
    }

    @Test
    void testOrderCanceledConstructorInstanceOfMessage() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertInstanceOf(POE.Message.class, orderCanceled);
    }

    // ========== OrderExecuted Constructor Tests ==========

    @Test
    void testOrderExecutedConstructorCreatesInstance() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertNotNull(orderExecuted);
    }

    @Test
    void testOrderExecutedConstructorInitializesOrderId() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertNotNull(orderExecuted.orderId);
        assertEquals(16, orderExecuted.orderId.length);
    }

    @Test
    void testOrderExecutedConstructorOrderIdIsEmptyArray() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        byte[] expectedEmptyArray = new byte[16];
        assertArrayEquals(expectedEmptyArray, orderExecuted.orderId);
    }

    @Test
    void testOrderExecutedConstructorAllBytesAreZero() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        for (int i = 0; i < orderExecuted.orderId.length; i++) {
            assertEquals(0, orderExecuted.orderId[i]);
        }
    }

    @Test
    void testOrderExecutedConstructorTimestampIsZero() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertEquals(0L, orderExecuted.timestamp);
    }

    @Test
    void testOrderExecutedConstructorQuantityIsZero() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertEquals(0L, orderExecuted.quantity);
    }

    @Test
    void testOrderExecutedConstructorPriceIsZero() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertEquals(0L, orderExecuted.price);
    }

    @Test
    void testOrderExecutedConstructorLiquidityFlagIsZero() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertEquals(0, orderExecuted.liquidityFlag);
    }

    @Test
    void testOrderExecutedConstructorMatchNumberIsZero() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertEquals(0L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorMultipleInstancesHaveDifferentArrays() {
        POE.OrderExecuted orderExecuted1 = new POE.OrderExecuted();
        POE.OrderExecuted orderExecuted2 = new POE.OrderExecuted();

        assertNotSame(orderExecuted1.orderId, orderExecuted2.orderId);
    }

    @Test
    void testOrderExecutedConstructorArrayCanBeModified() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        orderExecuted.orderId[0] = (byte) 1;
        orderExecuted.orderId[15] = (byte) 255;

        assertEquals((byte) 1, orderExecuted.orderId[0]);
        assertEquals((byte) 255, orderExecuted.orderId[15]);
    }

    @Test
    void testOrderExecutedConstructorCreatesValidOrderIdLength() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertEquals(POE.ORDER_ID_LENGTH, orderExecuted.orderId.length);
    }

    @Test
    void testOrderExecutedConstructorOrderIdIsNotNull() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertNotNull(orderExecuted.orderId);
    }

    @Test
    void testOrderExecutedConstructorFieldsAreAccessible() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        orderExecuted.timestamp = 123456L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        orderExecuted.matchNumber = 300L;

        assertEquals(123456L, orderExecuted.timestamp);
        assertEquals(100L, orderExecuted.quantity);
        assertEquals(200L, orderExecuted.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, orderExecuted.liquidityFlag);
        assertEquals(300L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorCanCreateMultipleInstances() {
        POE.OrderExecuted orderExecuted1 = new POE.OrderExecuted();
        POE.OrderExecuted orderExecuted2 = new POE.OrderExecuted();
        POE.OrderExecuted orderExecuted3 = new POE.OrderExecuted();

        assertNotNull(orderExecuted1);
        assertNotNull(orderExecuted2);
        assertNotNull(orderExecuted3);
        assertNotSame(orderExecuted1, orderExecuted2);
        assertNotSame(orderExecuted2, orderExecuted3);
        assertNotSame(orderExecuted1, orderExecuted3);
    }

    @Test
    void testOrderExecutedConstructorOrderIdArrayIsIndependent() {
        POE.OrderExecuted orderExecuted1 = new POE.OrderExecuted();
        POE.OrderExecuted orderExecuted2 = new POE.OrderExecuted();

        orderExecuted1.orderId[0] = (byte) 42;

        assertEquals((byte) 42, orderExecuted1.orderId[0]);
        assertEquals((byte) 0, orderExecuted2.orderId[0]);
    }

    @Test
    void testOrderExecutedConstructorUsesOrderIdLengthConstant() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertEquals(POE.ORDER_ID_LENGTH, orderExecuted.orderId.length);
        assertEquals(16, POE.ORDER_ID_LENGTH);
    }

    @Test
    void testOrderExecutedConstructorInitializesAllFieldsToDefaults() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertNotNull(orderExecuted.orderId);
        assertEquals(16, orderExecuted.orderId.length);
        assertEquals(0L, orderExecuted.timestamp);
        assertEquals(0L, orderExecuted.quantity);
        assertEquals(0L, orderExecuted.price);
        assertEquals(0, orderExecuted.liquidityFlag);
        assertEquals(0L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorOrderIdArrayNotShared() {
        POE.OrderExecuted orderExecuted1 = new POE.OrderExecuted();
        byte[] array1 = orderExecuted1.orderId;

        POE.OrderExecuted orderExecuted2 = new POE.OrderExecuted();
        byte[] array2 = orderExecuted2.orderId;

        assertNotSame(array1, array2);
    }

    @Test
    void testOrderExecutedConstructorRepeatedCalls() {
        for (int i = 0; i < 10; i++) {
            POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
            assertNotNull(orderExecuted);
            assertNotNull(orderExecuted.orderId);
            assertEquals(16, orderExecuted.orderId.length);
        }
    }

    @Test
    void testOrderExecutedConstructorInstanceOfOutboundMessage() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertInstanceOf(POE.OutboundMessage.class, orderExecuted);
    }

    @Test
    void testOrderExecutedConstructorInstanceOfMessage() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertInstanceOf(POE.Message.class, orderExecuted);
    }

    // ========== OrderRejected Constructor Tests ==========

    @Test
    void testOrderRejectedConstructorCreatesInstance() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertNotNull(orderRejected);
    }

    @Test
    void testOrderRejectedConstructorInitializesOrderId() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertNotNull(orderRejected.orderId);
        assertEquals(16, orderRejected.orderId.length);
    }

    @Test
    void testOrderRejectedConstructorOrderIdIsEmptyArray() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        byte[] expectedEmptyArray = new byte[16];
        assertArrayEquals(expectedEmptyArray, orderRejected.orderId);
    }

    @Test
    void testOrderRejectedConstructorAllBytesAreZero() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        for (int i = 0; i < orderRejected.orderId.length; i++) {
            assertEquals(0, orderRejected.orderId[i]);
        }
    }

    @Test
    void testOrderRejectedConstructorTimestampIsZero() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertEquals(0L, orderRejected.timestamp);
    }

    @Test
    void testOrderRejectedConstructorReasonIsZero() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertEquals(0, orderRejected.reason);
    }

    @Test
    void testOrderRejectedConstructorMultipleInstancesHaveDifferentArrays() {
        POE.OrderRejected orderRejected1 = new POE.OrderRejected();
        POE.OrderRejected orderRejected2 = new POE.OrderRejected();

        assertNotSame(orderRejected1.orderId, orderRejected2.orderId);
    }

    @Test
    void testOrderRejectedConstructorArrayCanBeModified() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        orderRejected.orderId[0] = (byte) 1;
        orderRejected.orderId[15] = (byte) 255;

        assertEquals((byte) 1, orderRejected.orderId[0]);
        assertEquals((byte) 255, orderRejected.orderId[15]);
    }

    @Test
    void testOrderRejectedConstructorCreatesValidOrderIdLength() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertEquals(POE.ORDER_ID_LENGTH, orderRejected.orderId.length);
    }

    @Test
    void testOrderRejectedConstructorOrderIdIsNotNull() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertNotNull(orderRejected.orderId);
    }

    @Test
    void testOrderRejectedConstructorFieldsAreAccessible() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        orderRejected.timestamp = 123456L;
        orderRejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        assertEquals(123456L, orderRejected.timestamp);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, orderRejected.reason);
    }

    @Test
    void testOrderRejectedConstructorCanCreateMultipleInstances() {
        POE.OrderRejected orderRejected1 = new POE.OrderRejected();
        POE.OrderRejected orderRejected2 = new POE.OrderRejected();
        POE.OrderRejected orderRejected3 = new POE.OrderRejected();

        assertNotNull(orderRejected1);
        assertNotNull(orderRejected2);
        assertNotNull(orderRejected3);
        assertNotSame(orderRejected1, orderRejected2);
        assertNotSame(orderRejected2, orderRejected3);
        assertNotSame(orderRejected1, orderRejected3);
    }

    @Test
    void testOrderRejectedConstructorOrderIdArrayIsIndependent() {
        POE.OrderRejected orderRejected1 = new POE.OrderRejected();
        POE.OrderRejected orderRejected2 = new POE.OrderRejected();

        orderRejected1.orderId[0] = (byte) 42;

        assertEquals((byte) 42, orderRejected1.orderId[0]);
        assertEquals((byte) 0, orderRejected2.orderId[0]);
    }

    @Test
    void testOrderRejectedConstructorUsesOrderIdLengthConstant() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertEquals(POE.ORDER_ID_LENGTH, orderRejected.orderId.length);
        assertEquals(16, POE.ORDER_ID_LENGTH);
    }

    @Test
    void testOrderRejectedConstructorInitializesAllFieldsToDefaults() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertNotNull(orderRejected.orderId);
        assertEquals(16, orderRejected.orderId.length);
        assertEquals(0L, orderRejected.timestamp);
        assertEquals(0, orderRejected.reason);
    }

    @Test
    void testOrderRejectedConstructorOrderIdArrayNotShared() {
        POE.OrderRejected orderRejected1 = new POE.OrderRejected();
        byte[] array1 = orderRejected1.orderId;

        POE.OrderRejected orderRejected2 = new POE.OrderRejected();
        byte[] array2 = orderRejected2.orderId;

        assertNotSame(array1, array2);
    }

    @Test
    void testOrderRejectedConstructorRepeatedCalls() {
        for (int i = 0; i < 10; i++) {
            POE.OrderRejected orderRejected = new POE.OrderRejected();
            assertNotNull(orderRejected);
            assertNotNull(orderRejected.orderId);
            assertEquals(16, orderRejected.orderId.length);
        }
    }

    @Test
    void testOrderRejectedConstructorInstanceOfOutboundMessage() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertInstanceOf(POE.OutboundMessage.class, orderRejected);
    }

    @Test
    void testOrderRejectedConstructorInstanceOfMessage() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertInstanceOf(POE.Message.class, orderRejected);
    }

    @Test
    void testOrderRejectedConstructorOrderIdLengthMatches() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertEquals(POE.ORDER_ID_LENGTH, orderRejected.orderId.length);
    }

    @Test
    void testOrderRejectedConstructorWithReasonConstants() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        orderRejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, orderRejected.reason);

        orderRejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, orderRejected.reason);

        orderRejected.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, orderRejected.reason);
    }
}
