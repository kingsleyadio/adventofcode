package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 10).readLines().map { it.toCharArray().map(Char::digitToInt) }
    part1(input)
    part2(input)
}

private fun part1(input: List<List<Int>>) {
    val trails = hashSetOf<Pair<Index, Index>>()
    solve(input) { a, b -> trails.add(a to b) }
    println(trails.size)
}

private fun part2(input: List<List<Int>>) {
    var result = 0
    solve(input) { _, _ -> result++ }
    println(result)
}

private fun solve(input: List<List<Int>>, onTrail: (Index, Index) -> Unit) {
    val bounds = Rect(0..input[0].lastIndex, 0..input.lastIndex)
    fun walk(start: Index, current: Index = start) {
        val value = input[current.y][current.x]
        if (value == 9) return onTrail(start, current)
        for (direction in Directions.Cardinal) {
            val next = current + direction
            if (next in bounds && input[next.y][next.x] == value + 1) walk(start, next)
        }
    }
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) if (row[x] == 0) walk(Index(x, y))
    }
}
