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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

public class EnterCommandTest {

    @Test
    public void testBuyCommandConstructor() {
        EnterCommand command = new EnterCommand(POE.BUY);
        assertNotNull(command);
    }

    @Test
    public void testSellCommandConstructor() {
        EnterCommand command = new EnterCommand(POE.SELL);
        assertNotNull(command);
    }

    @Test
    public void testGetNameBuy() {
        EnterCommand command = new EnterCommand(POE.BUY);
        assertEquals("buy", command.getName());
    }

    @Test
    public void testGetNameSell() {
        EnterCommand command = new EnterCommand(POE.SELL);
        assertEquals("sell", command.getName());
    }

    @Test
    public void testGetDescriptionBuy() {
        EnterCommand command = new EnterCommand(POE.BUY);
        assertEquals("Enter a buy order", command.getDescription());
    }

    @Test
    public void testGetDescriptionSell() {
        EnterCommand command = new EnterCommand(POE.SELL);
        assertEquals("Enter a sell order", command.getDescription());
    }

    @Test
    public void testGetUsageBuy() {
        EnterCommand command = new EnterCommand(POE.BUY);
        assertEquals("buy <quantity> <instrument> <price>", command.getUsage());
    }

    @Test
    public void testGetUsageSell() {
        EnterCommand command = new EnterCommand(POE.SELL);
        assertEquals("sell <quantity> <instrument> <price>", command.getUsage());
    }

    @Test
    public void testGetNameReturnsCorrectValueForBuySide() {
        EnterCommand command = new EnterCommand((byte) 'B');
        String name = command.getName();
        assertEquals("buy", name);
    }

    @Test
    public void testGetNameReturnsCorrectValueForSellSide() {
        EnterCommand command = new EnterCommand((byte) 'S');
        String name = command.getName();
        assertEquals("sell", name);
    }

    @Test
    public void testGetDescriptionIncludesCommandName() {
        EnterCommand buyCommand = new EnterCommand(POE.BUY);
        EnterCommand sellCommand = new EnterCommand(POE.SELL);

        String buyDescription = buyCommand.getDescription();
        String sellDescription = sellCommand.getDescription();

        assertEquals("Enter a buy order", buyDescription);
        assertEquals("Enter a sell order", sellDescription);
    }

    @Test
    public void testGetUsageIncludesCommandName() {
        EnterCommand buyCommand = new EnterCommand(POE.BUY);
        EnterCommand sellCommand = new EnterCommand(POE.SELL);

        String buyUsage = buyCommand.getUsage();
        String sellUsage = sellCommand.getUsage();

        assertEquals("buy <quantity> <instrument> <price>", buyUsage);
        assertEquals("sell <quantity> <instrument> <price>", sellUsage);
    }
}
