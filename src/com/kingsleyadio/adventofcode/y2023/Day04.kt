package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    part1()
    part2()
}

private fun part1() {
    val points = readInput(2023, 4).useLines { lines ->
        lines.sumOf { line ->
            val (winner, own) = line.split(" | ")
            val numbers = winner.substringAfter(": ").trim().split(" +".toRegex()).mapTo(hashSetOf()) { it.toInt() }
            val ownNumbers = own.trim().split(" +".toRegex()).mapTo(hashSetOf()) { it.toInt() }
            val winCount = numbers.intersect(ownNumbers).size
            if (winCount == 0) 0 else 1 shl winCount - 1
        }
    }
    println(points)
}

private fun part2() {
    val game = readInput(2023, 4).readLines()
    val wins = IntArray(game.size) { 1 }
    for (i in game.indices) {
        val (winner, own) = game[i].split(" | ")
        val numbers = winner.substringAfter(": ").trim().split(" +".toRegex()).mapTo(hashSetOf()) { it.toInt() }
        val ownNumbers = own.trim().split(" +".toRegex()).mapTo(hashSetOf()) { it.toInt() }
        val winCount = numbers.intersect(ownNumbers).size
        val max = minOf(i + winCount, game.lastIndex)
        for (j in i+1..max) wins[j] += wins[i]
    }
    val total = wins.fold(0) { acc, n -> acc + n }
    println(total)
}
