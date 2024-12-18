package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*
import java.util.*

fun main() {
    val isSample = false
    val input = readInput(2024, 18, isSample).readLines().map {
        val (x, y) = N.findAll(it).map { it.value.toInt() }.toList()
        Index(x, y)
    }
    val bounds = if (isSample) Rect(0..6, 0..6) else Rect(0..70, 0..70)
    val fallen = if (isSample) 12 else 1024
    part1(input, bounds, fallen)
    part2(input, bounds, fallen)
    part2linear(input, bounds, fallen)
}

private fun part1(input: List<Index>, bound: Rect, fallen: Int) {
    val result = walk(input.take(fallen).toSet(), bound)
    println(result)
}

private fun part2(input: List<Index>, bound: Rect, fallen: Int) {
    var left = fallen
    var right = input.lastIndex
    while (left <= right) {
        val mid = (left + right) / 2
        val walk = walk(input.take(mid).toSet(), bound)
        if (walk == -1) right = mid - 1
        else left = mid + 1
    }
    println(input[right])
}

private fun part2linear(input: List<Index>, bound: Rect, fallen: Int) {
    for (i in fallen + 1..input.size) {
        if (walk(input.take(i).toSet(), bound) == -1) {
            println(input[i - 1])
            break
        }
    }
}

private fun walk(input: Set<Index>, bound: Rect): Int {
    val start = Index(0, 0)
    val end = Index(bound.x.last, bound.y.last)

    val map = hashMapOf<Index, Int>()
    val pq = PriorityQueue<Path18> { a, b -> a.cost - b.cost }
    pq.offer(Path18(start, 0))
    while (pq.isNotEmpty()) {
        val (to, cost) = pq.poll()
        if (to in map) continue
        map.putIfAbsent(to, cost)
        if (to == end) break
        for (d in Directions.Cardinal) {
            val next = to + d
            if (next in bound && next !in map && next !in input) pq.offer(Path18(next, cost + 1))
        }
    }
    return map.getOrDefault(end, -1)
}

private data class Path18(val to: Index, val cost: Int)

private val N = "\\d+".toRegex()
