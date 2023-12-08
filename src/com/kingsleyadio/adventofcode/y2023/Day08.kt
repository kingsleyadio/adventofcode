package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.lcm
import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val (directions, network) = readInput(2023, 8).useLines { sequence ->
        val lines = sequence.iterator()
        val directions = lines.next()
        lines.next()
        val network = buildMap {
            while (lines.hasNext()) {
                val (src, dest) = lines.next().split(" = ")
                val (left, right) = dest.substring(1..<dest.lastIndex).split(", ")
                put(src, left to right)
            }
        }
        directions to network
    }
    part1(directions, network)
    part2(directions, network)
}

private fun part1(directions: String, network: Network) {
    val moves = network.countMoves(directions, "AAA") { it == "ZZZ" }
    println(moves)
}

private fun part2(directions: String, network: Network) {
    val start = network.keys.filter { it.endsWith('A') }
    val moves = start.map { s -> network.countMoves(directions, s) { it.endsWith('Z')} }
    val result = moves.fold(1L) { acc, move -> lcm(acc, move.toLong()) }
    println(result)
}

private inline fun Network.countMoves(directions: String, start: String, end: (String) -> Boolean): Int {
    var moves = 0
    var directionIndex = 0
    var current = start
    while (!end(current)) {
        val direction = directions[directionIndex]
        val (left, right) = getValue(current)
        current = if (direction == 'L') left else right
        directionIndex = (directionIndex + 1) % directions.length
        moves++
    }
    return moves
}

private typealias Network = Map<String, Pair<String, String>>
