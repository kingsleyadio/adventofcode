package util

import java.io.File

sealed class AocYear(private val id: String) {
    fun readInput(day: String): File = File("src/$id/$day/input.txt")
}

object Aoc21 : AocYear("y2021")
object Aoc22 : AocYear("y2022")

fun readInput(year: Int, day: Int): File = File("src/y$year/day${"%02d".format(day)}/input.txt")
