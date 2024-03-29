package com.kingsleyadio.adventofcode.y2022.day06

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2022, 6).readText()
    solution(input, 4)
    solution(input, 14)
}

fun solution(input: String, distinctCount: Int) {
    val lookup = IntArray(26) { -1 }
    var start = -1
    for (index in input.indices) {
        val char = input[index]
        val lastOccurrence = lookup[char - 'a']
        if (lastOccurrence >= start) start = lastOccurrence + 1
        lookup[char - 'a'] = index
        if (index - start + 1 == distinctCount) {
            println(index + 1)
            break
        }
    }
}
