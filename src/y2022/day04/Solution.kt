package y2022.day04
import java.io.File

fun main() {
    solution { a, b, x, y -> a in x..y && b in x..y || x in a..b && y in a..b }
    solution { a, b, x, y -> a in x..y || b in x..y || x in a..b || y in a..b }
}

fun solution(predicate: (Int, Int, Int, Int) -> Boolean) {
    var result = 0
    File("input.txt").forEachLine { line ->
        val (a, b, x, y) = line.split(",").flatMap { it.split("-") }.map { it.toInt() }
        if (predicate(a, b, x, y)) result++
    }
    println(result)
}
