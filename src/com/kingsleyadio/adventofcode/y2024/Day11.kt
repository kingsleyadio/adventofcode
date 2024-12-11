package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 11).readLine().split(" ")
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) = solve(input, 25)
private fun part2(input: List<String>) = solve(input, 75)

private fun solve(input: List<String>, rounds: Int) {
    val cache = hashMapOf<Pair<String, Int>, Long>()
    fun run(stones: List<String>, round: Int): Long = stones.sumOf { s ->
        val split = when {
            s == "0" -> listOf("1")
            s.length % 2 == 0 -> listOf(s.substring(0, s.length / 2), s.substring(s.length / 2).toLong().toString())
            else -> listOf((s.toLong() * 2024).toString())
        }
        cache.getOrPut(s to round) { if (round == rounds) split.size.toLong() else run(split, round + 1) }
    }

    val result = run(input, 1)
    println(result)
}
