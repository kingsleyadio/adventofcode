package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    part1()
    part2()
}

private fun part1() {
    val maxR = 12
    val maxG = 13
    val maxB = 14
    readInput(2023, 2).useLines { lines ->
        val result = lines.map { line ->
            val (id, gameInfo) = line.split(": ")
            val gameNumber = id.split(" ").last().toInt()
            val max = gameInfo.split("; ").map { game ->
                game.split(", ").associate { pick ->
                    val (count, color) = pick.split(" ")
                    color to count.toInt()
                }
            }.fold(hashMapOf<String, Int>()) { acc, game ->
                acc.apply {
                    for ((color, count) in game) {
                        if (getOrDefault(color, 0) < count) put(color, count)
                    }
                }
            }
            gameNumber to max
        }.fold(0) { acc, game ->
            val (gameNumber, max) = game
            val r = max.getOrDefault("red", 0)
            val g = max.getOrDefault("green", 0)
            val b = max.getOrDefault("blue", 0)
            acc + when {
                r <= maxR && g <= maxG && b <= maxB -> gameNumber
                else -> 0
            }
        }
        println(result)
    }
}

private fun part2() {
    readInput(2023, 2).useLines { lines ->
        val result = lines.map { line ->
            val (_, gameInfo) = line.split(": ")
            val max = gameInfo.split("; ").map { game ->
                game.split(", ").associate { pick ->
                    val (count, color) = pick.split(" ")
                    color to count.toInt()
                }
            }.fold(hashMapOf<String, Int>()) { acc, game ->
                acc.apply {
                    for ((color, count) in game) {
                        if (getOrDefault(color, 0) < count) put(color, count)
                    }
                }
            }
            max.values.fold(1) { acc, n -> acc * n }
        }.fold(0) { acc, score -> acc + score }
        println(result)
    }
}
