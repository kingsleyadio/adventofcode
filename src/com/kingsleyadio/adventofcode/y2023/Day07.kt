package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2023, 7).readLines()
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) {
    val hands = input.map { Hand.of(it, withJoker = false) }
    solution(hands)
}

private fun part2(input: List<String>) {
    val hands = input.map { Hand.of(it, withJoker = true) }
    solution(hands)
}

private fun solution(hands: List<Hand>) {
    val sorted = hands.sortedWith(compareBy(Hand::type, Hand::power))
    val score = sorted.withIndex().fold(0) { acc, n -> acc + (n.index + 1) * n.value.bid }
    println(score)
}

private data class Hand(val power: Int, val type: Int, val bid: Int) {
    companion object {

        // '2' to 'A' as a mod-14 number system
        private val POWER = mapOf(
            '*' to 0, // Placeholder for Joker
            '2' to 1, '3' to 2, '4' to 3, '5' to 4, '6' to 5, '7' to 6, '8' to 7, '9' to 8,
            'T' to 9, 'J' to 10, 'Q' to 11, 'K' to 12, 'A' to 13,
        )

        fun of(line: String, withJoker: Boolean): Hand {
            val (deal, bid) = line.split(" ")
            val frequency = hashMapOf<Char, Int>()
            var power = 0
            for (char in deal) {
                frequency[char] = frequency.getOrDefault(char, 0) + 1
                val charPower = if (char == 'J' && withJoker) 0 else POWER.getValue(char)
                power = power * 14 + charPower
            }
            val type = frequency.evaluateType(withJoker)
            return Hand(power, type, bid.toInt())
        }

        private fun MutableMap<Char, Int>.evaluateType(withJoker: Boolean): Int {
            val jCount = getOrDefault('J', 0)
            if (withJoker && jCount > 0) {
                remove('J')
                if (isEmpty()) return jCount * jCount
                val (k, v) = maxBy { (_, v) -> v }
                this[k] = v + jCount
            }
            return values.fold(0) { acc, f -> acc + f * f }
        }
    }
}
