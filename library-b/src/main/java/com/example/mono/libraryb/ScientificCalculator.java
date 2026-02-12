/*
 * Copyright 2024 Example Organization
 * Licensed under the Apache License, Version 2.0
 */
package com.example.mono.libraryb;

import com.example.mono.librarya.Calculator;

/**
 * A scientific calculator extending basic calculator functionality.
 *
 * <p>This class demonstrates how library-b depends on library-a while being independently
 * versioned.
 */
public class ScientificCalculator {

  private final Calculator basicCalculator;

  /** Creates a new ScientificCalculator instance. */
  public ScientificCalculator() {
    this.basicCalculator = new Calculator();
  }

  /**
   * Calculates the power of a number.
   *
   * @param base the base number
   * @param exponent the exponent (must be non-negative)
   * @return base raised to the power of exponent
   * @throws IllegalArgumentException if exponent is negative
   */
  public int power(int base, int exponent) {
    if (exponent < 0) {
      throw new IllegalArgumentException("Exponent must be non-negative");
    }

    if (exponent == 0) {
      return 1;
    }

    int result = 1;
    for (int i = 0; i < exponent; i++) {
      result = basicCalculator.multiply(result, base);
    }
    return result;
  }

  /**
   * Calculates the factorial of a number.
   *
   * @param n the number (must be non-negative)
   * @return factorial of n
   * @throws IllegalArgumentException if n is negative
   */
  public long factorial(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("Factorial is not defined for negative numbers");
    }

    if (n == 0 || n == 1) {
      return 1;
    }

    long result = 1;
    for (int i = 2; i <= n; i++) {
      result *= i;
    }
    return result;
  }

  /**
   * Calculates the average of multiple numbers.
   *
   * @param numbers the numbers to average
   * @return the average value
   * @throws IllegalArgumentException if array is null or empty
   */
  public double average(int[] numbers) {
    if (numbers == null || numbers.length == 0) {
      throw new IllegalArgumentException("Array must not be null or empty");
    }

    int sum = 0;
    for (int num : numbers) {
      sum = basicCalculator.add(sum, num);
    }

    return (double) sum / numbers.length;
  }

  /**
   * Checks if a number is even.
   *
   * @param number the number to check
   * @return true if the number is even, false otherwise
   */
  public boolean isEven(int number) {
    return number % 2 == 0;
  }
}
