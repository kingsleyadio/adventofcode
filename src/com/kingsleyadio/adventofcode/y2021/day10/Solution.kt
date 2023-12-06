package com.kingsleyadio.adventofcode.y2021.day10

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    println(part1())
    println(part2())
}

fun part1(): Int {
    fun findInvalidChar(line: String): Char {
        val stack = StringBuilder()
        for (char in line) {
            val complement = when (char) {
                ']' -> '['
                '}' -> '{'
                ')' -> '('
                '>' -> '<'
                else -> { stack.append(char); continue }
            }
            if (stack.isEmpty() || stack.last() != complement) return char
            stack.deleteCharAt(stack.lastIndex)
        }
        return ' ' // Ignore
    }
    readInput(2021, 10).useLines { lines ->
        val scoresMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        return lines.map { findInvalidChar(it) }.sumOf { scoresMap[it] ?: 0 }
    }
}

fun part2(): Long {
    fun evaluateCompletionString(line: String): String? {
        val stack = StringBuilder()
        for (char in line) {
            val complement = when (char) {
                ']' -> '['
                '}' -> '{'
                ')' -> '('
                '>' -> '<'
                else -> { stack.append(char); continue }
            }
            if (stack.isEmpty() || stack.last() != complement) return null
            stack.deleteCharAt(stack.lastIndex)
        }
        return stack.reverse().toString()
    }
    readInput(2021, 10).useLines { lines ->
        val scoresMap = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
        val points =  lines
            .mapNotNull { evaluateCompletionString(it) }
            .map { it.fold(0L) { acc, c -> acc * 5 + scoresMap.getValue(c) } }
            .toList()
            .sorted()
        return points[points.size shr 1]
    }
}
