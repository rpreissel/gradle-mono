/*
 * Copyright 2024 Example Organization
 * Licensed under the Apache License, Version 2.0
 */
package com.example.mono.librarya.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ArrayUtils Tests")
class ArrayUtilsTest {

  @Test
  @DisplayName("Sum should calculate correctly")
  void testSum() {
    assertThat(ArrayUtils.sum(new int[] {1, 2, 3, 4, 5})).isEqualTo(15);
    assertThat(ArrayUtils.sum(new int[] {-1, 1})).isEqualTo(0);
    assertThat(ArrayUtils.sum(new int[] {})).isEqualTo(0);
    assertThat(ArrayUtils.sum(null)).isEqualTo(0);
  }

  @Test
  @DisplayName("Max should find maximum value")
  void testMax() {
    assertThat(ArrayUtils.max(new int[] {1, 5, 3, 2, 4})).isEqualTo(5);
    assertThat(ArrayUtils.max(new int[] {-10, -5, -20})).isEqualTo(-5);
    assertThat(ArrayUtils.max(new int[] {42})).isEqualTo(42);
  }

  @Test
  @DisplayName("Max should throw exception for empty array")
  void testMaxEmptyArray() {
    assertThatThrownBy(() -> ArrayUtils.max(new int[] {}))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must not be null or empty");

    assertThatThrownBy(() -> ArrayUtils.max(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must not be null or empty");
  }

  @Test
  @DisplayName("Min should find minimum value")
  void testMin() {
    assertThat(ArrayUtils.min(new int[] {1, 5, 3, 2, 4})).isEqualTo(1);
    assertThat(ArrayUtils.min(new int[] {-10, -5, -20})).isEqualTo(-20);
    assertThat(ArrayUtils.min(new int[] {42})).isEqualTo(42);
  }

  @Test
  @DisplayName("Min should throw exception for empty array")
  void testMinEmptyArray() {
    assertThatThrownBy(() -> ArrayUtils.min(new int[] {}))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> ArrayUtils.min(null)).isInstanceOf(IllegalArgumentException.class);
  }
}
