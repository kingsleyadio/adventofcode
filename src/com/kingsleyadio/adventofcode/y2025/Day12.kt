package com.kingsleyadio.adventofcode.y2025

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val presents = mutableListOf<Day12Present>()
    val regions = mutableListOf<Day12Region>()
    readInput(2025, 12, false).useLines {
        val lines = it.iterator()
        while (lines.hasNext()) {
            val raw = lines.next()
            if (raw.contains('x')) {
                val (area, counts) = raw.split(": ")
                val (xf, yf) = area.split("x").map(String::toInt)
                regions.add(Day12Region(Rect(0..<xf, 0..<yf), counts.split(" ").map(String::toInt)))
            } else if (raw.isNotEmpty()) {
                val indices = mutableSetOf<Index>()
                for (y in 0..2) for ((x, c) in lines.next().withIndex()) if (c == '#') indices.add(Index(x, y))
                presents.add(Day12Present(indices))
            }
        }
    }
    part1crazy(regions)
}

private fun part1crazy(regions: List<Day12Region>) {
    val result = regions.count { region ->
        val area = region.field.run { x.size * y.size }
        val totalPresents = region.presents.sum()
        area >= totalPresents * 9
    }
    println(result)
}

private data class Day12Present(val indices: Set<Index>)
private data class Day12Region(val field: Rect, val presents: List<Int>)
