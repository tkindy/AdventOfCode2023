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
    String input = Utils.readInput(7);

    System.out.println(
      "Part 1: " + calculateTotalWinnings(parseHandsAndBids(input, Part.PART1))
    );
    System.out.println(
      "Part 2: " + calculateTotalWinnings(parseHandsAndBids(input, Part.PART2))
    );
  }

  public static Multiset<HandAndBid> parseHandsAndBids(String input, Part part) {
    return input
      .lines()
      .map(line -> parseHandAndBid(line, part))
      .collect(ImmutableMultiset.toImmutableMultiset());
  }

  private static HandAndBid parseHandAndBid(String line, Part part) {
    Matcher matcher = HAND_AND_BID.matcher(line);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Unexpected hand and bid line: " + line);
    }

    Hand hand = parseHand(matcher.group("hand"), part);
    int bid = Integer.parseInt(matcher.group("bid"));

    return new HandAndBid(hand, bid);
  }

  private static Hand parseHand(String handStr, Part part) {
    return new Hand(
      handStr.chars().mapToObj((int label) -> Card.fromLabel(label, part)).toList()
    );
  }

  public static long calculateTotalWinnings(Multiset<HandAndBid> handsAndBids) {
    List<HandAndBid> sortedHands = handsAndBids.stream().sorted().toList();

    long total = 0;
    for (int i = 0; i < sortedHands.size(); i++) {
      HandAndBid handAndBid = sortedHands.get(i);
      long rank = i + 1;

      total += ((long) handAndBid.bid()) * rank;
    }

    return total;
  }

  public record HandAndBid(Hand hand, int bid) implements Comparable<HandAndBid> {
    @Override
    public int compareTo(HandAndBid o) {
      return hand.compareTo(o.hand);
    }
  }

  public record Hand(List<Card> cards) implements Comparable<Hand> {
    public HandType type() {
      Multiset<Card> allCards = ImmutableMultiset.copyOf(cards);
      int numJokers = allCards.count(Card.JOKER);

      Multiset<Integer> counts = allCards
        .entrySet()
        .stream()
        .filter(entry -> entry.getElement() != Card.JOKER)
        .map(Entry::getCount)
        .collect(ImmutableMultiset.toImmutableMultiset());

      if (counts.isEmpty() || counts.equals(ImmutableMultiset.of(5 - numJokers))) {
        return HandType.FIVE_OF_A_KIND;
      }
      if (counts.equals(ImmutableMultiset.of(4 - numJokers, 1))) {
        return HandType.FOUR_OF_A_KIND;
      }
      if (counts.equals(ImmutableMultiset.of(3 - numJokers, 2))) {
        return HandType.FULL_HOUSE;
      }
      if (counts.equals(ImmutableMultiset.of(3 - numJokers, 1, 1))) {
        return HandType.THREE_OF_A_KIND;
      }
      if (counts.equals(ImmutableMultiset.of(2, 2, 1))) {
        return HandType.TWO_PAIR;
      }
      if (counts.equals(ImmutableMultiset.of(2 - numJokers, 1, 1, 1))) {
        return HandType.ONE_PAIR;
      }
      if (counts.equals(ImmutableMultiset.of(1, 1, 1, 1, 1))) {
        return HandType.HIGH_CARD;
      }
      throw new IllegalStateException("Failed to match hand type for " + cards);
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
    JOKER,
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

    static Card fromLabel(int label, Part part) {
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
        case 'J' -> part == Part.PART1 ? JACK : JOKER;
        case 'Q' -> QUEEN;
        case 'K' -> KING;
        case 'A' -> ACE;
        default -> throw new IllegalArgumentException("Unexpected card label: " + label);
      };
    }
  }

  public enum Part {
    PART1,
    PART2,
  }
}
