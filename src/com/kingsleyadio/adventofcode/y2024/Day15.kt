package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    val (warehouse, robot, moves) = readInput(2024, 15, false).useLines {
        val lines = it.iterator()
        val warehouse = mutableListOf<CharArray>()
        var robot = Index(0, 0)
        while (lines.hasNext()) {
            val line = lines.next()
            if (robot.x == 0 && robot.y == 0) {
                val found = line.indexOf('@')
                if (found > 0) robot = Index(found, warehouse.size)
            }
            if (line.isNotEmpty()) warehouse.add(line.toCharArray()) else break
        }
        val moves = StringBuilder()
        while (lines.hasNext()) moves.append(lines.next())
        Triple(warehouse, robot, moves.toString())
    }
    part1(warehouse, robot, moves)
    part2(warehouse, robot, moves)
}

private fun part1(wh: List<CharArray>, r: Index, moves: String) = solve(wh.copy(), r, moves)
private fun part2(wh: List<CharArray>, r: Index, moves: String) = solve(wh.enlarge(), Index(r.x * 2, r.y), moves)

private fun solve(warehouse: List<CharArray>, robot: Index, moves: String) {
    fun moveVertical(start: Index, direction: Index): Boolean {
        if (warehouse[start.y][start.x] == '#') return false
        if (warehouse[start.y][start.x] == '.') return true
        fun boxParts(part: Index) = buildList {
            add(part)
            if (warehouse[part.y][part.x] == 'O') return@buildList
            if (warehouse[part.y][part.x] == '[') add(part + Directions.E)
            if (warehouse[part.y][part.x] == ']') add(part + Directions.W)
        }

        fun collectDeps(box: Index, set: MutableSet<Index>) {
            val parts = boxParts(box)
            val deps = parts.mapNotNull { part ->
                val dep = part + direction
                if (warehouse[dep.y][dep.x].isBox()) dep else null
            }
            set.addAll(parts)
            deps.forEach { collectDeps(it, set) }
        }

        val set = buildSet { collectDeps(start, this) }
        val map = set.associateWith { warehouse[it.y][it.x] }
        set.forEach { part ->
            val dest = part + direction
            if (warehouse[dest.y][dest.x] == '#') return false
        }
        set.sortedBy { if (direction == Directions.N) it.y else -it.y }.forEach { part ->
            val dest = part + direction
            warehouse[dest.y][dest.x] = map.getValue(part)
            warehouse[part.y][part.x] = '.'
        }
        return true
    }

    fun moveHorizontal(start: Index, direction: Index): Boolean {
        var pos = start
        var foundSpace = false
        while (warehouse[pos.y][pos.x] != '#') {
            if (warehouse[pos.y][pos.x] == '.') {
                foundSpace = true
                break
            }
            pos += direction
        }
        if (!foundSpace) return false
        while (pos != start) {
            val itemPos = pos - direction
            val item = warehouse[itemPos.y][itemPos.x]
            warehouse[pos.y][pos.x] = item
            pos -= direction
        }
        return true
    }

    val walls = Rect(0..warehouse[0].lastIndex, 0..warehouse.lastIndex)
    val bound = Rect(1..<walls.x.last, 1..<walls.y.last)
    var current = robot
    for (move in moves) {
        val diff = when (move) {
            '^' -> Directions.N
            '>' -> Directions.E
            'v' -> Directions.S
            else -> Directions.W
        }
        val next = current + diff
        val moved = if (diff.x != 0) moveHorizontal(next, diff) else moveVertical(next, diff)
        if (moved) {
            warehouse[next.y][next.x] = '@'
            warehouse[current.y][current.x] = '.'
            current = next
        }
    }
    var sum = 0L
    for (y in bound.y) for (x in bound.x) if (warehouse[y][x].isBox(any = false)) sum += 100L * y + x
    println(sum)
}

private fun List<CharArray>.copy() = List(size) { i -> get(i).let { it.sliceArray(0..it.lastIndex) } }

private fun List<CharArray>.enlarge(): List<CharArray> {
    return map { line ->
        val resized = CharArray(line.size * 2)
        for (i in line.indices) {
            val t = when (line[i]) {
                '#' -> "##"
                'O' -> "[]"
                '.' -> ".."
                '@' -> "@."
                else -> error("Invalid")
            }
            resized[i * 2] = t[0]
            resized[i * 2 + 1] = t[1]
        }
        resized
    }
}

private fun Char.isBox(any: Boolean = true) = this == 'O' || this == '[' || (any && this == ']')
