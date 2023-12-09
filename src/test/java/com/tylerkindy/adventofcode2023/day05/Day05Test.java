package com.tylerkindy.adventofcode2023.day05;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.tylerkindy.adventofcode2023.day05.Day05.Almanac;
import com.tylerkindy.adventofcode2023.day05.Day05.CategoryMap;
import com.tylerkindy.adventofcode2023.day05.Day05.CategoryMapRange;
import com.tylerkindy.adventofcode2023.day05.Day05.CategoryMapper;
import java.util.Set;
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
  void itMaps() {
    CategoryMapper mapper = new CategoryMapper(
      new CategoryMap(
        "seed",
        "soil",
        Set.of(new CategoryMapRange(98, 50, 2), new CategoryMapRange(50, 52, 48))
      )
    );

    assertThat(mapper.map(Range.singleton(79L)))
      .isEqualTo(Set.of(Range.singleton(81L).canonical(DiscreteDomain.longs())));
    //    assertThat(mapper.map(14)).isEqualTo(14);
    //    assertThat(mapper.map(55)).isEqualTo(57);
    //    assertThat(mapper.map(13)).isEqualTo(13);
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
}
