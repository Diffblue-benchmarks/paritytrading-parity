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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.paritytrading.parity.net.pmr.PMR;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

public class TradeProcessorTest {

    @Test
    public void testOrderConstructorCopiesUsername() throws Exception {
        PMR.OrderEntered message = new PMR.OrderEntered();
        message.username = 123456789L;
        message.side = PMR.BUY;
        message.instrument = 987654321L;
        message.price = 10050L;
        message.quantity = 100L;

        Object order = createOrder(message);

        long username = getOrderField(order, "username");
        assertEquals(123456789L, username);
    }

    @Test
    public void testOrderConstructorCopiesSide() throws Exception {
        PMR.OrderEntered message = new PMR.OrderEntered();
        message.username = 111L;
        message.side = PMR.SELL;
        message.instrument = 222L;
        message.price = 5000L;
        message.quantity = 50L;

        Object order = createOrder(message);

        byte side = getOrderFieldByte(order, "side");
        assertEquals(PMR.SELL, side);
    }

    @Test
    public void testOrderConstructorCopiesInstrument() throws Exception {
        PMR.OrderEntered message = new PMR.OrderEntered();
        message.username = 333L;
        message.side = PMR.BUY;
        message.instrument = 444444444L;
        message.price = 2000L;
        message.quantity = 25L;

        Object order = createOrder(message);

        long instrument = getOrderField(order, "instrument");
        assertEquals(444444444L, instrument);
    }

    @Test
    public void testOrderConstructorCopiesPrice() throws Exception {
        PMR.OrderEntered message = new PMR.OrderEntered();
        message.username = 555L;
        message.side = PMR.BUY;
        message.instrument = 666L;
        message.price = 15075L;
        message.quantity = 200L;

        Object order = createOrder(message);

        long price = getOrderField(order, "price");
        assertEquals(15075L, price);
    }

    @Test
    public void testOrderConstructorCopiesQuantityToRemainingQuantity() throws Exception {
        PMR.OrderEntered message = new PMR.OrderEntered();
        message.username = 777L;
        message.side = PMR.SELL;
        message.instrument = 888L;
        message.price = 9500L;
        message.quantity = 300L;

        Object order = createOrder(message);

        long remainingQuantity = getOrderField(order, "remainingQuantity");
        assertEquals(300L, remainingQuantity);
    }

    @Test
    public void testOrderConstructorWithBuySide() throws Exception {
        PMR.OrderEntered message = new PMR.OrderEntered();
        message.username = 1001L;
        message.side = PMR.BUY;
        message.instrument = 2002L;
        message.price = 12000L;
        message.quantity = 150L;

        Object order = createOrder(message);

        byte side = getOrderFieldByte(order, "side");
        assertEquals(PMR.BUY, side);
    }

    private Object createOrder(PMR.OrderEntered message) throws Exception {
        Class<?> orderClass = Class.forName("com.paritytrading.parity.reporter.TradeProcessor$Order");
        Constructor<?> constructor = orderClass.getDeclaredConstructor(PMR.OrderEntered.class);
        constructor.setAccessible(true);
        return constructor.newInstance(message);
    }

    private long getOrderField(Object order, String fieldName) throws Exception {
        Field field = order.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getLong(order);
    }

    private byte getOrderFieldByte(Object order, String fieldName) throws Exception {
        Field field = order.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getByte(order);
    }
}
