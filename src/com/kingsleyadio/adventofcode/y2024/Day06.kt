package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = mutableListOf<String>()
    var start = Index(0, 0)
    readInput(2024, 6, false).forEachLine {
        input.add(it)
        val s = it.indexOf('^')
        if (s > -1) start = Index(s, input.lastIndex)
    }
    part1(input, start)
    part2(input, start)
}

private fun part1(input: List<String>, start: Index) {
    val visited = patrol(input, start)
    println(visited)
}

private fun part2(input: List<String>, start: Index) {
    // Can be optimized further by using part1's index set instead of iterating all cells
    var count = 0
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) if (row[x] == '.' && patrol(input, start, Index(x, y)) == -1) count++
    }
    println(count)
}

private fun patrol(input: List<String>, start: Index, extraObstacle: Index? = null): Int {
    // Memory can be optimized further by capturing only corner cells
    // though unique visits will then need to be adjusted accordingly
    val visited = hashSetOf<Any>()
    var pos = start
    var directionIndex = 0
    while (pos.y in input.indices && pos.x in input[0].indices) {
        val key: Any = if (extraObstacle == null) pos else pos to directionIndex
        if (!visited.add(key) && extraObstacle != null) return -1
        var newPos = pos + directions[directionIndex]
        while (input.getOrNull(newPos.y)?.getOrNull(newPos.x) == '#' || newPos == extraObstacle) {
            directionIndex = (directionIndex + 1) % directions.size
            newPos = pos + directions[directionIndex]
        }
        pos = newPos
    }
    return visited.size
}

private val directions = listOf(Index(0, -1), Index(1, 0), Index(0, 1), Index(-1, 0))
