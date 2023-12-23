package day02

import java.io.File

fun partOne(): Int {
    val configuration = mapOf(
        Color.RED to 12,
        Color.GREEN to 13,
        Color.BLUE to 14,
    )

    return File("input/input-02.txt")
        .readLines()
        .sumOf { line ->
            val game = parseLine(line)
            if (isPossible(configuration, game)) game.number else 0
        }
}

enum class Color {
    RED, GREEN, BLUE, UNKNOWN
}

data class Round(val hands: List<Pair<Int, Color>>)

data class Game(val number: Int, val rounds: List<Round>)

fun parseLine(line: String): Game {
    val gameAndResults = line.split(": ")

    return Game(
        number = gameAndResults.first()
            .split(" ")
            .last()
            .toInt(),
        rounds = gameAndResults.last()
            .split("; ")
            .map { setsOfCubes ->
                Round(
                    setsOfCubes.split(", ")
                        .map { setOfCubes ->
                            val cubes = setOfCubes.split(" ")
                            Pair(
                                cubes.first().toInt(),
                                when (cubes.last()) {
                                    "red" -> Color.RED
                                    "green" -> Color.GREEN
                                    "blue" -> Color.BLUE
                                    else -> Color.UNKNOWN
                                }
                            )
                        })
            }

    )
}

fun isPossible(configuration: Map<Color, Int>, game: Game): Boolean {
    val mostMarblesShown = game.rounds
        .fold(emptyMap<Color, Int>()) { acc, round ->
            listOf(Color.RED, Color.GREEN, Color.BLUE)
                .associateWith { color ->
                    val count = round.hands
                        .filter { it.second == color }
                        .maxOfOrNull { it.first }
                        ?: 0
                    acc.getOrDefault(color, 0).coerceAtLeast(count)
                }
        }

    return listOf(Color.RED, Color.GREEN, Color.BLUE)
        .none { color ->
            mostMarblesShown.getOrDefault(color, 0) > configuration.getOrDefault(color, 0)
        }
}

fun main() {
    println("Part one: ${partOne()}")
}
