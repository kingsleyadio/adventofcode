package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2023, 14).readLines().map { it.toCharArray() }
    part1(input)
    part2(input)
}

private fun part1(input: List<CharArray>) {
    val sum = tiltVertical(input, north = true, modify = false)
    println(sum)
}

private fun part2(input: List<CharArray>) {
    val scoreToSpin = hashMapOf<Pair<Int, Int>, Int>() // (hash, score) => spin
    val spinToScore = hashMapOf<Int, Int>()

    var spin = 0
    while (true) {
        tiltVertical(input, north = true, modify = true)
        tiltHorizontal(input, west = true)
        tiltVertical(input, north = false, modify = true)
        val sum = tiltHorizontal(input, west = false)
        val hash = input.contentHashCode()
        val pair = hash to sum
        if (pair in scoreToSpin) {
            val previousSpin = scoreToSpin.getValue(pair)
            val loop = (1_000_000_000 - spin - 1) % (spin - previousSpin)
            println(spinToScore[loop + previousSpin])
            break
        }
        scoreToSpin[pair] = spin
        spinToScore[spin++] = sum
    }
}

private fun tiltVertical(grid: List<CharArray>, north: Boolean, modify: Boolean): Int {
    val spaces = IntArray(grid[0].size)
    var sum = 0
    val progression = grid.indices.run { if (north) this else reversed() }
    for (j in progression) {
        val row = grid[j]
        for (i in row.indices) when (val cell = row[i]) {
            '#' -> spaces[i] = 0
            '.' -> spaces[i]++
            else -> {
                val dest = if (north) j - spaces[i] else j + spaces[i]
                if (modify && spaces[i] > 0) {
                    grid[dest][i] = cell
                    row[i] = '.'
                }
                sum += grid.size - dest
            }
        }
    }
    return sum
}

private fun tiltHorizontal(grid: List<CharArray>, west: Boolean): Int {
    val spaces = IntArray(grid.size)
    var sum = 0
    val progression = grid[0].indices.run { if (west) this else reversed() }
    for (i in progression) {
        for (j in grid.indices) when (val cell = grid[j][i]) {
            '#' -> spaces[j] = 0
            '.' -> spaces[j]++
            else -> {
                if (spaces[j] > 0) {
                    val dest = if (west) i - spaces[j] else i + spaces[j]
                    grid[j][dest] = cell
                    grid[j][i] = '.'
                }
                sum += grid.size - j
            }
        }
    }
    return sum
}

private fun List<CharArray>.contentHashCode(): Int {
    var hash = 31
    for (c in this) hash += c.contentHashCode() * 31
    return hash
}
