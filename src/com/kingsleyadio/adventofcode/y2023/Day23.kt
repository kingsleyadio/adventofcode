package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
    val input = readInput(2023, 23, false).readLines()
    part1(input)
    part2(input)
    measureTime { part1(input) }.also { println(it) }
    measureTime { part2(input) }.also { println(it) }
}

private fun part1(input: List<String>) {
    val network = buildNetwork(input, noSlope = false)
//    val network = buildThickNetwork(input, Index(1, 0), withSlope = true)
    val result = longestPath(Index(1, 0), Index(input[0].lastIndex - 1, input.lastIndex), network)
    println(result)
}

private fun part2(input: List<String>) {
//    val network = buildNetwork(input, noSlope = true)
//    val network = buildNetwork(input.map { it.replace("[>v<^]".toRegex(), ".") }, noSlope = false)
//    val network = buildCuteNetwork(
//        input,
//        Index(1, 0)
//    ).filter { (k, v) -> k == Index(1, 0) || k == Index(input[0].lastIndex - 1, input.lastIndex) || v.size >= 2 }
    val network = buildThickNetwork(input, Index(1, 0), withSlope = false)
//    val result = longestPath(Index(1, 0), Index(input[0].lastIndex - 1, input.lastIndex), network)
    val result = longPathSlow(Index(1, 0), Index(input[0].lastIndex - 1, input.lastIndex), network)
//    val result = longPathCoroutines(Index(1, 0), Index(input[0].lastIndex - 1, input.lastIndex), network)
    println(result)
}

private fun longPathSlow(
    start: Index,
    end: Index,
    network: Map<Index, List<Edge>>
): Int = DeepRecursiveFunction<Pair<Index, MutableSet<Index>>, Int> { (index, visited) ->
    if (index == end) return@DeepRecursiveFunction 0
    val neighbors = network.getValue(index)
    if (index != start && neighbors.size < 2) return@DeepRecursiveFunction Int.MIN_VALUE // dead end
    neighbors
        .filter { it.to !in visited }
        .maxOfOrNull {
            visited.add(it.to)
            val inner = callRecursive(Pair(it.to, visited))
            visited.remove(it.to)
            it.weight + inner
        } ?: Int.MIN_VALUE
}.invoke(Pair(start, mutableSetOf(start)))

private fun buildThickNetwork(input: List<String>, start: Index, withSlope: Boolean): Map<Index, List<Edge>> {
    val dx = intArrayOf(1, 0, -1, 0)
    val dy = intArrayOf(0, 1, 0, -1)
    val network = mutableMapOf<Index, MutableSet<Edge>>()
    val queue = ArrayDeque<Pair<Index, Index>>()
    for (i in dx.indices) queue.addLast(start to Index(dx[i], dy[i]))
    while (queue.isNotEmpty()) {
        val (current, direction) = queue.removeFirst()
        val (next, weight) = findNextJunction(current, direction, input, withSlope)
        if (weight > 0 && network.getOrPut(current) { mutableSetOf() }.add(Edge(next, weight))) {
            for (i in dx.indices) queue.addLast(next to Index(dx[i], dy[i]))
        }
    }
    return network.mapValues { (_, v) -> v.toList() }
}

private val directions = listOf(Index(1, 0), Index(0, 1), Index(-1, 0), Index(0, -1))
private val rdlu = ">v<^"
private fun findNextJunction(current: Index, direction: Index, input: List<String>, withSlope: Boolean): Edge {
    var weight = 0
    var index = current
    var dir = direction
    while (index.x + dir.x in input[0].indices && index.y + dir.y in input.indices) {
        val cell = input[index.y + dir.y][index.x + dir.x]
        if (cell == '#') {
            if (dir.x != 0) {
                val canGoUp = index.y - 1 >= 0 && input[index.y - 1][index.x] != '#'
                val canGoDown = index.y + 1 < input.size && input[index.y + 1][index.x] != '#'
                dir = if (canGoUp && canGoDown) break
                else if (canGoUp) Index(0, -1)
                else if (canGoDown) Index(0, 1)
                else break
            } else {
                val canGoLeft = index.x - 1 >= 0 && input[index.y][index.x - 1] != '#'
                val canGoRight = index.x + 1 < input[0].length && input[index.y][index.x + 1] != '#'
                dir = if (canGoLeft && canGoRight) break
                else if (canGoLeft) Index(-1, 0)
                else if (canGoRight) Index(1, 0)
                else break
            }
            continue
        }
        val multiplier = if (withSlope && rdlu.indexOf(cell) == directions.indexOf(direction)) 2 else 1
        index = Index(index.x + dir.x * multiplier, index.y + dir.y * multiplier)
        weight += multiplier
        if (!Index(index.x + dir.x, index.y + dir.y).isValid(input)) break
        if (input[index.y + dir.y][index.x + dir.x] == '#') continue
        if (dir.x != 0) {
            if (index.y + 1 < input.size && input[index.y + 1][index.x] != '#') break
            if (index.y - 1 >= 0 && input[index.y - 1][index.x] != '#') break
        } else {
            if (index.x + 1 < input[0].length && input[index.y][index.x + 1] != '#') break
            if (index.x - 1 >= 0 && input[index.y][index.x - 1] != '#') break
        }
    }
    return Edge(index, weight)
}

private fun Index.isValid(input: List<String>): Boolean {
    return y in input.indices && x in input[0].indices
}

private fun buildCuteNetwork(input: List<String>, start: Index): Map<Index, List<Edge>> {
    val network = mutableMapOf<Index, MutableSet<Edge>>()
    val queue = ArrayDeque<Index>()
    queue.addLast(start)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        findHorizontalJunction(current, input).forEach { next ->
            val dx = abs(next.x - current.x)
            if (network.getOrPut(current) { mutableSetOf() }.add(Edge(next, dx))) queue.addLast(next)
        }
        findVerticalJunction(current, input).forEach { next ->
            val dy = abs(next.y - current.y)
            if (network.getOrPut(current) { mutableSetOf() }.add(Edge(next, dy))) queue.addLast(next)
        }
    }
    return network.mapValues { (_, v) -> v.toList() }
}

private fun findHorizontalJunction(current: Index, input: List<String>): List<Index> {
    val reaches = arrayListOf<Index>()
    for (direction in intArrayOf(1, -1)) {
        var dx = 0
        while (current.x + dx + direction in 0..input[0].lastIndex) {
            if (input[current.y][current.x + dx + direction] == '#') break
            dx += direction
            val downBranch = current.y + 1 < input.size && input[current.y + 1][current.x + dx] != '#'
            val upBranch = current.y - 1 >= 0 && input[current.y - 1][current.x + dx] != '#'
            if (downBranch || upBranch) break
//            if (dx != 0 && (downBranch || upBranch)) reaches.add(Index(current.x + dx, current.y))
        }
        if (dx != 0) reaches.add(Index(current.x + dx, current.y))
    }
    return reaches
}

private fun findVerticalJunction(current: Index, input: List<String>): List<Index> {
    val reaches = arrayListOf<Index>()
    for (direction in intArrayOf(1, -1)) {
        var dy = 0
        while (current.y + dy + direction in 0..input.lastIndex) {
            if (input[current.y + dy + direction][current.x] == '#') break
            dy += direction
            val rightBranch = current.x + 1 < input[0].length && input[current.y + dy][current.x + 1] != '#'
            val leftBranch = current.x - 1 >= 0 && input[current.y + dy][current.x - 1] != '#'
            if (rightBranch || leftBranch) break
//            if (dy != 0 && (rightBranch || leftBranch)) reaches.add(Index(current.x, current.y + dy))
        }
        if (dy != 0) reaches.add(Index(current.x, current.y + dy))
    }
    return reaches
}

private fun buildNetwork(input: List<String>, noSlope: Boolean): Map<Index, List<Edge>> {
    val network = mutableMapOf<Index, MutableList<Edge>>()
    val slopes = ">v<^"
    val dx = intArrayOf(1, 0)
    val dy = intArrayOf(0, 1)
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) {
            if (row[x] == '#' || slopes.indexOf(row[x]) >= 0) continue
            val index = Index(x, y)
            for (i in dx.indices) {
                val np = Index(index.x + dx[i], index.y + dy[i])
                if (np.y !in input.indices || np.x !in input[0].indices) continue
                if (input[np.y][np.x] == '#') continue
                val forward = np.x > index.x

                val edge = when {
                    input[np.y][np.x] == '.' -> Edge(np, 1)
                    input[np.y][np.x] == '>' && forward -> Edge(Index(np.x + 1, np.y), 2)
                    input[np.y][np.x] == 'v' && !forward -> Edge(Index(np.x, np.y + 1), 2)
                    input[np.y][np.x] == '<' && forward -> Edge(Index(np.x + 1, np.y), -2)
                    input[np.y][np.x] == '^' && !forward -> Edge(Index(np.x, np.y + 1), -2)
                    else -> error("Invalid")
                }
                if (edge.weight == 1) {
                    network.getOrPut(index) { mutableListOf() }.add(edge)
                    network.getOrPut(np) { mutableListOf() }.add(Edge(index, 1))
                } else if (edge.weight > 1) {
                    network.getOrPut(index) { mutableListOf() }.add(edge)
                    if (noSlope) network.getOrPut(edge.to) { mutableListOf() }.add(Edge(index, edge.weight))
                } else {
                    network.getOrPut(edge.to) { mutableListOf() }.add(Edge(index, abs(edge.weight)))
                    if (noSlope) network.getOrPut(index) { mutableListOf() }.add(Edge(edge.to, abs(edge.weight)))
                }
            }
        }
    }
    return network
}

private fun longestPath(s: Index, end: Index, network: Map<Index, List<Edge>>): Int {
    val visited = hashSetOf<Index>()
    val stack = ArrayDeque<Index>()
    val distance = mutableMapOf<Index, Int>()
    val topologicalSort = DeepRecursiveFunction { index ->
        // Mark the current node as visited
        visited.add(index)
        // Recur for all the vertices adjacent to this vertex 
        for (edge in network.getValue(index)) if (edge.to !in visited) callRecursive(edge.to)
        // Push current vertex to stack which stores topological sort 
        stack.addLast(index)
    }
    // Call the recursive helper function to store Topological sort
    // for start and all reachable nodes
    topologicalSort(s)
    // for ((index, _) in network) if (index !in visited) topologicalSort(index)

    // distance to source as 0 
    distance[s] = 0

    // Process vertices in topological order 
    while (stack.isNotEmpty()) {
        // Get the next vertex from topological order
        val u = stack.removeLast()

        // Update distances of all adjacent vertices ; 
        for (edge in network.getValue(u)) {
            if (distance.getOrDefault(edge.to, Int.MIN_VALUE) < distance.getValue(u) + edge.weight) {
                distance[edge.to] = distance.getValue(u) + edge.weight
            }
        }
    }

    return distance.getValue(end)
}

private data class Edge(val to: Index, val weight: Int)
