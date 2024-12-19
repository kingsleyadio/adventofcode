package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2024, 19).readLines()
    val patterns = input[0].split(", ")
    val designs = input.drop(2)
    part1(patterns, designs)
    part2(patterns, designs)
}

private fun part1(patterns: List<String>, designs: List<String>) {
    val count = designs.count { options(patterns, it) > 0 }
    println(count)
}

private fun part2(patterns: List<String>, designs: List<String>) {
    val count = designs.sumOf { options(patterns, it) }
    println(count)
}

private fun options(patterns: List<String>, design: String, cached: MutableMap<String, Long> = mutableMapOf()): Long {
    if (design.isEmpty()) return 1L
    if (design in cached) return cached.getValue(design)
    var valid = 0L
    for (p in patterns) if (design.startsWith(p)) valid += options(patterns, design.substring(p.length), cached)
    cached[design] = valid
    return valid
}
