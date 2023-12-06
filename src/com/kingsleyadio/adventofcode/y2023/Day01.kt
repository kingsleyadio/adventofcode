package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    part1()
    part2()
}

private fun part1() {
    var sum = 0
    readInput(2023, 1).forEachLine { line ->
        val first = line.first { it.isDigit() }.digitToInt()
        val last = line.last { it.isDigit() }.digitToInt()
        sum += first * 10 + last
    }
    println(sum)
}

private fun part2() {
    var sum = 0
    readInput(2023, 1).forEachLine { line ->
        val first = findNumber(line, reversed = false)
        val last = findNumber(line, reversed = true)
        sum += first * 10 + last
    }
    println(sum)
}

private val NUMBERS = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
private fun findNumber(line: String, reversed: Boolean): Int {
    val progression = line.indices.run { if (reversed) reversed() else this }
    for (i in progression) {
        val char = line[i]
        if (char.isDigit()) return char.digitToInt()
        NUMBERS.forEachIndexed { index, num -> if (line.startsWith(num, i)) return index }
    }
    error("Not found")
}
