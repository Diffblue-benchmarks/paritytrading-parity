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

import static org.junit.jupiter.api.Assertions.*;

class MarketBenchmarkTest {

    private MarketBenchmark benchmark;

    @BeforeEach
    void setUp() {
        benchmark = new MarketBenchmark();
        benchmark.prepare();
    }

    @Test
    void prepare() {
        MarketBenchmark b = new MarketBenchmark();

        assertDoesNotThrow(() -> b.prepare());
    }

    @Test
    void add() {
        assertDoesNotThrow(() -> benchmark.add());
    }

    @Test
    void addMultipleOrders() {
        benchmark.add();
        benchmark.add();
        benchmark.add();
    }

    @Test
    void addAndModify() {
        assertDoesNotThrow(() -> benchmark.addAndModify());
    }

    @Test
    void addAndModifyMultiple() {
        benchmark.addAndModify();
        benchmark.addAndModify();
    }

    @Test
    void addAndExecute() {
        assertDoesNotThrow(() -> benchmark.addAndExecute());
    }

    @Test
    void addAndExecuteMultiple() {
        benchmark.addAndExecute();
        benchmark.addAndExecute();
    }

    @Test
    void addAndCancel() {
        assertDoesNotThrow(() -> benchmark.addAndCancel());
    }

    @Test
    void addAndCancelMultiple() {
        benchmark.addAndCancel();
        benchmark.addAndCancel();
    }

    @Test
    void addAndDelete() {
        assertDoesNotThrow(() -> benchmark.addAndDelete());
    }

    @Test
    void addAndDeleteMultiple() {
        benchmark.addAndDelete();
        benchmark.addAndDelete();
    }

    @Test
    void mixedOperations() {
        benchmark.add();
        benchmark.addAndModify();
        benchmark.addAndExecute();
        benchmark.addAndCancel();
        benchmark.addAndDelete();
    }

    @Test
    void prepareResetsState() {
        benchmark.add();
        benchmark.add();

        benchmark.prepare();

        assertDoesNotThrow(() -> benchmark.add());
    }
}
