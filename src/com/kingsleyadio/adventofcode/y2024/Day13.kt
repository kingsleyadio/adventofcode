package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 13, false).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) = solve(input, 0L)
private fun part2(input: List<String>) = solve(input, 10000000000000L)

private fun solve(input: List<String>, offset: Long) {
    val cost = buildMachines(input, offset).sumOf(Machine::cost)
    println(cost)
}

private fun buildMachines(input: List<String>, priceOffset: Long): List<Machine> = input.chunked(4) { (a, b, p) ->
    val (ax, ay) = NUMBER.findAll(a).map { it.value.toInt() }.toList()
    val (bx, by) = NUMBER.findAll(b).map { it.value.toInt() }.toList()
    val (px, py) = NUMBER.findAll(p).map { it.value.toLong() }.toList()
    Machine(Button(ax, ay), Button(bx, by), Price(priceOffset + px, priceOffset + py))
}

private class Button(val x: Int, val y: Int)
private class Price(val x: Long, val y: Long)
private class Machine(val a: Button, val b: Button, val p: Price) {
    fun cost(): Long {
        val bn = p.y * a.x - p.x * a.y
        val bd = a.x * b.y - a.y * b.x
        val moveB = if (bn % bd == 0L) bn / bd else return 0
        val an = p.x - moveB * b.x
        val moveA = if (an % a.x == 0L) an / a.x else return 0
        return moveA * 3 + moveB
    }
}

private val NUMBER = "\\d+".toRegex()
