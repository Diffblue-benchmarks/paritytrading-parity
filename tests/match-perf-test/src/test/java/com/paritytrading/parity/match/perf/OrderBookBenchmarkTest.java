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
package com.paritytrading.parity.match.perf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderBookBenchmarkTest {

    private OrderBookBenchmark benchmark;

    @BeforeEach
    public void setUp() {
        benchmark = new OrderBookBenchmark();
    }

    @Test
    public void testPrepare() {
        benchmark.prepare();

        assertNotNull(benchmark);
    }

    @Test
    public void testEnter() {
        benchmark.prepare();

        assertDoesNotThrow(() -> {
            benchmark.enter();
        });
    }

    @Test
    public void testEnterMultipleTimes() {
        benchmark.prepare();

        assertDoesNotThrow(() -> {
            benchmark.enter();
            benchmark.enter();
            benchmark.enter();
        });
    }

    @Test
    public void testEnterAndCancel() {
        benchmark.prepare();

        assertDoesNotThrow(() -> {
            benchmark.enterAndCancel();
        });
    }

    @Test
    public void testEnterAndCancelMultipleTimes() {
        benchmark.prepare();

        assertDoesNotThrow(() -> {
            benchmark.enterAndCancel();
            benchmark.enterAndCancel();
            benchmark.enterAndCancel();
        });
    }

    @Test
    public void testEnterAndCancelAfterEnter() {
        benchmark.prepare();

        assertDoesNotThrow(() -> {
            benchmark.enter();
            benchmark.enterAndCancel();
            benchmark.enter();
        });
    }

    @Test
    public void testPrepareInitializesOrderBook() {
        benchmark.prepare();

        assertDoesNotThrow(() -> {
            benchmark.enter();
        });
    }
}
