package com.tylerkindy.adventofcode2023.day03;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Day03Test {

  @Test
  void itSumsPartNumbers() {
    assertThat(
      Day03.sumPartNumbers(
        """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598.."""
      )
    )
      .isEqualTo(4361);
  }

  @Test
  void itSumsGearRatios() {
    assertThat(
      Day03.sumGearRatios(
        """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598.."""
      )
    )
      .isEqualTo(467835);
  }
}
