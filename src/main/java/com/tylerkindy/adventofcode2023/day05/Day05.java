package com.tylerkindy.adventofcode2023.day05;

import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.tylerkindy.adventofcode2023.Utils;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day05 {

  private static final Pattern NUMBER = Pattern.compile("\\b(?<number>\\d+)\\b");
  private static final Pattern SEED_RANGE = Pattern.compile(
    "(?<start>\\d+) (?<length>\\d+)"
  );
  private static final Pattern MAP_HEADER = Pattern.compile(
    "(?<src>\\w+)-to-(?<dest>\\w+) map:"
  );

  public static void main(String[] args) {
    String input = Utils.readInput(5);

    System.out.println("Part 1: " + lowestLocationNumber(parseAlmanacV1(input)));
    System.out.println("Part 2: " + lowestLocationNumber(parseAlmanacV2(input)));
  }

  public static Almanac parseAlmanacV1(String input) {
    return parseAlmanac(input, Day05::parseSeedsV1);
  }

  public static Almanac parseAlmanacV2(String input) {
    return parseAlmanac(input, Day05::parseSeedsV2);
  }

  private static Almanac parseAlmanac(
    String input,
    Function<String, Set<Range<Long>>> seedsParser
  ) {
    List<String> blocks = Arrays.asList(input.split("\n\n"));

    Set<Range<Long>> seeds = seedsParser.apply(blocks.getFirst());
    List<CategoryMap> maps = parseMaps(blocks.subList(1, blocks.size()));

    return new Almanac(seeds, maps);
  }

  private static Set<Range<Long>> parseSeedsV1(String seedsBlock) {
    ImmutableSet.Builder<Range<Long>> seeds = ImmutableSet.builder();

    Matcher matcher = NUMBER.matcher(seedsBlock);
    while (matcher.find()) {
      long number = Long.parseLong(matcher.group("number"));

      seeds.add(Range.singleton(number).canonical(DiscreteDomain.longs()));
    }

    return seeds.build();
  }

  private static Set<Range<Long>> parseSeedsV2(String seedsBlock) {
    ImmutableSet.Builder<Range<Long>> ranges = ImmutableSet.builder();

    Matcher matcher = SEED_RANGE.matcher(seedsBlock);
    while (matcher.find()) {
      long start = Long.parseLong(matcher.group("start"));
      long length = Long.parseLong(matcher.group("length"));

      ranges.add(Range.closedOpen(start, start + length));
    }

    return ranges.build();
  }

  private static List<CategoryMap> parseMaps(List<String> mapBlocks) {
    return mapBlocks.stream().map(Day05::parseMap).toList();
  }

  private static CategoryMap parseMap(String mapBlock) {
    List<String> lines = Arrays.asList(mapBlock.trim().split("\n"));

    String header = lines.getFirst();
    Matcher headerMatcher = MAP_HEADER.matcher(header);
    if (!headerMatcher.matches()) {
      throw new IllegalArgumentException("Unexpected block header: " + header);
    }

    List<CategoryMapping> listedMappings = lines
      .subList(1, lines.size())
      .stream()
      .map(Day05::parseCategoryMapping)
      .sorted(Comparator.comparing(mapping -> mapping.srcRange().lowerEndpoint()))
      .toList();

    ImmutableSet.Builder<CategoryMapping> mappings = ImmutableSet
      .<CategoryMapping>builder()
      .addAll(listedMappings);

    mappings.add(
      new CategoryMapping(
        Range.lessThan(listedMappings.getFirst().srcRange().lowerEndpoint()),
        0
      )
    );
    mappings.add(
      new CategoryMapping(
        Range.atLeast(listedMappings.getLast().srcRange().upperEndpoint()),
        0
      )
    );

    return new CategoryMap(mappings.build());
  }

  private static CategoryMapping parseCategoryMapping(String line) {
    List<String> numbers = Arrays.asList(line.split(" "));

    long srcStart = Long.parseLong(numbers.get(1));
    long destStart = Long.parseLong(numbers.get(0));
    long length = Long.parseLong(numbers.get(2));

    return new CategoryMapping(
      Range.closedOpen(srcStart, srcStart + length),
      destStart - srcStart
    );
  }

  public static long lowestLocationNumber(Almanac almanac) {
    Set<Range<Long>> ranges = almanac.seedRanges();
    for (CategoryMap categoryMap : almanac.categoryMaps()) {
      ranges = mapOnce(categoryMap, ranges);
    }

    return ranges.stream().mapToLong(Range::lowerEndpoint).min().orElseThrow();
  }

  public static Set<Range<Long>> mapOnce(
    CategoryMap categoryMap,
    Set<Range<Long>> ranges
  ) {
    return ranges
      .stream()
      .flatMap(range -> categoryMap.map(range).stream())
      .collect(Collectors.toSet());
  }

  public record Almanac(Set<Range<Long>> seedRanges, List<CategoryMap> categoryMaps) {}

  public record CategoryMap(Set<CategoryMapping> mappings) {
    public Set<Range<Long>> map(Range<Long> range) {
      return mappings
        .stream()
        .map(mappingRange -> {
          if (!range.isConnected(mappingRange.srcRange())) {
            return Optional.<Range<Long>>empty();
          }
          Range<Long> intersection = range
            .intersection(mappingRange.srcRange())
            .canonical(DiscreteDomain.longs());

          if (intersection.isEmpty()) {
            return Optional.<Range<Long>>empty();
          }

          return Optional.of(
            Range.closedOpen(
              intersection.lowerEndpoint() + mappingRange.delta(),
              intersection.upperEndpoint() + mappingRange.delta()
            )
          );
        })
        .flatMap(Optional::stream)
        .collect(Collectors.toSet());
    }
  }

  record CategoryMapping(Range<Long> srcRange, long delta) {}
}
