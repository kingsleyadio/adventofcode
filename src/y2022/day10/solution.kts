package y2022.day10

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    var cycle = 0
    var value = 1
    var signalSums = 0
    fun checksum() {
        val interestingCycles = setOf(20, 60, 100, 140, 180, 220)
        if (cycle in interestingCycles) signalSums += cycle * value
    }
    File("input.txt").forEachLine { line ->
        val split = line.split(" ")
        cycle++
        checksum()
        if (split[0] == "addx") {
            cycle++
            value += split[1].toInt()
            checksum()
        }
    }
    println(signalSums)
}

fun part2() {
    var cycle = 0
    var value = 1
    val crt = Array(6) { CharArray(40) }
    fun display() {
        val index = cycle - 1
        val y = index / 40
        val x = index % 40
        crt[y][x] = if (x in value - 1..value + 1) '#' else ' '
    }
    File("input.txt").forEachLine { line ->
        val split = line.split(" ")
        cycle++
        display()
        if (split[0] == "addx") {
            cycle++
            display()
            value += split[1].toInt()
        }
    }
    crt.forEach { println(String(it)) }
}

main()
