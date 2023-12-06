package y2021.day07

import util.readInput

fun part1(input: List<Int>): Int {
    val min = input.minOrNull()!!
    val max = input.maxOrNull()!!
    val plane = IntArray(max - min + 1)
    for (value in input) plane[value - min]++

    val aligningForward = IntArray(plane.size)
    var subs = plane[0]
    for (i in 1..plane.lastIndex) {
        aligningForward[i] = aligningForward[i - 1] + subs
        subs += plane[i]
    }
    val aligningBackward = IntArray(plane.size)
    subs = plane.last()
    for (i in plane.lastIndex - 1 downTo 0) {
        aligningBackward[i] = aligningBackward[i + 1] + subs
        subs += plane[i]
    }
    for (i in aligningBackward.indices) aligningForward[i] += aligningBackward[i]
    return aligningForward.minOrNull()!!
}

fun main() {
    val input = readInput(2021, 7).useLines { lines ->
        lines.first().split(",").map { it.toInt() }
    }
    println(part1(input))
}
