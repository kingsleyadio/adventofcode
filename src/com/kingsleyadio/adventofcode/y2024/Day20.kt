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
    val bound = Rect(0..grid[0].lastIndex, 0..grid.lastIndex)
    val fromStart = walk(grid, start)
    val fromEnd = walk(grid, end)
    val normalTime = fromStart.getValue(end)
    var count = 0
    for (y in bound.y) for (x in bound.x) {
        if (grid[y][x] == '#') continue
        val index = Index(x, y)
        for (yy in -maxCheat..maxCheat) for (xx in -maxCheat..maxCheat) {
            val twin = Index(index.x + xx, index.y + yy)
            if (twin !in bound || grid[twin.y][twin.x] == '#') continue
            val distance = index.manhattanDistanceTo(twin)
            if (distance == 0 || distance > maxCheat) continue
            val newTime = fromStart.getValue(index) + distance + fromEnd.getValue(twin)
            if (normalTime - newTime >= minSave) count++
        }
    }
    println(count)
}

private fun walk(grid: List<String>, start: Index): Map<Index, Int> = buildMap {
    val bound = Rect(0..grid[0].lastIndex, 0..grid.lastIndex)
    val pq = PriorityQueue<Path20> { a, b -> a.cost - b.cost }
    pq.offer(Path20(start, 0))
    while (pq.isNotEmpty()) {
        val (to, cost) = pq.poll()
        if (putIfAbsent(to, cost) != null) continue
        for (d in Directions.Cardinal) {
            val next = to + d
            if (next in bound && next !in this && grid[next.y][next.x] != '#') pq.offer(Path20(next, cost + 1))
        }
    }
}

private data class Path20(val to: Index, val cost: Int)
