package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*

fun main() {
    readInput(2024, 15).useLines {
        val lines = it.iterator()
        val warehouse = mutableMapOf<Index, Char>()
        var robot = Index(0, 0)
        for ((y, line) in lines.withIndex()) {
            if (line.isEmpty()) break
            for (x in line.indices) when (val c = line[x]) {
                '#', 'O' -> warehouse[Index(x, y)] = c
                '@' -> robot = Index(x, y)
            }
        }
        val moves = buildString { lines.forEach(::append) }

        part1(warehouse, robot, moves)
        part2(warehouse, robot, moves)
    }
}

private fun part1(wh: Map<Index, Char>, r: Index, moves: String) = solve(wh, r, moves)
private fun part2(wh: Map<Index, Char>, r: Index, moves: String) = solve(wh.enlarge(), Index(r.x * 2, r.y), moves)

private fun solve(wh: Map<Index, Char>, robot: Index, moves: String) {
    val warehouse = wh.toMutableMap()
    fun move(start: Index, direction: Index): Boolean {
        fun collectDeps(box: Index, map: MutableMap<Index, Char>) {
            val parts = buildList {
                add(box)
                if (warehouse[box] == '[') add(box + Directions.E)
                if (warehouse[box] == ']') add(box + Directions.W)
            }
            val deps = parts.mapNotNull { part -> (part + direction).takeIf { warehouse[it]?.isBox() == true } }
            for (part in parts) map[part] = warehouse.getValue(part)
            for (dep in deps) if (dep !in map) collectDeps(dep, map)
        }

        if (start !in warehouse) return true
        if (warehouse[start] == '#') return false
        val deps = buildMap { collectDeps(start, this) }
        for ((part) in deps) if (warehouse[part + direction] == '#') return false
        deps.entries.sortedBy { (k, _) -> k.y * -direction.y + k.x * -direction.x }.forEach { (part, value) ->
            warehouse[part + direction] = value
            warehouse.remove(part)
        }
        return true
    }

    moves.fold(robot) { current, move ->
        val direction = when (move) {
            '^' -> Directions.N
            '>' -> Directions.E
            'v' -> Directions.S
            else -> Directions.W
        }
        val next = current + direction
        if (move(next, direction)) next else current
    }
    var sum = 0
    for ((k, v) in warehouse) if (v.isBox(includeClose = false)) sum += 100 * k.y + k.x
    println(sum)
}

private fun Map<Index, Char>.enlarge() = buildMap {
    for ((k, v) in this@enlarge) {
        put(Index(k.x * 2, k.y), if (v == 'O') '[' else '#')
        put(Index(k.x * 2 + 1, k.y), if (v == 'O') ']' else '#')
    }
}

private fun Char.isBox(includeClose: Boolean = true) = this == 'O' || this == '[' || (includeClose && this == ']')
