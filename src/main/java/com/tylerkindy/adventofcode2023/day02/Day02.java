package com.tylerkindy.adventofcode2023.day02;

import static java.util.function.Predicate.not;

import com.tylerkindy.adventofcode2023.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02 {

  private static final Pattern GAME_LINE = Pattern.compile(
    "Game (?<id>\\d+): (?<reveals>.*)"
  );
  private static final Pattern REVEAL_PART = Pattern.compile(
    "(?<count>\\d+) (?<color>red|green|blue)"
  );

  public static void main(String[] args) {
    String gamesString = Utils.readInput(2).trim();

    System.out.println("Part 1: " + sumPossibleGames(gamesString));
    System.out.println("Part 2: " + sumPowers(gamesString));
  }

  public static long sumPossibleGames(String gamesString) {
    return gamesString
      .lines()
      .map(Day02::parseGame)
      .filter(Day02::isPossibleGame)
      .mapToLong(Game::id)
      .sum();
  }

  private static Game parseGame(String gameLine) {
    Matcher matcher = GAME_LINE.matcher(gameLine);
    matcher.matches();

    int id = Integer.parseInt(matcher.group("id"));
    String revealsStr = matcher.group("reveals");

    List<Reveal> reveals = Arrays
      .stream(revealsStr.split(";"))
      .map(reveal -> parseReveal(reveal.trim()))
      .toList();

    return new Game(id, reveals);
  }

  private static Reveal parseReveal(String reveal) {
    Matcher matcher = REVEAL_PART.matcher(reveal);

    int red = 0;
    int green = 0;
    int blue = 0;
    while (matcher.find()) {
      int count = Integer.parseInt(matcher.group("count"));
      switch (matcher.group("color")) {
        case "red" -> {
          red = count;
        }
        case "green" -> {
          green = count;
        }
        case "blue" -> {
          blue = count;
        }
      }
    }

    return new Reveal(red, green, blue);
  }

  private static boolean isPossibleGame(Game game) {
    return game
      .reveals()
      .stream()
      .filter(not(Day02::isPossibleReveal))
      .findAny()
      .isEmpty();
  }

  private static boolean isPossibleReveal(Reveal reveal) {
    return (reveal.numRed() <= 12 && reveal.numGreen() <= 13 && reveal.numBlue() <= 14);
  }

  static long sumPowers(String gamesString) {
    return gamesString
      .lines()
      .map(Day02::parseGame)
      .mapToLong(Day02::calculateMinimumPower)
      .sum();
  }

  private static long calculateMinimumPower(Game game) {
    int minRed = 0;
    int minGreen = 0;
    int minBlue = 0;

    for (Reveal reveal : game.reveals()) {
      minRed = Math.max(minRed, reveal.numRed());
      minGreen = Math.max(minGreen, reveal.numGreen());
      minBlue = Math.max(minBlue, reveal.numBlue());
    }

    return (long) minRed * minGreen * minBlue;
  }

  record Game(int id, List<Reveal> reveals) {}

  record Reveal(int numRed, int numGreen, int numBlue) {}
}
