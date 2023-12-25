package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val connections = readInput(2023, 25, false).useLines { lines ->
        buildMap<String, MutableSet<String>> {
            for (line in lines) {
                val (key, other) = line.split(": ")
                val others = other.split(" ")
                getOrPut(key, ::hashSetOf).addAll(others)
                others.forEach { c -> getOrPut(c, ::hashSetOf).add(key) }
            }
        }
    }
    part1(connections)
}

private fun part1(connections: Map<String, Set<String>>) {
    val arrangement = findArrangement(connections)
    val groups = IntArray(2)
    var found = 0
    for ((_, group) in arrangement) {
        if (group.size == 3) found++
        groups[found] += group.size
    }
    println(groups[0] * groups[1])
}

private fun findArrangement(connections: Map<String, Set<String>>): Map<Int, Set<String>> {
    for (start in connections.keys) {
        val queue = ArrayDeque<Pair<String, Int>>()
        val visited = hashSetOf<String>()
        val groups = hashMapOf<Int, MutableSet<String>>()
        queue.addLast(start to 0)
        while (queue.isNotEmpty()) {
            val (current, level) = queue.removeFirst()
            if (visited.add(current)) {
                groups.getOrPut(level) { hashSetOf() }.add(current)
                for (other in connections.getValue(current)) if (other !in visited) queue.add(other to level + 1)
            }
        }
        for ((level, group) in groups) {
            // Condition to find the right arrangement (-_-)
            if (group.size == 3 && level != groups.keys.last()) return groups
        }
    }
    error("Arrangement not found")
}
