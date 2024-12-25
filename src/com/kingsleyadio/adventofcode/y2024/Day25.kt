package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2024, 25).readLines().windowed(7, 8)
    solve(input)
}

private fun solve(input: List<List<String>>) {
    val locks = mutableListOf<IntArray>()
    val keys = mutableListOf<IntArray>()
    for (grid in input) {
        val id = IntArray(5) { x -> (1..5).count { y -> grid[y][x] == '#' } }
        if (grid[0].all { it == '#' }) locks.add(id) else keys.add(id)
    }
    fun IntArray.fits(other: IntArray) = indices.all { this[it] + other[it] <= 5 }
    val result = locks.sumOf { lock -> keys.count(lock::fits) }
    println(result)
}
