package y2022.day15

import java.io.File
import kotlin.math.abs

fun main() {
    val data = parseInput()
    part1(data, 2_000_000)
    part2(data, 4_000_000)
}

fun part1(data: Map<Point, Point>, rowIndex: Int) {
    val (exclusions, sortedBeacons) = evaluateRow(data, rowIndex)
    var count = 0
    var bIndex = 0
    for (range in exclusions) {
        count += range.endInclusive - range.start + 1
        while (bIndex < sortedBeacons.size && sortedBeacons[bIndex] < range.start) bIndex++
        if (bIndex < sortedBeacons.size && sortedBeacons[bIndex] in range) count--
    }
    println(count)
}

fun part2(data: Map<Point, Point>, maxIndex: Int) {
    for (y in 0..maxIndex) {
        val (exclusions, _) = evaluateRow(data, y)
        var left = 0
        for (r in exclusions) {
            if (r.start > left) break
            else left = r.endInclusive + 1
        }
        if (left <= maxIndex) {
            println(left * 4_000_000L + y)
            return
        }
    }
}

fun evaluateRow(data: Map<Point, Point>, rowIndex: Int): RowData {
    val exclusions = mutableListOf<ClosedRange<Int>>() // all empty cells at row $rowIndex
    val obstructingBeacons = hashSetOf<Int>()
    data.forEach { (sensor, beacon) ->
        val dx = abs(sensor.x - beacon.x)
        val dy = abs(sensor.y - beacon.y)
        val radius = dx + dy
        val ds = abs(sensor.y - rowIndex)
        if (radius < ds) return@forEach
        val boundX = radius - ds
        exclusions.add((sensor.x - boundX)..(sensor.x + boundX))
        if (beacon.y == rowIndex) obstructingBeacons.add(beacon.x)
    }
    exclusions.sortBy { it.start }
    val sortedBeacons = obstructingBeacons.sorted()

    var left = 0
    for (i in 1..exclusions.lastIndex) {
        val range = exclusions[i]
        val previous = exclusions[left]
        when (range.start) {
            in previous -> exclusions[left] = previous.start..maxOf(previous.endInclusive, range.endInclusive)
            else -> exclusions[++left] = range
        }
    }
    return RowData(exclusions.take(left + 1), sortedBeacons)
}

fun parseInput(): Map<Point, Point> {
    val np = "(-?\\d+)"
    val pattern = "Sensor at x=$np, y=$np: closest beacon is at x=$np, y=$np".toRegex()
    val data = mutableMapOf<Point, Point>()
    File("input.txt").forEachLine { line ->
        val (sx, sy, bx, by) = pattern.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
        data[Point(sx, sy)] = Point(bx, by)
    }
    return data
}

data class Point(val x: Int, val y: Int)
data class RowData(val exclusiveIndices: List<ClosedRange<Int>>, val beaconIndices: List<Int>)

main()
