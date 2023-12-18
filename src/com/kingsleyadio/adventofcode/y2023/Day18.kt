package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput
import kotlin.math.absoluteValue

fun main() {
    val input = readInput(2023, 18).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) {
    evaluate(input) { entry ->
        val (direction, value) = entry.split(" ")
        direction[0] to value.toInt()
    }
}

private fun part2(input: List<String>) {
    val directions = charArrayOf('R', 'D', 'L', 'U')
    evaluate(input) { entry ->
        val encoded = entry.substringAfterLast("#").dropLast(1)
        val value = encoded.dropLast(1).toInt(16)
        directions[encoded.last().digitToInt()] to value
    }
}

private inline fun evaluate(input: List<String>, splitter: (String) -> Pair<Char, Int>) {
    val moves = arrayListOf<Int>()
    val coordinates = buildList<Index> {
        add(Index(0, 0))
        input.forEach { entry ->
            val previous = last()
            val (direction, value) = splitter(entry)
            val next = when (direction) {
                'R' -> Index(previous.x + value, previous.y)
                'D' -> Index(previous.x, previous.y + value)
                'L' -> Index(previous.x - value, previous.y)
                'U' -> Index(previous.x, previous.y - value)
                else -> error("Invalid direction")
            }
            add(next)
            moves.add(value)
        }
    }

    val a = coordinates.zipWithNext { a, b -> (b.x - a.x) * a.y.toLong() }.sum().absoluteValue
    val p = moves.sumOf { it.toLong() }
    println(a + p / 2 + 1)
}
