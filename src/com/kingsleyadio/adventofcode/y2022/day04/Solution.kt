package com.kingsleyadio.adventofcode.y2022.day04

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    solution { a, b, x, y -> a in x..y && b in x..y || x in a..b && y in a..b }
    solution { a, b, x, y -> a in x..y || b in x..y || x in a..b || y in a..b }
}

fun solution(predicate: (Int, Int, Int, Int) -> Boolean) {
    var result = 0
    readInput(2022, 4).forEachLine { line ->
        val (a, b, x, y) = line.split(",").flatMap { it.split("-") }.map { it.toInt() }
        if (predicate(a, b, x, y)) result++
    }
    println(result)
}
