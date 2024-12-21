package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*
import java.util.*

fun main() {
    val isSample = false
    val grid = readInput(2024, 20, isSample).readLines()
    var start = Index(0, 0)
    for ((y, line) in grid.withIndex()) for (x in line.indices) if (line[x] == 'S') start = Index(x, y)
    var end = Index(0, 0)
    for ((y, line) in grid.withIndex()) for (x in line.indices) if (line[x] == 'E') end = Index(x, y)
    val minSave = if (isSample) 50 else 100

    part1(grid, start, end, minSave)
    part2(grid, start, end, minSave)
}

private fun part1(grid: List<String>, start: Index, end: Index, minSave: Int) = solve(grid, start, end, minSave, 2)
private fun part2(grid: List<String>, start: Index, end: Index, minSave: Int) = solve(grid, start, end, minSave, 20)

private fun solve(grid: List<String>, start: Index, end: Index, minSave: Int, maxCheat: Int) {
    val path = path(grid, start, end)
    var count = 0
    for (i in 0..<path.lastIndex) for (j in i + 1..path.lastIndex) {
        val distance = path[i].manhattanDistanceTo(path[j])
        val save = j - i - distance
        if (distance <= maxCheat && save >= minSave) count++
    }
    println(count)
}

private fun path(grid: List<String>, start: Index, end: Index) = buildList(grid.size shr 1) {
    val parent = mutableMapOf<Index, Index>()
    val bound = Rect(0..grid[0].lastIndex, 0..grid.lastIndex)
    val pq = PriorityQueue<Path20Alt> { a, b -> a.cost - b.cost }
    pq.offer(Path20Alt(end, 0))
    while (pq.isNotEmpty()) {
        val (to, cost) = pq.poll()
        if (to == start) break
        for (d in Directions.Cardinal) {
            val next = to + d
            if (next in bound && next !in parent && grid[next.y][next.x] != '#') {
                parent.putIfAbsent(next, to)
                pq.offer(Path20Alt(next, cost + 1))
            }
        }
    }
    add(start)
    while (last() != end) add(parent.getValue(last()))
}

private data class Path20Alt(val to: Index, val cost: Int)
