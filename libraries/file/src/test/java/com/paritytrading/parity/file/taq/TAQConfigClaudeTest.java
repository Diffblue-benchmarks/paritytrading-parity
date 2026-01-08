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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import org.junit.jupiter.api.Test;

class TAQConfigClaudeTest {

    @Test
    void getEncodingReturnsDefaultUsAscii() {
        TAQConfig config = new TAQConfig.Builder().build();

        assertEquals(StandardCharsets.US_ASCII, config.getEncoding());
    }

    @Test
    void getEncodingReturnsCustomEncoding() {
        TAQConfig config = new TAQConfig.Builder()
            .setEncoding(StandardCharsets.UTF_8)
            .build();

        assertEquals(StandardCharsets.UTF_8, config.getEncoding());
    }

    @Test
    void getEncodingReturnsUtf16Encoding() {
        TAQConfig config = new TAQConfig.Builder()
            .setEncoding(StandardCharsets.UTF_16)
            .build();

        assertEquals(StandardCharsets.UTF_16, config.getEncoding());
    }

    @Test
    void getEncodingReturnsIso88591Encoding() {
        TAQConfig config = new TAQConfig.Builder()
            .setEncoding(StandardCharsets.ISO_8859_1)
            .build();

        assertEquals(StandardCharsets.ISO_8859_1, config.getEncoding());
    }

    @Test
    void getPriceFormatReturnsDefaultFormatWithTwoFractionDigits() {
        TAQConfig config = new TAQConfig.Builder().build();
        DecimalFormat format = config.getPriceFormat("UNKNOWN_INSTRUMENT");

        assertNotNull(format);
        assertEquals(2, format.getMinimumFractionDigits());
        assertEquals(2, format.getMaximumFractionDigits());
        assertEquals("100.50", format.format(100.5));
    }

    @Test
    void getPriceFormatReturnsInstrumentSpecificFormat() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("FOO", 4)
            .build();
        DecimalFormat format = config.getPriceFormat("FOO");

        assertNotNull(format);
        assertEquals(4, format.getMinimumFractionDigits());
        assertEquals(4, format.getMaximumFractionDigits());
        assertEquals("100.5000", format.format(100.5));
    }

    @Test
    void getPriceFormatReturnsInstrumentSpecificFormatWithZeroDigits() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("BAR", 0)
            .build();
        DecimalFormat format = config.getPriceFormat("BAR");

        assertNotNull(format);
        assertEquals(0, format.getMinimumFractionDigits());
        assertEquals(0, format.getMaximumFractionDigits());
        assertEquals("100", format.format(100.5));
    }

    @Test
    void getPriceFormatReturnsInstrumentSpecificFormatWithSixDigits() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("BTC", 6)
            .build();
        DecimalFormat format = config.getPriceFormat("BTC");

        assertNotNull(format);
        assertEquals(6, format.getMinimumFractionDigits());
        assertEquals(6, format.getMaximumFractionDigits());
        assertEquals("0.975000", format.format(0.975));
    }

    @Test
    void getPriceFormatReturnsDefaultWhenInstrumentNotConfigured() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("FOO", 6)
            .setPriceFractionDigits("BAR", 4)
            .build();
        DecimalFormat format = config.getPriceFormat("UNCONFIGURED");

        assertNotNull(format);
        assertEquals(2, format.getMinimumFractionDigits());
        assertEquals(2, format.getMaximumFractionDigits());
    }

    @Test
    void getPriceFormatReturnsCustomDefaultFormat() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits(5)
            .build();
        DecimalFormat format = config.getPriceFormat("ANY_INSTRUMENT");

        assertNotNull(format);
        assertEquals(5, format.getMinimumFractionDigits());
        assertEquals(5, format.getMaximumFractionDigits());
        assertEquals("100.50000", format.format(100.5));
    }

    @Test
    void getPriceFormatPreservesInstrumentSpecificOverDefault() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits(3)
            .setPriceFractionDigits("SPECIAL", 8)
            .build();

        DecimalFormat specialFormat = config.getPriceFormat("SPECIAL");
        assertEquals(8, specialFormat.getMinimumFractionDigits());

        DecimalFormat defaultFormat = config.getPriceFormat("OTHER");
        assertEquals(3, defaultFormat.getMinimumFractionDigits());
    }

    @Test
    void getSizeFormatReturnsDefaultFormatWithZeroFractionDigits() {
        TAQConfig config = new TAQConfig.Builder().build();
        DecimalFormat format = config.getSizeFormat("UNKNOWN_INSTRUMENT");

        assertNotNull(format);
        assertEquals(0, format.getMinimumFractionDigits());
        assertEquals(0, format.getMaximumFractionDigits());
        assertEquals("1000", format.format(1000));
    }

    @Test
    void getSizeFormatReturnsInstrumentSpecificFormat() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("FOO", 8)
            .build();
        DecimalFormat format = config.getSizeFormat("FOO");

        assertNotNull(format);
        assertEquals(8, format.getMinimumFractionDigits());
        assertEquals(8, format.getMaximumFractionDigits());
        assertEquals("0.00000100", format.format(0.000001));
    }

    @Test
    void getSizeFormatReturnsInstrumentSpecificFormatWithFourDigits() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("BAR", 4)
            .build();
        DecimalFormat format = config.getSizeFormat("BAR");

        assertNotNull(format);
        assertEquals(4, format.getMinimumFractionDigits());
        assertEquals(4, format.getMaximumFractionDigits());
        assertEquals("1234.5678", format.format(1234.5678));
    }

    @Test
    void getSizeFormatReturnsDefaultWhenInstrumentNotConfigured() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("FOO", 6)
            .setSizeFractionDigits("BAR", 4)
            .build();
        DecimalFormat format = config.getSizeFormat("UNCONFIGURED");

        assertNotNull(format);
        assertEquals(0, format.getMinimumFractionDigits());
        assertEquals(0, format.getMaximumFractionDigits());
    }

    @Test
    void getSizeFormatReturnsCustomDefaultFormat() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits(3)
            .build();
        DecimalFormat format = config.getSizeFormat("ANY_INSTRUMENT");

        assertNotNull(format);
        assertEquals(3, format.getMinimumFractionDigits());
        assertEquals(3, format.getMaximumFractionDigits());
        assertEquals("1000.000", format.format(1000));
    }

    @Test
    void getSizeFormatPreservesInstrumentSpecificOverDefault() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits(2)
            .setSizeFractionDigits("SPECIAL", 6)
            .build();

        DecimalFormat specialFormat = config.getSizeFormat("SPECIAL");
        assertEquals(6, specialFormat.getMinimumFractionDigits());

        DecimalFormat defaultFormat = config.getSizeFormat("OTHER");
        assertEquals(2, defaultFormat.getMinimumFractionDigits());
    }

    @Test
    void defaultConfigurationHasUsAsciiEncoding() {
        assertEquals(StandardCharsets.US_ASCII, TAQConfig.DEFAULTS.getEncoding());
    }

    @Test
    void defaultConfigurationHasTwoDigitPriceFormat() {
        DecimalFormat format = TAQConfig.DEFAULTS.getPriceFormat("ANY");
        assertEquals(2, format.getMinimumFractionDigits());
        assertEquals(2, format.getMaximumFractionDigits());
    }

    @Test
    void defaultConfigurationHasZeroDigitSizeFormat() {
        DecimalFormat format = TAQConfig.DEFAULTS.getSizeFormat("ANY");
        assertEquals(0, format.getMinimumFractionDigits());
        assertEquals(0, format.getMaximumFractionDigits());
    }

    @Test
    void multipleInstrumentsCanHaveDifferentPriceFormats() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("STOCK", 2)
            .setPriceFractionDigits("CRYPTO", 8)
            .setPriceFractionDigits("FOREX", 5)
            .build();

        assertEquals(2, config.getPriceFormat("STOCK").getMinimumFractionDigits());
        assertEquals(8, config.getPriceFormat("CRYPTO").getMinimumFractionDigits());
        assertEquals(5, config.getPriceFormat("FOREX").getMinimumFractionDigits());
    }

    @Test
    void multipleInstrumentsCanHaveDifferentSizeFormats() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("STOCK", 0)
            .setSizeFractionDigits("CRYPTO", 8)
            .setSizeFractionDigits("FOREX", 2)
            .build();

        assertEquals(0, config.getSizeFormat("STOCK").getMinimumFractionDigits());
        assertEquals(8, config.getSizeFormat("CRYPTO").getMinimumFractionDigits());
        assertEquals(2, config.getSizeFormat("FOREX").getMinimumFractionDigits());
    }

    @Test
    void priceAndSizeFormatsAreIndependent() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("FOO", 6)
            .setSizeFractionDigits("FOO", 8)
            .build();

        assertEquals(6, config.getPriceFormat("FOO").getMinimumFractionDigits());
        assertEquals(8, config.getSizeFormat("FOO").getMinimumFractionDigits());
    }

    @Test
    void getPriceFormatHandlesEmptyStringInstrument() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("", 5)
            .build();

        DecimalFormat format = config.getPriceFormat("");
        assertEquals(5, format.getMinimumFractionDigits());
    }

    @Test
    void getSizeFormatHandlesEmptyStringInstrument() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("", 3)
            .build();

        DecimalFormat format = config.getSizeFormat("");
        assertEquals(3, format.getMinimumFractionDigits());
    }

    @Test
    void getPriceFormatHandlesInstrumentWithSpecialCharacters() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("FOO-BAR.USD", 4)
            .build();

        DecimalFormat format = config.getPriceFormat("FOO-BAR.USD");
        assertEquals(4, format.getMinimumFractionDigits());
    }

    @Test
    void getSizeFormatHandlesInstrumentWithSpecialCharacters() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("FOO-BAR.USD", 6)
            .build();

        DecimalFormat format = config.getSizeFormat("FOO-BAR.USD");
        assertEquals(6, format.getMinimumFractionDigits());
    }

    @Test
    void getPriceFormatUsesUsLocaleForFormatting() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("TEST", 2)
            .build();

        DecimalFormat format = config.getPriceFormat("TEST");
        String formatted = format.format(1234.56);

        assertEquals("1234.56", formatted);
    }

    @Test
    void getSizeFormatUsesUsLocaleForFormatting() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("TEST", 2)
            .build();

        DecimalFormat format = config.getSizeFormat("TEST");
        String formatted = format.format(1234.56);

        assertEquals("1234.56", formatted);
    }

    @Test
    void builderCanChainAllMethods() {
        TAQConfig config = new TAQConfig.Builder()
            .setEncoding(StandardCharsets.UTF_8)
            .setPriceFractionDigits(3)
            .setSizeFractionDigits(4)
            .setPriceFractionDigits("FOO", 6)
            .setSizeFractionDigits("BAR", 8)
            .build();

        assertEquals(StandardCharsets.UTF_8, config.getEncoding());
        assertEquals(3, config.getPriceFormat("OTHER").getMinimumFractionDigits());
        assertEquals(4, config.getSizeFormat("OTHER").getMinimumFractionDigits());
        assertEquals(6, config.getPriceFormat("FOO").getMinimumFractionDigits());
        assertEquals(8, config.getSizeFormat("BAR").getMinimumFractionDigits());
    }

    @Test
    void sameInstrumentCanBeReconfiguredForPrice() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("FOO", 2)
            .setPriceFractionDigits("FOO", 4)
            .build();

        DecimalFormat format = config.getPriceFormat("FOO");
        assertEquals(4, format.getMinimumFractionDigits());
    }

    @Test
    void sameInstrumentCanBeReconfiguredForSize() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("FOO", 2)
            .setSizeFractionDigits("FOO", 6)
            .build();

        DecimalFormat format = config.getSizeFormat("FOO");
        assertEquals(6, format.getMinimumFractionDigits());
    }

    @Test
    void defaultPriceFormatCanBeReconfigured() {
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits(5)
            .setPriceFractionDigits(7)
            .build();

        DecimalFormat format = config.getPriceFormat("UNCONFIGURED");
        assertEquals(7, format.getMinimumFractionDigits());
    }

    @Test
    void defaultSizeFormatCanBeReconfigured() {
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits(3)
            .setSizeFractionDigits(5)
            .build();

        DecimalFormat format = config.getSizeFormat("UNCONFIGURED");
        assertEquals(5, format.getMinimumFractionDigits());
    }
}
