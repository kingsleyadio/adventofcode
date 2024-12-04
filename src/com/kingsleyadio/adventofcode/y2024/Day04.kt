package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 4).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) = solve(input, cross = false)
private fun part2(input: List<String>) = solve(input, cross = true)

private fun solve(input: List<String>, cross: Boolean) {
    var count = 0
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) count += if (cross) countCrossmas(input, x, y) else countXmas(input, x, y)
    }
    // Note: cross-mas variant can be optimized further to avoid the double matching
    val result = if (cross) count.shr(1) else count
    println(result)
}

private fun countXmas(input: List<String>, x: Int, y: Int): Int {
    val xmas = "XMAS"
    val dxa = intArrayOf(0, 1, 1, 1, 0, -1, -1, -1)
    val dya = intArrayOf(-1, -1, 0, 1, 1, 1, 0, -1)
    fun traverse(index: Int, x: Int, y: Int, dx: Int, dy: Int): Boolean {
        if (input[y][x] != xmas[index]) return false
        if (index == xmas.lastIndex) return true
        val newX = x + dx
        val newY = y + dy
        if (newX !in input[0].indices || newY !in input.indices) return false
        return traverse(index + 1, newX, newY, dx, dy)
    }
    return dxa.indices.count { i -> traverse(0, x, y, dxa[i], dya[i]) }
}

private fun countCrossmas(input: List<String>, x: Int, y: Int): Int {
    val mas = "MAS"
    val dxa = intArrayOf(1, 1, -1, -1)
    val dya = intArrayOf(-1, 1, 1, -1)
    fun checkInverse(sx: Int, sy: Int, dx: Int, dy: Int): Boolean {
        val my = sy - 2 * dy
        val cross = buildString { for (i in 0..2) append(input[my + i * dy][sx - i * dx]) }
        return cross == mas || cross == mas.reversed()
    }
    fun traverse(index: Int, x: Int, y: Int, dx: Int, dy: Int): Boolean {
        if (input[y][x] != mas[index]) return false
        if (index == mas.lastIndex) return checkInverse(x, y, dx, dy)
        val newX = x + dx
        val newY = y + dy
        if (newX !in input[0].indices || newY !in input.indices) return false
        return traverse(index + 1, newX, newY, dx, dy)
    }
    return dxa.indices.count { i -> traverse(0, x, y, dxa[i], dya[i]) }
}
