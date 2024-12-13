package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput
import kotlin.math.floor
import kotlin.math.roundToLong

fun main() {
    val input = readInput(2024, 13, false).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) = solve(input, 0L)
private fun part2(input: List<String>) = solve(input, 10000000000000L)

private fun solve(input: List<String>, offset: Long) {
    val machines = buildMachines(input, offset)
    val cost = machines.map { m -> m.movesForB().let { b -> Pair(m.movesForA(b), b) } }.sumOf { (a, b) -> a * 3 + b }
    println(cost)
}

private fun buildMachines(input: List<String>, priceOffset: Long): List<Machine> = buildList {
    val lines = input.iterator()
    while (lines.hasNext()) {
        val (ax, ay) = REGEX_B.matchEntire(lines.next())!!.groupValues.drop(1).map(String::toInt)
        val (bx, by) = REGEX_B.matchEntire(lines.next())!!.groupValues.drop(1).map(String::toInt)
        val (px, py) = REGEX_P.matchEntire(lines.next())!!.groupValues.drop(1).map(String::toLong)
        add(Machine(Button(ax, ay), Button(bx, by), Price(priceOffset + px, priceOffset + py)))
        if (lines.hasNext()) lines.next()
    }
}

private class Button(val x: Int, val y: Int)
private class Price(val x: Long, val y: Long)
private class Machine(val a: Button, val b: Button, val p: Price) {

    fun movesForB(): Long {
        val moveB = (p.y * a.x - p.x * a.y) / (a.x * b.y - a.y * b.x).toDouble()
        return if (moveB == floor(moveB)) moveB.roundToLong() else 0
    }

    fun movesForA(moveB: Long): Long {
        if (moveB == 0L) return 0
        val moveA = (p.x - moveB * b.x) / a.x.toDouble()
        return if (moveA == floor(moveA)) moveA.roundToLong() else -1
    }
}

private val REGEX_B = "Button [AB]: X\\+(\\d+), Y\\+(\\d+)".toRegex()
private val REGEX_P = "Prize: X=(\\d+), Y=(\\d+)".toRegex()
