package day07

import java.io.File

data class Hand(val cards: List<Card>, val bid: Int)

data class Card(val label: Char, val isJoker: Boolean = false) {
    companion object {
        fun make(values: String): List<Card> {
            return values.map { make(it) }
        }

        private fun make(value: Char): Card {
            return Card(value)
        }
    }

    val value: Int by lazy {
        when {
            label.isDigit() -> label.digitToInt()
            'A' == label -> 14
            'K' == label -> 13
            'Q' == label -> 12
            'J' == label -> 11
            'T' == label -> 10
            else -> -1
        }
    }

    override fun toString(): String {
        return "$label${if (isJoker) "(J)" else ""}"
    }
}

enum class TypeOfHand {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind;

    companion object {
        fun make(cards: List<Card>): TypeOfHand {
            val groupedCards = cards.groupBy { it.value }
            return when (groupedCards.size) {
                1 -> FiveOfAKind
                2 -> {
                    when {
                        groupedCards.values.any { it.size == 4 } -> FourOfAKind
                        else -> FullHouse
                    }
                }

                3 -> {
                    when {
                        groupedCards.values.any { it.size == 3 } -> ThreeOfAKind
                        else -> TwoPair
                    }
                }

                4 -> OnePair
                5 -> HighCard
                else -> HighCard
            }
        }
    }
}

class HandComparator : Comparator<Hand> {
    override fun compare(o1: Hand?, o2: Hand?): Int {
        return when {
            o1 == null && o2 == null -> 0
            o1 == null -> -1
            o2 == null -> 1
            else -> CardsComparator()
                .compare(o1.cards, o2.cards)
        }
    }
}

class CardsComparator : Comparator<List<Card>> {
    override fun compare(o1: List<Card>?, o2: List<Card>?): Int {
        return when {
            o1 == null && o2 == null -> 0
            o1 == null -> -1
            o2 == null -> 1
            else -> TypeOfHandComparator()
                .thenComparing(LabelComparator())
                .compare(o1, o2)
        }
    }
}

class TypeOfHandComparator : Comparator<List<Card>> {
    override fun compare(o1: List<Card>?, o2: List<Card>?): Int {
        return when {
            o1 == null && o2 == null -> 0
            o1 == null -> -1
            o2 == null -> 1
            else -> TypeOfHand.make(o1).compareTo(TypeOfHand.make(o2))
        }
    }
}

class LabelComparator : Comparator<List<Card>> {
    override fun compare(o1: List<Card>?, o2: List<Card>?): Int {
        return when {
            o1 == null && o2 == null -> 0
            o1 == null -> -1
            o2 == null -> 1
            else -> {
                val o1Values = o1.map {
                    if (it.isJoker) 1 else it.value
                }
                val o2Values = o2.map {
                    if (it.isJoker) 1 else it.value
                }

                val dropWhile = o1Values.zip(o2Values)
                    .map { it.first.compareTo(it.second) }
                    .dropWhile { it == 0 }
                when {
                    dropWhile.isEmpty() -> 0
                    else -> dropWhile.first()
                }
            }
        }
    }
}

fun partOne(): Int {
    return File("input/input-07.txt")
        .readLines()
        .map { line ->
            val cardsAndBid = line.split(" ")
            Hand(
                cards = Card.make(cardsAndBid[0]),
                bid = cardsAndBid[1].toInt()
            )
        }
        .sortedWith(HandComparator())
        .mapIndexed { index, hand -> hand.bid * (index + 1) }
        .sum()
}

fun partTwo(): Int {
    return File("input/input-07.txt")
        .readLines()
        .map { line ->
            val cardsAndBid = line.split(" ")
            val originalCards = Card.make(cardsAndBid[0])
            val bestHandUsingJokers = bestHand(originalCards)
            Hand(cards = bestHandUsingJokers, bid = cardsAndBid[1].toInt())
        }
        .sortedWith(HandComparator())
        .mapIndexed { index, hand -> hand.bid * (index + 1) }
        .sum()
}

fun bestHand(cards: List<Card>): List<Card> {
    return possibleHands(cards)
        .sortedWith(CardsComparator())
        .reversed()
        .first()
}

fun possibleHands(cards: List<Card>): List<List<Card>> {
    return when {
        cards.isEmpty() -> emptyList()
        cards.size == 1 -> {
            when (cards[0].label) {
                'J' -> "AKQT98765432".map { listOf(Card(label = it, isJoker = true)) }
                else -> listOf(cards)
            }
        }

        else -> when (cards.first().label) {
            'J' -> {
                "AKQT98765432".flatMap { replacementCard ->
                    possibleHands(cards.drop(1))
                        .map { possibleHandWithTail ->
                            listOf(Card(label = replacementCard, isJoker = true)) + possibleHandWithTail
                        }
                }
            }

            else -> {
                possibleHands(cards.drop(1))
                    .map { listOf(cards.first()) + it }
            }
        }
    }
}

fun main() {
    println("Part one: ${partOne()}")
    println("Part two: ${partTwo()}")
}
