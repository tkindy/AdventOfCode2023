package com.tylerkindy.adventofcode2023.day05;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Range;
import com.tylerkindy.adventofcode2023.day05.Day05.Almanac;
import com.tylerkindy.adventofcode2023.day05.Day05.CategoryMapping;
import org.junit.jupiter.api.Test;

class Day05Test {

  @Test
  void itFindsLowestLocationNumber() {
    Almanac almanac = Day05.parseAlmanacV1(
      """
      seeds: 79 14 55 13

      seed-to-soil map:
      50 98 2
      52 50 48

      soil-to-fertilizer map:
      0 15 37
      37 52 2
      39 0 15

      fertilizer-to-water map:
      49 53 8
      0 11 42
      42 0 7
      57 7 4

      water-to-light map:
      88 18 7
      18 25 70

      light-to-temperature map:
      45 77 23
      81 45 19
      68 64 13

      temperature-to-humidity map:
      0 69 1
      1 0 69

      humidity-to-location map:
      60 56 37
      56 93 4"""
    );

    assertThat(Day05.lowestLocationNumber(almanac)).isEqualTo(35);
  }

  @Test
  void itFindsLowestLocationNumberV2() {
    Almanac almanac = Day05.parseAlmanacV2(
      """
      seeds: 79 14 55 13

      seed-to-soil map:
      50 98 2
      52 50 48

      soil-to-fertilizer map:
      0 15 37
      37 52 2
      39 0 15

      fertilizer-to-water map:
      49 53 8
      0 11 42
      42 0 7
      57 7 4

      water-to-light map:
      88 18 7
      18 25 70

      light-to-temperature map:
      45 77 23
      81 45 19
      68 64 13

      temperature-to-humidity map:
      0 69 1
      1 0 69

      humidity-to-location map:
      60 56 37
      56 93 4"""
    );

    assertThat(Day05.lowestLocationNumber(almanac)).isEqualTo(46);
  }

  @Test
  void itParsesSeedsV1() {
    Almanac almanac = Day05.parseAlmanacV1(
      """
      seeds: 79 14 55 13

      seed-to-soil map:
      50 98 2
      52 50 48

      soil-to-fertilizer map:
      0 15 37
      37 52 2
      39 0 15

      fertilizer-to-water map:
      49 53 8
      0 11 42
      42 0 7
      57 7 4

      water-to-light map:
      88 18 7
      18 25 70

      light-to-temperature map:
      45 77 23
      81 45 19
      68 64 13

      temperature-to-humidity map:
      0 69 1
      1 0 69

      humidity-to-location map:
      60 56 37
      56 93 4"""
    );

    assertThat(almanac.seedRanges())
      .containsOnly(
        Range.closedOpen(79L, 80L),
        Range.closedOpen(14L, 15L),
        Range.closedOpen(55L, 56L),
        Range.closedOpen(13L, 14L)
      );
  }

  @Test
  void itParsesSeedsV2() {
    Almanac almanac = Day05.parseAlmanacV2(
      """
      seeds: 79 14 55 13

      seed-to-soil map:
      50 98 2
      52 50 48

      soil-to-fertilizer map:
      0 15 37
      37 52 2
      39 0 15

      fertilizer-to-water map:
      49 53 8
      0 11 42
      42 0 7
      57 7 4

      water-to-light map:
      88 18 7
      18 25 70

      light-to-temperature map:
      45 77 23
      81 45 19
      68 64 13

      temperature-to-humidity map:
      0 69 1
      1 0 69

      humidity-to-location map:
      60 56 37
      56 93 4"""
    );

    assertThat(almanac.seedRanges())
      .containsOnly(Range.closedOpen(55L, 68L), Range.closedOpen(79L, 93L));
  }

  @Test
  void itParsesCategories() {
    Almanac almanac = Day05.parseAlmanacV2(
      """
      seeds: 79 14 55 13

      seed-to-soil map:
      50 98 2
      52 50 48

      soil-to-fertilizer map:
      0 15 37
      37 52 2
      39 0 15

      fertilizer-to-water map:
      49 53 8
      0 11 42
      42 0 7
      57 7 4

      water-to-light map:
      88 18 7
      18 25 70

      light-to-temperature map:
      45 77 23
      81 45 19
      68 64 13

      temperature-to-humidity map:
      0 69 1
      1 0 69

      humidity-to-location map:
      60 56 37
      56 93 4"""
    );

    assertThat(almanac.categoryMaps().getFirst().mappings())
      .containsOnly(
        new CategoryMapping(Range.lessThan(50L), 0),
        new CategoryMapping(Range.closedOpen(50L, 98L), 2),
        new CategoryMapping(Range.closedOpen(98L, 100L), -48),
        new CategoryMapping(Range.atLeast(100L), 0)
      );
  }
}
