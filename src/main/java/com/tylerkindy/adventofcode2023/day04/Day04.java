package com.tylerkindy.adventofcode2023.day04;

import com.google.common.collect.Sets;
import com.tylerkindy.adventofcode2023.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04 {

  private static final Pattern CARD = Pattern.compile(
    "Card\\s+(?<id>\\d+):\\s+(?<winningNumbers>.*)\\s+\\|\\s+(?<yourNumbers>.*)"
  );
  private static final Pattern SPACES = Pattern.compile("\\s+");

  public static void main(String[] args) {
    List<Card> cards = parseCards(Utils.readInput(4));

    System.out.println("Part 1: " + sumCardScores(cards));
  }

  public static long sumCardScores(String cardsStr) {
    return sumCardScores(parseCards(cardsStr));
  }

  public static long sumCardScores(List<Card> cards) {
    return cards.stream().mapToLong(Day04::score).sum();
  }

  private static List<Card> parseCards(String cardsStr) {
    return cardsStr.lines().map(Day04::parseCard).toList();
  }

  private static Card parseCard(String cardsLine) {
    Matcher matcher = CARD.matcher(cardsLine);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Unexpected card line: " + cardsLine);
    }

    int id = Integer.parseInt(matcher.group("id"));
    Set<Integer> winningNumbers = parseNumbers(matcher.group("winningNumbers"));
    Set<Integer> yourNumbers = parseNumbers(matcher.group("yourNumbers"));

    return new Card(id, winningNumbers, yourNumbers);
  }

  private static Set<Integer> parseNumbers(String numbers) {
    return Arrays
      .stream(SPACES.split(numbers.trim()))
      .map(Integer::parseInt)
      .collect(Collectors.toSet());
  }

  public static long score(Card card) {
    Set<Integer> matchingNumbers = Sets.intersection(
      card.winningNumbers(),
      card.yourNumbers()
    ).immutableCopy();

    if (matchingNumbers.isEmpty()) {
      return 0;
    }
    return (long) Math.pow(2, matchingNumbers.size() - 1);
  }

  public record Card(int id, Set<Integer> winningNumbers, Set<Integer> yourNumbers) {}
}
