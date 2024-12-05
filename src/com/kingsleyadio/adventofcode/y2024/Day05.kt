package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    readInput(2024, 5).useLines {
        val lines = it.iterator()
        val rules = buildMap<Int, MutableList<Int>> {
            for (line in lines) {
                if (line.isEmpty()) break
                val (left, right) = line.split("|").map(String::toInt)
                getOrPut(left) { mutableListOf() }.add(right)
            }
        }
        val updates = buildList {
            for (line in lines) add(line.split(",").map(String::toInt))
        }
        part1(rules, updates)
        part2(rules, updates)
    }
}

private fun part1(rules: Map<Int, List<Int>>, updates: List<List<Int>>) {
    val result = updates.filter { isValidOrder(rules, it) }.sumOf { it[it.size / 2] }
    println(result)
}

private fun part2(rules: Map<Int, List<Int>>, updates: List<List<Int>>) {
    val result = updates.filterNot { isValidOrder(rules, it) }.sumOf {
        val ordered = it.sortedWith { a, b -> if (a in rules[b].orEmpty()) 1 else -1 }
        ordered[ordered.size / 2]
    }
    println(result)
}

private fun isValidOrder(rules: Map<Int, List<Int>>, pages: List<Int>): Boolean {
    for (i in 0..<pages.lastIndex) if (pages[i+1] !in rules[pages[i]].orEmpty()) return false
    return true
}
