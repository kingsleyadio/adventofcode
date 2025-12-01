package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*
import kotlin.math.abs

fun main() {
    part1()
    part2()
}

private fun part1() = solve { _, new -> if (new.mod(100) == 0) 1 else 0 }

private fun part2() = solve { old, new ->
    if (new > 0) new / 100
    else if (new < 0) (if (old == 0) 0 else 1) + abs(new) / 100
    else 1
}

private inline fun solve(crossinline condition: (Int, Int) -> Int) {
    var state = 50
    var result = 0
    readInput(2025, 1).forEachLine { line ->
        val signum = if (line[0] == 'L') -1 else 1
        val value = line.drop(1).toInt()
        val newState = state + (signum * value)
        result += condition(state, newState)
        state = newState.mod(100)
    }
    println(result)
}
