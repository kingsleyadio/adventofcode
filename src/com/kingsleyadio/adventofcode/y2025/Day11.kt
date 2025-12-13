package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = buildMap {
        readInput(2025, 11).forEachLine { line ->
            val (input, out) = line.split(": ")
            put(input, out.split(" "))
        }
    }
    part1(input)
    part2(input)
}

private fun part1(input: Map<String, List<String>>) {
    val visited = mutableSetOf<String>()
    fun navigate(from: String): Int {
        if (from == "out") return 1
        val destinations = input[from] ?: return 0
        visited.add(from)
        val result = destinations.sumOf { navigate(it) }
        visited.remove(from)
        return result
    }
    val result = navigate("you")
    println(result)
}

private fun part2(input: Map<String, List<String>>) {
    val visited = mutableSetOf<String>()
    val cache = mutableMapOf<String, Long>()
    fun navigate(from: String): Long {
        if (from == "out") return if ("fft" in visited && "dac" in visited) 1 else 0
        val key= "$from-${"dac" in visited}-${"fft" in visited}"
        cache[key]?.let { return it }
        val destinations = input[from] ?: return 0
        visited.add(from)
        val result = destinations.filter { it !in visited }.sumOf { navigate(it) }
        visited.remove(from)
        cache[key] = result
        return result
    }
    val result = navigate("svr")
    println(result)
}