package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2025, 10).readLines().map(String::toModel)
    part1(input)
}

private fun part1(input: List<Day10Model>) {
    fun Day10Model.getMinimumSolution(state: MutableList<Boolean>, startIndex: Int, count: Int): Int {
        var min = Int.MAX_VALUE
        for (i in startIndex..buttons.lastIndex) {
            val button = buttons[i]
            for (index in button) state[index] = !state[index]
            val result = if (state == goal) count + 1 else getMinimumSolution(state, i + 1, count + 1)
            min = minOf(min, result)
            for (index in button) state[index] = !state[index]
        }
        return min
    }

    val result = input.sumOf { it.getMinimumSolution(MutableList(it.goal.size) { false }, 0, 0) }
    println(result)
}

private data class Day10Model(val goal: List<Boolean>, val buttons: List<List<Int>>, val joltages: List<Int>)

private fun String.toModel(): Day10Model {
    val parts = split(" ")
    val goal = parts[0].substring(1..<parts[0].lastIndex).map { it == '#' }
    val buttons = parts.subList(1, parts.lastIndex).map { it.trim('(', ')').split(",").map(String::toInt) }
    val joltages = parts.last().trim('{', '}').split(",").map(String::toInt)
    return Day10Model(goal, buttons, joltages)
}
