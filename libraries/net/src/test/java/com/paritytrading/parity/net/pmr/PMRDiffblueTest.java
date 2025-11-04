package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmr.PMR.OrderAdded;
import com.paritytrading.parity.net.pmr.PMR.OrderCanceled;
import com.paritytrading.parity.net.pmr.PMR.OrderEntered;
import com.paritytrading.parity.net.pmr.PMR.Trade;
import com.paritytrading.parity.net.pmr.PMR.Version;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class PMRDiffblueTest {
  /**
   * Test OrderAdded {@link OrderAdded#get(ByteBuffer)}.
   * <ul>
   *   <li>Then {@link OrderAdded} (default constructor) {@link OrderAdded#orderNumber} is {@code 4708585257725083992}.</li>
   * </ul>
   * <p>
   * Method under test: {@link OrderAdded#get(ByteBuffer)}
   */
  @Test
  @DisplayName("Test OrderAdded get(ByteBuffer); then OrderAdded (default constructor) orderNumber is '4708585257725083992'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void OrderAdded.get(ByteBuffer)"})
  void testOrderAddedGet_thenOrderAddedOrderNumberIs4708585257725083992() throws UnsupportedEncodingException {
    // Arrange
    OrderAdded orderAdded = new OrderAdded();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAXAXAXAXAX".getBytes("UTF-8"));

    // Act
    orderAdded.get(buffer);

    // Assert
    assertEquals(4708585257725083992L, orderAdded.orderNumber);
    assertEquals(4708585257725083992L, orderAdded.timestamp);
    assertFalse(buffer.hasRemaining());
    assertEquals(Short.SIZE, buffer.position());
  }

  /**
   * Test OrderAdded new {@link OrderAdded} (default constructor).
   * <p>
   * Method under test: default or parameterless constructor of {@link OrderAdded}
   */
  @Test
  @DisplayName("Test OrderAdded new OrderAdded (default constructor)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void OrderAdded.<init>()"})
  void testOrderAddedNewOrderAdded() {
    // Arrange and Act
    OrderAdded actualOrderAdded = new OrderAdded();

    // Assert
    assertEquals(0L, actualOrderAdded.orderNumber);
    assertEquals(0L, actualOrderAdded.timestamp);
  }

  /**
   * Test OrderAdded {@link OrderAdded#put(ByteBuffer)}.
   * <ul>
   *   <li>Then wrap {@code AXAXAXAXAXAXAXAXAXAXAXAX} Bytes is {@code UTF-8} position is seventeen.</li>
   * </ul>
   * <p>
   * Method under test: {@link OrderAdded#put(ByteBuffer)}
   */
  @Test
  @DisplayName("Test OrderAdded put(ByteBuffer); then wrap 'AXAXAXAXAXAXAXAXAXAXAXAX' Bytes is 'UTF-8' position is seventeen")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void OrderAdded.put(ByteBuffer)"})
  void testOrderAddedPut_thenWrapAxaxaxaxaxaxaxaxaxaxaxaxBytesIsUtf8PositionIsSeventeen()
      throws UnsupportedEncodingException {
    // Arrange
    OrderAdded orderAdded = new OrderAdded();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAXAXAXAXAXAXAXAXAX".getBytes("UTF-8"));

    // Act
    orderAdded.put(buffer);

    // Assert
    assertEquals(17, buffer.position());
    assertArrayEquals(
        new byte[]{'A', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'X', 'A', 'X', 'A', 'X', 'A', 'X'},
        buffer.array());
  }

  /**
   * Test OrderCanceled {@link OrderCanceled#get(ByteBuffer)}.
   * <p>
   * Method under test: {@link OrderCanceled#get(ByteBuffer)}
   */
  @Test
  @DisplayName("Test OrderCanceled get(ByteBuffer)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void OrderCanceled.get(ByteBuffer)"})
  void testOrderCanceledGet() throws UnsupportedEncodingException {
    // Arrange
    OrderCanceled orderCanceled = new OrderCanceled();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAXAXAXAXAXAXAXAXAX".getBytes("UTF-8"));

    // Act
    orderCanceled.get(buffer);

    // Assert
    assertEquals(24, buffer.position());
    assertEquals(4708585257725083992L, orderCanceled.canceledQuantity);
    assertEquals(4708585257725083992L, orderCanceled.orderNumber);
    assertEquals(4708585257725083992L, orderCanceled.timestamp);
    assertFalse(buffer.hasRemaining());
  }

  /**
   * Test OrderCanceled new {@link OrderCanceled} (default constructor).
   * <p>
   * Method under test: default or parameterless constructor of {@link OrderCanceled}
   */
  @Test
  @DisplayName("Test OrderCanceled new OrderCanceled (default constructor)")
  @Tag("MaintainedByDiffblue")
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
   * Test OrderEntered new {@link OrderEntered} (default constructor).
   * <p>
   * Method under test: default or parameterless constructor of {@link OrderEntered}
   */
  @Test
  @DisplayName("Test OrderEntered new OrderEntered (default constructor)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void OrderEntered.<init>()"})
  void testOrderEnteredNewOrderEntered() {
    // Arrange and Act
    OrderEntered actualOrderEntered = new OrderEntered();

    // Assert
    assertEquals(0L, actualOrderEntered.instrument);
    assertEquals(0L, actualOrderEntered.orderNumber);
    assertEquals(0L, actualOrderEntered.price);
    assertEquals(0L, actualOrderEntered.quantity);
    assertEquals(0L, actualOrderEntered.timestamp);
    assertEquals(0L, actualOrderEntered.username);
    assertEquals((byte) 0, actualOrderEntered.side);
  }

  /**
   * Test Trade new {@link Trade} (default constructor).
   * <p>
   * Method under test: default or parameterless constructor of {@link Trade}
   */
  @Test
  @DisplayName("Test Trade new Trade (default constructor)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Trade.<init>()"})
  void testTradeNewTrade() {
    // Arrange and Act
    Trade actualTrade = new Trade();

    // Assert
    assertEquals(0L, actualTrade.incomingOrderNumber);
    assertEquals(0L, actualTrade.matchNumber);
    assertEquals(0L, actualTrade.quantity);
    assertEquals(0L, actualTrade.restingOrderNumber);
    assertEquals(0L, actualTrade.timestamp);
  }

  /**
   * Test Version {@link Version#get(ByteBuffer)}.
   * <ul>
   *   <li>When wrap {@code AXAXAXAX} Bytes is {@code UTF-8}.</li>
   *   <li>Then {@link Version} (default constructor) {@link Version#version} is {@code 1096302936}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Version#get(ByteBuffer)}
   */
  @Test
  @DisplayName("Test Version get(ByteBuffer); when wrap 'AXAXAXAX' Bytes is 'UTF-8'; then Version (default constructor) version is '1096302936'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Version.get(ByteBuffer)"})
  void testVersionGet_whenWrapAxaxaxaxBytesIsUtf8_thenVersionVersionIs1096302936() throws UnsupportedEncodingException {
    // Arrange
    Version version = new Version();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"));

    // Act
    version.get(buffer);

    // Assert
    assertEquals(1096302936L, version.version);
    assertEquals(4, buffer.position());
  }

  /**
   * Test Version new {@link Version} (default constructor).
   * <p>
   * Method under test: default or parameterless constructor of {@link Version}
   */
  @Test
  @DisplayName("Test Version new Version (default constructor)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Version.<init>()"})
  void testVersionNewVersion() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Version()).version);
  }

  /**
   * Test Version {@link Version#put(ByteBuffer)}.
   * <ul>
   *   <li>Then wrap {@code AXAXAXAX} Bytes is {@code UTF-8} position is five.</li>
   * </ul>
   * <p>
   * Method under test: {@link Version#put(ByteBuffer)}
   */
  @Test
  @DisplayName("Test Version put(ByteBuffer); then wrap 'AXAXAXAX' Bytes is 'UTF-8' position is five")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Version.put(ByteBuffer)"})
  void testVersionPut_thenWrapAxaxaxaxBytesIsUtf8PositionIsFive() throws UnsupportedEncodingException {
    // Arrange
    Version version = new Version();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"));

    // Act
    version.put(buffer);

    // Assert
    assertEquals(5, buffer.position());
    assertArrayEquals(new byte[]{'V', 0, 0, 0, 0, 'X', 'A', 'X'}, buffer.array());
  }
}
