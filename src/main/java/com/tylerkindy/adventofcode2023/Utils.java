package com.tylerkindy.adventofcode2023;

import com.google.common.io.Resources;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public final class Utils {
  private Utils() {}

  public static String readInput(int day) {
    try {
      return Resources.toString(
          Resources.getResource("input/day%02d.txt".formatted(day)),
          StandardCharsets.UTF_8
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
