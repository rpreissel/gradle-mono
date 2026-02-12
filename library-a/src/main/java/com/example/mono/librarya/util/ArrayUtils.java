/*
 * Copyright 2024 Example Organization
 * Licensed under the Apache License, Version 2.0
 */
package com.example.mono.librarya.util;

import java.util.Arrays;

/**
 * Utility class providing array-related operations.
 *
 * <p>Demonstrates additional functionality in library-a that can be used by other libraries.
 */
public final class ArrayUtils {

  private ArrayUtils() {
    throw new UnsupportedOperationException("Utility class should not be instantiated");
  }

  /**
   * Calculates the sum of all elements in an integer array.
   *
   * @param numbers the array of numbers to sum
   * @return the sum of all elements, or 0 for empty array
   */
  public static int sum(int[] numbers) {
    if (numbers == null || numbers.length == 0) {
      return 0;
    }
    return Arrays.stream(numbers).sum();
  }

  /**
   * Finds the maximum value in an integer array.
   *
   * @param numbers the array to search
   * @return the maximum value
   * @throws IllegalArgumentException if array is null or empty
   */
  public static int max(int[] numbers) {
    if (numbers == null || numbers.length == 0) {
      throw new IllegalArgumentException("Array must not be null or empty");
    }
    return Arrays.stream(numbers).max().orElseThrow();
  }

  /**
   * Finds the minimum value in an integer array.
   *
   * @param numbers the array to search
   * @return the minimum value
   * @throws IllegalArgumentException if array is null or empty
   */
  public static int min(int[] numbers) {
    if (numbers == null || numbers.length == 0) {
      throw new IllegalArgumentException("Array must not be null or empty");
    }
    return Arrays.stream(numbers).min().orElseThrow();
  }
}
