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

  @Test
  void itSumsCorrectCalibrationValues() {
    assertThat(
            Day01.sumCorrectCalibrationValues(
                """
                two1nine
                eightwothree
                abcone2threexyz
                xtwone3four
                4nineeightseven2
                zoneight234
                7pqrstsixteen
                """))
        .isEqualTo(281);
  }

  @Test
  void itParsesLineCorrectly() {
    assertThat(Day01.parseLineCorrectly("two1nine")).isEqualTo(29);
    assertThat(Day01.parseLineCorrectly("eightwothree")).isEqualTo(83);
    assertThat(Day01.parseLineCorrectly("7pqrstsixteen")).isEqualTo(76);
    assertThat(Day01.parseLineCorrectly("4nineeightseven2")).isEqualTo(42);
    assertThat(Day01.parseLineCorrectly("zoneight234")).isEqualTo(14);
    assertThat(Day01.parseLineCorrectly("oneight")).isEqualTo(18);
  }
}
