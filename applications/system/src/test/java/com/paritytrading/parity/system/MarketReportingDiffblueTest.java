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
package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketReportingDiffblueTest {

    @Mock
    private MoldUDP64Server transport;

    @Mock
    private MoldUDP64RequestServer requestTransport;

    private MarketReporting marketReporting;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<MarketReporting> constructor = MarketReporting.class.getDeclaredConstructor(
                MoldUDP64Server.class, MoldUDP64RequestServer.class);
        constructor.setAccessible(true);
        marketReporting = constructor.newInstance(transport, requestTransport);
    }

    @Test
    void testGetTransport_givenMockedTransport_thenReturnsTransport() {
        assertEquals(transport, marketReporting.getTransport());
    }

    @Test
    void testGetRequestTransport_givenMockedRequestTransport_thenReturnsRequestTransport() {
        assertEquals(requestTransport, marketReporting.getRequestTransport());
    }

    @Test
    void testServe_givenValidState_thenCallsServeOnRequestTransport() throws IOException {
        marketReporting.serve();

        verify(requestTransport).serve(any());
    }

    @Test
    void testVersion_givenValidState_thenSendsVersionMessage() throws IOException {
        marketReporting.version();

        verify(transport).send(any());
    }

    @Test
    void testOrderEntered_givenValidParameters_thenSendsOrderEnteredMessage() throws IOException {
        marketReporting.orderEntered(1L, 2L, (byte) 'B', 3L, 100L, 50L);

        verify(transport).send(any());
    }

    @Test
    void testOrderAdded_givenValidOrderNumber_thenSendsOrderAddedMessage() throws IOException {
        marketReporting.orderAdded(1L);

        verify(transport).send(any());
    }

    @Test
    void testOrderCanceled_givenValidParameters_thenSendsOrderCanceledMessage() throws IOException {
        marketReporting.orderCanceled(1L, 50L);

        verify(transport).send(any());
    }

    @Test
    void testTrade_givenValidParameters_thenSendsTradeMessage() throws IOException {
        marketReporting.trade(1L, 2L, 100L, 3L);

        verify(transport).send(any());
    }

}
