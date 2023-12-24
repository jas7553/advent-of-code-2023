package day03

import java.io.File

private typealias Board = List<List<Space>>

private sealed class Space {
    data class Symbol(val c: Char) : Space()
    data class PartNumberPiece(val n: Int) : Space()
    data object Empty : Space()
}

private enum class Direction {
    UP, LEFT, RIGHT, DOWN
}

private fun partOne(): Int {
    val board: Board = parseBoard(File("input/input-03.txt"))

    return board.indices
        .flatMap { row ->
            board[row].indices
                .flatMap { col ->
                    when (board[row][col]) {
                        Space.Empty -> emptyList()
                        is Space.PartNumberPiece -> emptyList()
                        is Space.Symbol -> readNumbersAdjacentToSpace(board, Pair(row, col))
                    }
                }
        }
        .sum()
}

private fun parseBoard(file: File): Board = file
    .readLines()
    .map { line ->
        line.map { c ->
            when {
                '.' == c -> Space.Empty
                c.isDigit() -> Space.PartNumberPiece(c.digitToInt())
                else -> Space.Symbol(c)
            }
        }
    }

private fun readNumbersAdjacentToSpace(board: Board, location: Pair<Int, Int>): List<Int> {
    val above = readNumbersAboveSpace(board, location)
    val below = readNumbersBelowSpace(board, location)
    val leftAndRight = listOfNotNull(
        readNumber(board, Pair(location.first, location.second - 1), Direction.LEFT),
        readNumber(board, Pair(location.first, location.second + 1), Direction.RIGHT),
    )

    return above + below + leftAndRight
}

private fun readNumbersAboveSpace(board: Board, location: Pair<Int, Int>): List<Int> {
    if (location.first == 0) {
        return emptyList()
    }

    val rowAbove = location.first - 1

    return when (board[rowAbove][location.second]) {
        Space.Empty, is Space.Symbol -> {
            listOfNotNull(
                readNumber(board, Pair(rowAbove, location.second - 1), Direction.LEFT),
                readNumber(board, Pair(rowAbove, location.second + 1), Direction.RIGHT),
            )
        }

        is Space.PartNumberPiece -> {
            val leftHalfOfNumber = readNumberAsString(board, Pair(rowAbove, location.second), Direction.LEFT)
            val rightHalfOfNumber = readNumberAsString(board, Pair(rowAbove, location.second + 1), Direction.RIGHT)
            listOf("${leftHalfOfNumber ?: ""}${rightHalfOfNumber ?: ""}".toInt())
        }
    }
}

private fun readNumbersBelowSpace(board: Board, location: Pair<Int, Int>): List<Int> {
    if (location.first == board.size - 1) {
        return emptyList()
    }

    val rowBelow = location.first + 1

    return when (board[rowBelow][location.second]) {
        Space.Empty, is Space.Symbol -> {
            listOfNotNull(
                readNumber(board, Pair(rowBelow, location.second - 1), Direction.LEFT),
                readNumber(board, Pair(rowBelow, location.second + 1), Direction.RIGHT),
            )
        }

        is Space.PartNumberPiece -> {
            val leftHalfOfNumber = readNumberAsString(board, Pair(rowBelow, location.second), Direction.LEFT)
            val rightHalfOfNumber = readNumberAsString(board, Pair(rowBelow, location.second + 1), Direction.RIGHT)
            listOf("${leftHalfOfNumber ?: ""}${rightHalfOfNumber ?: ""}".toInt())
        }
    }
}

private fun readNumber(
    board: List<List<Space>>,
    location: Pair<Int, Int>,
    direction: Direction,
): Int? {
    return readNumberAsString(board, location, direction)?.toInt()
}

private fun readNumberAsString(
    board: List<List<Space>>,
    location: Pair<Int, Int>,
    direction: Direction,
): String? {
    if (location.first < 0 || location.first >= board.size) {
        return null
    }

    if (location.second < 0 || location.second >= board[location.first].size) {
        return null
    }

    return when (val space = board[location.first][location.second]) {
        Space.Empty -> null
        is Space.Symbol -> null
        is Space.PartNumberPiece -> {
            val number = readNumberAsString(board, nextLocation(location, direction), direction)
            return when (direction) {
                Direction.UP, Direction.LEFT -> "${(number ?: "")}${space.n}"
                Direction.RIGHT, Direction.DOWN -> "${space.n}${(number ?: "")}"
            }
        }
    }
}

private fun nextLocation(location: Pair<Int, Int>, direction: Direction): Pair<Int, Int> {
    return when (direction) {
        Direction.UP -> Pair(location.first - 1, location.second)
        Direction.LEFT -> Pair(location.first, location.second - 1)
        Direction.RIGHT -> Pair(location.first, location.second + 1)
        Direction.DOWN -> Pair(location.first + 1, location.second)
    }
}

private fun partTwo(): Int {
    val board: Board = parseBoard(File("input/input-03.txt"))

    return board.indices
        .flatMap { row ->
            board[row].indices
                .map { col ->
                    when (val space = board[row][col]) {
                        Space.Empty -> null
                        is Space.PartNumberPiece -> null
                        is Space.Symbol -> {
                            when (space.c) {
                                '*' -> {
                                    handlePotentialGear(board, Pair(row, col))
                                }

                                else -> null
                            }

                        }
                    }
                }
        }
        .filterNotNull()
        .sum()
}

private fun handlePotentialGear(board: Board, location: Pair<Int, Int>): Int {
    val adjacentPartNumbers = readNumbersAdjacentToSpace(board, location)
    return when (adjacentPartNumbers.size) {
        2 -> adjacentPartNumbers[0] * adjacentPartNumbers[1]
        else -> 0
    }
}

fun main() {
    println("Part one: ${partOne()}")
    println("Part two: ${partTwo()}")
}
