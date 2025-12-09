package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*
import kotlin.math.abs

fun main() {
    val input = readInput(2025, 9).readLines().map { it.split(",").map(String::toInt) }
    part1(input)
    part2(input)
}

private fun part1(input: List<List<Int>>) = solve(input) { _, _ -> true }

private fun part2(input: List<List<Int>>) {
    val fence = buildSet {
        fun addSide(index: Index, previous: Index) {
            val xRange = if (index.x < previous.x) index.x..previous.x else previous.x..index.x
            val yRange = if (index.y < previous.y) index.y..previous.y else previous.y..index.y
            add(Rect(xRange, yRange))
        }
        for (i in 1..input.lastIndex) {
            val a = Index(input[i - 1][0], input[i - 1][1])
            val b = Index(input[i][0], input[i][1])
            addSide(a, b)
        }
        addSide(Index(input[0][0], input[0][1]), Index(input.last()[0], input.last()[1]))
    }

    fun intersectsWithFence(a: Index, b: Index): Boolean {
        val xRange = if (a.x < b.x) (a.x + 1)..<b.x else (b.x + 1)..<a.x
        val yRange = if (a.y < b.y) (a.y + 1)..<b.y else (b.y + 1)..<a.y
        for (side in fence) when (side.x.size) {
            1 -> if (side.x.first in xRange && (yRange.first in side.y || yRange.last in side.y)) return true
            else -> if (side.y.first in yRange && (xRange.first in side.x || xRange.last in side.x)) return true
        }
        return false
    }
    solve(input) { a, b -> !intersectsWithFence(a, b) }
}

private inline fun solve(input: List<List<Int>>, isValid: (Index, Index) -> Boolean) {
    var max = 0L
    for (i in 0..<input.lastIndex) for (j in (i + 1)..input.lastIndex) {
        val a = Index(input[i][0], input[i][1])
        val b = Index(input[j][0], input[j][1])
        if (isValid(a, b)) max = maxOf(max, (abs(a.x - b.x) + 1L) * (abs(a.y - b.y) + 1L))
    }
    println(max)
}
