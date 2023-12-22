package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2023, 22).useLines { lines ->
        buildList {
            lines.forEachIndexed { index, line ->
                val (start, end) = line.split("~")
                val (sx, sy, sz) = start.split(",").map { it.toInt() }
                val (ex, ey, ez) = end.split(",").map { it.toInt() }
                add(Cube(index + 1, sx..ex, sy..ey, sz..ez))
            }
        }
    }
    val (support, dependency) = simulate(input)
    part1(input, support, dependency)
    part2(support, dependency)
}

private fun part1(input: List<Cube>, support: Relationship, dependency: Relationship) {
    val redundant = support.count { (_, s) -> s.all { dependency.getValue(it).size > 1 } }
    val freeAgents = input.size - support.size + 1
    println(redundant + freeAgents)
}

private fun part2(support: Relationship, dependency: Relationship) {
    val result = (support.keys - 0).sumOf { disintegrate(it, support, dependency) }
    println(result)
}

private fun disintegrate(id: Int, support: Relationship, dependency: Relationship): Int {
    val startDeps = dependency.getValue(id)
    val toDisintegrate = hashSetOf<Int>()
    val queue = ArrayDeque<Set<Int>>()
    queue.addLast(setOf(id))
    toDisintegrate.addAll(startDeps)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst().filter { cId ->
            val unknown = cId !in toDisintegrate
            val canDisintegrate = dependency.getValue(cId).all { it in toDisintegrate }
            unknown && canDisintegrate
        }
        if (current.isEmpty()) continue
        toDisintegrate.addAll(current)
        val next = current.flatMapTo(hashSetOf()) { support.getOrDefault(it, emptySet()) }
        queue.addLast(next)
    }
    return toDisintegrate.size - startDeps.size - 1
}

private fun simulate(input: List<Cube>): Pair<Relationship, Relationship> {
    val upwardSupport = hashMapOf<Int, MutableSet<Int>>()
    val downwardDependency = hashMapOf<Int, MutableSet<Int>>()
    val plane = Array(10) { Array(10) { Marker(0, 0) } }
    input.sortedBy { it.z.first }.forEach { cube ->
        val markers = arrayListOf<Marker>()
        for (y in cube.y) for (x in cube.x) markers.add(plane[y][x])
        val highest = markers.maxBy { it.index }
        val contact = markers.filter { it.index == highest.index }.distinctBy { it.id }
        contact.forEach { marker ->
            upwardSupport.getOrPut(marker.id) { mutableSetOf() }.add(cube.id)
            downwardDependency.getOrPut(cube.id) { mutableSetOf() }.add(marker.id)
        }
        val newZ = highest.index + cube.z.size
        for (y in cube.y) for (x in cube.x) plane[y][x] = Marker(cube.id, newZ)
    }
    return upwardSupport to downwardDependency
}

private typealias Relationship = Map<Int, Set<Int>>

private data class Cube(val id: Int, val x: IntRange, val y: IntRange, val z: IntRange)
private data class Marker(val id: Int, val index: Int)
