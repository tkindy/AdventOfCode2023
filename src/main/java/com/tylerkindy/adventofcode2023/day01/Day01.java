package com.tylerkindy.adventofcode2023.day01;

import com.tylerkindy.adventofcode2023.Utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01 {

  private static final Pattern DIGIT = Pattern.compile(
    "[1-9]|one|two|three|four|five|six|seven|eight|nine"
  );

  public static void main(String[] args) {
    String document = Utils.readInput(1).trim();

    System.out.println("Part 1: " + sumCalibrationValues(document));
    System.out.println("Part 2: " + sumCorrectCalibrationValues(document));
  }

  static long sumCalibrationValues(String document) {
    return document.lines().mapToInt(Day01::parseLine).sum();
  }

  static int parseLine(String line) {
    char first = '\0';
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (Character.isDigit(c)) {
        first = c;
        break;
      }
    }

    char last = '\0';
    for (int i = line.length() - 1; i >= 0; i--) {
      char c = line.charAt(i);
      if (Character.isDigit(c)) {
        last = c;
        break;
      }
    }

    return Integer.parseInt("" + first + last);
  }

  static long sumCorrectCalibrationValues(String document) {
    return document.lines().mapToInt(Day01::parseLineCorrectly).sum();
  }

  static int parseLineCorrectly(String line) {
    Matcher matcher = DIGIT.matcher(line);
    matcher.find();

    int start = parseDigit(matcher.group());

    int end = start;
    int startIndex = matcher.start() + 1;
    while (matcher.find(startIndex)) {
      end = parseDigit(matcher.group());
      startIndex = matcher.start() + 1;
    }

    return start * 10 + end;
  }

  private static int parseDigit(String digit) {
    return switch (digit) {
      case "one" -> 1;
      case "two" -> 2;
      case "three" -> 3;
      case "four" -> 4;
      case "five" -> 5;
      case "six" -> 6;
      case "seven" -> 7;
      case "eight" -> 8;
      case "nine" -> 9;
      default -> Integer.parseInt(digit);
    };
  }
}
