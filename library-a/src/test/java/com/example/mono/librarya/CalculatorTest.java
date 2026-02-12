/*
 * Copyright 2024 Example Organization
 * Licensed under the Apache License, Version 2.0
 */
package com.example.mono.librarya;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Calculator Tests")
class CalculatorTest {

  private Calculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new Calculator();
  }

  @Test
  @DisplayName("Addition should work correctly")
  void testAdd() {
    assertThat(calculator.add(2, 3)).isEqualTo(5);
    assertThat(calculator.add(-1, 1)).isEqualTo(0);
    assertThat(calculator.add(-5, -3)).isEqualTo(-8);
  }

  @ParameterizedTest
  @CsvSource({"5,3,2", "10,7,3", "0,5,-5", "-10,-3,-7"})
  @DisplayName("Subtraction should work with various inputs")
  void testSubtract(int a, int b, int expected) {
    assertThat(calculator.subtract(a, b)).isEqualTo(expected);
  }

  @Test
  @DisplayName("Multiplication should work correctly")
  void testMultiply() {
    assertThat(calculator.multiply(3, 4)).isEqualTo(12);
    assertThat(calculator.multiply(-2, 3)).isEqualTo(-6);
    assertThat(calculator.multiply(0, 100)).isEqualTo(0);
  }

  @Test
  @DisplayName("Division should work correctly")
  void testDivide() {
    assertThat(calculator.divide(10, 2)).isEqualTo(5);
    assertThat(calculator.divide(7, 2)).isEqualTo(3); // Integer division
    assertThat(calculator.divide(-10, 2)).isEqualTo(-5);
  }

  @Test
  @DisplayName("Division by zero should throw ArithmeticException")
  void testDivideByZero() {
    assertThatThrownBy(() -> calculator.divide(10, 0))
        .isInstanceOf(ArithmeticException.class)
        .hasMessageContaining("Division by zero");
  }
}
