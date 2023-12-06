package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    part1()
    part2()
}

private fun part1() {
    val sum = readInput(2023, 6, sample = false).useLines { sequence ->
        val lines = sequence.iterator()
        val times = lines.next().substringAfter(":").trim().split(" +".toRegex()).map { it.toInt() }
        val distances = lines.next().substringAfter(":").trim().split(" +".toRegex()).map { it.toInt() }
        times.zip(distances)
    }.map { (time, distance) ->
        (1..<time).count { a -> (time - a) * a > distance }
    }.fold(1) { acc, n -> acc * n }
    println(sum)
}

private fun part2() {
    val possibilities = readInput(2023, 6, sample = false).useLines { sequence -> 
        val lines = sequence.iterator()
        val time = lines.next().substringAfter(":").replace(" ", "").toLong()
        val distance = lines.next().substringAfter(":").replace(" ", "").toLong()
        (1..<time).count { a -> (time - a) * a > distance }
    }
    println(possibilities)
}
