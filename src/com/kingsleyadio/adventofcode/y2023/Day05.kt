package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val (seeds, pipeline) = readInput(2023, 5).useLines { sequence ->
        val lines = sequence.iterator()
        val seeds = lines.next().substringAfter(": ").split(" ").map { it.toLong() }
        lines.next()
        val pipeline = buildList {
            repeat(7) {
                lines.next()
                add(lines.generateMap())
            }
        }
        seeds to pipeline
    }
    part1(seeds, pipeline)
    part2(seeds, pipeline)
}

private fun part1(seeds: List<Long>, pipeline: List<Map<LongRange, Long>>) {
    val lowestLocation = seeds.minOf { seed ->
        pipeline.fold(seed) { number, map -> map.getValue(number) }
    }
    println(lowestLocation)
}

private fun part2(spec: List<Long>, pipeline: List<Map<LongRange, Long>>) {
    val lowestLocation = spec.chunked(2).minOf { (start, size) ->
        var seed = start
        var min = Long.MAX_VALUE
        while(seed < start + size) {
            var current = seed
            var increment = Long.MAX_VALUE
            pipeline.forEach { map ->
                for ((range, dest) in map) {
                    if (current in range) {
                        increment = minOf(increment, range.last - current)
                        current = dest + current - range.first
                        return@forEach
                    }
                }
            }
            min = minOf(min, current) // current -> location
            seed += (increment + 1).coerceAtLeast(1)
        }
        min
    }
    println(lowestLocation)
}

private fun Iterator<String>.generateMap(): Map<LongRange, Long> = buildMap {
    while (hasNext()) {
        val line = next()
        if (line.isEmpty()) break
        val (dest, src, range) = line.split(" ").map { it.toLong() }
        put(src..<src + range, dest)
    }
}

private fun Map<LongRange, Long>.getValue(key: Long): Long {
    for ((range, v) in this) {
        if (key in range) {
            val diff = key - range.first
            return v + diff
        }
    }
    return key
}
