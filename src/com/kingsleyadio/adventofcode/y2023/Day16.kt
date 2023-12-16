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

private fun energize(grid: List<String>, current: Index, direction: Index): Int {
    val visited = List(grid.size) { BooleanArray(grid[0].length) }
    energize(grid, current, direction, visited, hashSetOf())
    return visited.sumOf { it.count { active -> active } }
}

private fun energize(
    grid: List<String>,
    current: Index,
    direction: Index,
    energized: List<BooleanArray>,
    visited: MutableSet<Pair<Index, Index>>
) {
    val visitId = current to direction
    if (current.y !in grid.indices || current.x !in grid[0].indices || visitId in visited) return
    visited.add(visitId)
    energized[current.y][current.x] = true
    val cell = grid[current.y][current.x]
    if (cell == '-') {
        if (direction.y == 0) { // horizontal, do nothing special
            val next = current + direction
            energize(grid, next, direction, energized, visited)
        } else {
            energize(grid, current + Index(1, 0), Index(1, 0), energized, visited)
            energize(grid, current + Index(-1, 0), Index(-1, 0), energized, visited)
        }
    } else if (cell == '|') {
        if (direction.x == 0) { // vertical, do nothing special
            val next = current + direction
            energize(grid, next, direction, energized, visited)
        } else {
            energize(grid, current + Index(0, 1), Index(0, 1), energized, visited)
            energize(grid, current + Index(0, -1), Index(0, -1), energized, visited)
        }
    } else if (cell == '/') {
        val newDirection = if (direction.x == 1) Index(0, -1)
        else if (direction.x == -1) Index(0, 1)
        else if (direction.y == 1) Index(-1, 0)
        else Index(1, 0)
        val next = current + newDirection
        energize(grid, next, newDirection, energized, visited)
    } else if (cell == '\\') {
        val newDirection = if (direction.x == 1) Index(0, 1)
        else if (direction.x == -1) Index(0, -1)
        else if (direction.y == 1) Index(1, 0)
        else Index(-1, 0)
        val next = current + newDirection
        energize(grid, next, newDirection, energized, visited)
    } else {
        val next = current + direction
        energize(grid, next, direction, energized, visited)
    }
}
