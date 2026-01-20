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
package com.paritytrading.parity.net.pmr;

import com.diffblue.cover.annotations.InterestingTestFactory;
import java.nio.ByteBuffer;

public class PMRTestFactory {

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderCanceledPut() {
        return ByteBuffer.allocate(25);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderEnteredGet() {
        return ByteBuffer.allocate(50);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderEnteredPut() {
        return ByteBuffer.allocate(50);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForTradeGet() {
        return ByteBuffer.allocate(37);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForTradePut() {
        return ByteBuffer.allocate(37);
    }
}
