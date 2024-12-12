package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 12).readLines()
    val regions = buildRegion(input)
    part1(regions)
    part2(regions)
}

private fun part1(regions: List<List<Index>>) {
    val totalPrice = regions.sumOf { it.priceOfFence() }
    println(totalPrice)
}

private fun part2(regions: List<List<Index>>) {
    val totalPrice = regions.sumOf { it.priceOfSide() }
    println(totalPrice)
}

private fun List<Index>.priceOfFence(): Int = size * perimeter().size

private fun List<Index>.priceOfSide(): Int {
    val perimeter = perimeter()
    var sides = 0
    for ((point, d) in perimeter) if (Fence(point + d, d) !in perimeter) sides++
    return size * sides
}

private fun List<Index>.perimeter(): Set<Fence> {
    val fences = mutableSetOf<Fence>()
    for (index in this) for (d in Directions.Cardinal) {
        val point = index + d
        val normalizedDirection = Index(d.y, d.x)
        if (point !in this) fences.add(Fence(point, normalizedDirection))
    }
    return fences
}

private fun buildRegion(input: List<String>): List<List<Index>> {
    val bounds = Rect(0..input[0].lastIndex, 0..input.lastIndex)
    val regions = mutableListOf<List<Index>>()
    val visited = hashSetOf<Index>()
    fun traverse(start: Index, index: Index = start, region: MutableList<Index> = mutableListOf()) {
        if (!visited.add(index)) return
        region.add(index)
        for (d in Directions.Cardinal) {
            val next = index + d
            if (next in visited || next !in bounds) continue
            if (input[next.y][next.x] == input[index.y][index.x]) traverse(start, next, region)
        }
        if (index == start) regions.add(region)
    }
    for (y in bounds.y) for (x in bounds.x) traverse(Index(x, y))
    return regions
}

private data class Fence(val point: Index, val direction: Index)
