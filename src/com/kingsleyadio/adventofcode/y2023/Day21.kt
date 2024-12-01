package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val (grid, start) = readInput(2023, 21, false).useLines { lines ->
        var start = Index(0, 0)
        var found = false
        val grid = buildList {
            for ((y, line) in lines.withIndex()) {
                add(line)
                if (!found) {
                    val x = line.indexOf('S')
                    if (x >= 0) {
                        start = Index(x, y)
                        found = true
                    }
                }
            }
        }
        grid to start
    }
//    part1(grid, start)
    part2(grid, start)
}

private fun part1(grid: List<String>, start: Index) {
//    grid.forEach(::println)
    println(start)
//    println()
//    val collector = hashSetOf<Index>()
//    countPlots(grid, start, 6, collector)
//    val result = collector.size

    val result = countPlots(grid, start, 64)
    println(result)
}

private fun part2(grid: List<String>, start: Index) {
//    countPlots(grid, start, 26501365)
//    grid.forEach(::println)
////    println(start)
//    
    val limit = 26501365
    val multipleY = limit / grid.size
    val multipleX = limit % grid.size
    println("$multipleY x $multipleX")
    grid.flatMap { it.toCharArray().asList() }.count { it == '#' }.let { println("#s: $it") }
//    println(countPlots(grid, start, 66, isInfinite = true))
    // Total: 17161
    // #s: 2021
    // Other: 15140
//    for( it in 0..300) {
//        val result = countPlots(grid, start, it, isInfinite = true)
//        if ((it - 65) % 131 == 0) println(result)
//    }
    (65..(65 + 131*2)).map {
        val result = countPlots(grid, start, it, isInfinite = true)
        println(result)
        result
    }.zipWithNext { a, b -> b - a }.also { println(it) }
    // 15135n^2 - 15019n + 3751 // 202301
    // 65 + (131 * n) // n => n (upper equation -1)
    // 619407349431167
    // 619404233770314
//    println(max)
//    val remainder = 5000 % max
//    val remainCount = countPlots(grid, start, remainder)
//    val multiple = countPlots(grid, start, max) * (5000 / max)
//    println(multiple + remainCount)
}

private fun countPlots(grid: List<String>, pos: Index, steps: Int, isInfinite: Boolean = false): Int {
    val dy = intArrayOf(0, 1, 0, -1)
    val dx = intArrayOf(1, 0, -1, 0)
    val collector = hashSetOf<Index>()
    val queue = ArrayDeque<Pair<Index, Int>>()
    val visited = hashMapOf<Index, Int>()
    queue.addLast(pos to steps)
    while (queue.isNotEmpty()) {
        val (current, stepsLeft) = queue.removeFirst()
        if (stepsLeft == 0) {
            collector.add(current)
            continue
        }
        dy.indices.forEach { i ->
            val next = Index(current.x + dx[i], current.y + dy[i])
            if (!isInfinite && (next.y !in grid.indices || next.x !in grid[0].indices)) return@forEach
            if (grid[next] == '#') return@forEach
            if (visited.put(next, stepsLeft - 1) != stepsLeft - 1) queue.addLast(next to stepsLeft - 1)
        }
    }
    return collector.size
}

private operator fun List<String>.get(index: Index): Char {
    val (maxX, maxY) = first().length to size
    val reducedX = index.x % maxX
    val reducedY = index.y % maxY
    return this[(reducedY + maxY) % maxY][(reducedX + maxX) % maxX]
}
