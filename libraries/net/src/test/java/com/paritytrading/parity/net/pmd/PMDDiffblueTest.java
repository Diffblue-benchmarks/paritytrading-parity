package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmd.PMD.OrderAdded;
import com.paritytrading.parity.net.pmd.PMD.OrderCanceled;
import com.paritytrading.parity.net.pmd.PMD.OrderExecuted;
import com.paritytrading.parity.net.pmd.PMD.Version;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class PMDDiffblueTest {
  /**
   * Test OrderAdded new {@link OrderAdded} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderAdded}
   */
  @Test
  @DisplayName("Test OrderAdded new OrderAdded (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAdded.<init>()"})
  void testOrderAddedNewOrderAdded() {
    // Arrange and Act
    OrderAdded actualOrderAdded = new OrderAdded();

    // Assert
    assertEquals(0L, actualOrderAdded.instrument);
    assertEquals(0L, actualOrderAdded.orderNumber);
    assertEquals(0L, actualOrderAdded.price);
    assertEquals(0L, actualOrderAdded.quantity);
    assertEquals(0L, actualOrderAdded.timestamp);
    assertEquals((byte) 0, actualOrderAdded.side);
  }

  /**
   * Test OrderCanceled {@link OrderCanceled#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then {@link OrderCanceled} (default constructor) {@link OrderCanceled#canceledQuantity}
   *       is {@code 4708585257725083992}.
   * </ul>
   *
   * <p>Method under test: {@link OrderCanceled#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderCanceled get(ByteBuffer); then OrderCanceled (default constructor) canceledQuantity is '4708585257725083992'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.get(ByteBuffer)"})
  void testOrderCanceledGet_thenOrderCanceledCanceledQuantityIs4708585257725083992()
      throws UnsupportedEncodingException {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();

    // Act
    orderCanceled.get(ByteBuffer.wrap("AXAXAXAXAXAXAXAXAXAXAXAX".getBytes("UTF-8")));

    // Assert
    assertEquals(4708585257725083992L, orderCanceled.canceledQuantity);
    assertEquals(4708585257725083992L, orderCanceled.orderNumber);
    assertEquals(4708585257725083992L, orderCanceled.timestamp);
  }

  /**
   * Test OrderCanceled new {@link OrderCanceled} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderCanceled}
   */
  @Test
  @DisplayName("Test OrderCanceled new OrderCanceled (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.<init>()"})
  void testOrderCanceledNewOrderCanceled() {
    // Arrange and Act
    OrderCanceled actualOrderCanceled = new OrderCanceled();

    // Assert
    assertEquals(0L, actualOrderCanceled.canceledQuantity);
    assertEquals(0L, actualOrderCanceled.orderNumber);
    assertEquals(0L, actualOrderCanceled.timestamp);
  }

  /**
   * Test OrderExecuted new {@link OrderExecuted} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OrderExecuted}
   */
  @Test
  @DisplayName("Test OrderExecuted new OrderExecuted (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderExecuted.<init>()"})
  void testOrderExecutedNewOrderExecuted() {
    // Arrange and Act
    OrderExecuted actualOrderExecuted = new OrderExecuted();

    // Assert
    assertEquals(0L, actualOrderExecuted.matchNumber);
    assertEquals(0L, actualOrderExecuted.orderNumber);
    assertEquals(0L, actualOrderExecuted.quantity);
    assertEquals(0L, actualOrderExecuted.timestamp);
  }

  /**
   * Test Version {@link Version#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>When wrap {@code AXAXAXAX} Bytes is {@code UTF-8}.
   *   <li>Then {@link Version} (default constructor) {@link Version#version} is {@code 1096302936}.
   * </ul>
   *
   * <p>Method under test: {@link Version#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test Version get(ByteBuffer); when wrap 'AXAXAXAX' Bytes is 'UTF-8'; then Version (default constructor) version is '1096302936'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Version.get(ByteBuffer)"})
  void testVersionGet_whenWrapAxaxaxaxBytesIsUtf8_thenVersionVersionIs1096302936()
      throws UnsupportedEncodingException {
    // Arrange
    Version version = new Version();

    // Act
    version.get(ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8")));

    // Assert
    assertEquals(1096302936L, version.version);
  }

  /**
   * Test Version new {@link Version} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link Version}
   */
  @Test
  @DisplayName("Test Version new Version (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Version.<init>()"})
  void testVersionNewVersion() {
    // Arrange, Act and Assert
    assertEquals(0L, new Version().version);
  }

  /**
   * Test Version {@link Version#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then wrap {@code AXAXAXAX} Bytes is {@code UTF-8} array is array of {@code byte} with
   *       {@code V} and zero.
   * </ul>
   *
   * <p>Method under test: {@link Version#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test Version put(ByteBuffer); then wrap 'AXAXAXAX' Bytes is 'UTF-8' array is array of byte with 'V' and zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Version.put(ByteBuffer)"})
  void testVersionPut_thenWrapAxaxaxaxBytesIsUtf8ArrayIsArrayOfByteWithVAndZero()
      throws UnsupportedEncodingException {
    // Arrange
    Version version = new Version();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"));

    // Act
    version.put(buffer);

    // Assert
    assertArrayEquals(new byte[] {'V', 0, 0, 0, 0, 'X', 'A', 'X'}, buffer.array());
  }
}
