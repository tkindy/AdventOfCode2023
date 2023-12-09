package com.tylerkindy.adventofcode2023.day06;

import static org.assertj.core.api.Assertions.assertThat;

import com.tylerkindy.adventofcode2023.day06.Day06.Distance;
import com.tylerkindy.adventofcode2023.day06.Day06.Race;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

class Day06Test {

  @Test
  void itParsesRaces() {
    assertThat(
      Day06.parseRaces(
        """
        Time:      7  15   30
        Distance:  9  40  200"""
      )
    )
      .isEqualTo(
        List.of(
          new Race(Duration.ofMillis(7), Distance.ofMillimeters(9)),
          new Race(Duration.ofMillis(15), Distance.ofMillimeters(40)),
          new Race(Duration.ofMillis(30), Distance.ofMillimeters(200))
        )
      );
  }

  @Test
  void itFindsWinProduct() {
    assertThat(
      Day06.winProduct(
        Day06.parseRaces(
          """
          Time:      7  15   30
          Distance:  9  40  200"""
        )
      )
    )
      .isEqualTo(288L);
  }

  @Test
  void itCalculatesWinCounts() {
    assertThat(
      Day06.calculateWinCount(new Race(Duration.ofMillis(7), Distance.ofMillimeters(9)))
    )
      .isEqualTo(4);
    assertThat(
      Day06.calculateWinCount(new Race(Duration.ofMillis(15), Distance.ofMillimeters(40)))
    )
      .isEqualTo(8);
    assertThat(
      Day06.calculateWinCount(
        new Race(Duration.ofMillis(30), Distance.ofMillimeters(200))
      )
    )
      .isEqualTo(9);
  }
}
