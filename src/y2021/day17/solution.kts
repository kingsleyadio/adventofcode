import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun part1(yRange: List<Int>): Int {
    val (y1, y2) = yRange.map { y -> if (y >= 0) y else y.inv() }
    val max = max(y1, y2)
    return max * (max + 1) shr 1
}

fun part2(xRange: List<Int>, yRange: List<Int>): Int {
    val (x1, x2) = xRange
    val (y1, y2) = yRange
    var count = 0
    for (xItem in 1..x2) {
        val ymax = yRange.sumOf { abs(it) } //Sufficiently large upper bound
        for (yItem in min(0, y1)..ymax) {
            var x = 0
            var dx = xItem
            var y = 0
            var dy = yItem
            while (x <= x2 && y >= y1) {
                x += dx
                y += dy--
                if (dx > 0) dx--
                if (x in x1..x2 && y in y1..y2) {
                    count++
                    break
                }
            }
        }
    }
    return count
}

fun main() {
    File("input.txt").forEachLine { line ->
        val (xdata, ydata) = line.substringAfter("target area: ").split(", ")
        val xRange = xdata.substringAfter("=").split("..").map { it.toInt() }
        val yRange = ydata.substringAfter("=").split("..").map { it.toInt() }

        println(part1(yRange))
        println(part2(xRange, yRange))
    }
}

main()
