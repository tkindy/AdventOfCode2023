package com.tylerkindy.adventofcode2023.day07;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
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

  public record HandAndBid(Hand hand, int bid) implements Comparable<HandAndBid> {
    @Override
    public int compareTo(HandAndBid o) {
      return hand.compareTo(o.hand);
    }
  }

  public record Hand(List<Card> cards) implements Comparable<Hand> {
    public HandType type() {
      Multiset<Integer> counts = ImmutableMultiset
        .copyOf(cards)
        .entrySet()
        .stream()
        .map(Entry::getCount)
        .collect(ImmutableMultiset.toImmutableMultiset());

      if (counts.equals(ImmutableMultiset.of(5))) {
        return HandType.FIVE_OF_A_KIND;
      }
      if (counts.equals(ImmutableMultiset.of(4, 1))) {
        return HandType.FOUR_OF_A_KIND;
      }
      if (counts.equals(ImmutableMultiset.of(3, 2))) {
        return HandType.FULL_HOUSE;
      }
      if (counts.equals(ImmutableMultiset.of(3, 1, 1))) {
        return HandType.THREE_OF_A_KIND;
      }
      if (counts.equals(ImmutableMultiset.of(2, 2, 1))) {
        return HandType.TWO_PAIR;
      }
      if (counts.equals(ImmutableMultiset.of(2, 1, 1, 1))) {
        return HandType.ONE_PAIR;
      }
      return HandType.HIGH_CARD;
    }

    @Override
    public int compareTo(Hand o) {
      int typeComparison = type().compareTo(o.type());
      if (typeComparison != 0) {
        return typeComparison;
      }

      for (int i = 0; i < cards.size(); i++) {
        int cardComparison = cards.get(i).compareTo(o.cards().get(i));
        if (cardComparison != 0) {
          return cardComparison;
        }
      }

      return 0;
    }
  }

  public enum HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
  }

  public enum Card {
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE;

    public static Card fromLabel(int label) {
      return switch (label) {
        case '2' -> TWO;
        case '3' -> THREE;
        case '4' -> FOUR;
        case '5' -> FIVE;
        case '6' -> SIX;
        case '7' -> SEVEN;
        case '8' -> EIGHT;
        case '9' -> NINE;
        case 'T' -> TEN;
        case 'J' -> JACK;
        case 'Q' -> QUEEN;
        case 'K' -> KING;
        case 'A' -> ACE;
        default -> throw new IllegalArgumentException("Unexpected card label: " + label);
      };
    }
  }
}
