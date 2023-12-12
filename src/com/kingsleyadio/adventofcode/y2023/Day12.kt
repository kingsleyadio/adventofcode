package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2023, 12, false).readLines().map { line ->
        val (spread, compact) = line.split(" ")
        spread to compact.split(",").map { it.toInt() }
    }
    part1(input)
    part2(input)
}

private fun part1(input: List<Pair<String, List<Int>>>) {
    val result = input.sumOf { (spread, compact) -> arrangements(spread, compact) }
    println(result)
}

private fun part2(input: List<Pair<String, List<Int>>>) {
    val result = input.sumOf { (s, c) ->
        val spread = (1..5).joinToString("?") { s }
        val compact = (1..5).flatMap { c }
        arrangements(spread, compact)
    }
    println(result)
}

private fun arrangements(spread: String, compact: List<Int>, cache: MutableMap<String, Long> = hashMapOf()): Long {
    var count = 0L
    for (i in spread.indices) {
        if (i > 0 && spread[i - 1] == '#') return count
        val char = spread[i]
        if (char == '.') continue
        if (compact.isEmpty() && char == '#') return 0
        if (compact.isEmpty()) continue
        val matchSize = compact.first()
        if (i + matchSize > spread.length) continue
        val spreadEquivalent = spread.substring(i, i + matchSize)
        val isMatch = spreadEquivalent.all { it != '.' }
        val hasDelimiter = i + matchSize == spread.length || spread[i + matchSize] != '#'
        if (isMatch) count += when {
            i + matchSize > spread.lastIndex -> arrangements("", compact.drop(1), cache)
            hasDelimiter -> {
                val next = spread.substring(i + matchSize + 1)
                val nextCompact = compact.drop(1)
                val key = "$next-${nextCompact.hashCode()}"
                cache.getOrPut(key) { arrangements(next, nextCompact, cache) }
            }
            else -> 0
        }
    }
    return if (compact.isEmpty()) 1 else count
}
