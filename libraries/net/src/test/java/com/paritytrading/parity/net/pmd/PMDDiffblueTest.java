package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class PMDDiffblueTest {
  /**
   * Method under test: default or parameterless constructor of
   * {@link PMD.OrderAdded}
   */
  @Test
  void testOrderAddedNewOrderAdded() {
    // Arrange and Act
    PMD.OrderAdded actualOrderAdded = new PMD.OrderAdded();

    // Assert
    assertEquals(0L, actualOrderAdded.instrument);
    assertEquals(0L, actualOrderAdded.orderNumber);
    assertEquals(0L, actualOrderAdded.price);
    assertEquals(0L, actualOrderAdded.quantity);
    assertEquals(0L, actualOrderAdded.timestamp);
    assertEquals((byte) 0, actualOrderAdded.side);
  }

  /**
   * Method under test: {@link PMD.OrderCanceled#get(ByteBuffer)}
   */
  @Test
  void testOrderCanceledGet() throws UnsupportedEncodingException {
    // Arrange
    PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
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
   * {@link PMD.OrderCanceled}
   */
  @Test
  void testOrderCanceledNewOrderCanceled() {
    // Arrange and Act
    PMD.OrderCanceled actualOrderCanceled = new PMD.OrderCanceled();

    // Assert
    assertEquals(0L, actualOrderCanceled.canceledQuantity);
    assertEquals(0L, actualOrderCanceled.orderNumber);
    assertEquals(0L, actualOrderCanceled.timestamp);
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link PMD.OrderExecuted}
   */
  @Test
  void testOrderExecutedNewOrderExecuted() {
    // Arrange and Act
    PMD.OrderExecuted actualOrderExecuted = new PMD.OrderExecuted();

    // Assert
    assertEquals(0L, actualOrderExecuted.matchNumber);
    assertEquals(0L, actualOrderExecuted.orderNumber);
    assertEquals(0L, actualOrderExecuted.quantity);
    assertEquals(0L, actualOrderExecuted.timestamp);
  }

  /**
   * Method under test: {@link PMD.Version#get(ByteBuffer)}
   */
  @Test
  void testVersionGet() throws UnsupportedEncodingException {
    // Arrange
    PMD.Version version = new PMD.Version();
    ByteBuffer buffer = ByteBuffer.wrap("AXAXAXAX".getBytes("UTF-8"));

    // Act
    version.get(buffer);

    // Assert
    assertEquals(1096302936L, version.version);
    assertEquals(4, buffer.position());
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link PMD.Version}
   */
  @Test
  void testVersionNewVersion() {
    // Arrange, Act and Assert
    assertEquals(0L, (new PMD.Version()).version);
  }

  /**
   * Method under test: {@link PMD.Version#put(ByteBuffer)}
   */
  @Test
  void testVersionPut() throws UnsupportedEncodingException {
    // Arrange
    PMD.Version version = new PMD.Version();
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
