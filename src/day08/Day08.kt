package day08

import java.io.File

sealed interface Move {
    data object Left : Move
    data object Right : Move
}

data class Node(val label: String)

data class DesertMap(val label: String, val elements: Pair<Node, Node>)

fun leftRightGenerator(input: String): Iterable<Move> = Iterable {
    var i = 0
    iterator {
        while (true) {
            yield(if (input[i] == 'L') Move.Left else Move.Right)
            i = (i + 1) % input.length
        }
    }
}

fun partOne(): Int {
    val lines = File("input/input-08.txt")
        .readLines()

    val instructions = leftRightGenerator(lines.first())
        .iterator()

    val desertMaps = parseDesertMaps(lines.drop(2))

    var currentNode = "AAA"
    var stepCount = 0
    while (currentNode != "ZZZ") {
        stepCount++
        val currentMap = desertMaps[currentNode]!!
        currentNode = when (instructions.next()) {
            Move.Left -> currentMap.first.label
            Move.Right -> currentMap.second.label
        }
    }

    return stepCount
}

fun parseDesertMaps(lines: List<String>): Map<String, Pair<Node, Node>> {
    return lines
        .map { line ->
            val labelAndNodePair = line.split("=").map { it.trim() }
            val label = labelAndNodePair[0]
            val nodePair = labelAndNodePair[1]
                .trim('(', ')')
                .split(", ")
            val leftNode = Node(nodePair[0])
            val rightNode = Node(nodePair[1])
            DesertMap(
                label = label,
                elements = Pair(leftNode, rightNode)
            )
        }
        .associate { desertMap ->
            desertMap.label to desertMap.elements
        }
}

fun main() {
    println("Part one: ${partOne()}")
}

