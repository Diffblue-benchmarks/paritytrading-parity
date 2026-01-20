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
package com.paritytrading.parity.net.poe;

import com.diffblue.cover.annotations.InterestingTestFactory;
import java.nio.ByteBuffer;

public class POETestFactory {

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForCancelOrderPut() {
        return ByteBuffer.allocate(25);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForEnterOrderGet() {
        return ByteBuffer.allocate(41);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForEnterOrderPut() {
        return ByteBuffer.allocate(42);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderAcceptedGet() {
        return ByteBuffer.allocate(57);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderAcceptedPut() {
        return ByteBuffer.allocate(58);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderRejectedGet() {
        return ByteBuffer.allocate(25);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderRejectedPut() {
        return ByteBuffer.allocate(26);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderExecutedGet() {
        return ByteBuffer.allocate(45);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderExecutedPut() {
        return ByteBuffer.allocate(46);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderCanceledGet() {
        return ByteBuffer.allocate(33);
    }

    @InterestingTestFactory
    public static ByteBuffer createByteBufferForOrderCanceledPut() {
        return ByteBuffer.allocate(34);
    }
}
