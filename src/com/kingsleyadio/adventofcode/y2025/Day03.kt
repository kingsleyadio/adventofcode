package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2025, 3).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) = solve(input, 2)

private fun part2(input: List<String>) = solve(input, 12)

private fun solve(input: List<String>, batteryCount: Int) {
    fun maxWithSpace(s: String, space: Int, minIndex: IntArray): Int {
        var max = -1
        var maxIndex = -1
        for (i in minIndex[0]..s.lastIndex) {
            val value = s[i].digitToInt()
            if (value > max && s.lastIndex - i >= space) {
                max = value
                maxIndex = i
            }
        }
        require(maxIndex >= 0)
        minIndex[0] = maxIndex + 1
        return max
    }

    val sum = input.sumOf { line ->
        val minIndex = intArrayOf(0)
        (1..batteryCount).fold(0L) { sum, n -> sum * 10 + maxWithSpace(line, batteryCount - n, minIndex) }
    }
    println(sum)
}
