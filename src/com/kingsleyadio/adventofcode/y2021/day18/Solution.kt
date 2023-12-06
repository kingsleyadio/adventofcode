package com.kingsleyadio.adventofcode.y2021.day18

import com.kingsleyadio.adventofcode.util.readInput

fun part1(input: List<String>): Int {
    return input.map(::parseMath).reduce { acc, math -> acc + math }.score()
}

fun part2(input: List<String>): Int {
    return sequence {
        for (i in input.indices) for (j in input.indices) {
            if (i != j) {
                // Parsing on-demand since DS is mutable, which makes reuse impossible
                val left = parseMath(input[i])
                val right = parseMath(input[j])
                yield((left + right).score())
            }
        }
    }.maxOrNull()!!
}

fun main() {
    val input = readInput(2021, 18).readLines()
    println(part1(input))
    println(part2(input))
}

fun parseMath(input: String): SnailMath {
    fun parse(reader: StringReader): SnailMath {
        return when (val char = reader.read(1)) {
            "[" -> {
                val left = parse(reader)
                reader.read(1)
                val right = parse(reader)
                reader.read(1)
                SnailMath.Pair(left, right)
            }
            else -> SnailMath.Literal(char.toInt())
        }
    }
    return parse(StringReader(input))
}

class StringReader(private val string: String) {
    private var index = 0
    fun read(size: Int): String {
        val result = string.substring(index, index + size)
        index += size
        return result
    }
}

fun explode(math: SnailMath, depth: Int): Diff {
    if (math !is SnailMath.Pair) return Diff(0, 0)
    val (left, right) = math
    if (depth == 5 && left is SnailMath.Literal && right is SnailMath.Literal) {
        return Diff(left.value, right.value)
    }
    // Left
    val diffL = explode(left, depth + 1)
    if (depth + 1 == 5 && math.left is SnailMath.Pair) math.left = SnailMath.Literal(0)
    math.right.addLeft(diffL.right)
    // Right
    val diffR = explode(right, depth + 1)
    if (depth + 1 == 5 && math.right is SnailMath.Pair) math.right = SnailMath.Literal(0)
    math.left.addRight(diffR.left)

    return Diff(diffL.left, diffR.right)
}

fun split(math: SnailMath) {
    var split = false // To ensure we only split 1
    fun splitToPair(value: Int): SnailMath.Pair {
        split = true
        val low = value / 2
        val high = value - low
        return SnailMath.Pair(SnailMath.Literal(low), SnailMath.Literal(high))
    }

    fun split(math: SnailMath) {
        if (split || math !is SnailMath.Pair) return
        val (left, right) = math
        if (left is SnailMath.Literal && left.value > 9) math.left = splitToPair(left.value) else split(left)
        if (split) return
        if (right is SnailMath.Literal && right.value > 9) math.right = splitToPair(right.value) else split(right)
    }
    split(math)
}

operator fun SnailMath.plus(other: SnailMath): SnailMath {
    return SnailMath.Pair(this, other).also { pair ->
        while (true) {
            val score = pair.toString()
            explode(pair, 1)
            split(pair)
            if (score == pair.toString()) break
        }
    }
}

sealed interface SnailMath {
    fun addLeft(value: Int)
    fun addRight(value: Int)
    fun score(): Int

    class Literal(var value: Int) : SnailMath {
        override fun addLeft(value: Int) = run { this.value += value }
        override fun addRight(value: Int) = run { this.value += value }
        override fun score() = value
        override fun toString() = value.toString()
    }

    data class Pair(var left: SnailMath, var right: SnailMath) : SnailMath {
        override fun addLeft(value: Int) = left.addLeft(value)
        override fun addRight(value: Int) = right.addRight(value)
        override fun score() = 3 * left.score() + 2 * right.score()
        override fun toString() = "[$left,$right]"
    }
}

data class Diff(val left: Int, val right: Int)
