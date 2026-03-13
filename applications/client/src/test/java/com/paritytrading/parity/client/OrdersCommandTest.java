package com.paritytrading.parity.client;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrdersCommandTest {

    @Test
    public void testGetName() {
        OrdersCommand command = new OrdersCommand();
        assertEquals("orders", command.getName());
    }

    @Test
    public void testGetDescription() {
        OrdersCommand command = new OrdersCommand();
        assertEquals("Display open orders", command.getDescription());
    }

    @Test
    public void testGetUsage() {
        OrdersCommand command = new OrdersCommand();
        assertEquals("orders", command.getUsage());
    }

    @Test
    public void testExecuteWithArguments() {
        OrdersCommand command = new OrdersCommand();
        Scanner scanner = new Scanner("extra arguments");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(null, scanner);
        });
    }
}
