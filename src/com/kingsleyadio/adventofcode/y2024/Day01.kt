package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput
import kotlin.math.abs

fun main() {
    part1()
    part2()
}

private fun part1() {
    val a = mutableListOf<Int>()
    val b = mutableListOf<Int>()
    readInput(2024, 1).forEachLine { line ->
        val (left, right) = line.split(" +".toRegex()).map(String::toInt)
        a.add(left)
        b.add(right)
    }
    a.sort()
    b.sort()
    val result = a.zip(b).sumOf { (left, right)-> abs(left - right) }
    println(result)
}

private fun part2() {
    val a = mutableListOf<Int>()
    val bFrequency = hashMapOf<Int, Int>()
    readInput(2024, 1).forEachLine { line ->
        val (left, right) = line.split(" +".toRegex()).map(String::toInt)
        a.add(left)
        bFrequency[right] = bFrequency.getOrDefault(right, 0) + 1
    }
    val result = a.sumOf { it * bFrequency.getOrDefault(it, 0) }
    println(result)
}
