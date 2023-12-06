package com.kingsleyadio.adventofcode.y2021.day20

import com.kingsleyadio.adventofcode.util.readInput

fun solution(input: List<BooleanArray>, algorithm: String, sampleSize: Int): Int {
    fun enhance(image: List<BooleanArray>, infinityPixel: Boolean): Pair<List<BooleanArray>, Boolean> {
        val enhanced = List(image.size + 2) { i ->
            BooleanArray(image.size + 2) { j ->
                val key = (0..8).map { index ->
                    val y = index / 3 + i - 2
                    val x = index % 3 + j - 2
                    if (image.getOrNull(y)?.getOrNull(x) ?: infinityPixel) '1' else '0'
                }.joinToString("").toInt(2)
                algorithm[key] == '#'
            }
        }
        val infinityKey = (if (infinityPixel) "1" else "0").repeat(9)
        val newInfinityPixel = algorithm[infinityKey.toInt(2)] == '#'

        return enhanced to newInfinityPixel
    }
    val (enhanced, _) = (1..sampleSize).fold(input to false) { (f, s), _ -> enhance(f, s) }
    return enhanced.sumOf { row -> row.count { it } }
}

fun main() {
    val lines = readInput(2021, 20).readLines()
    val algorithm = lines[0]
    val image = lines.drop(2).map { line -> line.map { it == '#' }.toBooleanArray() }
    println(solution(image, algorithm, 2))
    println(solution(image, algorithm, 50))
}
