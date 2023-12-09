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
    return races
      .stream()
      .mapToLong(Day06::calculateWinCount)
      .reduce(1, (l1, l2) -> l1 * l2);
  }

  public static long calculateWinCount(Race race) {
    long durationMillis = race.duration().toMillis();

    // Quadratic formula
    double sqrt = Math.sqrt(
      Math.pow(durationMillis, 2) - 4 * race.recordDistance().toMillimeters()
    );

    double plusRoot = (-1 * durationMillis + sqrt) / (-2);
    double minusRoot = (-1 * durationMillis - sqrt) / (-2);

    double leftRoot = Math.min(plusRoot, minusRoot);
    double leftRootCeil = Math.ceil(leftRoot);
    long minHoldTime = (long) (leftRootCeil == leftRoot
        ? leftRootCeil + 1
        : leftRootCeil);

    double rightRoot = Math.max(plusRoot, minusRoot);
    double rightRootFloor = Math.floor(rightRoot);
    long maxHoldTime = (long) (rightRootFloor == rightRoot
        ? rightRootFloor - 1
        : rightRootFloor);

    return maxHoldTime - minHoldTime + 1;
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

    public long toMillimeters() {
      return millimeters;
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
