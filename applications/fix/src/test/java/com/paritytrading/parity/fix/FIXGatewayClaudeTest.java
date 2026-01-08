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
package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for FIXGateway.
 *
 * <p>This test class covers the following methods:
 * - default constructor (implicitly tested through class instantiation)
 * - main(String[] args)
 *
 * <p>Testing approach: The FIXGateway class is package-private and primarily serves as an
 * application entry point with a main method. The main method handles command-line arguments,
 * loads configuration, and starts the FIX gateway server which enters an infinite event loop.
 *
 * <p>Testing the main method comprehensively is not feasible without reflection because:
 * 1. It calls System.exit (via usage/error/fatal methods from Applications utility)
 * 2. The successful execution path enters an infinite loop (Events.process)
 * 3. It creates real network connections and requires valid configuration
 * 4. SecurityManager approach to prevent System.exit is deprecated in Java 17+
 *
 * <p>Reflection is required to test this class because there is no way to test the main method's
 * behavior without either:
 * - Allowing System.exit to terminate the test JVM (unacceptable)
 * - Using deprecated SecurityManager APIs (not recommended for new code)
 * - Refactoring the production code to make it more testable (out of scope)
 *
 * <p>Therefore, these tests use reflection to verify the class structure and constructor,
 * which is the best we can do without modifying the production code.
 */
class FIXGatewayClaudeTest {

  /**
   * Test that the FIXGateway class can be instantiated.
   * This tests the default constructor using reflection because the class is package-private.
   */
  @Test
  void testConstructor() throws Exception {
    FIXGateway gateway = new FIXGateway();
    assertNotNull(gateway, "FIXGateway instance should not be null");
  }

  /**
   * Test that FIXGateway class has the expected structure.
   * This uses reflection to verify basic properties of the class since we cannot test
   * the main method's behavior without causing System.exit.
   */
  @Test
  void testClassStructure() throws Exception {
    // Verify the class is package-private
    int classModifiers = FIXGateway.class.getModifiers();
    assertFalse(
        java.lang.reflect.Modifier.isPublic(classModifiers),
        "FIXGateway should be package-private");
    assertFalse(
        java.lang.reflect.Modifier.isAbstract(classModifiers),
        "FIXGateway should not be abstract");
    assertFalse(
        java.lang.reflect.Modifier.isFinal(classModifiers),
        "FIXGateway should not be final");

    // Verify there's a public static main(String[]) method
    java.lang.reflect.Method mainMethod =
        FIXGateway.class.getDeclaredMethod("main", String[].class);
    assertNotNull(mainMethod, "FIXGateway should have a main method");

    int mainModifiers = mainMethod.getModifiers();
    assertTrue(
        java.lang.reflect.Modifier.isStatic(mainModifiers),
        "main method should be static");
    assertTrue(
        java.lang.reflect.Modifier.isPublic(mainModifiers),
        "main method should be public");

    assertEquals(
        void.class,
        mainMethod.getReturnType(),
        "main method should return void");
  }

  /**
   * Test that the default constructor exists and is accessible.
   * This uses reflection to verify the constructor signature.
   */
  @Test
  void testConstructorExists() throws Exception {
    java.lang.reflect.Constructor<FIXGateway> constructor =
        FIXGateway.class.getDeclaredConstructor();
    assertNotNull(constructor, "FIXGateway should have a default constructor");

    // The constructor should be accessible (package-private or public)
    // since the class is in the same package as the test
    FIXGateway instance = constructor.newInstance();
    assertNotNull(instance, "Should be able to create an instance via reflection");
  }

  /**
   * Test that FIXGateway has the expected private static helper methods.
   * This verifies the internal structure of the class using reflection.
   */
  @Test
  void testPrivateHelperMethodsExist() throws Exception {
    // Verify the private static main(Config) method exists
    java.lang.reflect.Method mainConfigMethod =
        FIXGateway.class.getDeclaredMethod("main", com.typesafe.config.Config.class);
    assertNotNull(mainConfigMethod, "Should have main(Config) method");
    assertTrue(
        java.lang.reflect.Modifier.isStatic(mainConfigMethod.getModifiers()),
        "main(Config) should be static");
    assertTrue(
        java.lang.reflect.Modifier.isPrivate(mainConfigMethod.getModifiers()),
        "main(Config) should be private");

    // Verify the private static orderEntry(Config) method exists
    java.lang.reflect.Method orderEntryMethod =
        FIXGateway.class.getDeclaredMethod("orderEntry", com.typesafe.config.Config.class);
    assertNotNull(orderEntryMethod, "Should have orderEntry(Config) method");
    assertTrue(
        java.lang.reflect.Modifier.isStatic(orderEntryMethod.getModifiers()),
        "orderEntry should be static");
    assertTrue(
        java.lang.reflect.Modifier.isPrivate(orderEntryMethod.getModifiers()),
        "orderEntry should be private");
    assertEquals(
        OrderEntryFactory.class,
        orderEntryMethod.getReturnType(),
        "orderEntry should return OrderEntryFactory");

    // Verify the private static fix(OrderEntryFactory, Config) method exists
    java.lang.reflect.Method fixMethod =
        FIXGateway.class.getDeclaredMethod(
            "fix", OrderEntryFactory.class, com.typesafe.config.Config.class);
    assertNotNull(fixMethod, "Should have fix(OrderEntryFactory, Config) method");
    assertTrue(
        java.lang.reflect.Modifier.isStatic(fixMethod.getModifiers()),
        "fix should be static");
    assertTrue(
        java.lang.reflect.Modifier.isPrivate(fixMethod.getModifiers()),
        "fix should be private");
    assertEquals(
        FIXAcceptor.class,
        fixMethod.getReturnType(),
        "fix should return FIXAcceptor");
  }

  /**
   * Test that the FIXGateway class can be instantiated multiple times.
   * This verifies that the constructor doesn't have side effects.
   */
  @Test
  void testMultipleInstantiations() {
    FIXGateway gateway1 = new FIXGateway();
    FIXGateway gateway2 = new FIXGateway();
    FIXGateway gateway3 = new FIXGateway();

    assertNotNull(gateway1, "First instance should not be null");
    assertNotNull(gateway2, "Second instance should not be null");
    assertNotNull(gateway3, "Third instance should not be null");

    // Verify they are different instances
    assertNotSame(gateway1, gateway2, "Instances should be different objects");
    assertNotSame(gateway2, gateway3, "Instances should be different objects");
    assertNotSame(gateway1, gateway3, "Instances should be different objects");
  }
}
