package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2025, 4, false).readLines().map(String::toCharArray)
    part1(input)
    part2(input)
}

private fun part1(input: List<CharArray>) {
    val result = move(input, false)
    println(result)
}

private fun part2(input: List<CharArray>) {
    var result = 0
    do {
        val intermediate = move(input, true)
        result += intermediate
    } while (intermediate > 0)
    println(result)
}

private fun move(input: List<CharArray>, remove: Boolean): Int {
    fun Index.isPaper() = input.getOrNull(y)?.getOrNull(x) == '@'
    var result = 0
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) if (row[x] == '@') {
            val neighbors = Directions.Compass.map { Index(x, y) + it }.count { it.isPaper() }
            if (neighbors < 4) {
                if (remove) row[x] = '.'
                result++
            }
        }
    }
    return result
}
