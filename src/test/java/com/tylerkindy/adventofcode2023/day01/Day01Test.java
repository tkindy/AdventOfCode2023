package com.tylerkindy.adventofcode2023.day01;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day01Test {

  @Test
  void itSumsCalibrationValues() {
    assertThat(
            Day01.sumCalibrationValues(
                """
                1abc2
                pqr3stu8vwx
                a1b2c3d4e5f
                treb7uchet"""))
        .isEqualTo(142);
  }

  @Test
  void itParsesLines() {
    assertThat(Day01.parseLine("1abc2")).isEqualTo(12);
    assertThat(Day01.parseLine("pqr3stu8vwx")).isEqualTo(38);
    assertThat(Day01.parseLine("a1b2c3d4e5f")).isEqualTo(15);
    assertThat(Day01.parseLine("treb7uchet")).isEqualTo(77);
  }
}
