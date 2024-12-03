package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 3).readText()
    part1(input)
    part2(input)
}

private fun part1(input: String) {
    val result = mul.findAll(input).map { it.groupValues }.sumOf { (_, x, y) -> x.toInt() * y.toInt() }
    println(result)
}

private fun part2(input: String) {
    val pattern = "$mul|$on|$off".toRegex()
    var sum = 0
    var enabled = true
    pattern.findAll(input).forEach { match ->
        when (val m = match.groupValues[0].substringBefore("(")) {
            "mul" -> if (enabled) sum += match.groupValues.slice(1..2).fold(1) { acc, n -> acc * n.toInt() }
            else -> enabled = m == "do"
        }
    }
    println(sum)
}

private val mul = "mul\\((\\d+),(\\d+)\\)".toRegex()
private val on = "do\\(\\)".toRegex()
private val off = "don\'t\\(\\)".toRegex()
