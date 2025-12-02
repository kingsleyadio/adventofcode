package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2025, 2).readText()
        .split(",")
        .map { it.substringBefore('-').toLong()..it.substringAfterLast('-').toLong() }
    part1(input)
    part2(input)
}

private fun part1(input: List<LongRange>) = solve(input) { 2 }

private fun part2(input: List<LongRange>) = solve(input, String::length)

private inline fun solve(input: List<LongRange>, maxRepeats: (String) -> Int) {
    val result = input.sumOf { range ->
        range.sumOf { long ->
            val str = long.toString()
            if (str.isInvalid(maxRepeats(str))) long else 0
        }
    }
    println(result)
}

private fun String.isInvalid(maxRepeats: Int): Boolean {
    for (repeats in 2..maxRepeats) {
        if (length % repeats != 0) continue
        if (chunked(length / repeats).toSet().size == 1) return true
    }
    return false
}