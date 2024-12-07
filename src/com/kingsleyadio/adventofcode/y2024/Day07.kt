package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = mutableListOf<Equation>()
    readInput(2024, 7).forEachLine {
        val (value, operands) = it.split(": ")
        input.add(Equation(operands.split(" ").map(String::toLong), value.toLong()))
    }
    part1(input)
    part2(input)
}

private fun part1(input: List<Equation>) = solve(input, listOf(Long::plus, Long::times))
private fun part2(input: List<Equation>) = solve(input, listOf(Long::plus, Long::times, CONCAT))

private fun solve(input: List<Equation>, operators: List<Operator>) {
    val result = input.filter { it.canRecover(operators) }.sumOf { it.value }
    println(result)
}

private fun Equation.canRecover(operators: List<Operator>): Boolean {
    fun recover(intermediate: Long, op: Operator, next: Int): Boolean {
        val newIntermediate = op(intermediate, operands[next])
        return if (next == operands.lastIndex) newIntermediate == value
        else if (newIntermediate > value) false
        else operators.any { recover(newIntermediate, it, next + 1) }
    }
    return operators.any { op -> recover(operands[0], op, 1) }
}

private class Equation(val operands: List<Long>, val value: Long)
private typealias Operator = (Long, Long) -> Long

private val CONCAT: Operator = { a, b -> var ten = 1; while (b / ten > 0) ten *= 10; a * ten + b }
