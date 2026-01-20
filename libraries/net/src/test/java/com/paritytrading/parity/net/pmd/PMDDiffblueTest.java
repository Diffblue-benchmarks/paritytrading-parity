package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmd.PMD.OrderAdded;
import com.paritytrading.parity.net.pmd.PMD.OrderCanceled;
import com.paritytrading.parity.net.pmd.PMD.OrderExecuted;
import com.paritytrading.parity.net.pmd.PMD.Version;
import com.paritytrading.parity.net.poe.POETestFactory;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class PMDDiffblueTest {
  /**
   * Test OrderAdded {@link OrderAdded#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is forty-one.
   * </ul>
   *
   * <p>Method under test: {@link OrderAdded#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderAdded get(ByteBuffer); then createByteBufferForOrderExecutedPut position is forty-one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAdded.get(ByteBuffer)"})
  void testOrderAddedGet_thenCreateByteBufferForOrderExecutedPutPositionIsFortyOne() {
    // Arrange
    OrderAdded orderAdded = new OrderAdded();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    orderAdded.get(buffer);

    // Assert
    assertEquals(41, buffer.position());
  }

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
   * Test OrderAdded {@link OrderAdded#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is forty-two.
   * </ul>
   *
   * <p>Method under test: {@link OrderAdded#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderAdded put(ByteBuffer); then createByteBufferForOrderExecutedPut position is forty-two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAdded.put(ByteBuffer)"})
  void testOrderAddedPut_thenCreateByteBufferForOrderExecutedPutPositionIsFortyTwo() {
    // Arrange
    OrderAdded orderAdded = new OrderAdded();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    orderAdded.put(buffer);

    // Assert
    assertEquals(42, buffer.position());
    assertArrayEquals(
        new byte[] {
          'A', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        buffer.array());
  }

  /**
   * Test OrderCanceled {@link OrderCanceled#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is twenty-four.
   * </ul>
   *
   * <p>Method under test: {@link OrderCanceled#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderCanceled get(ByteBuffer); then createByteBufferForOrderRejectedPut position is twenty-four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.get(ByteBuffer)"})
  void testOrderCanceledGet_thenCreateByteBufferForOrderRejectedPutPositionIsTwentyFour() {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    orderCanceled.get(buffer);

    // Assert
    assertEquals(24, buffer.position());
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
   * Test OrderCanceled {@link OrderCanceled#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is twenty-five.
   * </ul>
   *
   * <p>Method under test: {@link OrderCanceled#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderCanceled put(ByteBuffer); then createByteBufferForOrderRejectedPut position is twenty-five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.put(ByteBuffer)"})
  void testOrderCanceledPut_thenCreateByteBufferForOrderRejectedPutPositionIsTwentyFive() {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    orderCanceled.put(buffer);

    // Assert
    assertEquals(25, buffer.position());
    assertArrayEquals(
        new byte[] {'X', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        buffer.array());
  }

  /**
   * Test OrderExecuted {@link OrderExecuted#get(ByteBuffer)}.
   *
   * <p>Method under test: {@link OrderExecuted#get(ByteBuffer)}
   */
  @Test
  @DisplayName("Test OrderExecuted get(ByteBuffer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderExecuted.get(ByteBuffer)"})
  void testOrderExecutedGet() {
    // Arrange
    OrderExecuted orderExecuted = new OrderExecuted();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    orderExecuted.get(buffer);

    // Assert
    assertEquals(28, buffer.position());
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
   * Test OrderExecuted {@link OrderExecuted#put(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderExecutedPut position is twenty-nine.
   * </ul>
   *
   * <p>Method under test: {@link OrderExecuted#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test OrderExecuted put(ByteBuffer); then createByteBufferForOrderExecutedPut position is twenty-nine")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderExecuted.put(ByteBuffer)"})
  void testOrderExecutedPut_thenCreateByteBufferForOrderExecutedPutPositionIsTwentyNine() {
    // Arrange
    OrderExecuted orderExecuted = new OrderExecuted();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderExecutedPut();

    // Act
    orderExecuted.put(buffer);

    // Assert
    assertEquals(29, buffer.position());
    assertArrayEquals(
        new byte[] {
          'E', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        buffer.array());
  }

  /**
   * Test Version {@link Version#get(ByteBuffer)}.
   *
   * <ul>
   *   <li>Then createByteBufferForOrderRejectedPut position is four.
   * </ul>
   *
   * <p>Method under test: {@link Version#get(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test Version get(ByteBuffer); then createByteBufferForOrderRejectedPut position is four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Version.get(ByteBuffer)"})
  void testVersionGet_thenCreateByteBufferForOrderRejectedPutPositionIsFour() {
    // Arrange
    Version version = new Version();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    version.get(buffer);

    // Assert
    assertEquals(4, buffer.position());
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
   *   <li>Then createByteBufferForOrderRejectedPut position is five.
   * </ul>
   *
   * <p>Method under test: {@link Version#put(ByteBuffer)}
   */
  @Test
  @DisplayName(
      "Test Version put(ByteBuffer); then createByteBufferForOrderRejectedPut position is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Version.put(ByteBuffer)"})
  void testVersionPut_thenCreateByteBufferForOrderRejectedPutPositionIsFive() {
    // Arrange
    Version version = new Version();
    ByteBuffer buffer = POETestFactory.createByteBufferForOrderRejectedPut();

    // Act
    version.put(buffer);

    // Assert
    assertEquals(5, buffer.position());
    assertArrayEquals(
        new byte[] {'V', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        buffer.array());
  }
}
