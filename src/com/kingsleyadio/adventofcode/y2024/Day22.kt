package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2024, 22).readLines().map(String::toLong)
    part1(input)
    part2(input)
}

private fun part1(input: List<Long>) {
    val result = input.sumOf { calculateSecret(it).elementAt(2000) }
    println(result)
}

private fun part2(input: List<Long>) {
    val map = IntArray(608850)
    for (n in input) for ((k, v) in sequences(n)) map[k] += v
    println(map.max())
}

private fun sequences(secret: Long) = buildMap {
    val list = calculateSecret(secret).take(2000).map { (it % 10).toInt() }.toList()
    val diffs = list.zipWithNext { a, b -> b - a }
    for ((i, d) in diffs.withIndex()) if (i >= 3 && d > 0) putIfAbsent(diffs.subList(i - 3, i + 1).toKey(), list[i + 1])
}

private fun calculateSecret(start: Long) = generateSequence(start) { prev ->
    var next = prev xor (prev shl 6) and 0xffffffL
    next = next xor (next shr 5) and 0xffffffL
    next xor (next shl 11) and 0xffffffL
}

private fun List<Int>.toKey() = (this[0] + 9 shl 15) + (this[1] + 9 shl 10) + (this[2] + 9 shl 5) + (this[3] + 9)
