package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val grids = readInput(2023, 13).useLines { sequence ->
        val lines = sequence.iterator()
        buildList<List<String>> {
            var current = arrayListOf<String>()
            while (lines.hasNext()) {
                val line = lines.next()
                if (line.isEmpty()) {
                    add(current)
                    current = arrayListOf()
                } else current.add(line)
            }
            add(current)
        }
    }
    part1(grids)
    part2(grids)
}

private fun part1(grids: List<List<String>>) {
    val sum = grids.sumOf { findMirror(it, 0) }
    println(sum)
}

private fun part2(grids: List<List<String>>) {
    val sum = grids.sumOf { findMirror(it, 1) }
    println(sum)
}

private fun findMirror(grid: List<String>, smudges: Int): Int {
    val horizontal = findHorizontal(grid, smudges)
    return if (horizontal > 0) horizontal else findVertical(grid, smudges)
}

private fun findHorizontal(grid: List<String>, smudges: Int): Int {
    outer@ for (i in 1..grid.lastIndex) {
        var miss = 0
        var diff = 0
        while (diff < minOf(i, grid.size - i)) {
            val top = i - diff - 1
            val bottom = i + diff
            for (x in grid[0].indices) if (grid[top][x] != grid[bottom][x]) miss++
            if (miss > smudges) continue@outer
            diff++
        }
        if (miss == smudges) return i * 100
    }
    return 0
}

private fun findVertical(grid: List<String>, smudges: Int): Int {
    outer@ for (i in 1..grid[0].lastIndex) {
        var diff = 0
        var miss = 0
        while (diff < minOf(i, grid[0].length - i)) {
            val left = i - diff - 1
            val right = i + diff
            for (y in grid.indices) if (grid[y][left] != grid[y][right]) miss++
            if (miss > smudges) continue@outer
            diff++
        }
        if (miss == smudges) return i
    }
    return 0
}
