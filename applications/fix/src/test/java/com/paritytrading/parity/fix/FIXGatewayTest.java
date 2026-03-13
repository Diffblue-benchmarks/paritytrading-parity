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

import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

class FIXGatewayTest {

    private Config createTestConfig() {
        String configString =
            "order-entry {\n" +
            "  address = \"127.0.0.1\"\n" +
            "  port = 4000\n" +
            "}\n" +
            "fix {\n" +
            "  address = \"127.0.0.1\"\n" +
            "  port = 5000\n" +
            "  sender-comp-id = \"TEST_SENDER\"\n" +
            "}\n" +
            "instruments {\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";
        return ConfigFactory.parseString(configString);
    }

    @Test
    public void testOrderEntryWithValidConfig() throws Exception {
        Config config = createTestConfig();

        Method orderEntryMethod = FIXGateway.class.getDeclaredMethod("orderEntry", Config.class);
        orderEntryMethod.setAccessible(true);

        OrderEntryFactory result = (OrderEntryFactory) orderEntryMethod.invoke(null, config);

        assertNotNull(result);
    }

    @Test
    public void testOrderEntryCreatesFactory() throws Exception {
        Config config = createTestConfig();

        Method orderEntryMethod = FIXGateway.class.getDeclaredMethod("orderEntry", Config.class);
        orderEntryMethod.setAccessible(true);

        OrderEntryFactory factory = (OrderEntryFactory) orderEntryMethod.invoke(null, config);

        assertNotNull(factory);
    }

    @Test
    public void testFixCreatesAcceptor() throws Exception {
        Config config = createTestConfig();

        OrderEntryFactory orderEntry = new OrderEntryFactory(
            new InetSocketAddress(InetAddress.getLoopbackAddress(), 0)
        );

        Method fixMethod = FIXGateway.class.getDeclaredMethod("fix", OrderEntryFactory.class, Config.class);
        fixMethod.setAccessible(true);

        FIXAcceptor acceptor = (FIXAcceptor) fixMethod.invoke(null, orderEntry, config);

        assertNotNull(acceptor);
        assertNotNull(acceptor.getServerChannel());
        acceptor.getServerChannel().close();
    }

    @Test
    public void testFixWithValidConfiguration() throws Exception {
        Config config = createTestConfig();

        OrderEntryFactory orderEntry = new OrderEntryFactory(
            new InetSocketAddress(InetAddress.getLoopbackAddress(), 9999)
        );

        Method fixMethod = FIXGateway.class.getDeclaredMethod("fix", OrderEntryFactory.class, Config.class);
        fixMethod.setAccessible(true);

        FIXAcceptor acceptor = (FIXAcceptor) fixMethod.invoke(null, orderEntry, config);

        assertNotNull(acceptor);
        acceptor.getServerChannel().close();
    }

    @Test
    public void testOrderEntryMethodAccessibility() throws Exception {
        Method orderEntryMethod = FIXGateway.class.getDeclaredMethod("orderEntry", Config.class);
        assertNotNull(orderEntryMethod);
    }

    @Test
    public void testFixMethodAccessibility() throws Exception {
        Method fixMethod = FIXGateway.class.getDeclaredMethod("fix", OrderEntryFactory.class, Config.class);
        assertNotNull(fixMethod);
    }

    @Test
    public void testMainWithConfig() throws Exception {
        String configString =
            "order-entry {\n" +
            "  address = \"127.0.0.1\"\n" +
            "  port = 0\n" +
            "}\n" +
            "fix {\n" +
            "  address = \"127.0.0.1\"\n" +
            "  port = 0\n" +
            "  sender-comp-id = \"TEST_SENDER\"\n" +
            "}\n" +
            "instruments {\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";
        Config config = ConfigFactory.parseString(configString);

        Method mainMethod = FIXGateway.class.getDeclaredMethod("main", Config.class);
        mainMethod.setAccessible(true);

        final Exception[] exception = new Exception[1];
        Thread thread = new Thread(() -> {
            try {
                mainMethod.invoke(null, config);
            } catch (Exception e) {
                exception[0] = e;
            }
        });

        thread.start();
        Thread.sleep(200);
        thread.interrupt();
        thread.join(1000);

        if (exception[0] != null && !(exception[0].getCause() instanceof java.nio.channels.ClosedByInterruptException)
            && !(exception[0].getCause() instanceof java.nio.channels.ClosedSelectorException)) {
            throw exception[0];
        }
    }
}
