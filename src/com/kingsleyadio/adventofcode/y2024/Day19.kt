package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2024, 19).readLines()
    val patterns = input[0].split(", ")
    val designs = input.drop(2)
    solve(patterns, designs)
}

private fun solve(patterns: List<String>, designs: List<String>) {
    val cache = hashMapOf("" to 1L)
    fun options(d: String): Long = cache.getOrPut(d) {
        patterns.sumOf { if (d.startsWith(it)) options(d.substring(it.length)) else 0L }
    }

    val part1 = designs.count { options(it) > 0 }
    val part2 = designs.sumOf { options(it) }
    println(part1)
    println(part2)
}
