package com.tylerkindy.adventofcode2023.day05;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.tylerkindy.adventofcode2023.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day05 {

  private static final Pattern NUMBER = Pattern.compile("\\b(?<number>\\d+)\\b");
  private static final Pattern MAP_HEADER = Pattern.compile(
    "(?<src>\\w+)-to-(?<dest>\\w+) map:"
  );

  public static void main(String[] args) {
    Almanac almanac = parseAlmanac(Utils.readInput(5));

    System.out.println("Part 1: " + lowestLocationNumber(almanac));
  }

  public static Almanac parseAlmanac(String input) {
    List<String> blocks = Arrays.asList(input.split("\n\n"));

    Set<Long> seeds = parseSeeds(blocks.getFirst());
    List<CategoryMap> maps = parseMaps(blocks.subList(1, blocks.size()));

    return new Almanac(seeds, maps);
  }

  private static Set<Long> parseSeeds(String seedsBlock) {
    ImmutableSet.Builder<Long> seeds = ImmutableSet.builder();

    Matcher matcher = NUMBER.matcher(seedsBlock);
    while (matcher.find()) {
      seeds.add(Long.parseLong(matcher.group("number")));
    }

    return seeds.build();
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
    List<CategoryMapper> mappers = almanac
      .maps()
      .stream()
      .map(CategoryMapper::new)
      .toList();

    return almanac
      .seeds()
      .stream()
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

  public record Almanac(Set<Long> seeds, List<CategoryMap> maps) {}

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
