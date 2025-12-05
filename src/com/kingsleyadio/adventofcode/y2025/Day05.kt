package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val (freshRange, available) = readInput(2025, 5).useLines {
        val iterator = it.iterator()
        val freshRanges = buildList {
            for (line in iterator) {
                if (line.isEmpty()) break
                add(line.substringBefore('-').toLong()..line.substringAfterLast('-').toLong())
            }
        }
        val available = buildList {
            for (line in iterator) add(line.toLong())
        }
        freshRanges to available
    }
    part1(freshRange, available)
    part2(freshRange)
}

private fun part1(freshRange: List<LongRange>, available: List<Long>) {
    val freshCount = available.count { freshRange.any { range -> it in range } }
    println(freshCount)
}

private fun part2(freshRange: List<LongRange>) {
    val sorted = freshRange.sortedBy { it.first }
    val totalFresh = buildList<LongRange> {
        for (range in sorted) {
            val previous = lastOrNull()
            if (previous == null || range.first !in previous) {
                add(range)
            } else {
                removeLast()
                add(previous.first..(maxOf(previous.last, range.last)))
            }
        }
    }.sumOf { it.last - it.first + 1 }
    println(totalFresh)
}