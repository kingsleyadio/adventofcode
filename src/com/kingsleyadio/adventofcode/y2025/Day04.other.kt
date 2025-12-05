package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2025, 4, false).readLines()
    val papers = getPapers(input).toMutableSet()
    part1(papers)
    part2(papers)
}

private fun part1(papers: MutableSet<Index>) {
    val result = move(papers, false)
    println(result)
}

private fun part2(papers: MutableSet<Index>) {
    val size = papers.size
    while (move(papers, true) > 0) {
    }
    println(size - papers.size)
}

private fun getPapers(input: List<String>): MutableSet<Index> = hashSetOf<Index>().apply {
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) if (row[x] == '@') add(Index(x, y))
    }
}

private fun move(input: MutableSet<Index>, remove: Boolean): Int {
    var result = 0
    val iterator = input.iterator()
    for (index in iterator) {
        val neighbors = Directions.Compass.map { index + it }.count { it in input }
        if (neighbors < 4) {
            if (remove) iterator.remove()
            result++
        }
    }
    return result
}
