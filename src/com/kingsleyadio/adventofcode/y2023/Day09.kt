package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2023, 9).readLines().map { line -> line.split(" ").map(String::toInt) }
    part1(input)
    part2(input)
}

private fun part1(input: List<List<Int>>) = println(input.sumOf(::findNextNumber))

// Could also use findNext(line.reversed())
private fun part2(input: List<List<Int>>) = println(input.sumOf(::findPreviousNumber))

private fun findNextNumber(sequence: List<Int>): Int {
    val diff = sequence.zipWithNext { a, b -> b - a }
    // Could also reduce until sequence is empty
    return if (diff.all { it == 0 }) sequence.last() else sequence.last() + findNextNumber(diff)
}

private fun findPreviousNumber(sequence: List<Int>): Int {
    val diff = sequence.zipWithNext { a, b -> b - a }
    // Could also reduce until sequence is empty
    return if (diff.all { it == 0 }) sequence.first() else sequence.first() - findPreviousNumber(diff)
}
