package com.tylerkindy.adventofcode2023.day03;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.tylerkindy.adventofcode2023.Utils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {

  private static final Pattern NUMBER = Pattern.compile("\\d+");
  private static final Pattern SYMBOL = Pattern.compile("[^\\d.]");

  public static void main(String[] args) {
    Schematic schematic = parseSchematic(Utils.readInput(3).trim());

    System.out.println("Part 1: " + sumPartNumbers(schematic));
    System.out.println("Part 2: " + sumGearRatios(schematic));
  }

  public static long sumPartNumbers(String schematic) {
    return sumPartNumbers(parseSchematic(schematic));
  }

  public static long sumPartNumbers(Schematic schematic) {
    return schematic.partNumbers().stream().mapToLong(partNumber -> partNumber).sum();
  }

  public static long sumGearRatios(String schematic) {
    return sumGearRatios(parseSchematic(schematic));
  }

  private static long sumGearRatios(Schematic schematic) {
    return schematic.gears().stream().mapToLong(Gear::ratio).sum();
  }

  private static Schematic parseSchematic(String schematic) {
    List<String> lines = Arrays.asList(schematic.split("\n"));

    Set<SchematicNumber> schematicNumbers = new HashSet<>();
    Set<Point> symbolLocations = new HashSet<>();
    Set<Point> potentialGearLocations = new HashSet<>();

    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);

      Matcher numberMatcher = NUMBER.matcher(line);
      while (numberMatcher.find()) {
        String numberStr = numberMatcher.group();
        schematicNumbers.add(
          new SchematicNumber(
            Integer.parseInt(numberStr),
            new Span(numberMatcher.start(), y, numberStr.length())
          )
        );
      }

      Matcher symbolMatcher = SYMBOL.matcher(line);
      while (symbolMatcher.find()) {
        Point point = new Point(symbolMatcher.start(), y);
        symbolLocations.add(point);

        if (symbolMatcher.group().equals("*")) {
          potentialGearLocations.add(point);
        }
      }
    }

    ImmutableMultiset.Builder<Integer> partNumbers = ImmutableMultiset.builder();
    ListMultimap<Point, SchematicNumber> adjacentToParts = MultimapBuilder
      .hashKeys()
      .arrayListValues()
      .build();

    for (SchematicNumber schematicNumber : schematicNumbers) {
      Set<Point> boundary = schematicNumber.span().boundaryPoints();
      boundary.forEach(point -> adjacentToParts.put(point, schematicNumber));

      SetView<Point> matchingSymbols = Sets.intersection(boundary, symbolLocations);

      if (!matchingSymbols.isEmpty()) {
        partNumbers.add(schematicNumber.number());
      }
    }

    ImmutableMultiset.Builder<Gear> gears = ImmutableMultiset.builder();
    for (Point potentialGearLocation : potentialGearLocations) {
      List<SchematicNumber> adjacent = adjacentToParts.get(potentialGearLocation);
      if (adjacent.size() != 2) {
        continue;
      }

      gears.add(new Gear(adjacent.get(0), adjacent.get(1)));
    }

    return new Schematic(partNumbers.build(), gears.build());
  }

  public record Schematic(Multiset<Integer> partNumbers, Multiset<Gear> gears) {}

  record SchematicNumber(int number, Span span) {}

  record Point(int x, int y) {}

  record Span(int startX, int y, int length) {
    Set<Point> boundaryPoints() {
      ImmutableSet.Builder<Point> points = ImmutableSet.builder();
      for (int row = y - 1; row <= y + 1; row++) {
        for (int column = startX - 1; column <= startX + length; column++) {
          if (row == y && (column >= startX && column < startX + length)) {
            continue;
          }

          points.add(new Point(column, row));
        }
      }

      return points.build();
    }
  }

  record Gear(SchematicNumber partA, SchematicNumber partB) {
    long ratio() {
      return (long) partA.number() * partB.number();
    }
  }
}
