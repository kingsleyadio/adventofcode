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

private fun part1(input: List<Coord>, isSample: Boolean) {
    val relationship = input.buildRelationship()
    relationship.connectCircuit(if (isSample) 10 else 1000)
    val result = relationship.first.values.map { it.size }.sorted().takeLast(3).reduce(Int::times)
    println(result)
}

private fun part2(input: List<Coord>) {
    val relationship = input.buildRelationship()
    val (from, to) = relationship.connectCircuit(relationship.third.size)
    val result = from[0] * to[0].toLong()
    println(result)
}

private typealias Coord = List<Int>
private typealias Day08Edge = Triple<Coord, Coord, Double> // from, to, distance
private typealias Relationship = Triple<MutableMap<Int, Set<Coord>>, MutableMap<Coord, Int>, PriorityQueue<Day08Edge>>

private fun distance(from: Coord, to: Coord): Double {
    val (x1, y1, z1) = from
    val (x2, y2, z2) = to
    return sqrt(abs(x1 - x2).toDouble().pow(2) + abs(y1 - y2).toDouble().pow(2) + abs(z1 - z2).toDouble().pow(2))
}

private fun List<Coord>.buildRelationship(): Relationship {
    val pq = PriorityQueue<Day08Edge> { a, b -> compareValues(a.third, b.third) }
    for (i in 0..<lastIndex) {
        val from = this[i]
        for (j in (i+1)..lastIndex) pq.add(Day08Edge(from, this[j], distance(from, this[j])))
    }
    var circuitId = 0
    val circuitToBox = hashMapOf<Int, Set<Coord>>()
    val boxToCircuit = hashMapOf<Coord, Int>()
    for (box in this) {
        val id = circuitId++
        boxToCircuit[box] = id
        circuitToBox[id] = setOf(box)
    }
    return Triple(circuitToBox, boxToCircuit, pq)
}

private fun Relationship.connectCircuit(maxAttempts: Int): Pair<Coord, Coord> {
    val (circuitToBox, boxToCircuit, pq) = this
    var attempts = 0
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
        if (++attempts == maxAttempts || circuitToBox.size == 1) return Pair(from, to)
    }
}
