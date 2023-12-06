package y2021.day11

import util.readInput

fun solution(steps: Int, isPart2: Boolean): Int {
    // NOTE: isPart2=false returns number of glows and isPart2=true returns number of steps
    val input = readInput(2021, 11).useLines { lines ->
        lines.map { line -> line.toCharArray().map { it.digitToInt() }.toIntArray() }.toList()
    }
    fun glow(y: Int, x: Int, glowing: MutableSet<String>) {
        if (input[y][x] > 9 && "$y#$x" !in glowing) {
            glowing.add("$y#$x")
            input[y][x] = 0
            adjacentPoints(y, x, input).forEach { (newY, newX) ->
                if ("$newY#$newX" !in glowing) input[newY][newX]++
                glow(newY, newX, glowing)
            }
        }
    }

    var totalGlows = 0
    val octopusCount = input.size * input.first().size
    repeat(steps) { step ->
        val glowingPerStep = hashSetOf<String>()
        input.forEachIndexed { _, inner -> for (j in inner.indices) inner[j]++ }
        input.forEachIndexed { i, inner -> for (j in inner.indices) glow(i, j, glowingPerStep) }
        totalGlows += glowingPerStep.size
        if (isPart2 && glowingPerStep.size == octopusCount) return step + 1
    }
    return totalGlows
}

fun adjacentPoints(y: Int, x: Int, input: List<IntArray>): List<IntArray> {
    return listOf(
        intArrayOf(y - 1, x),
        intArrayOf(y - 1, x + 1),
        intArrayOf(y, x + 1),
        intArrayOf(y + 1, x + 1),
        intArrayOf(y + 1, x),
        intArrayOf(y + 1, x - 1),
        intArrayOf(y, x - 1),
        intArrayOf(y - 1, x - 1)
    ).filter { (y, x) -> input.getOrNull(y)?.getOrNull(x) != null }
}

fun main() {
    println(solution(100, isPart2 = false))
    println(solution(Int.MAX_VALUE, isPart2 = true))
}
