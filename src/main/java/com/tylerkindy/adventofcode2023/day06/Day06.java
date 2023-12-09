package com.tylerkindy.adventofcode2023.day06;

import com.google.common.collect.ImmutableList;
import com.tylerkindy.adventofcode2023.Utils;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day06 {

  private static final Pattern NUMBER = Pattern.compile("\\d+");

  public static void main(String[] args) {
    List<Race> races = parseRaces(Utils.readInput(6));

    System.out.println("Part 1: " + winProduct(races));
  }

  public static List<Race> parseRaces(String input) {
    String[] lines = input.trim().split("\n");
    List<Long> times = parseLine(lines[0]);
    List<Long> distances = parseLine(lines[1]);

    ImmutableList.Builder<Race> races = ImmutableList.builder();

    for (int i = 0; i < times.size(); i++) {
      races.add(
        new Race(
          Duration.ofMillis(times.get(i)),
          Distance.ofMillimeters(distances.get(i))
        )
      );
    }

    return races.build();
  }

  public static long winProduct(List<Race> races) {
    return 0;
  }

  private static List<Long> parseLine(String line) {
    Matcher matcher = NUMBER.matcher(line);

    ImmutableList.Builder<Long> numbers = ImmutableList.builder();
    while (matcher.find()) {
      numbers.add(Long.parseLong(matcher.group()));
    }

    return numbers.build();
  }

  public record Race(Duration duration, Distance recordDistance) {}

  public static final class Distance {

    private final long millimeters;

    private Distance(long millimeters) {
      this.millimeters = millimeters;
    }

    public static Distance ofMillimeters(long millimeters) {
      return new Distance(millimeters);
    }

    @Override
    public boolean equals(Object o) {
      return o instanceof Distance d && millimeters == d.millimeters;
    }

    @Override
    public int hashCode() {
      return Objects.hash(millimeters);
    }
  }
}
