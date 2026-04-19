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
package com.paritytrading.parity.net;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import com.paritytrading.parity.net.pmd.PMD;
import org.junit.jupiter.api.Test;

class ProtocolMessageTest {

    @Test
    void getReadsFieldsFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putInt(42);
        buffer.flip();

        PMD.Version message = new PMD.Version();
        message.get(buffer);

        assertEquals(42, message.version);
    }

    @Test
    void putWritesFieldsToBuffer() {
        PMD.Version message = new PMD.Version();
        message.version = 99;

        ByteBuffer buffer = ByteBuffer.allocate(16);
        message.put(buffer);

        buffer.flip();
        byte messageType = buffer.get();
        int version = buffer.getInt();

        assertEquals('V', messageType);
        assertEquals(99, version);
    }
}
