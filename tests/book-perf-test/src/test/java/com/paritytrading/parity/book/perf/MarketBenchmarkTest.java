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
package com.paritytrading.parity.book.perf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MarketBenchmarkTest {

    private MarketBenchmark benchmark;

    @BeforeEach
    public void setUp() {
        benchmark = new MarketBenchmark();
        benchmark.prepare();
    }

    @Test
    public void testPrepare() {
        MarketBenchmark testBenchmark = new MarketBenchmark();
        testBenchmark.prepare();
        assertNotNull(testBenchmark);
    }

    @Test
    public void testAdd() {
        benchmark.add();
    }

    @Test
    public void testAddAndModify() {
        benchmark.addAndModify();
    }

    @Test
    public void testAddAndExecute() {
        benchmark.addAndExecute();
    }

    @Test
    public void testAddAndCancel() {
        benchmark.addAndCancel();
    }

    @Test
    public void testAddAndDelete() {
        benchmark.addAndDelete();
    }

    @Test
    public void testMultipleOperations() {
        benchmark.add();
        benchmark.addAndModify();
        benchmark.addAndExecute();
        benchmark.addAndCancel();
        benchmark.addAndDelete();
    }
}
