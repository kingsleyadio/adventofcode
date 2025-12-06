package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2025, 6).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) = solve(input) { range ->
    LongArray(input.size - 1) { input[it].substring(range).trim().toLong() }
}

private fun part2(input: List<String>) = solve(input) { range ->
    val result = LongArray(range.size)
    for (row in 0..<input.lastIndex) for (col in range) {
        val char = input[row][col]
        val rIndex = col - range.first
        if (char != ' ') result[rIndex] = result[rIndex] * 10 + char.digitToInt()
    }
    result
}

private inline fun solve(input: List<String>, extractNumbers: (IntRange) -> LongArray) {
    val signs = input.last()
    var sum = 0L
    var right = signs.lastIndex
    for (left in signs.lastIndex downTo 0) if (signs[left] != ' ') {
        sum += extractNumbers(left..right).reduce(if (signs[left] == '+') Long::plus else Long::times)
        right = left - 2
    }
    println(sum)
}
