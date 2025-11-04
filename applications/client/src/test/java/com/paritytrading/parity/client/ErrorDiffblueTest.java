package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

class ErrorDiffblueTest {
  /**
   * Method under test: {@link Error#format()}
   */
  @Test
  void testFormat() {
    // Arrange, Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 <unknown>         ",
        (new Error(new Event.OrderRejected(new POE.OrderRejected()))).format());
  }

  /**
   * Method under test: {@link Error#format()}
   */
  @Test
  void testFormat2() {
    // Arrange
    POE.OrderRejected message = new POE.OrderRejected();
    message.reason = (byte) 'I';

    // Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 Unknown instrument",
        (new Error(new Event.OrderRejected(message))).format());
  }

  /**
   * Method under test: {@link Error#format()}
   */
  @Test
  void testFormat3() {
    // Arrange
    POE.OrderRejected message = new POE.OrderRejected();
    message.reason = (byte) 'P';

    // Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 Invalid price     ",
        (new Error(new Event.OrderRejected(message))).format());
  }

  /**
   * Method under test: {@link Error#format()}
   */
  @Test
  void testFormat4() {
    // Arrange
    POE.OrderRejected message = new POE.OrderRejected();
    message.reason = (byte) 'Q';

    // Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 Invalid quantity  ",
        (new Error(new Event.OrderRejected(message))).format());
  }

  /**
   * Method under test: {@link Error#Error(Event.OrderRejected)}
   */
  @Test
  void testNewError() {
    // Arrange, Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 <unknown>         ",
        (new Error(new Event.OrderRejected(new POE.OrderRejected()))).format());
  }
}
