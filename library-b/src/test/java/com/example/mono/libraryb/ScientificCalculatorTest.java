/*
 * Copyright 2024 Example Organization
 * Licensed under the Apache License, Version 2.0
 */
package com.example.mono.libraryb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("ScientificCalculator Tests")
class ScientificCalculatorTest {

  private ScientificCalculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new ScientificCalculator();
  }

  @ParameterizedTest
  @CsvSource({"2,3,8", "5,2,25", "10,0,1", "3,4,81"})
  @DisplayName("Power calculation should work correctly")
  void testPower(int base, int exponent, int expected) {
    assertThat(calculator.power(base, exponent)).isEqualTo(expected);
  }

  @Test
  @DisplayName("Power with negative exponent should throw exception")
  void testPowerNegativeExponent() {
    assertThatThrownBy(() -> calculator.power(2, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("non-negative");
  }

  @ParameterizedTest
  @CsvSource({"0,1", "1,1", "5,120", "6,720"})
  @DisplayName("Factorial should calculate correctly")
  void testFactorial(int n, long expected) {
    assertThat(calculator.factorial(n)).isEqualTo(expected);
  }

  @Test
  @DisplayName("Factorial of negative number should throw exception")
  void testFactorialNegative() {
    assertThatThrownBy(() -> calculator.factorial(-1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("not defined for negative");
  }

  @Test
  @DisplayName("Average should calculate correctly")
  void testAverage() {
    assertThat(calculator.average(new int[] {1, 2, 3, 4, 5})).isCloseTo(3.0, within(0.001));
    assertThat(calculator.average(new int[] {10, 20, 30})).isCloseTo(20.0, within(0.001));
    assertThat(calculator.average(new int[] {42})).isCloseTo(42.0, within(0.001));
  }

  @Test
  @DisplayName("Average of empty array should throw exception")
  void testAverageEmptyArray() {
    assertThatThrownBy(() -> calculator.average(new int[] {}))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> calculator.average(null)).isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @CsvSource({"2,true", "3,false", "0,true", "-4,true", "-3,false"})
  @DisplayName("IsEven should work correctly")
  void testIsEven(int number, boolean expected) {
    assertThat(calculator.isEven(number)).isEqualTo(expected);
  }
}
