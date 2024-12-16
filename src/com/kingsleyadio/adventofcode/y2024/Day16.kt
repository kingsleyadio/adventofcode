package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*
import java.util.*

fun main() {
    val input = readInput(2024, 16).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) {
    val map = walk(input, trackRoute = false) { false }
    val end = Index(input[0].lastIndex - 1, 1)
    val result = Directions.Cardinal.firstNotNullOf { map[Pos(end, it)] }
    println(result)
}

private fun part2(input: List<String>) {
    val end = Index(input[0].lastIndex - 1, 1)
    var bestCost = Int.MAX_VALUE
    val bestSeats = hashSetOf(end)
    walk(input, trackRoute = true) { (_, cost, previous) ->
        bestCost = minOf(bestCost, cost)
        if (cost <= bestCost) bestSeats.addAll(previous)
        return@walk true
    }
    println(bestSeats.size)
}

private fun walk(input: List<String>, trackRoute: Boolean, onEnd: (Path) -> Boolean): Map<Pos, Int> {
    val start = Index(1, input.lastIndex - 1)
    val end = Index(input[0].lastIndex - 1, 1)

    val pq = PriorityQueue<Path> { a, b -> a.cost - b.cost }
    pq.offer(Path(Pos(start, Directions.E), 0))
    val map = hashMapOf<Pos, Int>()
    while (pq.isNotEmpty()) {
        val path = pq.poll()
        val (pos, cost, previous) = path
        map.putIfAbsent(pos, cost)
        if (pos.index == end) if (onEnd(path)) continue else break
        for (d in Directions.Cardinal) {
            val next = Pos(pos.index + d, d)
            if (next in map || input[next.index.y][next.index.x] == '#') continue
            val potential = if (d == pos.direction) 1 else 1001
            val routeSoFar = if (trackRoute) previous + pos.index else emptyList()
            pq.offer(Path(next, cost + potential, routeSoFar))
        }
    }
    return map
}

private data class Pos(val index: Index, val direction: Index)
private data class Path(val pos: Pos, val cost: Int, val previous: List<Index> = emptyList())
