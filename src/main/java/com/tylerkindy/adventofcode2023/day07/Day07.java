package com.tylerkindy.adventofcode2023.day07;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.tylerkindy.adventofcode2023.Utils;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 {

  private static final Pattern HAND_AND_BID = Pattern.compile(
    "(?<hand>\\w+) (?<bid>\\d+)"
  );

  public static void main(String[] args) {
    Multiset<HandAndBid> handsAndBids = parseHandsAndBids(Utils.readInput(7));
  }

  public static Multiset<HandAndBid> parseHandsAndBids(String input) {
    return input
      .lines()
      .map(Day07::parseHandAndBid)
      .collect(ImmutableMultiset.toImmutableMultiset());
  }

  private static HandAndBid parseHandAndBid(String line) {
    Matcher matcher = HAND_AND_BID.matcher(line);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Unexpected hand and bid line: " + line);
    }

    Hand hand = parseHand(matcher.group("hand"));
    int bid = Integer.parseInt(matcher.group("bid"));

    return new HandAndBid(hand, bid);
  }

  private static Hand parseHand(String handStr) {
    return new Hand(handStr.chars().mapToObj(Card::fromLabel).toList());
  }

  public record HandAndBid(Hand hand, int bid) {}

  public record Hand(List<Card> cards) {}

  public enum Card {
    ACE(12),
    KING(11),
    QUEEN(10),
    JACK(9),
    TEN(8),
    NINE(7),
    EIGHT(6),
    SEVEN(5),
    SIX(4),
    FIVE(3),
    FOUR(2),
    THREE(1),
    TWO(0);

    private final int rank;

    Card(int rank) {
      this.rank = rank;
    }

    public static Card fromLabel(int label) {
      return switch (label) {
        case 'A' -> ACE;
        case 'K' -> KING;
        case 'Q' -> QUEEN;
        case 'J' -> JACK;
        case 'T' -> TEN;
        case '9' -> NINE;
        case '8' -> EIGHT;
        case '7' -> SEVEN;
        case '6' -> SIX;
        case '5' -> FIVE;
        case '4' -> FOUR;
        case '3' -> THREE;
        case '2' -> TWO;
        default -> throw new IllegalArgumentException("Unexpected card label: " + label);
      };
    }
  }
}
