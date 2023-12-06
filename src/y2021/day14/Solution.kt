package y2021.day14

import util.readInput

private fun solution(template: String, map: Map<String, Char>, steps: Int): Long {
    val mem = hashMapOf<String, Map<Char, Long>>()
    fun memo(a: Char, b: Char, step: Int, func: (Char, Char, Int) -> Map<Char, Long>): Map<Char, Long> {
        val key = "$a$b-$step"
        return mem.getOrPut(key) { func(a, b, step) }
    }

    fun Map<Char, Long>.mergeTo(other: MutableMap<Char, Long>) {
        forEach { (char, count) -> other[char] = other.getOrDefault(char, 0) + count }
    }

    fun synthesize(a: Char, b: Char, step: Int): Map<Char, Long> {
        if (step == 0) return emptyMap()
        val product = map.getValue("$a$b")
        return buildMap {
            put(product, 1L)
            memo(a, product, step - 1, ::synthesize).mergeTo(this)
            memo(product, b, step - 1, ::synthesize).mergeTo(this)
        }
    }

    val counter = hashMapOf<Char, Long>()
    for (char in template) counter[char] = counter.getOrDefault(char, 0) + 1
    template.zipWithNext { a, b -> memo(a, b, steps, ::synthesize).mergeTo(counter) }
    val counts = counter.values.sorted()
    return counts.last() - counts.first()
}

fun main() {
    readInput(2021, 14).useLines { lines ->
        val iterator = lines.iterator()
        val template = iterator.next()
        iterator.next()
        val map = buildMap { for (line in iterator) line.split(" -> ").let { (k, v) -> put(k, v[0]) } }

        println(solution(template, map, 10))
        println(solution(template, map, 40))
    }
}
