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
package com.paritytrading.parity.client;

import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventVisitorTest {

    @Test
    public void testVisitOrderAccepted() {
        TestVisitor visitor = new TestVisitor();
        POE.OrderAccepted poeMessage = createOrderAcceptedMessage();
        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        event.accept(visitor);

        assertTrue(visitor.orderAcceptedVisited);
    }

    @Test
    public void testVisitOrderRejected() {
        TestVisitor visitor = new TestVisitor();
        POE.OrderRejected poeMessage = createOrderRejectedMessage();
        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        event.accept(visitor);

        assertTrue(visitor.orderRejectedVisited);
    }

    @Test
    public void testVisitOrderExecuted() {
        TestVisitor visitor = new TestVisitor();
        POE.OrderExecuted poeMessage = createOrderExecutedMessage();
        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        event.accept(visitor);

        assertTrue(visitor.orderExecutedVisited);
    }

    @Test
    public void testVisitOrderCanceled() {
        TestVisitor visitor = new TestVisitor();
        POE.OrderCanceled poeMessage = createOrderCanceledMessage();
        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        event.accept(visitor);

        assertTrue(visitor.orderCanceledVisited);
    }

    private POE.OrderAccepted createOrderAcceptedMessage() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000L;
        setOrderId(message.orderId, "ORDER001");
        message.side = POE.BUY;
        message.instrument = 100L;
        message.quantity = 50L;
        message.price = 1000L;
        message.orderNumber = 1L;
        return message;
    }

    private POE.OrderRejected createOrderRejectedMessage() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2000L;
        setOrderId(message.orderId, "ORDER002");
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;
        return message;
    }

    private POE.OrderExecuted createOrderExecutedMessage() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 3000L;
        setOrderId(message.orderId, "ORDER003");
        message.quantity = 25L;
        message.price = 1050L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber = 1L;
        return message;
    }

    private POE.OrderCanceled createOrderCanceledMessage() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 4000L;
        setOrderId(message.orderId, "ORDER004");
        message.canceledQuantity = 10L;
        message.reason = POE.ORDER_CANCEL_REASON_REQUEST;
        return message;
    }

    private void setOrderId(byte[] orderId, String value) {
        byte[] bytes = value.getBytes();
        System.arraycopy(bytes, 0, orderId, 0, Math.min(bytes.length, orderId.length));
    }

    private static class TestVisitor implements EventVisitor {
        boolean orderAcceptedVisited = false;
        boolean orderRejectedVisited = false;
        boolean orderExecutedVisited = false;
        boolean orderCanceledVisited = false;

        @Override
        public void visit(Event.OrderAccepted event) {
            orderAcceptedVisited = true;
        }

        @Override
        public void visit(Event.OrderRejected event) {
            orderRejectedVisited = true;
        }

        @Override
        public void visit(Event.OrderExecuted event) {
            orderExecutedVisited = true;
        }

        @Override
        public void visit(Event.OrderCanceled event) {
            orderCanceledVisited = true;
        }
    }
}
