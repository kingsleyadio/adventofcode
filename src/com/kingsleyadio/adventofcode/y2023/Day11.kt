package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput
import java.util.*

fun main() {
    part1()
    part2()
}

private fun part1() = fastEvaluate(2)

private fun part2() = fastEvaluate(1_000_000)

private fun fastEvaluate(expansion: Int) {
    val (world, planets) = buildModel(expansion)
    var sumOfDistances = 0L
    for (i in planets.indices) {
        val planet = planets[i]
        for (k in i + 1..planets.lastIndex) sumOfDistances += planet.fastDistanceTo(planets[k], world)
    }
    println(sumOfDistances)
}

private fun Index.fastDistanceTo(to: Index, world: List<IntArray>): Int {
    val xDistance = (minOf(x, to.x) + 1..maxOf(x, to.x)).sumOf { xi -> world[minOf(y, to.y)][xi].coerceAtLeast(1) }
    val yDistance = (minOf(y, to.y) + 1..maxOf(y, to.y)).sumOf { yi -> world[yi][minOf(x, to.x)].coerceAtLeast(1) }
    return xDistance + yDistance
}

private fun evaluate(expansion: Int) {
    val (world, planets) = buildModel(expansion)
    var sumOfDistances = 0L
    for (i in planets.indices) {
        val planet = planets[i]
        val paths = shortestPath(world, planet)
        for (k in i + 1..planets.lastIndex) {
            val second = planets[k]
            sumOfDistances += paths[second.y][second.x]
        }
    }
    println(sumOfDistances)
}

private fun shortestPath(world: List<IntArray>, start: Index): List<IntArray> {
    val plane = List(world.size) { IntArray(world.size) { Int.MAX_VALUE } }
    val queue = PriorityQueue<Path> { a, b -> a.cost - b.cost }
    queue.offer(Path(start, 0))

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val (to, cost) = current
        if (plane[to.y][to.x] != Int.MAX_VALUE) continue
        plane[to.y][to.x] = cost
        sequence {
            for (y in -1..1) for (x in -1..1) {
                if (y != 0 && x != 0) continue
                val point = Index(to.x + x, to.y + y)
                val cell = plane.getOrNull(point.y)?.getOrNull(point.x) ?: continue
                if (cell == Int.MAX_VALUE) yield(point)
            }
        }.forEach { point ->
            val move = world[point.y][point.x].coerceAtLeast(1)
            queue.add(Path(point, cost + move))
        }
    }
    return plane
}

private fun buildModel(expansion: Int): Pair<List<IntArray>, List<Index>> {
    return readInput(2023, 11, false).useLines { sequence ->
        val world = arrayListOf<IntArray>()
        for (line in sequence) {
            val values = IntArray(line.length)
            var space = 0
            for (i in line.indices) {
                val char = line[i]
                val value = if (char == '#') 0 else 1
                values[i] = value
                if (value > 0) space++
            }
            if (space == values.size) for (i in values.indices) values[i] += expansion - 1
            world.add(values)
        }
        for (i in world[0].indices) {
            val allSpaces = world.all { it[i] > 0 }
            if (allSpaces) for (j in world.indices) world[j][i] += expansion - 1
        }
        val planetIndices = arrayListOf<Index>()
        for (j in world.indices) {
            val row = world[j]
            for (i in row.indices) {
                val value = row[i]
                if (value == 0) planetIndices.add(Index(i, j))
            }
        }
        world to planetIndices
    }
}
