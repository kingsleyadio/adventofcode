package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val grid = readInput(2023, 16).readLines()
    part1(grid)
    part2(grid)
}

private fun part1(grid: List<String>) {
    val energized = energize(grid, Index(0, 0), Index(1, 0))
    println(energized)
}

private fun part2(grid: List<String>) {
    val top = grid[0].indices.maxOf { x -> energize(grid, Index(x, 0), Index(0, 1)) }
    val left = grid.indices.maxOf { y -> energize(grid, Index(0, y), Index(1, 0)) }
    val bottom = grid[0].indices.maxOf { x -> energize(grid, Index(x, grid.lastIndex), Index(0, -1)) }
    val right = grid.indices.maxOf { y -> energize(grid, Index(grid[0].lastIndex, y), Index(-1, 0)) }
    val max = listOf(top, left, bottom, right).max()
    println(max)
}

private fun energize(grid: List<String>, start: Index, startDirection: Index): Int {
    val queue = ArrayDeque<Pair<Index, Index>>()
    val energized = List(grid.size) { BooleanArray(grid[0].length) }
    val visited = hashSetOf<Pair<Index, Index>>()
    queue.addLast(start to startDirection)
    while (queue.isNotEmpty()) {
        val visitId = queue.removeFirst()
        val (current, direction) = visitId
        if (current.y !in grid.indices || current.x !in grid[0].indices || visitId in visited) continue
        energized[current.y][current.x] = true
        visited.add(visitId)
        
        val cell = grid[current.y][current.x]
        if (cell == '-') {
            if (direction.y == 0) { // horizontal, do nothing special
                queue.addLast(current + direction to direction)
            } else {
                queue.addLast(current + Index(1, 0) to Index(1, 0))
                queue.addLast(current + Index(-1, 0) to Index(-1, 0))
            }
        } else if (cell == '|') {
            if (direction.x == 0) { // vertical, do nothing special
                queue.addLast(current + direction to direction)
            } else {
                queue.addLast(current + Index(0, 1) to Index(0, 1))
                queue.addLast(current + Index(0, -1) to Index(0, -1))
            }
        } else if (cell == '/') {
            val newDirection = if (direction.x == 1) Index(0, -1)
            else if (direction.x == -1) Index(0, 1)
            else if (direction.y == 1) Index(-1, 0)
            else Index(1, 0)
            queue.addLast(current + newDirection to newDirection)
        } else if (cell == '\\') {
            val newDirection = if (direction.x == 1) Index(0, 1)
            else if (direction.x == -1) Index(0, -1)
            else if (direction.y == 1) Index(1, 0)
            else Index(-1, 0)
            queue.addLast(current + newDirection to newDirection)
        } else {
            queue.addLast(current + direction to direction)
        }
    }
    return energized.sumOf { it.count { active -> active } }
}
