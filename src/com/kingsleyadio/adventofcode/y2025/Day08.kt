package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val isSample = false
    val input = readInput(2025, 8, isSample).readLines().map { it.split(",").map(String::toInt) }
    part1(input, isSample)
    part2(input)
}

private fun part1(input: List<List<Int>>, isSample: Boolean) {
    val pq = PriorityQueue<Day08Pair> { a, b -> compareValues(a.distance, b.distance) }
    for (i in 0..<input.lastIndex) {
        val from = input[i]
        for (j in (i+1)..input.lastIndex) {
            val to = input[j]
            pq.add(Day08Pair(from, to, distance(from, to)))
        }
    }
    var circuitId = 0
    val circuitToBox = hashMapOf<Int, Set<List<Int>>>()
    val boxToCircuit = hashMapOf<List<Int>, Int>()
    for (box in input) {
        val id = circuitId++
        boxToCircuit[box] = id
        circuitToBox[id] = setOf(box)
    }
    var attempts = 0
    while(attempts++ < if (isSample) 10 else 1000) {
        val (from, to) = pq.poll()
        val fromCircuit = boxToCircuit.getValue(from)
        val fromFamily = circuitToBox.remove(fromCircuit).orEmpty()
        val toCircuit = boxToCircuit.getValue(to)
        val toFamily = circuitToBox.remove(toCircuit).orEmpty()
        if (fromFamily.size > toFamily.size) {
            toFamily.forEach { boxToCircuit[it] = fromCircuit }
            circuitToBox[fromCircuit] = fromFamily + toFamily
        } else {
            fromFamily.forEach { boxToCircuit[it] = toCircuit }
            circuitToBox[toCircuit] = fromFamily + toFamily
        }
    }
    val result = circuitToBox.values.map { it.size }.sorted().takeLast(3).reduce(Int::times)
    println(result)
}

private fun part2(input: List<List<Int>>) {
    val pq = PriorityQueue<Day08Pair> { a, b -> compareValues(a.distance, b.distance) }
    for (i in 0..<input.lastIndex) {
        val from = input[i]
        for (j in (i+1)..input.lastIndex) {
            val to = input[j]
            pq.add(Day08Pair(from, to, distance(from, to)))
        }
    }
    var circuitId = 0
    val circuitToBox = hashMapOf<Int, Set<List<Int>>>()
    val boxToCircuit = hashMapOf<List<Int>, Int>()
    for (box in input) {
        val id = circuitId++
        boxToCircuit[box] = id
        circuitToBox[id] = setOf(box)
    }
    while(true) {
        val (from, to) = pq.poll()
        val fromCircuit = boxToCircuit.getValue(from)
        val fromFamily = circuitToBox.remove(fromCircuit).orEmpty()
        val toCircuit = boxToCircuit.getValue(to)
        val toFamily = circuitToBox.remove(toCircuit).orEmpty()
        if (fromFamily.size > toFamily.size) {
            toFamily.forEach { boxToCircuit[it] = fromCircuit }
            circuitToBox[fromCircuit] = fromFamily + toFamily
        } else {
            fromFamily.forEach { boxToCircuit[it] = toCircuit }
            circuitToBox[toCircuit] = fromFamily + toFamily
        }
        if (circuitToBox.size == 1) {
            val result = from[0] * to[0].toLong()
            println(result)
            break
        }
    }
}

private data class Day08Pair(val from: List<Int>, val to: List<Int>, val distance: Double)
private fun distance(from: List<Int>, to: List<Int>): Double {
    val (x1, y1, z1) = from
    val (x2, y2, z2) = to
    return sqrt(abs(x1 - x2).toDouble().pow(2) + abs(y1 - y2).toDouble().pow(2) + abs(z1 - z2).toDouble().pow(2))
}
