package day01

import java.io.File

fun partOne(): Int {
    return File("input/input-01.txt")
        .readLines()
        .sumOf { line ->
            val digits = line.filter { it.isDigit() }
            val firstDigit = digits.first().digitToInt()
            val lastDigit = digits.last().digitToInt()
            Integer.valueOf("$firstDigit$lastDigit")
        }
}

fun partTwo(): Int {
    return File("input/input-01.txt")
        .readLines()
        .sumOf { line ->
            val digits = line.indices.mapNotNull { parse(line, position = it) }
            val firstDigit = digits.first()
            val lastDigit = digits.last()
            Integer.valueOf("$firstDigit$lastDigit")
        }
}

val wordToDigit = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)

fun parse(line: String, position: Int): Int? {
    when {
        line[position].isDigit() -> {
            return line[position].digitToInt()
        }
        else -> {
            val substring = line.substring(position)
            return wordToDigit
                .map { if (substring.startsWith(it.key)) it.value else null }
                .filterNotNull()
                .firstOrNull()
        }
    }
}

fun main() {
    println("Part one: ${partOne()}")
    println("Part two: ${partTwo()}")
}
