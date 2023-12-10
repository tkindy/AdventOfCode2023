package com.tylerkindy.adventofcode2023.day07;

import static com.tylerkindy.adventofcode2023.day07.Day07.Card.ACE;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.FIVE;
import static com.tylerkindy.adventofcode2023.day07.Day07.Card.JACK;
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
        QQQJA 483"""
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
}
