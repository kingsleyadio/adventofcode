package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val problem = readInput(2023, 3).useLines { lines ->
        val grid = lines.toList()
        val numberIndices = mutableMapOf<Index, NumberInfo>()
        for (y in grid.indices) {
            val row = grid[y]
            for (x in row.indices) {
                val cell = row[x]
                val cellIndex = Index(x, y)
                if (cellIndex in numberIndices || !cell.isDigit()) continue
                var span = 0
                while (row.getOrNull(x + span + 1)?.isDigit() == true) span++
                val numberInfo = NumberInfo(y, x..x + span)
                for (i in numberInfo.xRange) numberIndices[Index(i, y)] = numberInfo
            }
        }
        Problem(grid, numberIndices)
    }
    part1(problem)
    part2(problem)
}

private fun part1(problem: Problem) {
    val validNumbers = mutableSetOf<NumberInfo>()
    val neighbors = intArrayOf(-1, 0, 1)
    for (y in problem.grid.indices) {
        val row = problem.grid[y]
        for (x in row.indices) {
            val cell = row[x]
            if (cell.isSymbol()) {
                for (j in neighbors) for (i in neighbors) {
                    val index = Index(x+i, y+j)
                    problem.numberIndices[index]?.let { validNumbers.add(it) }
                }
            }
        }
    }
    val sum = validNumbers.fold(0) { acc, n -> acc + n.evaluate(problem.grid) }
    println(sum)
}

private fun part2(problem: Problem) {
    var sumOfRatios = 0
    val neighbors = intArrayOf(-1, 0, 1)
    for (y in problem.grid.indices) {
        val row = problem.grid[y]
        for (x in row.indices) {
            val cell = row[x]
            if (cell == '*') {
                val numbers = mutableSetOf<NumberInfo>()
                for (j in neighbors) for (i in neighbors) {
                    val index = Index(x+i, y+j)
                    problem.numberIndices[index]?.let { numbers.add(it) }
                }
                if (numbers.size == 2) {
                    val ratio = numbers.fold(1) { acc, n -> acc * n.evaluate(problem.grid) }
                    sumOfRatios += ratio
                }
            }
        }
    }
    println(sumOfRatios)
}

// Alternative Solution (doesn't conform to part 2)
// Find all symbols and locate all their accessible number indices (single digit numbers only)
// Traverse this set of indices and attempt to recover the actual numbers
// Once a number is found, remove all its indices from the indices set <<- REPEAT
// Calculate the sum of numbers to get your answer

class Problem(val grid: List<String>, val numberIndices: Map<Index, NumberInfo>)
data class NumberInfo(val y: Int, val xRange: IntRange) {
    fun evaluate(grid: List<String>) = grid[y].substring(xRange).toInt()
}

fun Char.isSymbol() = this !in '0'..'9' && this != '.'
