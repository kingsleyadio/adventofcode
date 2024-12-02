package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    val input = readInput(2024, 2).readLines().map { it.split(" ").map(String::toInt) }
    part1(input)
    part2(input)
}

private fun part1(input: List<List<Int>>) = println(input.count(::checkSafe))
private fun part2(input: List<List<Int>>) = println(input.count(::checkSafeWithDampener))

private fun checkSafe(levels: List<Int>): Boolean {
    var reference = 0
    for (i in 1..levels.lastIndex) {
        val diff = levels[i] - levels[i - 1]
        if (diff.absoluteValue !in 1..3) return false
        if (reference != 0 && diff.sign != reference) return false
        reference = diff.sign
    }
    return true
}

private fun checkSafeWithDampener(levels: List<Int>): Boolean {
    return levels.indices.any { checkSafe(levels.toMutableList().apply { removeAt(it) }) }
}
