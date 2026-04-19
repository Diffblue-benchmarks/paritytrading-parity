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
package com.paritytrading.parity.file.taq;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class TAQConfigBuilderTest {

    @Test
    void setEncoding() throws Exception {
        TAQConfig config = new TAQConfig.Builder()
            .setEncoding(StandardCharsets.UTF_8)
            .build();

        assertEquals(StandardCharsets.UTF_8, config.getEncoding());
    }

    @Test
    void setSizeFractionDigitsForInstrument() throws Exception {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("FOO", 4)
            .build();

        TAQ.Trade trade = new TAQ.Trade();

        trade.date            = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument      = "FOO";
        trade.price           = 10.00;
        trade.size            = 5.5;
        trade.side            = TAQ.BUY;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                TAQWriter writer = new TAQWriter(out, config)) {
            writer.write(trade);
            writer.flush();

            String output = "" +
                "Date\t" +
                "Timestamp\t" +
                "Instrument\t" +
                "Record Type\t" +
                "Bid Price\t" +
                "Bid Size\t" +
                "Ask Price\t" +
                "Ask Size\t" +
                "Trade Price\t" +
                "Trade Size\t" +
                "Trade Side\n" +
                "2016-01-01\t" +
                "00:00:00.000\t" +
                "FOO\t" +
                "T\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t" +
                "10.00\t" +
                "5.5000\t" +
                "B\n";

            assertEquals(output, out.toString("US-ASCII"));
        }
    }

    @Test
    void setDefaultPriceFractionDigits() throws Exception {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits(4)
            .build();

        TAQ.Quote quote = new TAQ.Quote();

        quote.date            = "2016-01-01";
        quote.timestampMillis = 0;
        quote.instrument      = "FOO";
        quote.bidPrice        = 100.5;
        quote.bidSize         = 10;
        quote.askPrice        = 100.75;
        quote.askSize         = 20;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                TAQWriter writer = new TAQWriter(out, config)) {
            writer.write(quote);
            writer.flush();

            String output = "" +
                "Date\t" +
                "Timestamp\t" +
                "Instrument\t" +
                "Record Type\t" +
                "Bid Price\t" +
                "Bid Size\t" +
                "Ask Price\t" +
                "Ask Size\t" +
                "Trade Price\t" +
                "Trade Size\t" +
                "Trade Side\n" +
                "2016-01-01\t" +
                "00:00:00.000\t" +
                "FOO\t" +
                "Q\t" +
                "100.5000\t" +
                "10\t" +
                "100.7500\t" +
                "20\t" +
                "\t" +
                "\t" +
                "\n";

            assertEquals(output, out.toString("US-ASCII"));
        }
    }

}
