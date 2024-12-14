package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2024, 8, false).readLines()
    val boundary = Rect(0..input[0].lastIndex, 0..input.lastIndex)
    val frequencies = hashMapOf<Char, MutableList<Index>>()
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) if (row[x] != '.') frequencies.getOrPut(row[x]) { mutableListOf() }.add(Index(x, y))
    }
    part1(frequencies, boundary)
    part2(frequencies, boundary)
}

private fun part1(frequencies: Map<Char, List<Index>>, boundary: Rect) = solve(frequencies) { a, b ->
    val diff = a - b
    for (antinode in arrayOf(a + diff, b - diff)) if (antinode in boundary) add(antinode)
}

private fun part2(frequencies: Map<Char, List<Index>>, boundary: Rect) = solve(frequencies) { a, b ->
    val diff = a - b
    val antinodes = arrayOf(a - diff, b + diff) // Invert direction from p1 to ensure a & b are also captured
    while (antinodes[0] in boundary) { add(antinodes[0]); antinodes[0] -= diff }
    while (antinodes[1] in boundary) { add(antinodes[1]); antinodes[1] += diff }
}

private inline fun solve(frequencies: Map<Char, List<Index>>, find: MutableSet<Index>.(a: Index, b: Index) -> Unit) {
    val antinodes = hashSetOf<Index>()
    for ((_, v) in frequencies) {
        if (v.size < 2) continue
        for (i in 0..<v.lastIndex) for (j in i + 1..v.lastIndex) antinodes.find(v[i], v[j])
    }
    println(antinodes.size)
}
