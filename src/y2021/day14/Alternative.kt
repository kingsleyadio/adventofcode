package y2021.day14

import java.io.File

private fun solution(template: String, map: Map<String, Char>, steps: Int): Long {
    fun tick(input: Map<String, Long>): Map<String, Long> = buildMap {
        for ((pair, count) in input) {
            val product = map.getValue(pair)
            val first = "${pair[0]}$product"
            put(first, getOrDefault(first, 0) + count)
            val second = "$product${pair[1]}"
            put(second, getOrDefault(second, 0) + count)
        }
    }

    var pairs = buildMap<String, Long> { template.windowed(2).forEach { put(it, getOrDefault(it, 0) + 1) } }
    repeat(steps) { pairs = tick(pairs) }
    val charArray = LongArray(26)
    pairs.forEach { (pair, count) -> pair.forEach { charArray[it - 'A'] += count } }
    val max = charArray.maxOrNull()!!
    val min = charArray.minOf { if (it == 0L) Long.MAX_VALUE else it }

    return (max - min) / 2
}

fun main() {
    File("input.txt").useLines { lines ->
        val iterator = lines.iterator()
        val template = iterator.next()
        iterator.next()
        val map = buildMap { for (line in iterator) line.split(" -> ").let { (k, v) -> put(k, v[0]) } }

        println(solution(template, map, 10))
        println(solution(template, map, 40))
    }
}
