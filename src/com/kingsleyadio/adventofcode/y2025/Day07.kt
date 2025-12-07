package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2025, 7).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) {
    val start = Index(input[0].indexOf('S'), 0)
    require(start.x >= 0)
    val splitters = mutableSetOf<Index>()
    val queue = ArrayDeque<Index>().also { it.addLast(start) }
    while(queue.isNotEmpty()) {
        var next = queue.removeFirst()
        while(next.y <= input.lastIndex && input[next.y][next.x] != '^') next += Directions.S
        if (next.y <= input.lastIndex && splitters.add(next)) {
            queue.addLast(next + Directions.SW)
            queue.addLast(next + Directions.SE)
        }
    }
    println(splitters.size)
}

private fun part2(input: List<String>) {
    val cache = hashMapOf<Index, Long>()
    fun countTimelines(current: Index): Long {
        var next = current
        while(next.y <= input.lastIndex && input[next.y][next.x] != '^') next += Directions.S
        return if (next.y <= input.lastIndex) {
            val left = (next + Directions.SW).let { cache.getOrPut(it) { countTimelines(it)} }
            val right = (next + Directions.SE).let { cache.getOrPut(it) { countTimelines(it)} }
            left + right
        } else 1
    }
    val start = Index(input[0].indexOf('S'), 0)
    val timelines = countTimelines(start)
    println(timelines)
}
