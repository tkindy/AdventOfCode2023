package com.tylerkindy.adventofcode2023.day05;

import com.google.common.collect.*;
import com.tylerkindy.adventofcode2023.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
    Function<String, Iterable<Long>> seedsParser
  ) {
    List<String> blocks = Arrays.asList(input.split("\n\n"));

    Iterable<Long> seeds = seedsParser.apply(blocks.getFirst());
    List<CategoryMap> maps = parseMaps(blocks.subList(1, blocks.size()));

    return new Almanac(seeds, maps);
  }

  private static Set<Long> parseSeedsV1(String seedsBlock) {
    ImmutableSet.Builder<Long> seeds = ImmutableSet.builder();

    Matcher matcher = NUMBER.matcher(seedsBlock);
    while (matcher.find()) {
      seeds.add(Long.parseLong(matcher.group("number")));
    }

    return seeds.build();
  }

  private static Iterable<Long> parseSeedsV2(String seedsBlock) {
    Multiset<SeedRange> ranges = HashMultiset.create();

    Matcher matcher = SEED_RANGE.matcher(seedsBlock);
    while (matcher.find()) {
      long start = Long.parseLong(matcher.group("start"));
      long length = Long.parseLong(matcher.group("length"));

      ranges.add(new SeedRange(start, length));
    }

    return () ->
      ranges
        .stream()
        .flatMapToLong(range ->
          LongStream.range(range.start(), range.start() + range.length)
        )
        .iterator();
  }

  private record SeedRange(long start, long length) {}

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
    List<CategoryMapper> mappers = almanac
      .maps()
      .stream()
      .map(CategoryMapper::new)
      .toList();

    return Streams
      .stream(almanac.seeds())
      .mapToLong(seed -> {
        long number = seed;
        for (CategoryMapper mapper : mappers) {
          number = mapper.map(number);
        }
        return number;
      })
      .min()
      .orElseThrow();
  }

  public record Almanac(Iterable<Long> seeds, List<CategoryMap> maps) {}

  record CategoryMap(
    String srcCategory,
    String destCategory,
    Set<CategoryMapRange> ranges
  ) {}

  record CategoryMapRange(long srcStart, long destStart, long length) {}

  static class CategoryMapper {

    private final Set<MappingRange> ranges;

    CategoryMapper(CategoryMap map) {
      this.ranges =
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

    public long map(long number) {
      return ranges
        .stream()
        .filter(range -> range.srcRange().contains(number))
        .findAny()
        .map(range -> number + range.delta())
        .orElse(number);
    }

    private record MappingRange(Range<Long> srcRange, long delta) {}
  }
}
