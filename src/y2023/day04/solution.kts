package y2023.day04

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val points = File("input.txt").useLines { lines ->
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

fun part2() {
    val game = File("input.txt").readLines()
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

main()
