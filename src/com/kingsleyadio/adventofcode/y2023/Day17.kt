package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput
import java.util.*

fun main() {
    val input = readInput(2023, 17).readLines()
    part1(input)
    part2(input)
}

private fun part1(grid: List<String>) = println(shortestExit(grid, 1, 3))

private fun part2(grid: List<String>) = println(shortestExit(grid, 4, 10))

private fun shortestExit(grid: List<String>, min: Int, max: Int): Int {
    val start = Index(0, 0)
    val end = Index(grid[0].lastIndex, grid.lastIndex)
    val dx = intArrayOf(1, 0, -1, 0)
    val dy = intArrayOf(0, 1, 0, -1)
    val plane = List(grid.size) { IntArray(grid[0].length) { Int.MAX_VALUE } }
    val queue = PriorityQueue<ClumsyPath> { a, b -> a.cost - b.cost }
    queue.offer(ClumsyPath(start, Index(0, 0), 0, 0))
    val visited = hashSetOf<Triple<Index, Index, Int>>()

    while (queue.isNotEmpty()) {
        val (to, direction, directionCount, cost) = queue.poll()
        plane[to.y][to.x] = cost
        if (to == end) break
        dy.indices.forEach { i ->
            val nextDirection = Index(dx[i], dy[i])
            val multiplier = if (direction == nextDirection) 1 else min
            val next = Index(to.x + dx[i] * multiplier, to.y + dy[i] * multiplier)
            plane.getOrNull(next.y)?.getOrNull(next.x) ?: return@forEach
            if (nextDirection == Index(direction.x * -1, direction.y * -1)) return@forEach
            val newCount = if (direction == nextDirection) directionCount + 1 else multiplier
            if (newCount > max) return@forEach
            if (!visited.add(Triple(next, nextDirection, newCount))) return@forEach
            var moves = '0' - grid[to.y][to.x]
            for (x in minOf(next.x, to.x)..maxOf(next.x, to.x)) for (y in minOf(next.y, to.y)..maxOf(next.y, to.y)) {
                moves += grid[y][x] - '0'
            }
            queue.add(ClumsyPath(next, nextDirection, newCount, cost + moves))
        }
    }
    return plane[end.y][end.x]
}

private data class ClumsyPath(val to: Index, val direction: Index, val directionCounter: Int, val cost: Int)
