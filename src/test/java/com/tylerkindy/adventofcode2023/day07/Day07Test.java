package com.tylerkindy.adventofcode2023.day07;

import static com.tylerkindy.adventofcode2023.day07.Day07.Card.ACE;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.FIVE;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.FOUR;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.JACK;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.JOKER;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.KING;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.QUEEN;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.SEVEN;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.SIX;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.TEN;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.THREE;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.TWO;
import static org.assertj.core.api.Assertions.assertThat;

import com.tylerkindy.adventofcode2023.day07.Day07.Hand;
import com.tylerkindy.adventofcode2023.day07.Day07.HandAndBid;
import com.tylerkindy.adventofcode2023.day07.Day07.HandType;
import com.tylerkindy.adventofcode2023.day07.Day07.Part;
import java.util.List;
import org.junit.jupiter.api.Test;

class Day07Test {

  @Test
  void itParses() {
    assertThat(
      Day07.parseHandsAndBids(
        """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483""",
        Part.PART1
      )
    )
      .containsExactlyInAnyOrder(
        new HandAndBid(new Hand(List.of(THREE, TWO, TEN, THREE, KING)), 765),
        new HandAndBid(new Hand(List.of(TEN, FIVE, FIVE, JACK, FIVE)), 684),
        new HandAndBid(new Hand(List.of(KING, KING, SIX, SEVEN, SEVEN)), 28),
        new HandAndBid(new Hand(List.of(KING, TEN, JACK, JACK, TEN)), 220),
        new HandAndBid(new Hand(List.of(QUEEN, QUEEN, QUEEN, JACK, ACE)), 483)
      );
  }

  @Test
  void itSortsHands() {
    assertThat(
      Day07
        .parseHandsAndBids(
          """
          32T3K 765
          T55J5 684
          KK677 28
          KTJJT 220
          QQQJA 483""",
          Part.PART1
        )
        .stream()
        .sorted()
        .toList()
    )
      .containsExactly(
        new HandAndBid(new Hand(List.of(THREE, TWO, TEN, THREE, KING)), 765),
        new HandAndBid(new Hand(List.of(KING, TEN, JACK, JACK, TEN)), 220),
        new HandAndBid(new Hand(List.of(KING, KING, SIX, SEVEN, SEVEN)), 28),
        new HandAndBid(new Hand(List.of(TEN, FIVE, FIVE, JACK, FIVE)), 684),
        new HandAndBid(new Hand(List.of(QUEEN, QUEEN, QUEEN, JACK, ACE)), 483)
      );
  }

  @Test
  void itCalculatesTotalWinnings() {
    assertThat(
      Day07.calculateTotalWinnings(
        Day07.parseHandsAndBids(
          """
          32T3K 765
          T55J5 684
          KK677 28
          KTJJT 220
          QQQJA 483""",
          Part.PART1
        )
      )
    )
      .isEqualTo(6440L);
  }

  @Test
  void itParsesHandsV2() {
    assertThat(
      Day07.parseHandsAndBids(
        """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483""",
        Part.PART2
      )
    )
      .containsExactlyInAnyOrder(
        new HandAndBid(new Hand(List.of(THREE, TWO, TEN, THREE, KING)), 765),
        new HandAndBid(new Hand(List.of(TEN, FIVE, FIVE, JOKER, FIVE)), 684),
        new HandAndBid(new Hand(List.of(KING, KING, SIX, SEVEN, SEVEN)), 28),
        new HandAndBid(new Hand(List.of(KING, TEN, JOKER, JOKER, TEN)), 220),
        new HandAndBid(new Hand(List.of(QUEEN, QUEEN, QUEEN, JOKER, ACE)), 483)
      );
  }

  @Test
  void itCalculatesTotalWinningsV2() {
    assertThat(
      Day07.calculateTotalWinnings(
        Day07.parseHandsAndBids(
          """
          32T3K 765
          T55J5 684
          KK677 28
          KTJJT 220
          QQQJA 483""",
          Part.PART2
        )
      )
    )
      .isEqualTo(5905L);
  }

  @Test
  void itComputesType() {
    assertThat(new Hand(List.of(JOKER, JOKER, JOKER, JOKER, FOUR)).type())
      .isEqualTo(HandType.FIVE_OF_A_KIND);
    assertThat(new Hand(List.of(JOKER, TWO, TWO, THREE, THREE)).type())
      .isEqualTo(HandType.FULL_HOUSE);
    assertThat(new Hand(List.of(JOKER, JOKER, TWO, THREE, THREE)).type())
      .isEqualTo(HandType.FOUR_OF_A_KIND);
    assertThat(new Hand(List.of(JOKER, TWO, THREE, FOUR, FOUR)).type())
      .isEqualTo(HandType.THREE_OF_A_KIND);
    assertThat(new Hand(List.of(JOKER, JOKER, TWO, THREE, FOUR)).type())
      .isEqualTo(HandType.THREE_OF_A_KIND);
    assertThat(new Hand(List.of(JOKER, JOKER, JOKER, TWO, THREE)).type())
      .isEqualTo(HandType.FOUR_OF_A_KIND);
    assertThat(new Hand(List.of(JOKER, TWO, THREE, FOUR, FIVE)).type())
      .isEqualTo(HandType.ONE_PAIR);
  }
}
