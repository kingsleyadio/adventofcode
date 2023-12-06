package y2022.day02
import java.io.File

fun main() {
    val input = File("input.txt").readLines()
    part1(input)
    part2(input)
}

fun part1(input: List<String>) {
    val result = input.sumOf { line ->
        when(line) {
            "A X" -> 4
            "A Y" -> 8
            "A Z" -> 3
            "B X" -> 1
            "B Y" -> 5
            "B Z" -> 9
            "C X" -> 7
            "C Y" -> 2
            "C Z" -> 6
            else -> 0u.toInt() // Hack around weird KT compile error
        }
    }
    println(result)
}

fun part2(input: List<String>) {
    val values = mapOf('A' to 1, 'B' to 2, 'C' to 3)
    val result = input.sumOf { line ->
        val (opponent, _, strategy) = line.toCharArray()
        when(strategy) {
            'X' -> (values.getValue(opponent) + 1) % 3 + 1
            'Y' -> 3 + values.getValue(opponent)
            'Z' -> 6 + values.getValue(opponent) % 3 + 1
            else -> 0
        }
    }
    println(result)
}
