import com.paritytrading.parity.file.taq.TAQConfig;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TAQConfigTest {

    @Test
    public void testGetEncoding() {
        TAQConfig config = TAQConfig.DEFAULTS;
        assertNotNull(config.getEncoding());
        assertEquals(StandardCharsets.US_ASCII, config.getEncoding());
    }

    @Test
    public void testGetPriceFormatWithDefaultInstrument() {
        TAQConfig config = TAQConfig.DEFAULTS;
        DecimalFormat format = config.getPriceFormat("AAPL");
        assertNotNull(format);
        assertEquals(2, format.getMinimumFractionDigits());
        assertEquals(2, format.getMaximumFractionDigits());
    }

    @Test
    public void testGetSizeFormatWithDefaultInstrument() {
        TAQConfig config = TAQConfig.DEFAULTS;
        DecimalFormat format = config.getSizeFormat("AAPL");
        assertNotNull(format);
        assertEquals(0, format.getMinimumFractionDigits());
        assertEquals(0, format.getMaximumFractionDigits());
    }

    @Test
    public void testBuilderWithCustomEncoding() {
        TAQConfig config = new TAQConfig.Builder()
                .setEncoding(StandardCharsets.UTF_8)
                .build();
        assertEquals(StandardCharsets.UTF_8, config.getEncoding());
    }

    @Test
    public void testBuilderWithCustomPriceFractionDigits() {
        TAQConfig config = new TAQConfig.Builder()
                .setPriceFractionDigits("AAPL", 4)
                .build();
        DecimalFormat format = config.getPriceFormat("AAPL");
        assertEquals(4, format.getMinimumFractionDigits());
        assertEquals(4, format.getMaximumFractionDigits());
    }

    @Test
    public void testBuilderWithCustomSizeFractionDigits() {
        TAQConfig config = new TAQConfig.Builder()
                .setSizeFractionDigits("AAPL", 3)
                .build();
        DecimalFormat format = config.getSizeFormat("AAPL");
        assertEquals(3, format.getMinimumFractionDigits());
        assertEquals(3, format.getMaximumFractionDigits());
    }

    @Test
    public void testBuilderWithDefaultPriceFractionDigits() {
        TAQConfig config = new TAQConfig.Builder()
                .setPriceFractionDigits(5)
                .build();
        DecimalFormat format = config.getPriceFormat("AAPL");
        assertEquals(5, format.getMinimumFractionDigits());
        assertEquals(5, format.getMaximumFractionDigits());
    }

    @Test
    public void testBuilderWithDefaultSizeFractionDigits() {
        TAQConfig config = new TAQConfig.Builder()
                .setSizeFractionDigits(1)
                .build();
        DecimalFormat format = config.getSizeFormat("AAPL");
        assertEquals(1, format.getMinimumFractionDigits());
        assertEquals(1, format.getMaximumFractionDigits());
    }

    @Test
    public void testGetPriceFormatReturnsDefaultForUnknownInstrument() {
        TAQConfig config = new TAQConfig.Builder()
                .setPriceFractionDigits("AAPL", 4)
                .build();
        DecimalFormat format = config.getPriceFormat("GOOG");
        assertEquals(2, format.getMinimumFractionDigits());
        assertEquals(2, format.getMaximumFractionDigits());
    }

    @Test
    public void testGetSizeFormatReturnsDefaultForUnknownInstrument() {
        TAQConfig config = new TAQConfig.Builder()
                .setSizeFractionDigits("AAPL", 3)
                .build();
        DecimalFormat format = config.getSizeFormat("GOOG");
        assertEquals(0, format.getMinimumFractionDigits());
        assertEquals(0, format.getMaximumFractionDigits());
    }
}
