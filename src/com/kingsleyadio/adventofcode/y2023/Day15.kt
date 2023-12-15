package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2023, 15).readText().trim().split(",")
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) {
    val sum = input.sumOf { hash(it) }
    println(sum)
}

private fun part2(input: List<String>) {
    val boxes = List(256) { LinkedHashMap<String, Int>() }
    input.forEach {
        val (label, next) = it.split('=', '-')
        val hash = hash(label)
        when {
            next.isEmpty() -> boxes[hash].remove(label)
            else -> boxes[hash][label] = next.toInt()
        }
    }
    val sum = boxes.withIndex().sumOf { (index, value) ->
        value.entries.withIndex().sumOf { (id, entry) -> (index + 1) * (id + 1) * entry.value }
    }
    println(sum)
}

private fun hash(input: String): Int {
    var hash = 0
    for (c in input) hash = ((hash + c.code) * 17) % 256
    return hash
}
