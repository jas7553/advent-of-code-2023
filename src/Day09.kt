package day09

import java.io.File

fun partOne(): Int {
    return File("input/input-09.txt")
        .readLines()
        .map { line -> parseLine(line) }
        .sumOf { extrapolate(it) }
}

fun partTwo(): Int {
    return File("input/input-09.txt")
        .readLines()
        .map { line -> parseLine(line) }
        .map { it.reversed() }
        .sumOf { extrapolate(it) }
}

fun parseLine(line: String): List<Int> = line
    .trim()
    .split("\\s+".toRegex())
    .map { it.trim().toInt() }

fun extrapolate(history: List<Int>) = sequencesOfDifferences(history)
    .toList()
    .reversed()
    .reduce { h1, h2 -> listOf(h1.last() + h2.last()) }
    .last()

fun sequencesOfDifferences(inputs: List<Int>): Iterable<List<Int>> = Iterable {
    var nextOutputs = inputs
    iterator {
        while (nextOutputs.any { it != 0 }) {
            yield(nextOutputs)
            nextOutputs = differences(nextOutputs)
        }
        yield(nextOutputs)
    }
}

fun differences(history: List<Int>): List<Int> {
    return (0..history.size-2)
        .map { i -> history[i + 1] - history[i] }
}

fun main() {
    println("Part one: ${partOne()}")
    println("Part two: ${partTwo()}")
}
