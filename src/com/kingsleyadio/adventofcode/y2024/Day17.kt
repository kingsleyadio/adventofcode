package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    readInput(2024, 17, false).useLines {
        val lines = it.iterator()
        val a = N.find(lines.next())!!.value.toLong()
        val b = N.find(lines.next())!!.value.toLong()
        val c = N.find(lines.next())!!.value.toLong()
        lines.next()
        val program = lines.next().substringAfter(": ").split(",").map(String::toInt)

        part1(longArrayOf(a, b, c), program)
        part2(program)
    }
}

private fun part1(registers: LongArray, program: List<Int>) {
    val result = run(registers, program)
    println(result.joinToString(","))
}

private fun part2(program: List<Int>) {
    fun decipher(subA: Long, left: Int): Long {
        if (left < 0) return subA
        for (i in 0..7) {
            val a = subA * 8 + i
            val output = run(longArrayOf(a, 0, 0), program)
            if (output == program.subList(left, program.size)) {
                val result = decipher(a, left - 1)
                if (result != 0L) return result
            }
        }
        return 0L
    }
    println(decipher(0, program.lastIndex))
}

private fun run(registers: LongArray, program: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    var pointer = 0
    while (pointer < program.lastIndex) {
        val op = program[pointer++]
        val operand = program[pointer++]
        val co = when (operand) {
            in 0..3 -> operand.toLong()
            4 -> registers[0]
            5 -> registers[1]
            6 -> registers[2]
            else -> -1
        }
        when (op) {
            0 -> registers[0] = registers[0] / (1 shl co.toInt())
            1 -> registers[1] = registers[1] xor operand.toLong()
            2 -> registers[1] = co % 8
            3 -> if (registers[0] != 0L) pointer = operand
            4 -> registers[1] = registers[1] xor registers[2]
            5 -> result.add((co % 8).toInt())
            6 -> registers[1] = registers[0] / (1 shl co.toInt())
            7 -> registers[2] = registers[0] / (1 shl co.toInt())
        }
    }
    return result
}

private val N = "\\d+".toRegex()
