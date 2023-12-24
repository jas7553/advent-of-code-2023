package day06

import java.io.File
import java.math.BigInteger

data class Race(val time: Int, val record: Int)
data class Option(val holdMillis: Int, val distance: Int)
data class BigRace(val time: BigInteger, val record: BigInteger)

fun partOne(): Int {
    val lines = File("input/input-06.txt")
        .readLines()

    val times = parseNumbers(lines[0].split(":")[1])
    val distances = parseNumbers(lines[1].split(":")[1])

    return (times zip distances)
        .map { p -> Race(p.first, p.second) }
        .map { race: Race ->
            getOptions(race)
                .filter { option: Option -> option.distance > race.record }
                .size
        }
        .reduce(Int::times)
}

fun parseNumbers(input: String): List<Int> =
    input.trim()
        .split("\\s+".toRegex())
        .map { s -> s.toInt() }

fun parseAndJoinNumbers(input: String): BigInteger =
    input.trim()
        .split("\\s+".toRegex())
        .joinToString("")
        .toBigInteger()

fun getOptions(race: Race): List<Option> =
    (0..race.time)
        .map { holdTime ->
            Option(holdMillis = holdTime, distance = (race.time - holdTime) * holdTime)
        }

fun countOptions(race: BigRace): Int {
    var holdTime = BigInteger.valueOf(0)
    var currentlyWinning = false
    var numberOfOptions = 0

    while (true) {
        val distance = (race.time - holdTime) * holdTime

        when {
            distance < race.record && currentlyWinning -> {
                // finished winning
                break
            }

            distance >= race.record -> {
                if (!currentlyWinning) {
                    // started winning
                    currentlyWinning = true
                }
                numberOfOptions++
            }
        }

        holdTime++
    }

    return numberOfOptions
}

fun partTwo(): Int {
    val lines = File("input/input-06.txt")
        .readLines()

    val time = parseAndJoinNumbers(lines[0].split(":")[1])
    val record = parseAndJoinNumbers(lines[1].split(":")[1])

    val race = BigRace(time = time, record = record)

    return countOptions(race)
}

fun main() {
    println("Part one: ${partOne()}")
    println("Part two: ${partTwo()}")
}
