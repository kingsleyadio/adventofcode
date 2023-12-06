package com.kingsleyadio.adventofcode.y2021.day15

import com.kingsleyadio.adventofcode.util.readInput
import com.kingsleyadio.adventofcode.y2021.day13.Point
import java.util.*

fun solution(input: List<IntArray>): Int {
    val plane = List(input.size) { IntArray(input.size) { Int.MAX_VALUE } }
    val queue = PriorityQueue<Path> { a, b -> a.cost - b.cost }
    queue.offer(Path(Point(0, 0), 0))

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val (to, cost) = current
        if (plane[to.y][to.x] != Int.MAX_VALUE) continue
        plane[to.y][to.x] = cost
        sequence {
            for (y in -1..1) for (x in -1..1) {
                if (y != 0 && x != 0) continue
                val point = Point(to.x + x, to.y + y)
                val cell = plane.getOrNull(point.y)?.getOrNull(point.x) ?: continue
                if (cell == Int.MAX_VALUE) yield(point)
            }
        }.forEach { point ->
            queue.add(Path(point, cost + input[point.y][point.x]))
        }
    }
    return plane.last().last()
}

data class Point(val x: Int, val y: Int)
data class Path(val to: Point, val cost: Int)

fun main() {
    val input = buildList {
        readInput(2021, 15).forEachLine { line -> add(line.map { it.digitToInt() }.toIntArray()) }
    }
    println(solution(input))
    val newInput = input.expand(5)
    println(solution(newInput))
}

fun List<IntArray>.expand(times: Int): List<IntArray> {
    val self = this
    val newList = List(size * times) { IntArray(size * times) }
    for (i in 0..newList.lastIndex) {
        for (j in 0..newList.lastIndex) {
            val x = j % self.size
            val y = i % self.size
            val value = self[y][x] + j / self.size + i / self.size
            newList[i][j] = if (value > 9) (value % 10) + 1 else value
        }
    }
    return newList
}
