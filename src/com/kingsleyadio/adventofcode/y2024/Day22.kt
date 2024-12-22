package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2024, 22).readLines().map(String::toInt)
    part1(input)
    part2(input)
}

private fun part1(input: List<Int>) {
    val result = input.sumOf { calculateSecret(it.toLong(), 2000) }
    println(result)
}

private fun part2(input: List<Int>) {
    val sequences = input.map(::viableSaleSequences)
    val visited = hashSetOf<List<Int>>()
    var max = Int.MIN_VALUE
    for (sequence in sequences) for ((k, _) in sequence) if (visited.add(k)) {
        val sum = sequences.sumOf { it.getOrDefault(k, 0) }
        max = maxOf(max, sum)
    }
    println(max)
}

private fun viableSaleSequences(secret: Int, rounds: Int = 2000): Map<List<Int>, Int> {
    val list = mutableListOf(secret % 10)
    var current = secret.toLong()
    for (i in 1..rounds) {
        current = calculateSecret(current, 1)
        list.add((current % 10L).toInt())
    }
    return buildMap {
        val diffs = list.zipWithNext { a, b -> b - a }
        for ((i, d) in diffs.withIndex()) if (i >= 3 && d > 0) putIfAbsent(diffs.slice(i - 3..i), list[i + 1])
    }
}

private fun calculateSecret(start: Long, rounds: Int): Long {
    var current = start
    repeat(rounds) {
        current = (current xor (current shl 6)).mod(16777216L)
        current = (current xor (current shr 5)).mod(16777216L)
        current = (current xor (current shl 11)).mod(16777216L)
    }
    return current
}
