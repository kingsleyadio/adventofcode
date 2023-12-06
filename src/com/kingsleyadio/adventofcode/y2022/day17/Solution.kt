package com.kingsleyadio.adventofcode.y2022.day17

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = parseInput()
    part1(input)
    part2(input)
}

fun part1(input: String) {
    println(simulation(input, 2022))
}

fun part2(input: String) {
    println(simulation(input, 1_000_000_000_000))
}

fun simulation(jet: String, rockCount: Long): Long {
    val towerWidth = 7
    var tower = mutableListOf(List(towerWidth) { true })
    var jetIndex = -1
    var partialTowerHeight = 0L

    fun nextDirection(): Int {
        jetIndex = (jetIndex + 1) % jet.length
        return if (jet[jetIndex] == '<') -1 else 1
    }

    fun isClear(rock: Array<BooleanArray>, left: Int, depth: Int): Boolean {
        for (i in rock.lastIndex downTo 0) {
            val rSection = rock[i]
            val tSection = tower.getOrNull(rock.lastIndex - i + depth) ?: return true
            tSection.forEachIndexed { ti, t ->
                val r = rSection.getOrNull(ti - left) ?: false
                if (t && r) return false
            }
        }
        return true
    }

    fun checkHorizontalMove(rock: Array<BooleanArray>, left: Int, depth: Int, direction: Int): Boolean {
        if (left + direction < 0 || left + rock[0].size + direction > towerWidth) return false
        return isClear(rock, left + direction, depth)
    }

    fun checkDownwardMove(rock: Array<BooleanArray>, left: Int, depth: Int): Boolean {
        return isClear(rock, left, depth - 1)
    }

    for (rc in 1..rockCount) {
        val nextRockIndex = ((rc - 1) % ROCKS.size).toInt()
        val nextRock = ROCKS[nextRockIndex]
        var depth = tower.size + 3
        var left = 2
        while (true) {
            val jetDirection = nextDirection()
            if (checkHorizontalMove(nextRock, left, depth, jetDirection)) left += jetDirection
            if (checkDownwardMove(nextRock, left, depth)) depth-- else break
        }
        var potentialCutOff = 0
        // Add rock to tower
        for (i in depth until depth + nextRock.size) {
            val rSection = nextRock[nextRock.lastIndex - i + depth]
            val tSection = tower.getOrNull(i)
            val newSection = List(7) { index ->
                (rSection.getOrNull(index - left) ?: false) || (tSection?.get(index) ?: false)
            }
            if (tSection == null) tower.add(newSection) else tower[i] = newSection

            if (tower[i].all { it }) potentialCutOff = i
        }
        if (potentialCutOff > 0) {
            partialTowerHeight += potentialCutOff
            println("Full row index: $partialTowerHeight")
            depth -= potentialCutOff
            tower = tower.subList(potentialCutOff, tower.size).toMutableList()
        }
    }
    return partialTowerHeight + tower.lastIndex
}

fun parseInput(): String {
    return readInput(2022, 17).readText().trim()
}

val ROCKS = listOf(
    arrayOf(booleanArrayOf(true, true, true, true)),
    arrayOf(
        booleanArrayOf(false, true, false),
        booleanArrayOf(true, true, true),
        booleanArrayOf(false, true, false)
    ),
    arrayOf(
        booleanArrayOf(false, false, true),
        booleanArrayOf(false, false, true),
        booleanArrayOf(true, true, true)
    ),
    arrayOf(booleanArrayOf(true), booleanArrayOf(true), booleanArrayOf(true), booleanArrayOf(true)),
    arrayOf(booleanArrayOf(true, true), booleanArrayOf(true, true)),
)
