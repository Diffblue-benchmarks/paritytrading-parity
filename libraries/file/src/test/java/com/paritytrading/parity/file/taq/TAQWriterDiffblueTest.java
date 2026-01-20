package com.paritytrading.parity.file.taq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.file.taq.TAQ.Quote;
import com.paritytrading.parity.file.taq.TAQ.Trade;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.FieldPosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TAQWriterDiffblueTest {
  /**
   * Test {@link TAQWriter#TAQWriter(OutputStream)}.
   *
   * <p>Method under test: {@link TAQWriter#TAQWriter(OutputStream)}
   */
  @Test
  @DisplayName("Test new TAQWriter(OutputStream)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQWriter.<init>(OutputStream)"})
  void testNewTAQWriter() {
    // Arrange and Act
    TAQWriter actualTaqWriter = new TAQWriter(new ByteArrayOutputStream());

    // Assert
    assertEquals("", actualTaqWriter.getBuffer().toString());
    assertEquals("US-ASCII", actualTaqWriter.getConfig().getEncoding().name());
    FieldPosition position = actualTaqWriter.getPosition();
    assertNull(position.getFieldAttribute());
    assertEquals(0, position.getBeginIndex());
    assertEquals(0, position.getEndIndex());
    assertEquals(0, position.getField());
  }

  /**
   * Test {@link TAQWriter#TAQWriter(OutputStream, TAQConfig)}.
   *
   * <p>Method under test: {@link TAQWriter#TAQWriter(OutputStream, TAQConfig)}
   */
  @Test
  @DisplayName("Test new TAQWriter(OutputStream, TAQConfig)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQWriter.<init>(OutputStream, TAQConfig)"})
  void testNewTAQWriter2() {
    // Arrange and Act
    TAQWriter actualTaqWriter = new TAQWriter(new ByteArrayOutputStream(), TAQConfig.DEFAULTS);

    // Assert
    assertEquals("", actualTaqWriter.getBuffer().toString());
    TAQConfig config = actualTaqWriter.getConfig();
    assertEquals("US-ASCII", config.getEncoding().name());
    FieldPosition position = actualTaqWriter.getPosition();
    assertNull(position.getFieldAttribute());
    assertEquals(0, position.getBeginIndex());
    assertEquals(0, position.getEndIndex());
    assertEquals(0, position.getField());
    assertSame(TAQConfig.DEFAULTS, config);
  }

  /**
   * Test {@link TAQWriter#write(Quote)} with {@code Quote}.
   *
   * <ul>
   *   <li>Then {@link TAQWriter#TAQWriter(OutputStream)} with out is {@link
   *       ByteArrayOutputStream#ByteArrayOutputStream()} Buffer toString is {@code 10}.
   * </ul>
   *
   * <p>Method under test: {@link TAQWriter#write(Quote)}
   */
  @Test
  @DisplayName(
      "Test write(Quote) with 'Quote'; then TAQWriter(OutputStream) with out is ByteArrayOutputStream() Buffer toString is '10'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQWriter.write(Quote)"})
  void testWriteWithQuote_thenTAQWriterWithOutIsByteArrayOutputStreamBufferToStringIs10() {
    // Arrange
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    TAQWriter taqWriter = new TAQWriter(new ByteArrayOutputStream());
    Quote resultRecord = new Quote();
    resultRecord.askPrice = 10.0d;
    resultRecord.askSize = 10.0d;
    resultRecord.bidPrice = 10.0d;
    resultRecord.bidSize = 10.0d;
    resultRecord.date = "2020-03-01";
    resultRecord.instrument = "Instrument";
    resultRecord.timestampMillis = 10L;

    // Act
    taqWriter.write(resultRecord);

    // Assert
    assertEquals("10", taqWriter.getBuffer().toString());
    assertEquals(2, taqWriter.getPosition().getEndIndex());
  }

  /**
   * Test {@link TAQWriter#write(Quote)} with {@code Quote}.
   *
   * <ul>
   *   <li>Then {@link TAQWriter#TAQWriter(OutputStream)} with out is {@link
   *       ByteArrayOutputStream#ByteArrayOutputStream()} Buffer toString is {@code 10}.
   * </ul>
   *
   * <p>Method under test: {@link TAQWriter#write(Quote)}
   */
  @Test
  @DisplayName(
      "Test write(Quote) with 'Quote'; then TAQWriter(OutputStream) with out is ByteArrayOutputStream() Buffer toString is '10'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQWriter.write(Quote)"})
  void testWriteWithQuote_thenTAQWriterWithOutIsByteArrayOutputStreamBufferToStringIs102() {
    // Arrange
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    TAQWriter taqWriter = new TAQWriter(new ByteArrayOutputStream());
    Quote resultRecord = new Quote();
    resultRecord.askPrice = 10.0d;
    resultRecord.askSize = 0.0d;
    resultRecord.bidPrice = 10.0d;
    resultRecord.bidSize = 10.0d;
    resultRecord.date = "2020-03-01";
    resultRecord.instrument = "Instrument";
    resultRecord.timestampMillis = 10L;

    // Act
    taqWriter.write(resultRecord);

    // Assert
    assertEquals("10", taqWriter.getBuffer().toString());
    assertEquals(2, taqWriter.getPosition().getEndIndex());
  }

  /**
   * Test {@link TAQWriter#write(Quote)} with {@code Quote}.
   *
   * <ul>
   *   <li>Then {@link TAQWriter#TAQWriter(OutputStream)} with out is {@link
   *       ByteArrayOutputStream#ByteArrayOutputStream()} Buffer toString is {@code 10}.
   * </ul>
   *
   * <p>Method under test: {@link TAQWriter#write(Quote)}
   */
  @Test
  @DisplayName(
      "Test write(Quote) with 'Quote'; then TAQWriter(OutputStream) with out is ByteArrayOutputStream() Buffer toString is '10'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQWriter.write(Quote)"})
  void testWriteWithQuote_thenTAQWriterWithOutIsByteArrayOutputStreamBufferToStringIs103() {
    // Arrange
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    TAQWriter taqWriter = new TAQWriter(new ByteArrayOutputStream());
    Quote resultRecord = new Quote();
    resultRecord.askPrice = 10.0d;
    resultRecord.askSize = 10.0d;
    resultRecord.bidPrice = 10.0d;
    resultRecord.bidSize = 0.0d;
    resultRecord.date = "2020-03-01";
    resultRecord.instrument = "Instrument";
    resultRecord.timestampMillis = 10L;

    // Act
    taqWriter.write(resultRecord);

    // Assert
    assertEquals("10", taqWriter.getBuffer().toString());
    assertEquals(2, taqWriter.getPosition().getEndIndex());
  }

  /**
   * Test {@link TAQWriter#write(Trade)} with {@code Trade}.
   *
   * <ul>
   *   <li>Then {@link TAQWriter#TAQWriter(OutputStream)} with out is {@link
   *       ByteArrayOutputStream#ByteArrayOutputStream()} Buffer toString is {@code 10}.
   * </ul>
   *
   * <p>Method under test: {@link TAQWriter#write(Trade)}
   */
  @Test
  @DisplayName(
      "Test write(Trade) with 'Trade'; then TAQWriter(OutputStream) with out is ByteArrayOutputStream() Buffer toString is '10'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQWriter.write(Trade)"})
  void testWriteWithTrade_thenTAQWriterWithOutIsByteArrayOutputStreamBufferToStringIs10() {
    // Arrange
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    TAQWriter taqWriter = new TAQWriter(new ByteArrayOutputStream());
    Trade resultRecord = new Trade();
    resultRecord.date = "2020-03-01";
    resultRecord.instrument = "Instrument";
    resultRecord.price = 10.0d;
    resultRecord.side = 'A';
    resultRecord.size = 10.0d;
    resultRecord.timestampMillis = 10L;

    // Act
    taqWriter.write(resultRecord);

    // Assert
    assertEquals("10", taqWriter.getBuffer().toString());
    assertEquals(2, taqWriter.getPosition().getEndIndex());
  }

  /**
   * Test {@link TAQWriter#write(Trade)} with {@code Trade}.
   *
   * <ul>
   *   <li>Then {@link TAQWriter#TAQWriter(OutputStream)} with out is {@link
   *       ByteArrayOutputStream#ByteArrayOutputStream()} Buffer toString is {@code 10}.
   * </ul>
   *
   * <p>Method under test: {@link TAQWriter#write(Trade)}
   */
  @Test
  @DisplayName(
      "Test write(Trade) with 'Trade'; then TAQWriter(OutputStream) with out is ByteArrayOutputStream() Buffer toString is '10'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQWriter.write(Trade)"})
  void testWriteWithTrade_thenTAQWriterWithOutIsByteArrayOutputStreamBufferToStringIs102() {
    // Arrange
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    TAQWriter taqWriter = new TAQWriter(new ByteArrayOutputStream());
    Trade resultRecord = new Trade();
    resultRecord.date = "2020-03-01";
    resultRecord.instrument = "Instrument";
    resultRecord.price = 10.0d;
    resultRecord.side = TAQ.UNKNOWN;
    resultRecord.size = 10.0d;
    resultRecord.timestampMillis = 10L;

    // Act
    taqWriter.write(resultRecord);

    // Assert
    assertEquals("10", taqWriter.getBuffer().toString());
    assertEquals(2, taqWriter.getPosition().getEndIndex());
  }
}
