package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val input = readInput(2024, 23).readLines()
    val map = buildMap<String, MutableSet<String>> {
        input.map { it.split("-") }.forEach { (a, b) ->
            getOrPut(a) { mutableSetOf() }.add(b)
            getOrPut(b) { mutableSetOf() }.add(a)
        }
    }
    part1(map)
    part2(map)
}

private fun part1(map: Map<String, Set<String>>) {
    var ts = 0
    for ((k, vv) in map) {
        val v = vv.toList()
        for (i in 0..<v.lastIndex) for (j in i + 1..v.lastIndex) {
            if (v[i] in map.getValue(v[j]) && listOf(k, v[i], v[j]).any { it.startsWith('t') }) ts++
        }
    }
    println(ts.div(3))
}

private fun part2(map: Map<String, Set<String>>) {
    var group = emptySet<String>()
    for ((k, vv) in map) {
        val intersect = vv.fold(vv + k) { acc, n ->
            val next = acc.intersect(map.getValue(n) + n)
            if (next.size > 2) next else acc
        }
        if (intersect.size > group.size) group = intersect
    }
    println(group.sorted().joinToString(","))
}
