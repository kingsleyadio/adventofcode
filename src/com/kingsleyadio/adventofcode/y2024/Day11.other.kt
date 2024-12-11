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
    fun MutableMap<Long, Long>.addValue(k: Long, v: Long) = put(k, getOrDefault(k, 0) + v)
    var map = input.associateBy(String::toLong) { 1L }
    for (i in 1..rounds) map = buildMap {
        map.entries.forEach { (k, v) ->
            val ks = "$k"
            when {
                k == 0L -> addValue(1, v)
                ks.length % 2 == 0 -> ks.chunked(ks.length / 2).forEach { addValue(it.toLong(), v) }
                else -> addValue(k * 2024, v)
            }
        }
    }
    println(map.values.sum())
}
