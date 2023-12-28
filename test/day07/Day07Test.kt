package day07

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

internal class Day07Test {
    object Hands {
        val empty = emptyList<Card>()
        val highCard = Card.make("K15Q4")
        val onePair = Card.make("AA954")
        val twoPair = Card.make("KA9K9")
        val fiveOfAKind = Card.make("KKKKK")
        val fourOfAKind = Card.make("2KKKK")
        val threeOfAKind = Card.make("2KKK3")
        val fullHouse = Card.make("1JJ11")
    }

    @Nested
    inner class TypeOfHandTest {
        @Test
        fun `high card`() {
            assertEquals(TypeOfHand.HighCard, TypeOfHand.make(Hands.highCard))
        }

        @Test
        fun `one pair`() {
            assertEquals(TypeOfHand.OnePair, TypeOfHand.make(Hands.onePair))
        }

        @Test
        fun `two pair`() {
            assertEquals(TypeOfHand.TwoPair, TypeOfHand.make(Hands.twoPair))
        }

        @Test
        fun `three of a kind`() {
            assertEquals(TypeOfHand.ThreeOfAKind, TypeOfHand.make(Hands.threeOfAKind))
        }

        @Test
        fun `full house`() {
            assertEquals(TypeOfHand.FullHouse, TypeOfHand.make(Hands.fullHouse))
        }

        @Test
        fun `four of a kind`() {
            assertEquals(TypeOfHand.FourOfAKind, TypeOfHand.make(Hands.fourOfAKind))
        }

        @Test
        fun `five of a kind`() {
            assertEquals(TypeOfHand.FiveOfAKind, TypeOfHand.make(Hands.fiveOfAKind))
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    inner class TypeOfCardsComparatorTest {
        private val typeOfHandComparator = TypeOfHandComparator()

        @Test
        fun `two null hands are equal`() {
            assertEquals(0, typeOfHandComparator.compare(null, null))
        }

        @Test
        fun `first hand null less than second hand non-null`() {
            assertTrue(typeOfHandComparator.compare(null, Hands.empty) < 0)
        }

        @Test
        fun `first hand non-null greater than second hand null`() {
            assertTrue(typeOfHandComparator.compare(Hands.empty, null) > 0)
        }

        @Test
        fun `same empty hands are equal`() {
            assertEquals(0, typeOfHandComparator.compare(Hands.empty, Hands.empty))
        }

        @Test
        fun `high card less than one pair`() {
            assertTrue(typeOfHandComparator.compare(Hands.highCard, Hands.onePair) < 0)
        }

        @Test
        fun `full house greater than three of a kind`() {
            assertTrue(typeOfHandComparator.compare(Hands.fullHouse, Hands.threeOfAKind) > 0)
        }
    }

    @Nested
    inner class JokerTest {
        @Test
        fun `possible hands for hand without joker`() {
            val expectedHands = listOf(Card.make("12345"))
            val possibleHands = possibleHands(Card.make("12345"))
            assertEquals(expectedHands, possibleHands)
        }

        @Test
        fun `hand with just joker`() {
            val expected = "AKQT98765432"
                .map { c -> listOf(Card(c)) }
            val actual = possibleHands(Card.make("J"))
                .map { stripJokers(it) }
            assertEquals(expected, actual)
        }

        @Test
        fun `hand with just two jokers`() {
            val expected = "AKQT98765432"
                .flatMap { first ->
                    "AKQT98765432".map { second ->
                        Card.make("${first}${second}")
                    }
                }
            val actual = possibleHands(Card.make("JJ"))
                .map { stripJokers(it) }
            assertEquals(expected.size, actual.size)
        }

        @Test
        fun `hand with one joker and one other`() {
            val expected = "AKQT98765432"
                .map { Card.make("${it}3") }
            val actual = possibleHands(Card.make("J3"))
                .map { stripJokers(it) }
            assertEquals(expected.size, actual.size)
        }

        @Test
        fun `hand with several cards and one joker in the middle`() {
            val expectedHands = "AKQT98765432"
                .map { c -> Card.make("QQQ${c}A") }
            val possibleHands = possibleHands(Card.make("QQQJA"))
                .map { stripJokers(it) }
            assertEquals(expectedHands, possibleHands)
        }

        @Test
        fun `hand with several cards and two jokers in the middle`() {
            val expected = "AKQT98765432"
                .flatMap { first ->
                    "AKQT98765432".map { second ->
                        Card.make("KT${first}${second}T")
                    }
                }
            val possibleHands = possibleHands(Card.make("KTJJT"))
                .map { stripJokers(it) }
            assertEquals(expected, possibleHands)
        }
    }

    private fun stripJokers(cards: List<Card>): List<Card> {
        return cards.map { Card(it.label) }
    }
}
