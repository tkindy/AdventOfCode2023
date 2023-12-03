package com.tylerkindy.adventofcode2023.day03;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
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
    String schematic = Utils.readInput(3).trim();

    System.out.println("Part 1: " + sumPartNumbers(schematic));
  }

  public static long sumPartNumbers(String schematic) {
    return sumPartNumbers(parseSchematic(schematic));
  }

  public static long sumPartNumbers(Schematic schematic) {
    return schematic.partNumbers().stream().mapToLong(partNumber -> partNumber).sum();
  }

  private static Schematic parseSchematic(String schematic) {
    List<String> lines = Arrays.asList(schematic.split("\n"));

    Set<SchematicNumber> schematicNumbers = new HashSet<>();
    Set<Point> symbolLocations = new HashSet<>();

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
        symbolLocations.add(new Point(symbolMatcher.start(), y));
      }
    }

    ImmutableMultiset.Builder<Integer> partNumbers = ImmutableMultiset.builder();
    for (SchematicNumber schematicNumber : schematicNumbers) {
      SetView<Point> matchingSymbols = Sets.intersection(
        schematicNumber.span().boundaryPoints(),
        symbolLocations
      );

      if (!matchingSymbols.isEmpty()) {
        partNumbers.add(schematicNumber.number());
      }
    }

    return new Schematic(partNumbers.build());
  }

  public record Schematic(Multiset<Integer> partNumbers) {}

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
}
