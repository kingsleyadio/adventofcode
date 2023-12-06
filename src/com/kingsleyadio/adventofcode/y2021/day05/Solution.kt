package com.kingsleyadio.adventofcode.y2021.day05

import com.kingsleyadio.adventofcode.util.readInput
import kotlin.math.abs

fun main() {
    println(solution(useDiagonals = false))
    println(solution(useDiagonals = true))
}

fun solution(useDiagonals: Boolean): Int {
    val points = hashSetOf<String>()
    val result = hashSetOf<String>()
    readInput(2021, 5).forEachLine { line ->
        val (x1, y1, x2, y2) = line.split(" -> ").flatMap { it.split(",") }.map { it.toInt() }
        when {
            x1 == x2 -> (if (y1 < y2) y1..y2 else y2..y1).forEach { y ->
                val id = "$x1-$y"
                if (id in points) result.add(id) else points.add(id)
            }
            y1 == y2 -> (if (x1 < x2) x1..x2 else x2..x1).forEach { x ->
                val id = "$x-$y1"
                if (id in points) result.add(id) else points.add(id)
            }
            useDiagonals ->(0..abs(x1 - x2)).forEach { index ->
                val x = x1 + index * if (x1 < x2) 1 else -1
                val y = y1 + index * if (y1 < y2) 1 else -1
                val id = "$x-$y"
                if (id in points) result.add(id) else points.add(id)
            }
        }
    }
    return result.size
}
