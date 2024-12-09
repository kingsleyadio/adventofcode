package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val input = readInput(2024, 9).readLine()
    part1(input)
    part2(input)
}

private fun part1(input: String) {
    val compact = buildList {
        var left = 0
        var right = input.lastIndex
        var leftover = emptyList<Int>()
        while (left <= right) {
            if (left % 2 == 0) {
                val content = List(input[left].digitToInt()) { left / 2 }
                addAll(content)
            } else {
                var space = input[left].digitToInt()
                while (space > 0) {
                    if (leftover.isNotEmpty()) {
                        val toAppend = leftover.take(space)
                        addAll(toAppend)
                        space -= toAppend.size
                        leftover = leftover.subList(toAppend.size, leftover.size)
                    } else {
                        val content = List(input[right].digitToInt()) { right / 2 }
                        val toAppend = content.take(space)
                        addAll(toAppend)
                        space -= toAppend.size
                        if (space == 0) leftover = content.subList(toAppend.size, content.size)
                        right -= 2
                    }
                }
            }
            left++
        }
        if (leftover.isNotEmpty()) addAll(leftover)
    }
    val checksum = compact.withIndex().sumOf { (i, c) -> i * c.toLong() }
    println(checksum)
}

private fun part2(input: String) {
    val compact = mutableListOf<File>()
    val spaces = mutableListOf<Space>()
    for (i in input.indices) {
        if (i % 2 == 0) {
            val previousSpace = spaces.lastOrNull() ?: Space(0, 0)
            compact.add(File(i / 2, previousSpace.next(input[i].digitToInt())))
        } else {
            val previousSpace = compact.last().space
            spaces.add(previousSpace.next(input[i].digitToInt()))
        }
    }
    for (i in compact.lastIndex downTo 0) {
        val file = compact[i]
        for (j in spaces.indices) {
            val space = spaces[j]
            if (space.start > file.space.start) break
            if (space.size >= file.space.size) {
                file.space = Space(space.start, file.space.size)
                spaces[j] = Space(space.start + file.space.size, space.size - file.space.size)
                break
            }
        }
    }
    val result = compact.sumOf { it.checksum() }
    println(result)
}

private data class Space(val start: Long, val size: Int)
private data class File(val id: Int, var space: Space)

private fun Space.next(size: Int) = Space(start + this.size, size)
private fun File.checksum() = (0..<space.size).sumOf { id * (it + space.start) }
