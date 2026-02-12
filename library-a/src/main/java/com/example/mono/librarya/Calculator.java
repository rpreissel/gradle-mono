/*
 * Copyright 2024 Example Organization
 * Licensed under the Apache License, Version 2.0
 */
package com.example.mono.librarya;

/**
 * A simple calculator providing basic mathematical operations.
 *
 * <p>This class demonstrates a basic library component that can be versioned independently in a
 * Gradle monorepo setup.
 */
public class Calculator {

  /**
   * Adds two integers.
   *
   * @param a first number
   * @param b second number
   * @return sum of a and b
   */
  public int add(int a, int b) {
    return a + b;
  }

  /**
   * Subtracts two integers.
   *
   * @param a first number
   * @param b second number
   * @return difference of a and b
   */
  public int subtract(int a, int b) {
    return a - b;
  }

  /**
   * Multiplies two integers.
   *
   * @param a first number
   * @param b second number
   * @return product of a and b
   */
  public int multiply(int a, int b) {
    return a * b;
  }

  /**
   * Divides two integers.
   *
   * @param a dividend
   * @param b divisor
   * @return quotient of a divided by b
   * @throws ArithmeticException if b is zero
   */
  public int divide(int a, int b) {
    if (b == 0) {
      throw new ArithmeticException("Division by zero is not allowed");
    }
    return a / b;
  }
}
