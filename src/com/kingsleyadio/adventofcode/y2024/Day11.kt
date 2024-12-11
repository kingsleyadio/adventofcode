package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 11, false).readLine().split(" ")
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) = solve(input, 25)
private fun part2(input: List<String>) = solve(input, 75)

private fun solve(input: List<String>, rounds: Int) {
    val cache = hashMapOf<Pair<String, Int>, Long>()
    fun run(stones: List<String>, round: Int): Long {
        return stones.sumOf { stone ->
            val split = buildList {
                when {
                    stone == "0" -> add("1")
                    stone.length % 2 == 0 -> {
                        add(stone.substring(0, stone.length / 2))
                        add(stone.substring(stone.length / 2).toLong().toString())
                    }
                    else -> add((stone.toLong() * 2024).toString())
                }
            }
            cache.getOrPut(stone to round) {
                if (round == rounds) split.size.toLong() else run(split, round + 1)
            }
        }
    }
    val result = run(input, 1)
    println(result)
}
