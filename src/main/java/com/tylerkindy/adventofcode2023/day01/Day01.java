package com.tylerkindy.adventofcode2023.day01;

import com.google.common.io.Resources;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class Day01 {
  public static void main(String[] args) {
    String document = readInput().trim();

    System.out.println("Part 1: " + sumCalibrationValues(document));
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

  private static String readInput() {
    try {
      return Resources.toString(Resources.getResource("input/day01.txt"), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
