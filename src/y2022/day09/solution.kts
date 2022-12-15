package y2022.day09

import java.io.File
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    solution(2)
    solution(10)
}

fun solution(knotCount: Int) {
    val grid = Array(1000) { BooleanArray(1000) }
    val knots = List(knotCount) { intArrayOf(500, 500) }
    val h = knots.first()
    val t = knots.last()
    grid[t[0]][t[1]] = true
    File("input.txt").forEachLine { line ->
        val (direction, count) = line.split(" ")
        repeat(count.toInt()) {
            when (direction) {
                "R" -> h[1]++
                "U" -> h[0]--
                "L" -> h[1]--
                "D" -> h[0]++
            }
            for (knot in 0 until knots.lastIndex) {
                val lead = knots[knot]
                val follower = knots[knot + 1]
                val dy = lead[0] - follower[0]
                val dx = lead[1] - follower[1]
                if (abs(dy) <= 1 && abs(dx) <= 1) continue

                // Lead has drifted. Need to move follower
                follower[0] += dy.sign // -ve dy => move up and +ve dy => move down
                follower[1] += dx.sign // -ve dx => move left and +ve dx => move right
            }

            grid[t[0]][t[1]] = true
        }
    }
    val visits = grid.sumOf { it.count { visited -> visited } }
    println(visits)
}

main()
