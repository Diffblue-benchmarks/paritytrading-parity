package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class PMRDiffblueTest {
  /**
   * Method under test: {@link PMR.OrderAdded#get(ByteBuffer)}
   */
  @Test
  void testOrderAddedGet() throws UnsupportedEncodingException {
    // Arrange
    PMR.OrderAdded orderAdded = new PMR.OrderAdded();
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
   * Method under test: default or parameterless constructor of
   * {@link PMR.OrderAdded}
   */
  @Test
  void testOrderAddedNewOrderAdded() {
    // Arrange and Act
    PMR.OrderAdded actualOrderAdded = new PMR.OrderAdded();

    // Assert
    assertEquals(0L, actualOrderAdded.orderNumber);
    assertEquals(0L, actualOrderAdded.timestamp);
  }

  /**
   * Method under test: {@link PMR.OrderAdded#put(ByteBuffer)}
   */
  @Test
  void testOrderAddedPut() throws UnsupportedEncodingException {
    // Arrange
    PMR.OrderAdded orderAdded = new PMR.OrderAdded();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAXAXAXAXAXAXAXAXAX".getBytes("UTF-8"));

    // Act
    orderAdded.put(buffer);

    // Assert that nothing has changed
    byte[] arrayResult = buffer.array();
    assertEquals((byte) 0, arrayResult[1]);
    assertEquals((byte) 0, arrayResult[10]);
    assertEquals((byte) 0, arrayResult[11]);
    assertEquals((byte) 0, arrayResult[12]);
    assertEquals((byte) 0, arrayResult[13]);
    assertEquals((byte) 0, arrayResult[14]);
    assertEquals((byte) 0, arrayResult[15]);
    assertEquals((byte) 0, arrayResult[2]);
    assertEquals((byte) 0, arrayResult[3]);
    assertEquals((byte) 0, arrayResult[4]);
    assertEquals((byte) 0, arrayResult[5]);
    assertEquals((byte) 0, arrayResult[6]);
    assertEquals((byte) 0, arrayResult[7]);
    assertEquals((byte) 0, arrayResult[8]);
    assertEquals((byte) 0, arrayResult[9]);
    assertEquals((byte) 0, arrayResult[Short.SIZE]);
    assertEquals(17, buffer.position());
    assertEquals(24, arrayResult.length);
  }

  /**
   * Method under test: {@link PMR.OrderCanceled#get(ByteBuffer)}
   */
  @Test
  void testOrderCanceledGet() throws UnsupportedEncodingException {
    // Arrange
    PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
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
   * Method under test: default or parameterless constructor of
   * {@link PMR.OrderCanceled}
   */
  @Test
  void testOrderCanceledNewOrderCanceled() {
    // Arrange and Act
    PMR.OrderCanceled actualOrderCanceled = new PMR.OrderCanceled();

    // Assert
    assertEquals(0L, actualOrderCanceled.canceledQuantity);
    assertEquals(0L, actualOrderCanceled.orderNumber);
    assertEquals(0L, actualOrderCanceled.timestamp);
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link PMR.OrderEntered}
   */
  @Test
  void testOrderEnteredNewOrderEntered() {
    // Arrange and Act
    PMR.OrderEntered actualOrderEntered = new PMR.OrderEntered();

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
   * Method under test: default or parameterless constructor of {@link PMR.Trade}
   */
  @Test
  void testTradeNewTrade() {
    // Arrange and Act
    PMR.Trade actualTrade = new PMR.Trade();

    // Assert
    assertEquals(0L, actualTrade.incomingOrderNumber);
    assertEquals(0L, actualTrade.matchNumber);
    assertEquals(0L, actualTrade.quantity);
    assertEquals(0L, actualTrade.restingOrderNumber);
    assertEquals(0L, actualTrade.timestamp);
  }

  /**
   * Method under test: {@link PMR.Version#get(ByteBuffer)}
   */
  @Test
  void testVersionGet() throws UnsupportedEncodingException {
    // Arrange
    PMR.Version version = new PMR.Version();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"));

    // Act
    version.get(buffer);

    // Assert
    assertEquals(1096302936L, version.version);
    assertEquals(4, buffer.position());
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link PMR.Version}
   */
  @Test
  void testVersionNewVersion() {
    // Arrange, Act and Assert
    assertEquals(0L, (new PMR.Version()).version);
  }

  /**
   * Method under test: {@link PMR.Version#put(ByteBuffer)}
   */
  @Test
  void testVersionPut() throws UnsupportedEncodingException {
    // Arrange
    PMR.Version version = new PMR.Version();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"));

    // Act
    version.put(buffer);

    // Assert that nothing has changed
    byte[] arrayResult = buffer.array();
    assertEquals((byte) 0, arrayResult[1]);
    assertEquals((byte) 0, arrayResult[2]);
    assertEquals((byte) 0, arrayResult[3]);
    assertEquals((byte) 0, arrayResult[4]);
    assertEquals(5, buffer.position());
    assertEquals(8, arrayResult.length);
    assertEquals('V', arrayResult[0]);
  }
}
