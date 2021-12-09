import java.io.File
import kotlin.math.abs

fun solution(input: List<Int>, fuelFunc: (distance: Int) -> Int): Int {
    val min = input.minOrNull()!!
    val max = input.maxOrNull()!!
    return (min..max).minOf { pos -> input.sumOf { fuelFunc(abs(it - pos)) } }
}

fun main() {
    val input = File("input.txt").useLines { lines ->
        lines.first().split(",").map { it.toInt() }
    }
    println(solution(input) { it })
    println(solution(input) { it * (it + 1) shr 1 })
}

main()
