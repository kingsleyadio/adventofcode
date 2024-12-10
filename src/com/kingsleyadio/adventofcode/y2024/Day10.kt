package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 10, false).readLines().map { it.toCharArray().map(Char::digitToInt) }
    val bounds = Rect(0..input[0].lastIndex, 0..input.lastIndex)
    part1(input, bounds)
    part2(input, bounds)
}

private fun part1(input: List<List<Int>>, bounds: Rect) {
    var result = 0
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) if (row[x] == 0) {
            val unique = hashSetOf<Index>()
            walk(input, bounds, Index(x, y), unique::add)
            result += unique.size
        }
    }
    println(result)
}

private fun part2(input: List<List<Int>>, bounds: Rect) {
    var result = 0
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) if (row[x] == 0) walk(input, bounds, Index(x, y)) { result++ }
    }
    println(result)
}

private fun walk(input: List<List<Int>>, bounds: Rect, current: Index, onFinish: (Index) -> Unit) {
    val value = input[current.y][current.x]
    if (value == 9) return onFinish(current)
    for (i in dx.indices) {
        val next = current + Index(dx[i], dy[i])
        if (next in bounds && input[next.y][next.x] == value + 1) walk(input, bounds, next, onFinish)
    }
}

private val dx = intArrayOf(0, 1, 0, -1)
private val dy = intArrayOf(-1, 0, 1, 0)
