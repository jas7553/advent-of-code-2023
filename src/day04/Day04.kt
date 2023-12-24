package day04

import java.io.File
import kotlin.math.pow

data class Card(val number: Int, val winningNumbers: Set<Int>, val yourNumbers: Set<Int>)
data class CardWithCopies(val copyCount: Int, val card: Card)

fun partOne(): Int {
    return File("input/input-04.txt")
        .readLines()
        .map { line -> parseLine(line) }
        .sumOf { card ->
            when (val numberOfMatches = getNumberOfMatches(card)) {
                0 -> 0
                else -> 2.0.pow((numberOfMatches - 1).toDouble()).toInt()
            }
        }
}

fun parseLine(line: String): Card {
    val gameAndResults = line.split(": ")

    val gameNumber = gameAndResults[0].split("\\s+".toRegex())[1].toInt()
    val winningNumbersAndYourNumbers = gameAndResults[1].split(" | ").map { s -> s.trim() }

    return Card(
        number = gameNumber,
        winningNumbers = splitNumbers(winningNumbersAndYourNumbers[0]),
        yourNumbers = splitNumbers(winningNumbersAndYourNumbers[1]),
    )
}

fun splitNumbers(input: String): Set<Int> = input.split("\\s+".toRegex()).map { number -> number.toInt() }.toSet()

fun getNumberOfMatches(card: Card) = card.winningNumbers.intersect(card.yourNumbers).count()

fun partTwo(): Int {
    val cards = File("input/input-04.txt")
        .readLines()
        .map { line -> parseLine(line) }
        .map { card -> CardWithCopies(copyCount = 1, card = card) }
        .toMutableList()

    for (cardNumber in cards.indices) {
        val numberOfMatches = getNumberOfMatches(cards[cardNumber].card)
        for (copyCount in 0..<cards[cardNumber].copyCount) {
            for (j in 1..numberOfMatches) {
                val previousCard = cards[cardNumber + j]
                cards[cardNumber + j] = CardWithCopies(previousCard.copyCount + 1, previousCard.card)
            }
        }
    }

    return cards.sumOf { card -> card.copyCount }
}

fun main() {
    println("Part one: ${partOne()}")
    println("Part two: ${partTwo()}")
}
