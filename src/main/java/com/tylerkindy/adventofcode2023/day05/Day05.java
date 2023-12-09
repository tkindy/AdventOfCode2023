package com.tylerkindy.adventofcode2023.day05;

import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.tylerkindy.adventofcode2023.Utils;
import java.util.Arrays;
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

      seeds.add(Range.singleton(number));
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

    Set<CategoryMapRange> ranges = lines
      .subList(1, lines.size())
      .stream()
      .map(Day05::parseMapRange)
      .collect(Collectors.toSet());

    return new CategoryMap(
      headerMatcher.group("src"),
      headerMatcher.group("dest"),
      ranges
    );
  }

  private static CategoryMapRange parseMapRange(String line) {
    List<String> numbers = Arrays.asList(line.split(" "));

    return new CategoryMapRange(
      Long.parseLong(numbers.get(1)),
      Long.parseLong(numbers.get(0)),
      Long.parseLong(numbers.get(2))
    );
  }

  public static long lowestLocationNumber(Almanac almanac) {
    List<CategoryMapper> mappers = mappers(almanac);

    Set<Range<Long>> ranges = almanac.seedRanges();
    for (CategoryMapper mapper : mappers) {
      ranges =
        ranges
          .stream()
          .flatMap(range -> mapper.map(range).stream())
          .collect(Collectors.toSet());
    }

    return ranges.stream().mapToLong(Range::lowerEndpoint).min().orElseThrow();
  }

  public static List<CategoryMapper> mappers(Almanac almanac) {
    return almanac.maps().stream().map(CategoryMapper::new).toList();
  }

  public record Almanac(Set<Range<Long>> seedRanges, List<CategoryMap> maps) {}

  record CategoryMap(
    String srcCategory,
    String destCategory,
    Set<CategoryMapRange> ranges
  ) {}

  record CategoryMapRange(long srcStart, long destStart, long length) {}

  public static class CategoryMapper {

    private final Set<MappingRange> mappingRanges;

    CategoryMapper(CategoryMap map) {
      this.mappingRanges =
        map
          .ranges()
          .stream()
          .map(range ->
            new MappingRange(
              Range.closedOpen(range.srcStart(), range.srcStart() + range.length()),
              range.destStart() - range.srcStart()
            )
          )
          .collect(Collectors.toSet());
    }

    public Set<Range<Long>> map(Range<Long> range) {
      return mappingRanges
        .stream()
        .map(mappingRange -> {
          if (!range.isConnected(mappingRange.srcRange())) {
            return Optional.<Range<Long>>empty();
          }
          Range<Long> intersection = range
            .intersection(mappingRange.srcRange())
            .canonical(DiscreteDomain.longs());

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

    private record MappingRange(Range<Long> srcRange, long delta) {}
  }
}
