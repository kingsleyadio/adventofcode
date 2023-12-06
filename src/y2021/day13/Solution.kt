package y2021.day13

import java.io.File
import kotlin.math.max

fun part1(dots: Set<Point>, command: Point): Int {
    return fold(dots, command).size
}

fun part2(dots: Set<Point>, commands: List<Point>): String {
    val folded = commands.fold(dots) { acc, command -> fold(acc, command) }
    val x = commands.last { (x, _) -> x > 0 }.x
    val y = commands.last { (_, y) -> y > 0 }.y
    return buildString {
        for (i in 0..y) {
            for (j in 0..x) append(if (Point(j, i) in folded) '#' else ' ').append(' ')
            append('\n')
        }
    }
}

fun fold(dots: Set<Point>, command: Point): Set<Point> {
    val (x, y) = command
    val full = max(x, y) shl 1
    return buildSet {
        for (dot in dots) {
            val (dotX, dotY) = dot
            if ((x > 0 && dotX < x) || (y > 0 && dotY < y)) add(dot)
            else if (x in 1 until dotX) add(Point(full - dotX, dotY))
            else if (y in 1 until dotY) add(Point(dotX, full - dotY))
        }
    }
}

data class Point(val x: Int, val y: Int)

fun List<Int>.toPoint() = Point(this[0], this[1])

fun main() {
    val dots = hashSetOf<Point>()
    val commands = arrayListOf<Point>()
    var isDots = true
    File("input.txt").forEachLine { line ->
        if (line.isEmpty()) isDots = false
        else if (isDots) dots.add(line.split(",").map { it.toInt() }.toPoint())
        else {
            val (axis, value) = line.substringAfterLast(" ").split("=")
            commands.add(if (axis == "y") Point(0, value.toInt()) else Point(value.toInt(), 0))
        }
    }
    println(part1(dots, commands.first()))
    println(part2(dots, commands))
}
