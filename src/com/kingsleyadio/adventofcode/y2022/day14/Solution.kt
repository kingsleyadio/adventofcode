package com.kingsleyadio.adventofcode.y2022.day14

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val cave = buildCave()
    part1(cave)
    part2(cave)
}

fun part1(cave: Cave) {
    val sand = hashSetOf<Point>()
    fun pourSand(grain: IntArray): Boolean {
        while (true) {
            val current = Point(grain[0], grain[1])
            if (current.x !in cave.leftmost..cave.rightmost || current.y > cave.bottom) return false
            val down = Point(current.x, current.y + 1)
            val ld = Point(current.x - 1, down.y)
            val rd = Point(current.x + 1, down.y)
            grain[1]++
            if (down !in cave.outline && down !in sand) continue
            else if (ld !in cave.outline && ld !in sand) grain[0]--
            else if (rd !in cave.outline && rd !in sand) grain[0]++
            else break
        }
        grain[1]--
        return true
    }
    while (true) {
        val grain = intArrayOf(500, 0)
        if (pourSand(grain)) sand.add(Point(grain[0], grain[1])) else break
    }
    println(sand.size)
}

fun part2(cave: Cave) {
    val sand = hashSetOf<Point>()
    fun pourSand(grain: IntArray): Boolean {
        while (true) {
            val current = Point(grain[0], grain[1])
            val down = Point(current.x, current.y + 1)
            val ld = Point(current.x - 1, down.y)
            val rd = Point(current.x + 1, down.y)
            grain[1]++
            if (down.y == cave.bottom + 2) break // can't move any further
            if (down !in cave.outline && down !in sand) continue
            else if (ld !in cave.outline && ld !in sand) grain[0]--
            else if (rd !in cave.outline && rd !in sand) grain[0]++
            else break
        }
        grain[1]--
        return Point(grain[0], grain[1]) != Point(500, 0)
    }
    while (true) {
        val grain = intArrayOf(500, 0)
        if (pourSand(grain)) sand.add(Point(grain[0], grain[1])) else break
    }
    println(sand.size + 1) // cater for the last grain blocking the source
}

fun buildCave(): Cave {
    var left = Int.MAX_VALUE
    var right = 0
    var bottom = 0
    val outline = buildSet {
        readInput(2022, 14).forEachLine { line ->
            val spots = line.split(" -> ").map { spot ->
                val (x, y) = spot.split(",").map(String::toInt)
                Point(x, y)
            }
            spots.zipWithNext().forEach { (start, end) ->
                val xProgression = if (end.x > start.x) start.x..end.x else end.x..start.x
                val yProgression = if (end.y > start.y) start.y..end.y else end.y..start.y

                left = minOf(left, xProgression.first)
                right = maxOf(right, xProgression.last)
                bottom = maxOf(bottom, yProgression.last)

                for (x in xProgression) add(Point(x, start.y))
                for (y in yProgression) add(Point(start.x, y))
            }
        }
    }
    return Cave(outline, left, right, bottom)
}

class Cave(val outline: Set<Point>, val leftmost: Int, val rightmost: Int, val bottom: Int)
data class Point(val x: Int, val y: Int)
