package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val isSample = false
    val input = readInput(2024, 14, isSample).readLines()
    val robots = input.map { line ->
        val (x, y, vx, vy) = NUMBER.findAll(line).map { it.value.toInt() }.toList()
        Robot(x, y, vx, vy)
    }
    val (bx, by) = if (isSample) 11 to 7 else 101 to 103
    part1(robots, bx, by)
    part2(robots, bx, by)
}

private fun part1(robots: List<Robot>, bx: Int, by: Int) {
    val transformed = robots.map { r ->
        val newX = (r.x + 100 * r.vx).mod(bx)
        val newY = (r.y + 100 * r.vy).mod(by)
        Robot(newX, newY, r.vx, r.vy)
    }
    val quadrants = intArrayOf(0, 0, 0, 0) // tl, tr, bl, br
    val hx = bx / 2
    val hy = by / 2
    transformed.forEach { r ->
        when {
            r.x in 0..<hx && r.y in 0..<hy -> quadrants[0]++
            r.x in hx + 1..<bx && r.y in 0..<hy -> quadrants[1]++
            r.x in 0..<hx && r.y in hy + 1..<by -> quadrants[2]++
            r.x in hx + 1..<bx && r.y in hy + 1..<by -> quadrants[3]++
        }
    }
    val result = quadrants.fold(1L) { acc, n -> acc * n }
    println(result)
}

private fun part2(robots: List<Robot>, bx: Int, by: Int) {
    var transformed = robots
    var second = 0
    while (second++ < bx * by) {
        val set = hashSetOf<Index>()
        transformed = transformed.map { r ->
            val newX = (r.x + r.vx).mod(bx)
            val newY = (r.y + r.vy).mod(by)
            set.add(Index(newX, newY))
            Robot(newX, newY, r.vx, r.vy)
        }
        if (set.size == robots.size) break
    }
    println(second)
}

private data class Robot(val x: Int, val y: Int, val vx: Int, val vy: Int)

private val NUMBER = "-?\\d+".toRegex()
