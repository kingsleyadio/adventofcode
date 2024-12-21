package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    val input = readInput(2024, 21).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) = println(solve(input, 2))
private fun part2(input: List<String>) = println(solve(input, 25))
private fun solve(input: List<String>, robots: Int): Long = input.sumOf { complexity(it, robots) }

private fun complexity(keys: String, robots: Int): Long {
    val cache = mutableMapOf<Pair<String, Int>, Long>()
    fun simulate(aa: Char, bb: Char, level: Int): Long {
        if (aa == bb) return 1
        val key = "$aa$bb" to level
        if (key in cache) return cache.getValue(key)
        val keypad = if (level == 0) NUMERIC else ROBOTIC
        val a = keypad.getValue(aa)
        val b = keypad.getValue(bb)
        val invalid = keypad.getValue('.')
        val options = mutableListOf<List<Char>>()
        val (dx, dy) = b - a
        if (Index(a.x + dx, a.y) != invalid) options += buildList<Char> {
            add('A')
            for (i in 1..dx.absoluteValue) if (dx.sign < 0) add('<') else add('>')
            for (i in 1..dy.absoluteValue) if (dy.sign < 0) add('^') else add('v')
            add('A')
        }
        if (Index(a.x, a.y + dy) != invalid) options += buildList<Char> {
            add('A')
            for (i in 1..dy.absoluteValue) if (dy.sign < 0) add('^') else add('v')
            for (i in 1..dx.absoluteValue) if (dx.sign < 0) add('<') else add('>')
            add('A')
        }
        val size = options.minOf { option ->
            if (level == robots) option.size.toLong() - 1
            else option.zipWithNext().sumOf { (a, b) -> simulate(a, b, level + 1) }
        }
        return size.also { cache[key] = it }
    }

    val lowest = "A$keys".zipWithNext().sumOf { (a, b) -> simulate(a, b, 0) }
    val multiplier = keys.substringBeforeLast('A').toLong()
    return lowest * multiplier
}

private val NUMERIC = mapOf(
    '7' to Index(0, 0), '8' to Index(1, 0), '9' to Index(2, 0),
    '4' to Index(0, 1), '5' to Index(1, 1), '6' to Index(2, 1),
    '1' to Index(0, 2), '2' to Index(1, 2), '3' to Index(2, 2),
    '.' to Index(0, 3), '0' to Index(1, 3), 'A' to Index(2, 3),
)

private val ROBOTIC = mapOf(
    '.' to Index(0, 0), '^' to Index(1, 0), 'A' to Index(2, 0),
    '<' to Index(0, 1), 'v' to Index(1, 1), '>' to Index(2, 1),
)
