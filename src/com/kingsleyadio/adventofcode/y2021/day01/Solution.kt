package com.kingsleyadio.adventofcode.y2021.day01

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    question1()
    question2()
}

fun question1() {
//    val input = File("day02.txt").readLines().map { it.toInt() }
//    var answer = 0
//    for (i in 1..input.lastIndex) {
//        if (input[i] > input[i - 1]) answer++
//    }
//    println(answer)
    val answer = readInput(2021, 1).useLines { sequence ->
        sequence.map { it.toInt() }
            .windowed(2)
            .count { (first, second) -> second > first }
    }
    println(answer)
}

fun question2() {
//    val input = File("day02.txt").readLines().map { it.toInt() }
//    var answer = 0
//    for (i in 3..input.lastIndex) {
//        if (input[i] > input[i - 3]) answer++
//    }
    val answer = readInput(2021, 1).useLines { sequence ->
        sequence.map { it.toInt() }
            .windowed(4)
            .count { it.last() > it.first() }
    }
    println(answer)
}
