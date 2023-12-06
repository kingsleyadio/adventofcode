package y2021.day21

import util.readInput

fun part1(p1: Int, p2: Int): Int {
    val scores = intArrayOf(0, 0)
    val positions = intArrayOf(p1, p2)
    var rolls = 0
    var player = 0
    while (true) {
        val rolled = ++rolls + ++rolls + ++rolls
        positions[player] = (positions[player] + rolled - 1) % 10 + 1
        scores[player] += positions[player]
        player = ++player % 2

        val (min, max) = scores.sorted()
        if (max >= 1000) return min * rolls
    }
}

fun part2(p1: Int, p2: Int): Long {
    fun generateDieRolls() = sequence {
        for (i in 1..3) for (j in 1..3) for (k in 1..3) {
            yield(i + j + k)
        }
    }

    // ¯\_(ツ)_/¯
    val cache = hashMapOf<String, LongArray>()
    fun memo(
        positions: List<Int>,
        scores: List<Int>,
        player: Int,
        roll: Int,
        func: (List<Int>, List<Int>, Int, Int) -> LongArray
    ): LongArray {
        val key = "${positions.hashCode()}-${scores.hashCode()}-$player-$roll"
        return cache.getOrPut(key) { func(positions, scores, player, roll) }
    }

    fun play(positions: List<Int>, scores: List<Int>, player: Int, roll: Int): LongArray {
        val position = (positions[player] + roll - 1) % 10 + 1
        val score = scores[player] + position
        if (score >= 21) return longArrayOf(1L - player, player.toLong())

        val newPositions = if (player == 0) listOf(position, positions[1]) else listOf(positions[0], position)
        val newScores = if (player == 0) listOf(score, scores[1]) else listOf(scores[0], score)
        val newPlayer = (player + 1) % 2
        return generateDieRolls().fold(longArrayOf(0, 0)) { (p1, p2), rolled ->
            val (newP1, newP2) = memo(newPositions, newScores, newPlayer, rolled, ::play)
            longArrayOf(p1 + newP1, p2 + newP2)
        }
    }

    val startPositions = listOf(p1, p2)
    val startScores = listOf(0, 0)
    val wins = generateDieRolls().fold(longArrayOf(0, 0)) { (p1, p2), rolled ->
        val (newP1, newP2) = memo(startPositions, startScores, 0, rolled, ::play)
        longArrayOf(p1 + newP1, p2 + newP2)
    }
    return wins.maxOf { it }
}

fun main() {
    val (p1, p2) = readInput(2021, 21).readLines().map { it.substringAfterLast(" ").toInt() }
    println(part1(p1, p2))
    println(part2(p1, p2))
}
