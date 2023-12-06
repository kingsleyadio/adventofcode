package y2022.day12

import util.readInput
import java.util.*

fun main() {
    val grid = buildGrid()
    part1(grid)
    part2(grid)
}

fun part1(grid: Grid) {
    println(shortestDistance(grid))
}

fun part2(grid: Grid) {
    var shortestDistance = Int.MAX_VALUE
    val (outline, _, end) = grid
    for (i in outline.indices) {
        val row = outline[i]
        for (j in row.indices) {
            if (row[j] != 0) continue
            val newGrid = Grid(outline, Point(i, j), end)
            val nextShortestDistance = shortestDistance(newGrid)
            if (nextShortestDistance < 0) continue
            shortestDistance = minOf(shortestDistance, nextShortestDistance)
        }
    }
    println(shortestDistance)
}

fun shortestDistance(grid: Grid): Int {
    val (outline, start, end) = grid
    val costs = Array(outline.size) { IntArray(outline.first().size) { -1 } }
    val queue = PriorityQueue<Path> { a, b -> a.cost - b.cost }
    queue.add(Path(start, 0))
    while (queue.isNotEmpty()) {
        val (point, cost) = queue.poll()
        if (costs[point.y][point.x] in 0..cost) continue
        costs[point.y][point.x] = cost
        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(-1, 0, 1, 0)
        for (index in dx.indices) {
            val np = Point(point.y + dy[index], point.x + dx[index])
            if (outline.getOrNull(np.y)?.getOrNull(np.x) == null) continue
            if (costs[np.y][np.x] >= 0) continue // already fixed cost to this point
            val potential = (outline[np.y][np.x] - outline[point.y][point.x]).coerceAtLeast(1)
            if (potential == 1) queue.add(Path(np, cost + potential))
        }
    }
    return costs[end.y][end.x]
}

fun buildGrid(): Grid {
    var start = Point(0, 0)
    var end = Point(0, 0)
    var rowIndex = 0
    val grid = buildList {
        readInput(2022, 12).forEachLine { line ->
            val row = line
                .onEachIndexed { index, char -> if (char == 'S') start = Point(rowIndex, index) }
                .onEachIndexed { index, char -> if (char == 'E') end = Point(rowIndex, index) }
                .map { (if (it == 'S') 'a' else if (it == 'E') 'z' else it) - 'a' }
            add(row)
            rowIndex++
        }
    }
    return Grid(grid, start, end)
}

data class Grid(val outline: List<List<Int>>, val start: Point, val end: Point)
data class Point(val y: Int, val x: Int)
data class Path(val to: Point, val cost: Int)
