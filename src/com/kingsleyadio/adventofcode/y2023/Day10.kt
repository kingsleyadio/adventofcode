package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput
import kotlin.math.absoluteValue

fun main() {
    val grid = readInput(2023, 10, false).readLines()
    val sIndex = findS(grid)
    val next = findNext(sIndex.x, sIndex.y, grid)
    val path = buildPath(sIndex, next, grid)
    part1(path)
    part2(grid, path)
}

private fun part1(path: List<Index>) {
    println(path.size / 2)
}

private fun part2(grid: List<String>, path: List<Index>) {
    val pathSet = path.toSet()
    var count = 0
    for (j in grid.indices) {
        val row = grid[j]
        for (i in row.indices) {
            val cell = Index(i, j)
            if (cell !in pathSet && cell.isInPath(pathSet, grid)) count++
        }
    }
    println(count)
}

private fun part2alternative(path: List<Index>) {
    val perimeter = path.size
    // Shoelace method: https://en.wikipedia.org/wiki/Shoelace_formula
    val area = path.zip(path.drop(1) + path.take(1)) { a, b -> a.x * b.y - a.y * b.x }.sum().absoluteValue / 2
    // Picks theorem: https://en.wikipedia.org/wiki/Pick's_theorem
    val points = area - perimeter / 2 + 1
    println(points)
}

private fun buildPath(start: Index, next: Index, grid: List<String>): List<Index> {
    return buildList {
        add(start)
        var prev = start
        var cur = next
        while (cur != start) {
            add(cur)
            val forward = when (grid[cur.y][cur.x]) {
                '7' -> if (prev.y == cur.y) Index(cur.x, cur.y + 1) else Index(cur.x - 1, cur.y)
                '|' -> if (prev.y == cur.y-1) Index(cur.x, cur.y + 1) else Index(cur.x, cur.y-1)
                'J'  -> if (prev.y == cur.y-1) Index(cur.x - 1, cur.y) else Index(cur.x, cur.y-1)
                '-' -> if (prev.x == cur.x-1) Index(cur.x + 1, cur.y) else Index(cur.x-1, cur.y)
                'L' -> if (prev.y == cur.y-1) Index(cur.x+1, cur.y) else Index(cur.x, cur.y-1)
                'F' -> if (prev.y == cur.y+1) Index(cur.x + 1, cur.y) else Index(cur.x, cur.y+1)
                else -> error("Invalid path. Should never happen")
            }
            prev = cur
            cur = forward
        }
    }
}

private fun findS(grid: List<String>): Index {
    for (y in grid.indices) {
        val line = grid[y]
        for (x in line.indices) if (line[x] == 'S') return Index(x, y)
    }
    error("S not found")
}

private fun findNext(x: Int, y: Int, grid: List<String>): Index {
    val dx = intArrayOf(0, 1, 0, -1)
    val dy = intArrayOf(-1, 0, 1, 0)
    val dc = arrayOf(
        charArrayOf('7', '|', 'F'),
        charArrayOf('J', '-', '7'),
        charArrayOf('J', '|', 'L'),
        charArrayOf('L', '-', 'F')
    )
    for (i in dx.indices) {
        val index = Index(x + dx[i], y + dy[i])
        val cell = grid.getOrNull(index.y)?.getOrNull(index.x) ?: continue
        if (cell in dc[i]) return index
    }
    error("Next not found")
}

private fun Index.isInPath(path: Set<Index>, grid: List<String>): Boolean {
    // Ray casting: https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
    val hits = (0..<x).count { Index(it, y) in path && grid[y][it] !in setOf('J', 'L', '-') }
    return hits % 2 != 0
}
